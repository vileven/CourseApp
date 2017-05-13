package api.controllers;

import api.models.User;
import api.services.AccountService;
import api.utils.info.UserCreationInfo;
import api.utils.info.UserEmailInfo;
import api.utils.info.UserPasswordInfo;
import api.utils.response.Response;
import api.utils.response.generic.ResponseBody;
import api.utils.validator.ValidatorChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import static api.controllers.SessionController.USER_ID;

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

    @PostMapping("/create")
    public ResponseEntity<? extends ResponseBody> createUser(@RequestBody UserCreationInfo requestBody) {

        if (!ValidatorChain.isValid(requestBody, false, this.appContext)) {
            return Response.badValidator();
        }

        accountService.createUser(requestBody);
        return Response.ok("User created");
    }

    @PostMapping("/changeEmail")
    public ResponseEntity<? extends ResponseBody> changeUserEmail(@RequestBody UserEmailInfo requestBody,
                                                                  HttpSession session) {
        if (ValidatorChain.isValid(requestBody, false, appContext)) {
            final Object id = session.getAttribute(USER_ID);
            if (id instanceof Long) {
                accountService.changeEmail((Long) id, requestBody.getValue());
                return Response.ok("Email changed");
            } else {
                return Response.invalidSession();
            }
        } else {
            return Response.badValidator();
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<? extends ResponseBody> changeUserPassword(@RequestBody UserPasswordInfo requestBody,
                                                                     HttpSession session) {
        if (ValidatorChain.isValid(requestBody, false, appContext)) {
            final Object id = session.getAttribute(USER_ID);
            if (id instanceof Long) {
                accountService.changePassword((Long) id, requestBody.getValue());
                return Response.ok("Login changed");
            } else {
                return Response.invalidSession();
            }
        } else {
            return Response.badValidator();
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<? extends ResponseBody> deleteUser(HttpSession session) {

        final Object id = session.getAttribute(USER_ID);

        if (id instanceof Long) {
            accountService.deleteUserById((Long) id);
        } else {
            return Response.invalidSession();
        }

        session.invalidate();
        return Response.ok("User deleted");
    }



}
