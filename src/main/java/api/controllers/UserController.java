package api.controllers;

import api.models.User;
import api.services.AccountService;
import api.utils.ErrorCodes;
import api.utils.error.PermissionDeniedException;
import api.utils.info.*;
import api.utils.response.Response;
import api.utils.response.SelectBody;
import api.utils.response.UserResponseBody;
import api.utils.response.generic.ResponseBody;
import api.utils.validator.ValidatorChain;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import static api.controllers.SessionController.USER_ID;

@CrossOrigin(origins = {"http://localhost:3000", "*", "http://127.0.0.1:3000"})
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


    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = UserResponseBody.class),
            @ApiResponse(code = 404, message = "Not Found", response = ResponseBody.class)
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        final User user = accountService.getUserById(id);
        if (user == null) {
            return Response.userNotFound();
        }

        return Response.okWithUser(user);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = UserResponseBody.class),
            @ApiResponse(code = 400, message = "Bad request", response = ResponseBody.class)
    })
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserCreationInfo requestBody, HttpSession session) {

        if (!ValidatorChain.isValid(requestBody, false, this.appContext)) {
            return Response.badValidator();
        }

        final User user = accountService.createUser(requestBody, session);
        return Response.okWithUser(user);
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
    public ResponseEntity<?> deleteUser(@RequestBody IdInfo info, HttpSession session) {

        try {
            accountService.deleteUser(info, session);

            return ResponseEntity.ok("success");
        } catch (PermissionDeniedException e) {

            return Response.badRequest(ErrorCodes.PERMISSION_DENIED, e.message);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = UserResponseBody.class),
            @ApiResponse(code = 400, message = "Bad request", response = ResponseBody.class)
    })
    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateInfo info, HttpSession session) {
        try {
            final User user = accountService.update(info, session);
            return Response.okWithUser(user);

        } catch (PermissionDeniedException e) {

            return Response.badRequest(ErrorCodes.PERMISSION_DENIED, e.message);
        }
    }
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = SelectBody.class),
            @ApiResponse(code = 400, message = "Bad request", response = ResponseBody.class)
    })
    @PostMapping("/select")
    public ResponseEntity<?> selectUsers(@RequestBody SelectParametersInfo info, HttpSession session) {
        try {
            return ResponseEntity.ok(new SelectBody(accountService.selectUsersWithParams(info, session), accountService.getCount(info.getFilters())));
        } catch (PermissionDeniedException e) {
            return Response.badRequest(ErrorCodes.PERMISSION_DENIED, e.message);
        }
    }


//    @GetMapping("/getInfo")
//    public ResponseEntity<?> getUserInfo(HttpSession session) {
//
//    }

}
