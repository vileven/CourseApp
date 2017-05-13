package api.controllers;

import api.models.User;
import api.services.AccountService;
import api.utils.response.Response;
import api.utils.response.generic.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/user")
public class UserController {
    protected final AccountService accountService;
    protected final ApplicationContext appContext;


    @Autowired
    public UserController(AccountService accountService, ApplicationContext appContext) {
        this.accountService = accountService;
        this.appContext = appContext;
    }


    @GetMapping("/{id}")
    public ResponseEntity<? extends ResponseBody> getUserById(@PathVariable Long id) {
        final User user = accountService.getUserById(id);
        if (user == null) {
            return Response.userNotFound();
        }

        return Response.okWithUser(user, "success");
    }



}
