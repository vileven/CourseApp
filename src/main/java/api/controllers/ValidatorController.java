package api.controllers;

import api.services.AccountService;
import api.utils.info.ValueInfo;
import api.utils.response.Response;
import api.utils.validator.ValidatorMessage;
import api.utils.validator.generic.Validator;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:3000", "*", "http://127.0.0.1:3000"})
@RestController
@RequestMapping(path = "/validator")
public class ValidatorController {

    protected final AccountService accountService;
    protected final ApplicationContext appContext;

    @Autowired
    public ValidatorController(AccountService accountService, ApplicationContext appContext) {
        this.accountService = accountService;
        this.appContext = appContext;
    }


    /**
     * Выполнить валидацию данных
     * @param name имя валидатора
     * @return json с результатом работы валидатора
     */
    @PostMapping("/{name}")
    public ResponseEntity<?> getUserById(@PathVariable String name, @RequestBody ValueInfo validation) {
        final Object validator;

        try {
            validator = this.appContext.getBean(name + "Validator");

            if (!(validator instanceof Validator)) {
                throw new NoSuchBeanDefinitionException(name + "Validator");
            }
        } catch (NoSuchBeanDefinitionException e) {
            return Response.badValidator();
        }

        final Iterable<ValidatorMessage> messages = ((Validator) validator).validate(validation.getValue());
        return ResponseEntity.ok(messages);
    }

}
