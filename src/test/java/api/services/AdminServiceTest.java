package api.services;

import api.Application;
import api.repositories.GroupRepository;
import api.utils.error.PermissionDeniedException;
import api.utils.info.AcceptRequestInfo;
import api.utils.info.IdInfo;
import api.utils.response.UserResponseBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

import java.util.List;

import static api.controllers.SessionController.USER_ID;
import static org.junit.Assert.*;


/**
 * Created by Vileven on 19.05.17.
 */
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Sql(scripts = "../../filling.sql")
public class AdminServiceTest {

    @Autowired
    private AdminService adminService;

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private GroupRepository groupRepository;

    @Test
    public void acceptRequest() throws PermissionDeniedException {
        final HttpSession session = new MockHttpSession();
        session.setAttribute(USER_ID, 0L);
        adminService.acceptRequest(new AcceptRequestInfo(-1L, -3L), session);
        final List<UserResponseBody> res = groupRepository.getStudents(-3L, 1000, 0, null, null);
        assertEquals(1, res.size());
        assertEquals(Integer.valueOf(0), template.queryForObject("SELECT count(*) FROM  requests", Integer.class));
    }

    @Test
    public void deleteRequest() throws PermissionDeniedException {
        final HttpSession session = new MockHttpSession();
        session.setAttribute(USER_ID, 0L);
        adminService.deleteRequest(new IdInfo(-1L), session);
        assertEquals(Integer.valueOf(0), template.queryForObject("SELECT count(*) FROM  requests", Integer.class));
    }
}