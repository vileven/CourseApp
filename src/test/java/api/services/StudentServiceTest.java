package api.services;

import api.Application;
import api.models.ClassModel;
import api.models.Course;
import api.models.Group;
import api.utils.response.UserClass;
import api.utils.response.student_info.StudentInfoBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Vileven on 14.05.17.
 */

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Sql(scripts = "../../filling.sql")
public class StudentServiceTest {

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private StudentService studentService;

    @Test
    public void getStudentGroups() {
        final List<Group> res = studentService.getStudentGroups(-1);
        assertNotEquals(0, res.size());
        assertEquals(1, res.size());
        assertEquals("IU6-41", res.get(0).getName());
    }

    @Test
    public void getStudentCourses() {
        final List<Course> res = studentService.getStudentCourses(-1);
        assertNotEquals(0, res.size());
        assertEquals(1, res.size());
        assertEquals("first course", res.get(0).getName());
    }

    @Test
    public void getStudentClasses() {
        final List<ClassModel> res = studentService.getStudentClasses(-1, "2017-05-05", "2017-05-14");
        assertEquals(3, res.size());
        assertEquals("Math", res.get(0).getSubjectName());
        assertEquals("English", res.get(2).getSubjectName());
    }

    @Test
    public void createRequest() {
        studentService.createRequest(-1, -1);
        assertEquals(1, template.query("SELECT count(*) FROM requests",
                (rs, rowNum) -> rs.getInt("count")).size());
    }

    @Test
    public void getAvaliableCourses() {
        final List<Course> res = studentService.getAvaliableCourses(-1);
        assertEquals(2, res.size());
        res.forEach(row -> assertTrue(-2 >= row.getId()));
    }

    @Test
    public void getInfo() {
        final List<StudentInfoBody> res = studentService.getInfo(-2);
        assertNotNull(res);

        assertEquals(2, res.size());
        assertEquals(3,res.get(1).subjects.size());
        assertEquals("Math", res.get(1).subjects.get(2).subjectName);
    }



}