package api.repositories;

import api.Application;
import api.models.Course;
import api.models.Group;
import api.utils.pair.Pair;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
public class GroupRepositoryTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private CourseRepository courseRepository;

    private Group group;

    private final RowMapper<Group> groupMapper = (((rs, rowNum) -> new Group(rs.getLong("id"),
            rs.getLong("course_id"), rs.getString("course_name"), rs.getString("name"))));

    @Before
    public void setup() {
        template.update("DELETE FROM groups");
        final Course course = courseRepository.create(new Course("name"));
        assertNotNull(course);
        final SimpleJdbcInsert insert = new SimpleJdbcInsert(template).withTableName("groups").usingGeneratedKeyColumns("id");
        final Map<String, Object> parameters = new HashMap<>();
        group = new Group(course.getId(), course.getName(), "name");
        parameters.put("course_id", group.getCourseId());
        parameters.put("name", group.getName());
        final Number id = insert.executeAndReturnKey(parameters);
        group.setId((Long)id);
    }

    @Test
    public void create() {
        final Group createdSubject = groupRepository.create(new Group(group.getCourseId(), "", "create"));
        assertNotNull(createdSubject);
        assertFalse(createdSubject.isNew());
        assertEquals("create", createdSubject.getName());
    }

    @Test
    public void createNotExistCourse() {
        final Group subject = groupRepository.create(new Group(-1L, "", "name"));
        assertNull(subject);
    }

    @Test
    public void find() {
        final Group findedSubject = groupRepository.find(group.getId());
        assertNotNull(findedSubject);
        assertEquals(group, findedSubject);
    }

    @Test
    public void findByName() {
        final List<Group> res = groupRepository.findByName("name");
        assertEquals(1, res.size());
        assertEquals(group, res.get(0));
    }


    @Test
    public void selectWithParams() {
        for (int i = 0; i < 10; i++) {
            groupRepository.create(new Group(group.getCourseId(), "", Integer.toString(i)));
        }

        List<Group> res = groupRepository.selectWithParams(10, 0, null, null);
        assertNotNull(res);
        assertEquals(10, res.size());

        res = groupRepository.selectWithParams(5, 6, null, null);
        assertEquals(5, res.size());

        List<Pair<String, String>> params = Collections.singletonList(new Pair<>("id", "DESC"));

        res = groupRepository.selectWithParams(5, 6, params, null);
        assertEquals(5, res.size());
        assertEquals("0", res.get(3).getName());

        params = Collections.singletonList(new Pair<>("name", "1"));

        res = groupRepository.selectWithParams(1000, 0, null, params);
        assertEquals(1, res.size());
        assertEquals("1", res.get(0).getName());
    }


}