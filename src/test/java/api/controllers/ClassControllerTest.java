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
import org.springframework.test.context.jdbc.Sql;
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
@Sql(scripts = "../../filling.sql")
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
//        subjectRepository.deleteAll();
//        groupRepository.deleteAll();
//        courseRepository.deleteAll();
//        final Course course = courseRepository.create(new Course("course"));
//        assertNotNull(course);
//
//
//        subject = subjectRepository.create(new SubjectResponse(course.getId(),course.getName(),"Math"));
//        assertNotNull(subject);
//
//
//        group = groupRepository.create(new Group(course.getId(), course.getName(), "ИУ6-43"));
//        assertNotNull(group);
//
//        classRepository.deleteAll();
////        classModel = classRepository.create(new ClassModel("Machine learning", subject.getId(), group.getId(),
////                , profId, profFirstName, profLastName, "2017-05-06 14:00:00", "2017-05-06 15:30:00", "213"));
//        assertNotNull(classModel);
        admin = userRepository.find(-25L);
        assertNotNull(admin);
    }

    @Test
    public void getClassInController() throws Exception {
        classModel = classRepository.find(-1L);
        mockMvc
                .perform(get("/class/"+classModel.getId())
                        .sessionAttr(USER_ID, admin.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(classModel.getId()))
                .andExpect(jsonPath("group_id").value(classModel.getGroupId()))
                .andExpect(jsonPath("subject_id").value(classModel.getSubjectId()))
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
                                    "\"subject_id\": "+-1L+"," +
                                    "\"group_id\":"+-2L+ ',' +
                                    "\"prof_id\":"+-21L+ ',' +
                                    "\"location\":\"432\"," +
                                    "\"begin_time\":\"2017-05-06 14:00:00\"," +
                                    "\"end_time\":\"2017-05-06 15:30:00\"" +
                                '}'))
                .andExpect(status().isOk())
                .andExpect(jsonPath("topic").value("Normal equations"))
                .andExpect(jsonPath("id").exists())
        ;
    }

    @Test
    public void createBatchCourses() throws Exception {
        mockMvc
                .perform(post("/class/createBatch")
                        .sessionAttr(USER_ID, admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"topic\":\"Normal equations\"," +
                                "\"subject_id\": "+-1L+"," +
                                "\"group_ids\": [ -2, -1 ]," +
                                "\"prof_id\": -21," +
                                "\"location\":\"432\"," +
                                "\"amount\": 10," +
                                "\"week_offset\": 1," +
                                "\"begin_time\":\"2017-05-06T14:00:00\"," +
                                "\"end_time\":\"2017-05-06T15:30:00\"" +
                                '}'))
                .andExpect(status().isOk())
                .andExpect(jsonPath("msg").value("success"))
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
                                "\"subject_id\": "+-1L+"," +
                                "\"group_id\":"+-2L+ ',' +
                                "\"prof_id\":"+-21L+ ',' +
                                "\"location\":\"432\"," +
                                "\"begin_time\":\"2017-05-06 14:00:00\"," +
                                "\"end_time\":\"2017-05-06 15:30:00\"" +
                                '}'))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(ErrorCodes.PERMISSION_DENIED))
        ;
    }

    @Test
    public void updateClass() throws Exception {
        classModel = classRepository.find(-1L);
        assertNotNull(classModel);
        assertEquals(Long.valueOf(-1), classModel.getId());
        mockMvc
                .perform(post("/class/update")
                        .sessionAttr(USER_ID, admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"id\":\""+classModel.getId()+"\"," +
                                "\"topic\":\"updated_name\"," +
                                "\"subject_id\":\""+classModel.getSubjectId()+"\"," +
                                "\"group_id\":\""+classModel.getGroupId()+"\"," +
                                "\"prof_id\":\""+classModel.getProfId()+"\"," +
                                "\"location\":\"432\"," +
                                "\"begin_time\":\"2017-05-06 14:00:00\"," +
                                "\"end_time\":\"2017-05-06 15:30:00\"" +
                                '}'))
                .andExpect(status().isOk())
                .andExpect(jsonPath("topic").value("updated_name"))
                .andExpect(jsonPath("subject_id").value(classModel.getSubjectId()))
                .andExpect(jsonPath("id").value(classModel.getId()))
        ;
    }

    @Test
    public void deleteClass() throws Exception {
        classModel = classRepository.create(new ClassModel("topic", -1L, -2L
                , -21L, "2017-05-06 14:00:00", "2017-05-06 15:30:00", "213"));
        assertNotNull(classModel);
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
//        classRepository.create(new ClassModel("machine creating", classModel.getSubjectId(), classModel.getGroupId(),
//                classModel.getBegin(), classModel.getEnd(), "213"));
//        classRepository.create(new ClassModel("maches loss", classModel.getSubjectId(), classModel.getGroupId(),
//                classModel.getBegin(), classModel.getEnd(), "213"));
//        classRepository.create(new ClassModel("Normal equations", classModel.getSubjectId(), classModel.getGroupId(),
//                classModel.getBegin(), classModel.getEnd(), "213"));

        mockMvc
                .perform(post("/class/select")
                        .sessionAttr(USER_ID, admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"limit\":2," +
                                "\"offset\":1," +
                                "\"orders\": [" +
                                "[\"id\", \"ASC\"]" +
                                "]," +
                                "\"filters\": [" +
                                "[\"topic\", \"1\"]" +
                                ']' +
                                '}'))
                .andExpect(jsonPath("$..entries.length()").value(2))
                .andExpect(jsonPath("total").value(3))
        ;
    }


}