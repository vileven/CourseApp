package api.controllers;

import api.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static api.controllers.SessionController.USER_ID;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Vileven on 16.05.17.
 */

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc(print = MockMvcPrint.DEFAULT)
@Sql(scripts = "../../filling.sql")
public class ProfessorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getSubjectsGroups() throws Exception {
        mockMvc
                .perform(get("/professor/"+ -21 +"/subjectsGroups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(2))
        ;
    }

    @Test
    public void getClasses() throws Exception {
        mockMvc
                .perform(get("/professor/"+ -21 +"/classes?from=2017-05-09&to=2017-05-11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(3))
        ;
    }

    @Test
    public void createRequest() throws Exception {
        mockMvc
                .perform(post("/professor/createAttendencies")
                        .sessionAttr(USER_ID, -21L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"class_id\":\"-2\"," +
                                "\"student_id\":\"-14\"," +
                                "\"mark\":\"10\"," +
                                "\"comment\":\"some_comment\"" +
                                '}'))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals("success", result.getResponse().getContentAsString()))
        ;
    }
}