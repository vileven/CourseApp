package api.utils.response;

import api.utils.response.generic.ResponseBody;

/**
 * Тело ответа User
 */
public class UserResponseBody extends ResponseBody {

    public final Integer role;
    public final String email;
    public final String firstName;
    public final String lastName;
    public final String about;


    public UserResponseBody(int status, String msg, Integer role, String email, String firstName, String lastName, String about) {
        super(status, msg);
        this.role = role;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.about = about;
    }
}
