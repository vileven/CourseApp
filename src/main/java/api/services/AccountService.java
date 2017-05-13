package api.services;

import api.models.User;
import api.repositories.UserRepository;
import api.utils.info.UserCreationInfo;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Vileven on 13.05.17.
 */
@Service
public class AccountService {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    protected final UserRepository userRepository;
    protected final PasswordEncoder encoder;

    @Autowired
    AccountService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Nullable
    public User createUser(UserCreationInfo userData, @Nullable byte[] avatar) {
        final String encodedPassword = encoder.encode(userData.getPassword());
        final User newUser = new User(userData.getRole(), userData.getEmail(), encodedPassword,
                userData.getFirstName(), userData.getLastName(), avatar, userData.getAbout());

        return userRepository.create(newUser);
    }

    @Nullable
    public User createUser(UserCreationInfo userData) {
        return createUser(userData, null);
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

    public void update(User user) {
        userRepository.update(user);
    }

    public void deleteUserById(long id) {
        userRepository.delete(id);
    }

    public boolean hasEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }

}
