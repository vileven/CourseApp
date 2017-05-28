package api.repositories;

import api.Application;
import api.models.Mark;
import api.utils.error.EntityNotFoundException;
import api.utils.pair.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Vileven on 28.05.17.
 */
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Sql(scripts = "../../filling.sql")
public class MarkRepositoryTest {

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private MarkRepository markRepository;

    @Test
    public void create() {
        final Mark mark = markRepository.create(new Mark(20, 40, "ОК", -2L, ""));
        assertNotNull(mark);
        assertFalse(mark.isNew());
        assertEquals(new Integer(20), mark.getMin());
    }

    @Test
    public void find() {
        final Mark mark = markRepository.find(-2);
        assertNotNull(mark);
        assertFalse(mark.isNew());
        assertEquals("уд", mark.getName());
    }

    @Test
    public void update() throws EntityNotFoundException {
        final Mark mark = markRepository.find(-2);
        assertNotNull(mark);
        mark.setName("АВА");
        final Mark updatedMark = markRepository.update(mark);
        assertNotNull(updatedMark);
        assertEquals(mark, updatedMark);
        assertEquals("АВА", updatedMark.getName());
    }

    @Test
    public void delete() {
        final Mark mark = markRepository.create(new Mark(20, 40, "ОК", -2L, ""));
        assertNotNull(mark);
        assertFalse(mark.isNew());

        markRepository.delete(mark.getId());

        assertEquals(0, template.query("SELECT id FROM marks WHERE id = ?",
                (rs, rowNum) -> rs.getInt(1), mark.getId() ).size() );
    }

    @Test
    public void selectWithParams() {
        List<Mark> res = markRepository.selectWithParams(10, 0 , null , null);
        assertEquals(5, res.size());

        res = markRepository.selectWithParams(10, 0 , null ,
                Collections.singletonList(new Pair<>("subject_id", "-1")));
        assertEquals(4, res.size());

        final List<Pair<String, String>> filters = new ArrayList<Pair<String, String>>() {{
            add(new Pair<>("subject_id", "-1"));
            add(new Pair<>("name", "уд"));
        }};

        res = markRepository.selectWithParams(10, 0 , null , filters);
        assertEquals(2, res.size());

        res = markRepository.selectWithParams(10, 0,
                Collections.singletonList(new Pair<>("id", "DESC")), filters);
        assertEquals(2, res.size());
        assertEquals(Long.valueOf(-1), res.get(0).getId());

        res = markRepository.selectWithParams(1, 0,
                Collections.singletonList(new Pair<>("id", "DESC")), filters);
        assertEquals(1, res.size());
    }
}