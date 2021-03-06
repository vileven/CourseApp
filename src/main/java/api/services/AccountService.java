package api.services;

import api.models.User;
import api.repositories.UserRepository;
import api.utils.error.PermissionDeniedException;
import api.utils.info.IdInfo;
import api.utils.info.SelectParametersInfo;
import api.utils.info.UserCreationInfo;
import api.utils.info.UserUpdateInfo;
import api.utils.pair.Pair;
import api.utils.response.UserResponseBody;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

import static api.controllers.SessionController.USER_ID;

/**
 * Created by Vileven on 13.05.17.
 */
@Service
public class AccountService {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    protected final UserRepository userRepository;
    protected final PasswordEncoder encoder;
    private final JdbcTemplate template;


    @Autowired
    AccountService(UserRepository userRepository, PasswordEncoder encoder, JdbcTemplate template) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.template = template;
    }

    private boolean isAdmin(HttpSession session) {
        final Object number = session.getAttribute(USER_ID);
        if (number == null) {
            return false;
        }
        final User user = userRepository.find((Long) number);
        return user != null && user.getRole() == 0;

    }

    public void checkAdmin() {
        final User user = userRepository.findByEmail("volodin.sergey11@gmail.com");
        if (user == null) {
            userRepository.create(new User(0, "volodin.sergey11@gmail.com", encoder.encode("bmstuthebest"),
                    "sergey", "volodin", null, "admin"));
        }
    }

    @Nullable
    public User createUser(UserCreationInfo userData, HttpSession session, @Nullable byte[] avatar) {
        final int role = 1;
        final String encodedPassword = encoder.encode(userData.getPassword());
        final User newUser = new User(role, userData.getEmail(), encodedPassword,
                userData.getFirstName(), userData.getLastName(), avatar, userData.getAbout());

        return userRepository.create(newUser);
    }

    @Nullable
    public User createUser(UserCreationInfo userData, HttpSession session) {
        return createUser(userData, session, null);
    }

    @Nullable
    public User authenticateUser(String email, String password) {
        final User user = userRepository.findByEmail(email);

        if (user == null) {
            return null;
        }

        if (encoder.matches(password, user.getPassword())) {
            return user;
        }

        return null;
    }

    public void deleteUser(IdInfo info, HttpSession session) throws PermissionDeniedException {
        if (!isAdmin(session)) {
            throw new PermissionDeniedException("permission denied");
        }

        userRepository.delete(info.getId());
    }

    @Nullable
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Nullable
    public User getUserById(long id) {
        return userRepository.find(id);
    }

    public void changeEmail(long id, String email) {
        userRepository.updateEmail(id, email);
    }

    public void changePassword(long id, String password) {
        userRepository.updatePassword(id, encoder.encode(password));
    }

    public User update(UserUpdateInfo info, HttpSession session) throws PermissionDeniedException {

        if (info.getRole() != null && !isAdmin(session)) {
            throw new PermissionDeniedException("permission denied");
        }

        return userRepository.update(new User(info.getId(), info.getRole(), info.getEmail(), "",
                info.getFirstName(), info.getLastName(), null, info.getAbout()));
    }

    public void deleteUserById(long id) {
        userRepository.delete(id);
    }

    public boolean hasEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }


    public List<UserResponseBody> selectUsersWithParams(SelectParametersInfo info, HttpSession session) throws PermissionDeniedException {
        if (!isAdmin(session)) {
            throw new PermissionDeniedException("permission denied");
        }

        return userRepository.selectWithParams(info.getLimit(), info.getOffset(), info.getOrders(), info.getFilters());
    }

    public Integer getCount(List<Pair<String, String>> filters) {

        final StringBuilder sqlBuilder = new StringBuilder().append("SELECT count(*) FROM users ");

        if (filters != null && !filters.isEmpty()) {
            sqlBuilder.append(" WHERE ");
            for (int i = 0; i < filters.size(); i++) {
                if (filters.get(i).getKey().equals("role")) {
                    sqlBuilder
                            .append(filters.get(i).getKey())
                            .append(" =  ")
                            .append(filters.get(i).getValue())
                    ;
                } else {
                    sqlBuilder
                            .append(filters.get(i).getKey())
                            .append(" ~* '")
                            .append(filters.get(i).getValue())
                            .append('\'')
                    ;
                }
                if (i != filters.size() - 1) {
                    sqlBuilder.append(" AND ");
                }
            }
        }

        return template.queryForObject(sqlBuilder.toString(), Integer.class);
    }
}
