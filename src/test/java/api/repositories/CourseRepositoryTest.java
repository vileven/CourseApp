package api.repositories;

import api.Application;
import api.models.Course;
import api.utils.pair.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Vileven on 13.05.17.
 */
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class CourseRepositoryTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private CourseRepository courseRepository;

    private Course course;

    @Before
    public void setup() {
        template.update("DELETE FROM courses");
        course = new Course("name");
        final SimpleJdbcInsert courseInsert = new SimpleJdbcInsert(template).withTableName("courses").usingGeneratedKeyColumns("id");
        final Number id = courseInsert.executeAndReturnKey(Collections.singletonMap("name", course.getName()));
        course.setId((Long)id);
    }

    @After
    public void after() {
        template.update("DELETE FROM courses");
    }


    @Test
    public void create() {
        final Course createdCourse = courseRepository.create(new Course("hello"));
        assertNotNull(createdCourse);
        assertFalse(createdCourse.isNew());
        assertEquals("hello", createdCourse.getName());
    }

    @Test
    public void find() {
        final Course findedCourse = courseRepository.find(course.getId());
        assertNotNull(findedCourse);
        assertEquals(course, findedCourse);
    }

    @Test
    public void updateName() {
        courseRepository.updateName(course.getId(), "hello");
        final Course updatedCourse = template.queryForObject("SELECT * FROM courses WHERE id = ?",
                courseMapper, course.getId());
        assertEquals(course, updatedCourse);
        assertEquals("hello", updatedCourse.getName());
    }

    @Test
    public void findByName() {
        final List<Course> courses = courseRepository.findByName("name");
        assertEquals(1, courses.size());
        assertEquals(course, courses.get(0));
    }

    @Test
    public void selectWithParams() {
        for (int i = 0; i < 10; i++) {
            courseRepository.create(new Course(Integer.toString(i)));
        }

        List<Course> res = courseRepository.selectWithParams(10, 0, null);
        assertNotNull(res);
        assertEquals(10, res.size());

        res = courseRepository.selectWithParams(5, 6, null);
        assertEquals(5, res.size());
        assertEquals("9",res.get(4).getName());

        final List<Pair<String, String>> params = Collections.singletonList(new Pair<>("id", "DESC"));

        res = courseRepository.selectWithParams(5, 6, params);
        assertEquals(5, res.size());
        assertEquals("0", res.get(3).getName());
    }

    @Test
    public void delete() {
        courseRepository.delete(course.getId());

        exception.expect(EmptyResultDataAccessException.class);
        template.queryForObject("SELECT * FROM courses", courseMapper);
    }

    @Test
    public void deleteAll() {
        courseRepository.deleteAll();

        exception.expect(EmptyResultDataAccessException.class);
        template.queryForObject("SELECT * FROM courses", courseMapper);
    }

    private final RowMapper<Course> courseMapper = (((rs, rowNum) -> new Course(
            rs.getLong("id"), rs.getString("name"))));
}