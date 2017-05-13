package api.services;

import api.models.User;
import api.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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


}
