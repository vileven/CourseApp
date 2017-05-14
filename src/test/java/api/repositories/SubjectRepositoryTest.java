package api.repositories;

import api.Application;
import api.models.Course;
import api.models.Subject;
import api.utils.pair.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Vileven on 13.05.17.
 */
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SubjectRepositoryTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private CourseRepository courseRepository;

    private Subject subject;

    private final RowMapper<Subject> subjectMapper = (((rs, rowNum) -> new Subject(rs.getLong("id"),
            rs.getLong("course_id"), rs.getString("name"))));

    @Before
    public void setup() {
        template.update("DELETE FROM subjects");
        final Course course = courseRepository.create(new Course("name"));
        assertNotNull(course);
        final SimpleJdbcInsert insert = new SimpleJdbcInsert(template).withTableName("subjects").usingGeneratedKeyColumns("id");
        final Map<String, Object> parameters = new HashMap<>();
        subject = new Subject(course.getId(), "name");
        parameters.put("course_id", subject.getCourseId());
        parameters.put("name", subject.getName());
        final Number id = insert.executeAndReturnKey(parameters);
        subject.setId((Long)id);
    }

    @Test
    public void create() {
        final Subject createdSubject = subjectRepository.create(new Subject(subject.getCourseId(), "create"));
        assertNotNull(createdSubject);
        assertFalse(createdSubject.isNew());
        assertEquals("create", createdSubject.getName());
    }

    @Test
    public void createNotExistSubject() {
        final Subject subject = subjectRepository.create(new Subject(-1L, "name"));
        assertNull(subject);
    }

    @Test
    public void delete() {
        subjectRepository.delete(subject.getId());

        assertEquals(0, template.query("SELECT * FROM subjects", subjectMapper).size());
    }

    @Test
    public void find() {
        final Subject findedSubject = subjectRepository.find(subject.getId());
        assertNotNull(findedSubject);
        assertEquals(subject, findedSubject);
    }

    @Test
    public void findByName() {
        final List<Subject> res = subjectRepository.findByName("name");
        assertEquals(1, res.size());
        assertEquals(subject, res.get(0));
    }

    @Test
    public void updateName() {
        subjectRepository.updateName(subject.getId(), "second");
        final Subject updatedSubject = template.queryForObject("SELECT * FROM subjects", subjectMapper);
        assertNotNull(updatedSubject);
        assertEquals(subject, updatedSubject);
        assertEquals("second", updatedSubject.getName());
    }

    @Test
    public void selectWithParams() {
        for (int i = 0; i < 10; i++) {
            subjectRepository.create(new Subject(subject.getCourseId(), Integer.toString(i)));
        }

        List<Subject> res = subjectRepository.selectWithParams(10, 0, null,  null);
        assertNotNull(res);
        assertEquals(10, res.size());

        res = subjectRepository.selectWithParams(5, 6, null, null);
        assertEquals(5, res.size());
        assertEquals("9",res.get(4).getName());

        List<Pair<String, String>> params = Collections.singletonList(new Pair<>("id", "DESC"));

        res = subjectRepository.selectWithParams(5, 6, params, null);
        assertEquals(5, res.size());
        assertEquals("0", res.get(3).getName());

        params = Collections.singletonList(new Pair<>("name", "1"));

        res = subjectRepository.selectWithParams(1000, 0, null, params);
        assertEquals(1, res.size());
        assertEquals("1", res.get(0).getName());
    }

}