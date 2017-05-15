package api.repositories;

import api.Application;
import api.models.ClassModel;
import api.models.Course;
import api.models.Group;
import api.models.Subject;
import api.utils.error.EntityNotFoundException;
import api.utils.pair.Pair;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Vileven on 15.05.17.
 */
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ClassRepositoryTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private CourseRepository courseRepository;

    private Group group;

    @Autowired
    private SubjectRepository subjectRepository;

    private Subject subject;

    @Autowired
    private ClassRepository classRepository;

    private ClassModel classModel;

    @Before
    public void setup() {
        template.update("DELETE FROM classes");
        final Course course = courseRepository.create(new Course("name"));
        assertNotNull(course);

        group = groupRepository.create(new Group(course.getId(), "ИУ6-43"));
        assertNotNull(group);

        subject = subjectRepository.create(new Subject(course.getId(), "math"));
        assertNotNull(subject);

        final SimpleJdbcInsert insert = new SimpleJdbcInsert(template).withTableName("classes").usingGeneratedKeyColumns("id");
        classModel = new ClassModel("topic", subject.getId(), group.getId(), "2017-05-05 14:00:00",
                "2017-05-05 15:30:00");

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("topic", classModel.getTopic());
        parameters.put("subject_id", classModel.getSubject());
        parameters.put("group_id", classModel.getGroup());
        parameters.put("begin_time", classModel.getBegin());
        parameters.put("end_time", classModel.getEnd());
        final Number id = insert.executeAndReturnKey(parameters);
        classModel.setId((Long) id);
    }

    @Test
    public void create() {
        final ClassModel createdClass = classRepository.create(new ClassModel("topic", subject.getId(), group.getId(),
                "2017-05-06 14:00:00", "2017-05-06 15:30:00"));
        assertNotNull(createdClass);
        assertFalse(createdClass.isNew());
    }

    @Test
    public void find() {
        final ClassModel findedClass = classRepository.find(classModel.getId());
        assertNotNull(findedClass);
        assertEquals(classModel, findedClass);
    }

    @Test
    public void update() throws EntityNotFoundException {
        classModel.setTopic("eee");
        final ClassModel updatedClass = classRepository.update(classModel);
        assertEquals(classModel, updatedClass);
        assertEquals(updatedClass.getTopic(), classModel.getTopic());
    }

    @Test
    public void delete() {
        classRepository.delete(classModel.getId());

        assertEquals(0, template.query("SELECT * FROM classes", classRepository.getMapper()).size());
    }

    @Test
    public void selectWithParams() {
        for (int i = 0; i < 10; i++) {
            classRepository.create(new ClassModel(Integer.toString(i), classModel.getSubject(), classModel.getGroup(),
                    classModel.getBegin(), classModel.getEnd()));
        }

        List<ClassModel> res = classRepository.selectWithParams(10, 0, null,  null);
        assertNotNull(res);
        assertEquals(10, res.size());

        res = classRepository.selectWithParams(5, 6, null, null);
        assertEquals(5, res.size());
        assertEquals("9",res.get(4).getTopic());

        List<Pair<String, String>> params = Collections.singletonList(new Pair<>("id", "DESC"));

        res = classRepository.selectWithParams(5, 6, params, null);
        assertEquals(5, res.size());
        assertEquals("0", res.get(3).getTopic());

        params = Collections.singletonList(new Pair<>("topic", "1"));

        res = classRepository.selectWithParams(1000, 0, null, params);
        assertEquals(1, res.size());
        assertEquals("1", res.get(0).getTopic());
    }
}