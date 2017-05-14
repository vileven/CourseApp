package api.controllers;

import api.Application;
import api.models.Course;
import api.models.Subject;
import api.models.User;
import api.repositories.CourseRepository;
import api.repositories.SubjectRepository;
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
public class SubjectControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SubjectRepository subjectRepository;

    private Course course;

    private User admin;

    private Subject subject;

    @Before
    public void setup() throws Exception {
        courseRepository.deleteAll();
        course = courseRepository.create(new Course("course"));
        assertNotNull(course);

        subjectRepository.deleteAll();
        subject = subjectRepository.create(new Subject(course.getId(),"Math"));
        assertNotNull(subject);

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
                .andExpect(jsonPath("status").value(ErrorCodes.SUCCESS))
                .andReturn();

        assertEquals(admin.getId(), result.getRequest().getSession().getAttribute(USER_ID));
    }

    @Test
    public void getSubject() throws Exception {
        mockMvc
                .perform(get("/subject/"+subject.getId())
                        .sessionAttr(USER_ID, admin.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(subject.getId()))
                .andExpect(jsonPath("course_id").value(course.getId()))
                .andExpect(jsonPath("name").value(subject.getName()))
        ;
    }

    @Test
    public void createSubject() throws Exception {
        mockMvc
                .perform(post("/subject/create")
                        .sessionAttr(USER_ID, admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"course_id\":\""+course.getId()+"\"," +
                                "\"name\":\"created_subject\"" +
                                '}'))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("created_subject"))
                .andExpect(jsonPath("id").exists())
        ;
    }

    @Test
    public void updateSubject() throws Exception {
        mockMvc
                .perform(post("/subject/update")
                        .sessionAttr(USER_ID, admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"id\":\""+subject.getId()+"\"," +
                                "\"course_id\":\""+course.getId()+"\"," +
                                "\"name\":\"updated_name\"" +
                                '}'))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("updated_name"))
                .andExpect(jsonPath("course_id").value(course.getId()))
                .andExpect(jsonPath("id").value(subject.getId()))
        ;
    }

    @Test
    public void deleteSubject() throws Exception {
        mockMvc
                .perform(post("/subject/delete")
                        .sessionAttr(USER_ID, admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"id\":\""+subject.getId()+ '"' +
                                '}'))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals("success",result.getResponse().getContentAsString()))
        ;

        assertNull(subjectRepository.find(subject.getId()));
    }

    @Test
    public void selectSubjects() throws Exception {
        subjectRepository.create(new Subject(course.getId(),"physics"));
        subjectRepository.create(new Subject(course.getId(),"mathematics"));
        subjectRepository.create(new Subject(course.getId(), "Math Analisis"));
        mockMvc
                .perform(post("/subject/select")
                        .sessionAttr(USER_ID, admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"limit\":1," +
                                "\"offset\":1," +
                                "\"orders\": [" +
                                "[\"id\", \"ASC\"]" +
                                "]," +
                                "\"filters\": [" +
                                "[\"name\", \"math\"]" +
                                ']' +
                                '}'))
                .andExpect(jsonPath("length()").value(1));
    }

}