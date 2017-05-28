package api.services;

import api.Application;
import api.models.ClassModel;
import api.utils.error.PermissionDeniedException;
import api.utils.info.AttendancesInfo;
import api.utils.response.GroupAndSubjectResponse;
import api.utils.response.prof_info.ProfInfoBody;
import api.utils.response.student_info.StudentInfoBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;

import static api.controllers.SessionController.USER_ID;
import static org.junit.Assert.*;

/**
 * Created by Vileven on 16.05.17.
 */

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Sql(scripts = "../../filling.sql")
public class ProfessorServiceTest {
    @Autowired
    private JdbcTemplate template;

    @Autowired
    private ProfessorService professorService;

    @Test
    public void getGroupsAndSubject() {
        final List<GroupAndSubjectResponse> res = professorService.getGroupsAndSubjects(-22);
        assertEquals(2, res.size());
    }

    @Test
    public void getProfClasses() {
        final List<ClassModel> res = professorService.getProfClasses(-21, "2017-05-09", "2017-05-11");
        assertEquals(2, res.size());
    }

    @Test
    public void getProfClassesEmplty() {
        final List<ClassModel> res = professorService.getProfClasses(-21, "2017-05-09", null);
        assertEquals(3, res.size());
    }

    @Test
    public void addAttendencies() throws PermissionDeniedException {
        HttpSession session = new MockHttpSession();
        session.setAttribute(USER_ID, -21L);
        professorService.addAttendencies(new AttendancesInfo(-1L, -3L,10, null, false), session);


        assertEquals(Integer.valueOf(10), template.queryForObject("SELECT mark FROM attendances WHERE class_id = -1 AND student_id = -3;",
                (RowMapper<Integer>)((rs, rowNum) -> rs.getInt("mark"))));
    }

    @Test
    public void getInfo() {
        final List<ProfInfoBody> res = professorService.getInfo(-21);
        assertNotNull(res);

        assertEquals(2, res.size());
        assertEquals(2,res.get(1).groups.size());
        assertEquals("IU6-41", res.get(1).groups.get(1).groupName);
    }

}