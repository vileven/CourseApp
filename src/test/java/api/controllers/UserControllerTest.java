package api.controllers;

import api.Application;
import api.models.User;
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
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static api.controllers.SessionController.USER_ID;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Vileven on 13.05.17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc(print = MockMvcPrint.DEFAULT)
public class UserControllerTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    private Long id;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        userRepository.deleteAll();
        final User user = userRepository.create(new User(1, "email@mail.ru", passwordEncoder.encode("qwerty123"),
                "sergey", "volodin", null,"about"));
        assertNotNull(user);
        id = user.getId();
    }

    @Test
    public void getByIdSucces() throws Exception {
        mockMvc
                .perform(get(String.format("/user/%d", this.id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("first_name").value("sergey"))
                .andExpect(jsonPath("email").value("email@mail.ru"))
        ;
    }

    @Test
    public void getByIdUserNotFound() throws Exception {
        mockMvc
                .perform(get(String.format("/user/%d", this.id + 1)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("status").value(ErrorCodes.USER_NOT_FOUND));
        ;
    }

    @Test
    public void createUserSucces() throws Exception {
        mockMvc
                .perform(post("/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"role\":2," +
                                "\"email\":\"em221ail@mail.ru\"," +
                                "\"password\":\"3213124cdsdfsfgd\"," +
                                "\"first_name\":\"name\"," +
                                "\"last_name\":\"surname\"," +
                                "\"about\":\"about\"" +
                                '}'))
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value("em221ail@mail.ru"))
        ;
    }

    @Test
    public void createUserExistsEmail() throws Exception {
        mockMvc
                .perform(post("/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"role\":2," +
                                "\"email\":\"email@mail.ru\"," +
                                "\"password\":\"3213124cdsdfsfgd\"," +
                                "\"first_name\":\"name\"," +
                                "\"last_name\":\"surname\"," +
                                "\"about\":\"about\"" +
                                '}'))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(ErrorCodes.BAD_VALIDATOR))
                .andExpect(jsonPath("error").value("Bad validator"))
        ;
    }

    @Test
    public void createUserBadEmail() throws Exception {
        mockMvc
                .perform(post("/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"role\":2," +
                                "\"email\":\"email1mail.ru\"," +
                                "\"password\":\"3213124cdsdfsfgd\"," +
                                "\"first_name\":\"name\"," +
                                "\"last_name\":\"surname\"," +
                                "\"about\":\"about\"" +
                                '}'))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(ErrorCodes.BAD_VALIDATOR))
                .andExpect(jsonPath("error").value("Bad validator"))
        ;
    }

    @Test
    public void createUserBadPassword() throws Exception {
        mockMvc
                .perform(post("/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"role\":2," +
                                "\"email\":\"em221ail@mail.ru\"," +
                                "\"password\":\"s2\"," +
                                "\"first_name\":\"name\"," +
                                "\"last_name\":\"surname\"," +
                                "\"about\":\"about\"" +
                                '}'))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(ErrorCodes.BAD_VALIDATOR))
                .andExpect(jsonPath("error").value("Bad validator"))
        ;
    }

    @Test
    public void changeEmailSucces() throws Exception {
        mockMvc
                .perform(post("/user/changeEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .sessionAttr(USER_ID, this.id)
                        .content("{\"email\":\"email123@mail.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(ErrorCodes.SUCCESS))
        ;
    }

    @Test
    public void changeEmailSessionError() throws Exception {
        mockMvc
                .perform(post("/user/changeEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"email123@mail.com\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(ErrorCodes.SESSION_INVALID))
        ;
    }

    @Test
    public void changeEmailIncorrectEmail() throws Exception {
        mockMvc
                .perform(post("/user/changeEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .sessionAttr(USER_ID, this.id)
                        .content("{\"email\":\"12\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(ErrorCodes.BAD_VALIDATOR))
        ;
    }

    @Test
    public void changePasswordSucces() throws Exception {
        mockMvc
                .perform(post("/user/changePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .sessionAttr(USER_ID, this.id)
                        .content("{\"password\":\"qwerty123456\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(ErrorCodes.SUCCESS))
        ;
    }

    @Test
    public void changePasswordSessionError() throws Exception {
        mockMvc
                .perform(post("/user/changePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\":\"email123@mail.com\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(ErrorCodes.SESSION_INVALID))
        ;
    }

    @Test
    public void changePasswordIncorrectPassword() throws Exception {
        mockMvc
                .perform(post("/user/changePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .sessionAttr(USER_ID, this.id)
                        .content("{\"password\":\"12\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(ErrorCodes.BAD_VALIDATOR))
        ;
    }

    @Test
    public void deleteUserSucces() throws Exception {
        final User user = userRepository.create(new User(0, "email1@mail.ru", passwordEncoder.encode("qwerty123"),
                "sergey", "volodin", null,"about"));
        assertNotNull(user);
        mockMvc
                .perform(post("/user/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .sessionAttr(USER_ID, user.getId())
                        .content("{\"id\":\""+this.id+"\"}"))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals("success",result.getResponse().getContentAsString()))
        ;
    }

    @Test
    public void deleteUserWrongSession() throws Exception {
        mockMvc
                .perform(post("/user/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\""+this.id+"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(ErrorCodes.PERMISSION_DENIED))
        ;
    }

    @Test
    public void select() throws Exception {
        userRepository.create(new User(1, "physics","password","sergey",
                "volodin",null,"about"));
        userRepository.create(new User(1, "mathematics","password","sergey",
                "volodin",null,"about"));
        userRepository.create(new User(1, "Math  Analisis","password","sergey",
                "volodin",null,"about"));


        Long id = userRepository.create(new User(0, "math","password","sergey",
                "volodin",null,"about")).getId();

        mockMvc
                .perform(post("/user/select")
                        .sessionAttr(USER_ID, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"limit\":1," +
                                "\"offset\":1," +
                                "\"orders\": [" +
                                "[\"id\", \"ASC\"]" +
                                "]," +
                                "\"filters\": [" +
                                "[\"email\", \"math\"]" +
                                ']' +
                                '}'))
                .andExpect(jsonPath("length()").value(1));
    }

    @Test
    public void selectWithotArrays() throws Exception {
        userRepository.deleteAll();

        userRepository.create(new User(1, "physidcs","password","sergey",
                "volodin",null,"about"));
        userRepository.create(new User(1, "mathematics","password","sergey",
                "volodin",null,"about"));
        userRepository.create(new User(1, "Math Analisis","password","sergey",
                "volodin",null,"about"));


        Long id = userRepository.create(new User(0, "math","password","sergey",
                "volodin",null,"about")).getId();

        mockMvc
                .perform(post("/user/select")
                        .sessionAttr(USER_ID, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{' +
                                "\"limit\":1," +
                                "\"offset\":1," +
                                "\"orders\": [" +
                                "]," +
                                "\"filters\": [" +
                                "[\"email\", \"math\"]" +
                                ']' +
                                '}'))
                .andExpect(jsonPath("length()").value(1));
    }
}
