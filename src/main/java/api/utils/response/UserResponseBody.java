package api.utils.response;

import api.utils.response.generic.ResponseBody;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Тело ответа User
 */
public class UserResponseBody extends ResponseBody {

    public final Integer role;
    public final String email;

    @JsonProperty("first_name")
    public final String firstName;

    @JsonProperty("last_name")
    public final String lastName;

    public final String about;

    @JsonCreator
    public UserResponseBody(int status, String msg, Integer role, String email, String firstName, String lastName, String about) {
        super(status, msg);
        this.role = role;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.about = about;
    }
}
