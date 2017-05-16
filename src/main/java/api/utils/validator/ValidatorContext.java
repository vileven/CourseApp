package api.utils.validator;


import api.services.AccountService;
import api.validators.EmailValidator;
import api.validators.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ValidatorContext {

    private final AccountService accountService;

    @Autowired
    public ValidatorContext(AccountService accountService) {
        this.accountService = accountService;
        this.accountService.checkAdmin();
    }

    @Bean
    public PasswordValidator passwordValidator() {
        return new PasswordValidator();
    }

    @Bean
    public EmailValidator emailValidator() {
        return new EmailValidator(accountService);
    }

}
