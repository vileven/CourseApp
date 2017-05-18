package api.controllers;

import api.Application;
import api.models.*;
import api.repositories.*;
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
 * Created by Vileven on 15.05.17.
 */
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc(print = MockMvcPrint.DEFAULT)
public class ClassControllerTest {
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

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ClassRepository classRepository;

    private ClassModel classModel;

    private User admin;

    private SubjectResponse subject;

    private Group group;

    @Before
    public void setup() throws Exception {
        courseRepository.deleteAll();
        final Course course = courseRepository.create(new Course("course"));
        assertNotNull(course);

        subjectRepository.deleteAll();
        subject = subjectRepository.create(new SubjectResponse(course.getId(),course.getName(),"Math"));
        assertNotNull(subject);

        groupRepository.deleteAll();
        group = groupRepository.create(new Group(course.getId(), course.getName(), "ИУ6-43"));
        assertNotNull(group);

        classRepository.deleteAll();
        classModel = classRepository.create(new ClassModel("Machine learning", subject.getId(), group.getId(),
                "2017-05-06 14:00:00", "2017-05-06 15:30:00"));
        assertNotNull(classModel);

        userRepository.deleteAll();
        admin = userRepository.create(new User(0, "email@mail.ru",
                passwordEncoder.encode("qwerty123"), "sergey", "volodin",
                null,"about"));
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
    public void getClassInController() throws Exception {
        mockMvc
                .perform(get("/class/"+classModel.getId())
                        .sessionAttr(USER_ID, admin.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(classModel.getId()))
                .andExpect(jsonPath("group_id").value(group.getId()))
                .andExpect(jsonPath("subject_id").value(subject.getId()))
                .andExpect(jsonPath("topic").value(classModel.getTopic()))
        ;
    }

    @Test
    public void createClass() throws Exception {
        mockMvc
                .perform(post("/class/create")
                        .sessionAttr(USER_ID, admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                    "\"topic\":\"Normal equations\"," +
                                    "\"subject_id\":\""+subject.getId()+"\"," +
                                    "\"group_id\":\""+group.getId()+"\"," +
                                    "\"begin_time\":\"2017-05-06 14:00:00\"," +
                                    "\"end_time\":\"2017-05-06 15:30:00\"" +
                                '}'))
                .andExpect(status().isOk())
                .andExpect(jsonPath("topic").value("Normal equations"))
                .andExpect(jsonPath("id").exists())
        ;
    }

    @Test
    public void itShouldNotWorkWithoutAdminAuth() throws Exception {
        mockMvc
                .perform(post("/class/create")
                        .sessionAttr(USER_ID, admin.getId() + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"topic\":\"Normal equations\"," +
                                "\"subject_id\":\""+subject.getId()+"\"," +
                                "\"group_id\":\""+group.getId()+"\"," +
                                "\"begin_time\":\"2017-05-06 14:00:00\"," +
                                "\"end_time\":\"2017-05-06 15:30:00\"" +
                                '}'))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(ErrorCodes.PERMISSION_DENIED))
        ;
    }

    @Test
    public void updateClass() throws Exception {
        mockMvc
                .perform(post("/class/update")
                        .sessionAttr(USER_ID, admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"id\":\""+classModel.getId()+"\"," +
                                "\"topic\":\"updated_name\"," +
                                "\"subject_id\":\""+subject.getId()+"\"," +
                                "\"group_id\":\""+group.getId()+"\"," +
                                "\"begin_time\":\"2017-05-06 14:00:00\"," +
                                "\"end_time\":\"2017-05-06 15:30:00\"" +
                                '}'))
                .andExpect(status().isOk())
                .andExpect(jsonPath("topic").value("updated_name"))
                .andExpect(jsonPath("subject_id").value(subject.getId()))
                .andExpect(jsonPath("group_id").value(group.getId()))
                .andExpect(jsonPath("id").value(classModel.getId()))
        ;
    }

    @Test
    public void deleteClass() throws Exception {
        mockMvc
                .perform(post("/class/delete")
                        .sessionAttr(USER_ID, admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                    "\"id\":\""+classModel.getId()+ '"' +
                                '}'))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals("success",result.getResponse().getContentAsString()))
        ;

        assertNull(classRepository.find(classModel.getId()));
    }

    @Test
    public void select() throws  Exception {
        classRepository.create(new ClassModel("machine creating", classModel.getSubject(), classModel.getGroup(),
                classModel.getBegin(), classModel.getEnd()));
        classRepository.create(new ClassModel("maches loss", classModel.getSubject(), classModel.getGroup(),
                classModel.getBegin(), classModel.getEnd()));
        classRepository.create(new ClassModel("Normal equations", classModel.getSubject(), classModel.getGroup(),
                classModel.getBegin(), classModel.getEnd()));
        mockMvc
                .perform(post("/class/select")
                        .sessionAttr(USER_ID, admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"limit\":1," +
                                "\"offset\":1," +
                                "\"orders\": [" +
                                "[\"id\", \"ASC\"]" +
                                "]," +
                                "\"filters\": [" +
                                "[\"topic\", \"mach\"]" +
                                ']' +
                                '}'))
                .andExpect(jsonPath("$..entries.length()").value(1))
                .andExpect(jsonPath("total").value(3))
        ;
    }


}