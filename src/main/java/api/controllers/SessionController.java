package api.controllers;

import api.models.User;
import api.services.AccountService;
import api.utils.info.UserAuthInfo;
import api.utils.response.Response;
import api.utils.response.generic.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(path = "/session")
public class SessionController {
    protected final AccountService accountService;
    protected final ApplicationContext appContext;

    public static final String USER_ID = "USER_ID";

    @Autowired
    public SessionController(AccountService accountService, ApplicationContext appContext) {
        this.accountService = accountService;
        this.appContext = appContext;
    }

    /**
     * Залогинить пользователя
     * @param requestBody login и password из тела запроса в json
     * @param session объект <code>HttpSession</code> сессии пользователя
     * @return json <code>User</code> ответ если OK, иначе <code>HTTP</code> код соответсвующей ошибки
     */
    @PostMapping("/login")
    public ResponseEntity<? extends ResponseBody> loginUser(@RequestBody UserAuthInfo requestBody, HttpSession session) {

        final User user = accountService.authenticateUser(requestBody.getEmail(), requestBody.getPassword());
        if (user == null) {
            return Response.badLoginOrPassword();
        }

        session.setAttribute(USER_ID, user.getId());

        return Response.ok("Logged in");
    }


    /**
     * Разлогин
     * @param session объект <code>HttpSession</code> сессии
     * @return json ответ если OK, иначе <code>HTTP</code> код соответсвующей ошибки
     */
    @PostMapping("/logout")
    public ResponseEntity<? extends ResponseBody> logoutUser(HttpSession session) {
        session.invalidate();
        return Response.ok("User session deleted");
    }


    /**
     * Вернуть залогиненного пользователя
     * @param session объект <code>HttpSession</code> сессии
     * @return json <code>User</code>
     */
    @GetMapping("/current")
    public ResponseEntity<? extends ResponseBody> getLoggedUser(HttpSession session) {

        final User currentUser;
        final Object id = session.getAttribute(USER_ID);

        if (id instanceof Long) {
            currentUser = accountService.getUserById((Long) id);
            if (currentUser == null) {
                return Response.invalidSession();
            }
        } else {
            return Response.invalidSession();
        }

        return Response.okWithUser(currentUser, "success");
    }

}
