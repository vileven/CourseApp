package api.services;

import api.Application;
import api.models.User;
import api.repositories.UserRepository;
import api.utils.info.UserCreationInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class AccountServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder encoder;

    private User user;

    @Before
    public void setup() {
        userRepository.deleteAll();
        user = new User(1,"email@mail.ru", encoder.encode("qwerty123"),"sergey",
                "volodin",null,"about");
        user = userRepository.create(user);
        assertNotNull(user);
        assertFalse(user.isNew());
    }


    @Test
    public void createUser() throws Exception {
        final UserCreationInfo userData = new UserCreationInfo(null, 1, "mail@mail.ru", "password",
                "sergey", "peshkov", "fox");
        final byte[] array = new byte[] {0,2,1,3,5,14,6,36,53,64,56,2};

        final User createdUser = accountService.createUser(userData, array);

        assertNotNull(createdUser);
        assertFalse(createdUser.isNew());
        assertEquals("mail@mail.ru", createdUser.getEmail());
        assertNotEquals("password", createdUser.getPassword());
    }

    @Test
    public void createUserWithExistsEmail() throws Exception {
        final UserCreationInfo userData = new UserCreationInfo(null, 1, "email@mail.ru", "password",
                "sergey", "peshkov", "fox");
        final User createdUser = accountService.createUser(userData);
        assertNull(createdUser);
    }

    @Test
    public void changeEmail() {
        assertNotEquals(null, user);
        accountService.changeEmail(user.getId(), "e@yandex.ru");
        final User changedUser = userRepository.find(user.getId());
        assertNotNull(changedUser);
        assertNotEquals(user.getEmail(), changedUser.getEmail());
        assertEquals("e@yandex.ru", changedUser.getEmail());
    }

    @Test
    public void changePassword() {
        accountService.changePassword(user.getId(), "123qwerty");
        final User changedUser = userRepository.find(user.getId());
        assertNotNull(changedUser);
        assertNotEquals(user.getPassword(), changedUser.getPassword());
        assertTrue(encoder.matches("123qwerty", changedUser.getPassword()));
    }

    @Test
    public void deleteUser() {
        accountService.deleteUserById(user.getId());
        final User deletedUser = userRepository.find(user.getId());
        assertNull(deletedUser);
    }

    @Test
    public void authenticationSuccess() {
        final User authUser = accountService.authenticateUser(user.getEmail(), "qwerty123");
        assertNotNull(authUser);
        assertEquals(user, authUser);
    }

    @Test
    public void authenticationError() {
        User authUser = accountService.authenticateUser(user.getEmail(), "wrong password");
        assertNull(authUser);

        authUser = accountService.authenticateUser("Unexpected email", "qwerty123");
        assertNull(authUser);
    }

}