package api.controllers;

import api.Application;
import api.models.Course;
import api.models.Group;
import api.models.User;
import api.repositories.CourseRepository;
import api.repositories.GroupRepository;
import api.repositories.UserRepository;
import api.utils.ErrorCodes;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static api.controllers.SessionController.USER_ID;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Vileven on 14.05.17.
 */
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc(print = MockMvcPrint.DEFAULT)
public class GroupControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GroupRepository groupRepository;

    private Course course;

    private User admin;

    private Group group;

    @Before
    public void setup() throws Exception {
        courseRepository.deleteAll();
        course = courseRepository.create(new Course("course"));
        assertNotNull(course);

        groupRepository.deleteAll();
        group = groupRepository.create(new Group(course.getId(),"ИУ6-43"));
        assertNotNull(group);

        userRepository.deleteAll();
        admin = userRepository.create(new User(0, "email@mail.ru", passwordEncoder.encode("qwerty123"),
                "sergey", "volodin", null,"about"));
        assertNotNull(admin);

        //login
        final MvcResult result = mockMvc
                .perform(post("/session/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"email\":\"email@mail.ru\"," +
                                "\"password\":\"qwerty123\"" +
                                '}'))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(admin.getId(), result.getRequest().getSession().getAttribute(USER_ID));
    }

    @Test
    public void getGroup() throws Exception {
        mockMvc
                .perform(get("/group/"+group.getId())
                        .sessionAttr(USER_ID, admin.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(group.getId()))
                .andExpect(jsonPath("course_id").value(course.getId()))
                .andExpect(jsonPath("name").value(group.getName()))
        ;
    }

    @Test
    public void createGroup() throws Exception {
        mockMvc
                .perform(post("/group/create")
                        .sessionAttr(USER_ID, admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"course_id\":\""+course.getId()+"\"," +
                                "\"name\":\"created_group\"" +
                                '}'))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("created_group"))
                .andExpect(jsonPath("id").exists())
        ;
    }

    @Test
    public void itShuoldNotWorkWithotAdminAuth() throws Exception {
        mockMvc
                .perform(post("/group/create")
                        .sessionAttr(USER_ID, admin.getId() - 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"course_id\":\""+course.getId()+"\"," +
                                "\"name\":\"created_group\"" +
                                '}'))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(ErrorCodes.PERMISSION_DENIED))
                .andExpect(jsonPath("error").value("permission denied"))
        ;
    }

    @Test
    public void updateGroup() throws Exception {
        mockMvc
                .perform(post("/group/update")
                        .sessionAttr(USER_ID, admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"id\":\""+group.getId()+"\"," +
                                "\"course_id\":\""+course.getId()+"\"," +
                                "\"name\":\"updated_name\"" +
                                '}'))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("updated_name"))
                .andExpect(jsonPath("course_id").value(course.getId()))
                .andExpect(jsonPath("id").value(group.getId()))
        ;
    }

    @Test
    public void deleteGroup() throws Exception {
        mockMvc
                .perform(post("/group/delete")
                        .sessionAttr(USER_ID, admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"id\":\""+group.getId()+ '"' +
                                '}'))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals("success",result.getResponse().getContentAsString()))
        ;

        assertNull(groupRepository.find(group.getId()));
    }

    @Test
    public void select() throws Exception {
        groupRepository.create(new Group(course.getId(),"second"));
        groupRepository.create(new Group(course.getId(),"ИУ6-42"));
        mockMvc
                .perform(post("/group/select")
                        .sessionAttr(USER_ID, admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"limit\":1," +
                                "\"offset\":0," +
                                "\"orders\": [" +
                                "[\"id\", \"ASC\"]" +
                                "]," +
                                "\"filters\": [" +
                                "[\"name\", \"ИУ6\"]" +
                                ']' +
                                '}'))
                .andExpect(jsonPath("$..entries.length()").value(1));
    }

}