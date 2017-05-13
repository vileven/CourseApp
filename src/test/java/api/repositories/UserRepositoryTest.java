package api.repositories;

import api.Application;
import api.models.User;
import api.utils.pair.Pair;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by Vileven on 13.05.17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UserRepositoryTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @Before
    public void setup() {
        template.update("DELETE FROM users");
        user = new User(1,"email@mail.ru","password","sergey",
                "volodin",null,"about");

        final SimpleJdbcInsert insertUser = new SimpleJdbcInsert(template).withTableName("users").usingGeneratedKeyColumns("id");

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("role", user.getRole());
        parameters.put("email", user.getEmail());
        parameters.put("password", user.getPassword());
        parameters.put("first_name", user.getFirstName());
        parameters.put("last_name", user.getLastName());
        parameters.put("avatar", user.getAvatar());
        parameters.put("about", user.getAbout());

        final Number id = insertUser.executeAndReturnKey(parameters);
        user.setId((Long) id);
    }


    @Test
    public void create() throws DataAccessException {
        template.update("DELETE FROM users");
        final User newUser = userRepository.create(user);

        assertNotNull(newUser);
        assertFalse(newUser.isNew());

        assertEquals(user.getEmail(), newUser.getEmail());
        assertEquals(user.getFirstName(), newUser.getFirstName());
    }

    @Test
    public void update() throws DataAccessException {
        user.setEmail("mail@mail.ru");
        final int code = userRepository.update(user);

        assertEquals(Code.OK, code);

        final String query = "SELECT * FROM users WHERE id=" + user.getId();
        final User newUser = template.queryForObject(query, userMapper);

        assertNotNull(newUser);
        assertEquals(user, newUser);
        assertEquals("mail@mail.ru", newUser.getEmail());
    }

    @Test
    public void delete() throws DataAccessException {
        userRepository.delete(user.getId());

        exception.expect(EmptyResultDataAccessException.class);
        final String query = "SELECT * FROM users WHERE id=" + user.getId();
        template.queryForObject(query, userMapper);
    }

    @Test
    public void find() throws DataAccessException {
        final User findedUser = userRepository.find(user.getId());

        assertNotNull(findedUser);
        assertEquals(user, findedUser);
    }

    @Test
    public void findByEmail() throws DataAccessException {
        final User findedUser = userRepository.findByEmail(user.getEmail());

        assertNotNull(findedUser);
        assertEquals(user, findedUser);
    }

    @Test
    public void deleteAll() throws DataAccessException {
        userRepository.deleteAll();

        final List<User> res = template.query("SELECT * FROM users", userMapper);
        assertEquals(0, res.size());
    }

    @Test
    public void selectWithParams() {

        for (int i = 0; i < 10; i++) {
            final User newUser = new User(1,"email@mail.ru" + i,"password","sergey",
                    "volodin",null,"about");
            userRepository.create(newUser);
        }

        List<User> res = userRepository.selectWithParams(10, 0, null);
        assertNotNull(res);
        assertEquals(10, res.size());


        res = userRepository.selectWithParams(5, 6, null);
        assertEquals(5, res.size());
        assertEquals('9',res.get(4).getEmail().charAt(13));

        final List<Pair<String, String>> params = Collections.singletonList(new Pair<>("id", "DESC"));

        res = userRepository.selectWithParams(5, 6, params);
        assertEquals(5, res.size());
        assertEquals("0", String.valueOf(res.get(3).getEmail().charAt(13)));
    }

    private final RowMapper<User> userMapper = ((rs, rowNum) -> new User(rs.getLong("id"),
            rs.getInt("role"), rs.getString("email"),
            rs.getString("password"), rs.getString("first_name"),
            rs.getString("last_name"), rs.getBytes("avatar"),
            rs.getString("about")));

}