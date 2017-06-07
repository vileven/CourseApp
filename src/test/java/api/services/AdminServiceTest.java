package api.services;

import api.Application;
import api.models.ClassModel;
import api.models.Request;
import api.repositories.ClassRepository;
import api.repositories.GroupRepository;
import api.utils.error.PermissionDeniedException;
import api.utils.info.AcceptRequestInfo;
import api.utils.info.ClassCreationInfo;
import api.utils.info.IdInfo;
import api.utils.info.SelectParametersInfo;
import api.utils.pair.Pair;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collections;
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

    @Autowired
    private ClassRepository classRepository;

    @Test
    public void acceptRequest() throws PermissionDeniedException {
        final HttpSession session = new MockHttpSession();
        session.setAttribute(USER_ID, 0L);
        List<UserResponseBody> res = groupRepository.getStudents(-3L, 1000, 0, null, null);
        assertEquals(1, res.size());

        adminService.acceptRequest(new AcceptRequestInfo(-1L, -3L), session);

        res = groupRepository.getStudents(-3L, 1000, 0, null, null);
        assertEquals(2, res.size());

        assertEquals(Integer.valueOf(0), template.queryForObject("SELECT count(*) FROM  requests", Integer.class));
    }

    @Test
    public void deleteRequest() throws PermissionDeniedException {
        final HttpSession session = new MockHttpSession();
        session.setAttribute(USER_ID, 0L);
        adminService.deleteRequest(new IdInfo(-1L), session);
        assertEquals(Integer.valueOf(0), template.queryForObject("SELECT count(*) FROM  requests", Integer.class));
    }

    @Test
    public void createClassesBatch() throws PermissionDeniedException {
        final List<Long> groups = new ArrayList<>();
        groups.add(-1L);
        groups.add(-2L);
        final String begin = LocalDateTime.parse("2017-05-11 12:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).toString();
        final String end = LocalDateTime.parse("2017-05-11 14:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).toString();
        final ClassCreationInfo info = new ClassCreationInfo("", -1L, groups, -1L,
                begin, end, "501-u", 5, 1);
        final HttpSession session = new MockHttpSession();
        session.setAttribute(USER_ID, 0L);
        adminService.createBatchClass(info, session);

        List<Pair<String, String>> params = Collections.singletonList(new Pair<>("location", "501"));

        final List<ClassModel> classes = classRepository.selectWithParams(1000, 0, null, params);
        assertEquals(10, classes.size());
    }

    @Test
    public void selectRequests() {
        final List<Request> requests = adminService.selectRequests(100, 0);
        assertEquals(1, requests.size());
    }
}