package api.repositories;

import api.Application;
import api.models.ClassModel;
import api.models.Course;
import api.models.Group;
import api.utils.error.EntityNotFoundException;
import api.utils.response.SubjectResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Vileven on 15.05.17.
 */
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Sql(scripts = "../../filling.sql")
public class ClassRepositoryTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private ClassRepository classRepository;



    @Test
    public void create() {
        final ClassModel createdClass = classRepository.create(new ClassModel("topic", -1L, -2L
                , -21L, "2017-05-06 14:00:00", "2017-05-06 15:30:00", "213"));
        assertNotNull(createdClass);
        assertFalse(createdClass.isNew());
    }


    @Test
    public void find() {
        final ClassModel findedClass = classRepository.find(-2L);
        assertNotNull(findedClass);
        assertEquals("2m", findedClass.getTopic());
    }

    @Test
    public void update() throws EntityNotFoundException {
        final ClassModel classModel = classRepository.find(-2L);
        assertNotNull(classModel);

        classModel.setTopic("eee");
        final ClassModel updatedClass = classRepository.update(classModel);
        assertEquals(classModel, updatedClass);
        assertEquals(updatedClass.getTopic(), classModel.getTopic());
    }

    @Test
    public void delete() {
        final ClassModel classModel = classRepository.create(new ClassModel("topic", -1L, -2L
                , -21L, "2017-05-06 14:00:00", "2017-05-06 15:30:00", "213"));
        assertNotNull(classModel);
        classRepository.delete(classModel.getId());

        assertEquals(0, template.query("SELECT id FROM classes WHERE id = ?",
                ((rs, rowNum) -> rs.getInt(1)),
                classModel.getId()).size());
    }
}