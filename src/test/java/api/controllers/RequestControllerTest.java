package api.controllers;

import api.Application;
import api.utils.ErrorCodes;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Vileven on 19.05.17.
 */
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc(print = MockMvcPrint.DEFAULT)
@Sql(scripts = "../../filling.sql")
public class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void accept() throws Exception {
        mockMvc
                .perform(post("/request/accept")
                        .contentType(MediaType.APPLICATION_JSON)
                        .sessionAttr(USER_ID, 0L)
                        .content('{' +
                                    "\"request_id\":-1," +
                                    "\"group_id\":-3" +
                                '}'))
                .andExpect(status().isOk())
                .andExpect(jsonPath("msg").value("success"))
        ;
    }

    @Test
    public void delete() throws Exception {
        mockMvc
                .perform(post("/request/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .sessionAttr(USER_ID, 0L)
                        .content('{' +
                                "\"id\":-1" +
                                '}'))
                .andExpect(status().isOk())
                .andExpect(jsonPath("msg").value("success"))
        ;
    }

    @Test
    public void select() throws Exception {
        mockMvc
                .perform(post("/request/select")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"limit\":100," +
                                "\"offset\":0" +
                                '}'))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entries.length()").value(1))
        ;
    }
}