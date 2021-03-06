package api.services;

import api.models.ClassModel;
import api.models.Course;
import api.models.Group;
import api.repositories.CourseRepository;
import api.repositories.GroupRepository;
import api.repositories.SubjectRepository;
import api.repositories.UserRepository;
import api.utils.response.student_info.StudentInfoBody;
import api.utils.response.student_info.SubjectInfoBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;
    private final JdbcTemplate template;

    private final RowMapper<ClassModel> classMapper = (((rs, rowNum) ->
            new ClassModel(rs.getLong("id"),
                    rs.getString("topic"), rs.getLong("subject_id"),
                    rs.getString("subject_name"), rs.getLong("group_id"),
                    rs.getString("group_name"), rs.getLong("prof_id"),
                    rs.getString("prof_first_name"), rs.getString("prof_last_name"),
                    rs.getString("begin_time"), rs.getString("end_time"),
                    rs.getString("location"))
    ));

    @Autowired
    public StudentService(UserRepository userRepository, SubjectRepository subjectRepository,
                          GroupRepository groupRepository, CourseRepository courseRepository, JdbcTemplate template) {
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        this.groupRepository = groupRepository;
        this.courseRepository = courseRepository;
        this.template = template;
    }

    public List<Group> getStudentGroups(long id) {
        final String query =
                "SELECT " +
                "  g.id, " +
                "  g.course_id, " +
                "  c.name AS course_name, " +
                "  g.name " +
                "FROM " +
                "  users AS u " +
                "  JOIN applications AS a ON u.id = a.student_id " +
                "  JOIN groups AS g ON a.group_id = g.id " +
                "  JOIN courses AS c ON c.id = g.course_id " +
                "WHERE u.id = ? " +
                "ORDER BY g.id";

        return this.template.query(query, groupRepository.getMapper(), id);
    }

//    public List<StudentInfoBody> getStudentInfo

    public List<Course> getStudentCourses(long id) {
        final String query =
                "SELECT " +
                "  c.id, " +
                "  c.name " +
                "FROM " +
                "  users AS u " +
                "  JOIN applications AS a ON u.id = a.student_id " +
                "  JOIN groups AS g ON a.group_id = g.id " +
                "  JOIN courses AS c ON g.course_id = c.id " +
                "WHERE u.id = ? " +
                "GROUP BY c.id ";
        return this.template.query(query, courseRepository.getMapper(), id);
    }

    public List<ClassModel> getStudentClasses(long id, String from, String to) {
        if (from == null) {
            from = LocalDate.now().atStartOfDay().toString();
        }

        if (to == null) {
            to = (LocalDate.parse(from).plusDays(8)).toString();
        }

        final String query =
                "SELECT " +
                        "  cl.id, " +
                        "  cl.subject_id, " +
                        "  s.name AS subject_name, " +
                        "  cl.group_id, " +
                        "  g.name AS group_name, " +
                        "  cl.prof_id, " +
                        "  u.first_name AS prof_first_name, " +
                        "  u.last_name AS prof_last_name, " +
                        "  cl.topic, " +
                        "  cl.location, " +
                        "  cl.begin_time, " +
                        "  cl.end_time " +
                "FROM " +
                "  classes AS cl " +
                "  JOIN groups AS g ON cl.group_id = g.id " +
                "  JOIN applications AS a ON g.id = a.group_id " +
                "  JOIN users AS u ON a.student_id = u.id " +
                "  JOIN subjects AS s ON cl.subject_id = s.id " +
                "WHERE u.id = ? AND cl.begin_time >= ?::TIMESTAMPTZ AND cl.begin_time <= ?::TIMESTAMPTZ " +
                "ORDER BY cl.begin_time ";

        return template.query(query, classMapper, id, from, to);
    }

    public void createRequest(long studentId, long courseId) throws DataIntegrityViolationException {
        template.update("INSERT INTO requests (student_id, course_id) VALUES (?, ?) ", studentId, courseId);
    }

    public List<Course> getAvaliableCourses(long studentId) {
        return courseRepository.getAvaliableCourses(studentId);
    }

    public List<StudentInfoBody> getInfo(long studentId) {
        final String query =
                "SELECT " +
                "  c.id as course_id, " +
                "  c.name AS course_name, " +
                "  g.id   AS group_id, " +
                "  g.name as group_name, " +
                "  s.id   AS subject_id, " +
                "  s.name AS subject_name, " +
                "  COALESCE(ss.total, 0) AS total, " +
                "  COALESCE(m.name, 'не определено') AS mark_name " +
                "FROM " +
                "  users AS u " +
                "  JOIN applications AS a ON u.id = a.student_id " +
                "  JOIN groups AS g ON a.group_id = g.id " +
                "  JOIN courses AS c ON c.id = g.course_id " +
                "  JOIN subjects AS s ON c.id = s.course_id " +
                "  LEFT JOIN subject_stats AS ss ON s.id = ss.subject_id AND ss.student_id = u.id " +
                "  LEFT JOIN marks AS m ON ss.mark_id = m.id " +
                "WHERE u.id = ? " +
                "GROUP BY c.id, g.id, s.id, ss.total, m.name " +
                "ORDER BY c.id, g.id, s.id ";

        final List<StudentInfoBody> res = new ArrayList<>();
        final long[] currentCourse = {0};
        template.query(query, rs -> {
            final long cId = rs.getLong("course_id");
            if (cId != currentCourse[0]) {
                currentCourse[0] = cId;
                res.add(new StudentInfoBody(cId, rs.getString("course_name"),
                        rs.getLong("group_id"), rs.getString("group_name"), new ArrayList<>()));
            }

            res.get(res.size() - 1).subjects.add(new SubjectInfoBody(rs.getLong("subject_id"),
                    rs.getString("subject_name"), rs.getInt("total"),
                    rs.getString("mark_name")));
        }, studentId);

        return res;
    }

}
