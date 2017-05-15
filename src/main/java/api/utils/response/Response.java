package api.utils.response;

import api.models.Course;
import api.models.Group;
import api.models.User;
import api.utils.ErrorCodes;
import api.utils.response.generic.ResponseBody;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Response {

    public static ResponseEntity<? extends ResponseBody> ok(String msg) {
        return ResponseEntity.ok(new ResponseBody(ErrorCodes.SUCCESS, msg));
    }

    public static ResponseEntity<?> okWithUser(User user) {
        return ResponseEntity.ok(new UserResponseBody(user.getId(), user.getRole(), user.getEmail(),
                user.getFirstName(), user.getLastName(), user.getAbout()));
    }

    public static ResponseEntity<? extends ResponseBody> notFound(int status, String msg) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBody(status, msg));
    }

    public static ResponseEntity<? extends ResponseBody> badRequest(int status, String msg) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBody(status, msg));
    }


    public static ResponseEntity<? extends ResponseBody> userNotFound() {
        return Response.notFound(ErrorCodes.USER_NOT_FOUND, "User not found");
    }

    public static ResponseEntity<? extends ResponseBody> userAlreadyExists() {
        return Response.badRequest(ErrorCodes.USER_ALREADY_EXISTS, "User already exists");
    }

    public static ResponseEntity<? extends ResponseBody> invalidSession() {
        return Response.badRequest(ErrorCodes.SESSION_INVALID, "Invalid session");
    }

    public static ResponseEntity<? extends ResponseBody> badLoginOrPassword() {
        return Response.badRequest(ErrorCodes.BAD_LOGIN_OR_PASSWORD, "Bad login or password");
    }

    public static ResponseEntity<? extends ResponseBody> badValidator() {
        return Response.badRequest(ErrorCodes.BAD_VALIDATOR, "Bad validator");
    }

    public static ResponseEntity<?> okWithCourse(Course course) {
        return ResponseEntity.ok(course);
    }

//    public static ResponseEntity<?> okWithGroup(Group) {
//        return ResponseEntity.ok(new GroupBody)
//    }

}
