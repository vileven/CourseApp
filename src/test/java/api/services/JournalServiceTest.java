package api.services;

import api.Application;
import api.utils.response.journal.JournalResponseRow;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Vileven on 20.05.17.
 */
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Sql(scripts = "../../filling.sql")
public class JournalServiceTest {

    @Autowired
    private JournalService journalService;

    @Autowired
    private JdbcTemplate template;

    @Test
    public void getGroupJournal() {
        final List<JournalResponseRow> res = journalService.getGroupJournal(-1L, -1L);

        assertEquals(10, res.size());
        res.forEach(journalRow -> assertEquals(2, journalRow.getClasses().size()));
    }

    @Test
    public void addAttendance() {
        journalService.addAttendance(-1, -3, true,10, null);

        assertEquals(Integer.valueOf(10), template.queryForObject("SELECT mark FROM attendances " +
                "WHERE class_id = -1 AND student_id = -3", Integer.class));
    }

}