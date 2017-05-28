package api.services;

import api.models.ClassModel;
import api.repositories.CourseRepository;
import api.repositories.GroupRepository;
import api.repositories.SubjectRepository;
import api.repositories.UserRepository;
import api.utils.error.PermissionDeniedException;
import api.utils.info.AttendancesInfo;
import api.utils.response.GroupAndSubjectResponse;
import api.utils.response.UserClass;
import api.utils.response.prof_info.GroupInfoBody;
import api.utils.response.prof_info.ProfInfoBody;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static api.controllers.SessionController.USER_ID;

/**
 * Created by Vileven on 16.05.17.
 */
@Service
public class ProfessorService {
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;
    private final JdbcTemplate template;

    @Autowired
    public ProfessorService(UserRepository userRepository, SubjectRepository subjectRepository,
                            GroupRepository groupRepository, CourseRepository courseRepository, JdbcTemplate template) {
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        this.groupRepository = groupRepository;
        this.courseRepository = courseRepository;
        this.template = template;
    }

    private final RowMapper<ClassModel> classMapper = (((rs, rowNum) ->
            new ClassModel(rs.getLong("id"),
                    rs.getString("topic"), rs.getLong("subject_id"),
                    rs.getString("subject_name"), rs.getLong("group_id"),
                    rs.getString("group_name"), rs.getLong("prof_id"),
                    rs.getString("prof_first_name"), rs.getString("prof_last_name"),
                    rs.getString("begin_time"), rs.getString("end_time"),
                    rs.getString("location"))
    ));

    private final RowMapper<GroupAndSubjectResponse> groupAndSubjectMapper = (((rs, rowNum) ->
            new GroupAndSubjectResponse(rs.getLong("subject_id"), rs.getString("subject_name"),
                    rs.getLong("group_id"), rs.getString("group_name"))));

    private final RowMapper<Long> idMapper = ((rs, rowNum) -> rs.getLong("id"));

    public boolean isValidProf(HttpSession session, Long classId) {
        final Object id = session.getAttribute(USER_ID);
        if (id == null) {
            return false;
        }

        return template.query(
                  " SELECT " +
                        "  u.id " +
                        "FROM " +
                        "  users AS u " +
                        "  JOIN professors AS pr ON u.id = pr.prof_id " +
                        "  JOIN classes AS cl ON cl.subject_id = pr.subject_id " +
                        "WHERE cl.id = ? AND u.id =? ", idMapper, (Long) id, classId).isEmpty() || ((Long) id == 0 );
    }

    public List<ClassModel> getProfClasses(long id, @Nullable String from, @Nullable String to) {
        if (from == null) {
            from = LocalDateTime.now().toString();
        }

        if (to == null) {
            to = (LocalDate.parse(from).plusDays(8)).toString();
        }

        final String query = "SELECT\n" +
                                "  cl.id,\n" +
                                "  cl.subject_id,\n" +
                                "  s.name AS subject_name,\n" +
                                "  cl.group_id,\n" +
                                "  g.name AS group_name,\n" +
                                "  cl.prof_id,\n" +
                                "  u.first_name AS prof_first_name,\n" +
                                "  u.last_name AS prof_last_name,\n" +
                                "  cl.topic,\n" +
                                "  cl.location,\n" +
                                "  cl.begin_time,\n" +
                                "  cl.end_time\n" +
                                "FROM\n" +
                                "  classes cl\n" +
                                "  JOIN subjects s ON cl.subject_id = s.id\n" +
                                "  JOIN users u ON cl.prof_id = u.id\n" +
                                "  JOIN groups g ON cl.group_id = g.id\n" +
                                "WHERE cl.prof_id = ? AND cl.begin_time >= ?::TIMESTAMPTZ AND cl.begin_time <= ?::TIMESTAMPTZ " +
                        "ORDER BY cl.begin_time ";

        return template.query(query, classMapper, id, from, to);
    }

    public List<GroupAndSubjectResponse> getGroupsAndSubjects(long id) {
        final String query =
                " SELECT " +
                "  s.id AS subject_id, " +
                "  s.name AS subject_name, " +
                "  g.id AS group_id, " +
                "  g.name AS group_name " +
                " FROM " +
                "  users AS u " +
                "  JOIN professors AS pr ON u.id = pr.prof_id " +
                "  JOIN subjects AS s ON pr.subject_id = s.id " +
                "  JOIN groups AS g ON s.course_id = g.course_id " +
                " WHERE u.id = ? ";

        return template.query(query, groupAndSubjectMapper, id);
    }

    public void addAttendencies(AttendancesInfo info, HttpSession session) throws PermissionDeniedException, DataIntegrityViolationException {
        if (!isValidProf(session, info.classId)) {
            throw new PermissionDeniedException("permission denied");
        }

        final String query =
                " INSERT INTO attendances (class_id, student_id, mark, comment) VALUES " +
                "  (?, ?, ?, ?) ";

        template.update(query, info.classId, info.studentId, info.mark, info.comment);
    }


    public List<ProfInfoBody> getInfo(long profId) {
        final String query =
                "SELECT " +
                "  s.id as subject_id, " +
                "  s.name as subject_name, " +
                "  c.id as course_id, " +
                "  c.name as course_name, " +
                "  g.id as group_id, " +
                "  g.name as group_name " +
                "FROM " +
                "  professors pr " +
                "  JOIN subjects s ON pr.subject_id = s.id " +
                "  JOIN courses c ON s.course_id = c.id " +
                "  JOIN groups g ON c.id = g.course_id " +
                "WHERE pr.prof_id = ? " +
                "GROUP BY s.id, c.id, g.id " +
                "ORDER BY s.id, c.id, g.id";

        final List<ProfInfoBody> res = new ArrayList<>();
        final long[] currentSubject = {0};
        template.query(query, rs -> {
            final long sId = rs.getLong("subject_id");
            if (sId != currentSubject[0]) {
                currentSubject[0] = sId;
                res.add(new ProfInfoBody(rs.getLong("course_id"), rs.getString("course_name"),
                        sId, rs.getString("subject_name") , new ArrayList<>()));
            }

            res.get(res.size() - 1).groups.add(new GroupInfoBody(rs.getLong("group_id"),
                    rs.getString("group_name")));
        }, profId);

        return res;
    }
}
