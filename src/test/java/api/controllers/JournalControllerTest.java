package api.controllers;

import api.Application;
import api.utils.info.JournalInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Vileven on 22.05.17.
 */
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc(print = MockMvcPrint.DEFAULT)
@Sql(scripts = "../../filling.sql")
public class JournalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate template;

    @Test
    public void show() throws Exception {
        mockMvc
                .perform(post("/journal/show")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"subject_id\":-1,\"group_id\":-1}"))
                .andExpect(jsonPath("$.entries.length()").value(10))
                .andExpect(jsonPath("$.entries.[2].classes.length()").value(2))
        ;
    }

    @Test
    public void addAttendanceSuccess() throws Exception {
        mockMvc
                .perform(post("/journal/addAttendance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"class_id\":\"-1\"," +
                                "\"student_id\":\"-3\"," +
                                "\"attendance\": true," +
                                "\"mark\":\"11\"," +
                                "\"comment\":\"some_comment\"" +
                                '}'))
                .andExpect(status().isOk())
                .andExpect(jsonPath("msg").value("success"))
        ;

        assertEquals(Integer.valueOf(11), template.queryForObject("SELECT mark " +
                "FROM attendances WHERE class_id = -1 AND student_id = -3", Integer.class));
    }

    @Test
    public void addAttendanceUpdate() throws Exception {
        mockMvc
                .perform(post("/journal/addAttendance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"class_id\":\"-1\"," +
                                "\"student_id\":\"-1\"," +
                                "\"attendance\": true," +
                                "\"mark\":\"11\"," +
                                "\"comment\":\"some_comment\"" +
                                '}')
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("msg").value("success"))
        ;


        assertEquals(Integer.valueOf(11), template.queryForObject("SELECT mark FROM attendances " +
                "WHERE class_id = -1 AND student_id = -1 ",Integer.class));
        assertEquals("some_comment", template.queryForObject("SELECT comment FROM attendances " +
                "WHERE class_id = -1 AND student_id = -1 ", String.class));
    }

    @Test
    public void deleteAttendance() throws Exception {
        mockMvc
                .perform(post("/journal/addAttendance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"class_id\":\"-1\"," +
                                "\"student_id\":\"-1\"," +
                                "\"attendance\": false" +
                                '}')
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("msg").value("success"))
        ;

        assertEquals(0, template.query("SELECT mark FROM attendances " +
                "WHERE class_id = -1 AND student_id = -1", (rs, rowNum) -> rs.getInt("mark")).size())
        ;
    }

    @Test
    public void doNothing() throws  Exception {
        mockMvc
                .perform(post("/journal/addAttendance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"class_id\":\"-1\"," +
                                "\"student_id\":\"-3\"," +
                                "\"attendance\": false" +
                                '}')
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("msg").value("success"))
        ;
    }







}