package api.controllers;

import api.Application;
import api.models.Course;
import api.models.Group;
import api.models.User;
import api.repositories.CourseRepository;
import api.repositories.GroupRepository;
import api.repositories.SubjectRepository;
import api.repositories.UserRepository;
import api.utils.ErrorCodes;
import api.utils.response.SubjectResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static api.controllers.SessionController.USER_ID;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc(print = MockMvcPrint.DEFAULT)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Course course;

    private User admin;

    @Before
    public void setup() throws Exception {
        courseRepository.deleteAll();
        course = courseRepository.create(new Course("some course"));
        assertNotNull(course);

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
    public void getCourse() throws Exception, java.io.UnsupportedEncodingException {
        mockMvc
                .perform(get("/course/"+course.getId())
                        .sessionAttr(USER_ID, admin.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(course.getId()))
                .andExpect(jsonPath("name").value(course.getName()))
        ;
    }

    @Test
    public void createCourse() throws Exception {
        mockMvc
                .perform(post("/course/create")
                        .sessionAttr(USER_ID, admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"name\":\"created_course\"" +
                                '}'))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("created_course"))
                .andExpect(jsonPath("id").exists())
        ;
    }

    @Test
    public void itShouldNotWorkWithoutAdminAuth() throws Exception {
        mockMvc
                .perform(post("/course/create")
                        .sessionAttr(USER_ID, admin.getId() + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"name\":\"created_course\"" +
                                '}'))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(ErrorCodes.PERMISSION_DENIED))
                .andExpect(jsonPath("error").value("permission denied"))
        ;
    }

    @Test
    public void updateCourse() throws Exception {
        mockMvc
                .perform(post("/course/update")
                        .sessionAttr(USER_ID, admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"id\":\""+course.getId()+"\"," +
                                "\"name\":\"updated_name\"" +
                                '}'))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("updated_name"))
                .andExpect(jsonPath("id").value(course.getId()))
        ;
    }

    @Test
    public void deleteCourse() throws Exception {
        mockMvc
                .perform(post("/course/delete")
                        .sessionAttr(USER_ID, admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"id\":\""+course.getId()+ '"' +
                                '}'))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals("success",result.getResponse().getContentAsString()))
        ;

        assertNull(courseRepository.find(course.getId()));
    }

    @Test
    public void selectCourses() throws Exception {
        courseRepository.create(new Course("second"));
        mockMvc
                .perform(post("/course/select")
                        .sessionAttr(USER_ID, admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"limit\":100," +
                                "\"offset\":0," +
                                "\"orders\": [" +
                                "[\"id\", \"ASC\"]" +
                                "]," +
                                "\"filters\": [" +
                                "[\"name\", \"some\"]" +
                                ']' +
                                '}'))
                .andExpect(jsonPath("$..entries.length()").value(1))
                .andExpect(jsonPath("total").value(1))
        ;
    }

    @Test
    public void getCourseGroupsAndSubjects() throws Exception {

        IntStream.iterate(0, x -> x+1).limit(10).mapToObj(x -> new Group(course.getId(),
                course.getName(), String.valueOf(x))).forEach(x -> groupRepository.create(x));

        IntStream.iterate(0, x -> x+1).limit(10).mapToObj(x -> new SubjectResponse(course.getId(),
                course.getName(), String.valueOf(x))).forEach(x -> subjectRepository.create(x));

        mockMvc
                .perform(get("/course/"+course.getId()+"/subjectsAndGroups"))
                .andExpect(jsonPath("$..groups.length()").value(10))
                .andExpect(jsonPath("$..subjects.length()").value(10))
        ;
    }

}