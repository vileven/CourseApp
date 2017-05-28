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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Vileven on 28.05.17.
 */
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc(print = MockMvcPrint.DEFAULT)
@Sql(scripts = "../../filling.sql")
public class MarkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getMark() throws Exception {
        mockMvc
                .perform(get("/mark/-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("неуд"))
                .andExpect(jsonPath("subject_name").value("Math"))
        ;
    }

    @Test
    public void createMarkSuccess() throws Exception {
        mockMvc
                .perform(post("/mark/create")
                        .sessionAttr(USER_ID, -21L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"min\":100," +
                                " \"max\":300," +
                                " \"name\":\"БОГ\"," +
                                " \"subject_id\":-1" +
                                '}'))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("БОГ"))
                .andExpect(jsonPath("id").isNumber())
        ;
    }

    @Test
    public void createMarkError() throws Exception {
        mockMvc
                .perform(post("/mark/create")
                        .sessionAttr(USER_ID, -23L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"min\":100," +
                                " \"max\":300," +
                                " \"name\":\"БОГ\"," +
                                " \"subject_id\":-1" +
                                '}'))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(ErrorCodes.PERMISSION_DENIED))
        ;
    }

    @Test
    public void updateMark() throws  Exception {
        mockMvc
                .perform(post("/mark/update")
                        .sessionAttr(USER_ID, -21L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"id\":-1," +
                                "\"min\":100," +
                                " \"max\":300," +
                                " \"name\":\"БОГ\"," +
                                " \"subject_id\":-1" +
                                '}'))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("БОГ"))
                .andExpect(jsonPath("id").value(-1))
        ;
    }

    @Test
    public void deleteMark() throws Exception {
        mockMvc
                .perform(post("/mark/delete")
                        .sessionAttr(USER_ID, -23L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"id\":-5" +
                                '}'))
                .andExpect(status().isOk())
                .andExpect(jsonPath("msg").value("success"))
        ;
    }

    @Test
    public void selectMark() throws Exception {
        mockMvc
                .perform(post("/mark/select")
                        .sessionAttr(USER_ID, -21L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"limit\":1," +
                                "\"offset\":1," +
                                "\"orders\": [" +
                                "[\"id\", \"ASC\"]" +
                                "]," +
                                "\"filters\": [" +
                                "[\"name\", \"уд\"]" +
                                ']' +
                                '}'))
                .andExpect(jsonPath("$..entries.length()").value(1))
                .andExpect(jsonPath("total").value(2));
    }
}