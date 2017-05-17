package api.services;

import api.models.Course;
import api.models.Group;
import api.repositories.CourseRepository;
import api.repositories.GroupRepository;
import api.repositories.SubjectRepository;
import api.repositories.UserRepository;
import api.utils.response.UserClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentService {
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;
    private final JdbcTemplate template;

    private final RowMapper<UserClass> userClassMapper = (((rs, rowNum) -> new UserClass(rs.getLong("id"),
            rs.getLong("subject_id"), rs.getString("subject_name"),
            rs.getLong("group_id"), rs.getString("group_name"), rs.getString("begin_time"),
            rs.getString("end_time"))));

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
                "  g.name, " +
                "  g.course_id " +
                "FROM " +
                "  users AS u " +
                "  JOIN applications AS a ON u.id = a.student_id " +
                "  JOIN groups AS g ON a.group_id = g.id " +
                "WHERE u.id = ? ";

        return this.template.query(query, groupRepository.getMapper(), id);
    }

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

    public List<UserClass> getStudentClasses(long id, String from, String to) {
        if (from == null) {
            from = LocalDate.now().atStartOfDay().toString();
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
                "  classes AS cl " +
                "  JOIN groups AS g ON cl.group_id = g.id " +
                "  JOIN applications AS a ON g.id = a.group_id " +
                "  JOIN users AS u ON a.student_id = u.id " +
                "  JOIN subjects AS s ON cl.subject_id = s.id " +
                "WHERE u.id = ? AND cl.begin_time >= ?::TIMESTAMPTZ AND cl.begin_time <= ?::TIMESTAMPTZ " +
                "ORDER BY cl.begin_time ";

        return template.query(query, userClassMapper, id, from, to);
    }

    public void createRequest(long studentId, long courseId) throws DataIntegrityViolationException {
        template.update("INSERT INTO requests (student_id, course_id) VALUES (?, ?) ", studentId, courseId);
    }

}
