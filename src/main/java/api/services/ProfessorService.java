package api.services;

import api.models.User;
import api.repositories.CourseRepository;
import api.repositories.GroupRepository;
import api.repositories.SubjectRepository;
import api.repositories.UserRepository;
import api.utils.error.PermissionDeniedException;
import api.utils.info.AttendancesInfo;
import api.utils.response.GroupAndSubjectResponse;
import api.utils.response.UserClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
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

    private final RowMapper<UserClass> userClassMapper = (((rs, rowNum) -> new UserClass(rs.getLong("id"),
            rs.getLong("subject_id"), rs.getString("subject_name"),
            rs.getLong("group_id"), rs.getString("group_name"), rs.getString("begin_time"),
            rs.getString("end_time"))));

    private final RowMapper<GroupAndSubjectResponse> groupAndSubjectMapper = (((rs, rowNum) ->
            new GroupAndSubjectResponse(rs.getLong("subject_id"), rs.getString("subject_name"),
                    rs.getLong("group_id"), rs.getString("group_name"))));

    private final RowMapper<Long> idMapper = ((rs, rowNum) -> rs.getLong("id"));

    private boolean isValidProf(HttpSession session, Long classId) {
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
                        "WHERE cl.id = ? AND u.id =? ", idMapper, (Long) id, classId).isEmpty();
    }

    public List<UserClass> getProfClasses(long id, String from, String to) {
        if (from == null) {
            from = LocalDateTime.now().toString();
        }

        if (to == null) {
            to = (LocalDateTime.parse(from).plusDays(7)).toString();
        }

        final String query =
                        "SELECT " +
                        "  cl.id, " +
                        "  cl.subject_id, " +
                        "  s.name AS subject_name, " +
                        "  cl.group_id, " +
                        "  g.name AS group_name, " +
                        "  cl.begin_time, " +
                        "  cl.end_time " +
                        "FROM " +
                        " classes AS cl " +
                        " JOIN subjects AS s ON cl.subject_id = s.id " +
                        " JOIN professors AS pr ON s.id = pr.subject_id " +
                        " JOIN users AS u ON pr.prof_id = u.id " +
                        " JOIN groups AS g ON cl.group_id = g.id "+
                        "WHERE u.id = ? AND cl.begin_time >= ?::TIMESTAMPTZ AND cl.begin_time <= ?::TIMESTAMPTZ " +
                        "ORDER BY cl.begin_time ";

        return template.query(query, userClassMapper, id, from, to);
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

}
