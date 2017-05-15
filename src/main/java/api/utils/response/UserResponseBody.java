package api.utils.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Тело ответа User
 */
public class UserResponseBody  {

    public final Long id;
    public final Integer role;
    public final String email;

    @JsonProperty("first_name")
    public final String firstName;

    @JsonProperty("last_name")
    public final String lastName;

    public final String about;

    @JsonCreator
    public UserResponseBody(Long id, Integer role, String email, String firstName, String lastName, String about) {
        this.id = id;
        this.role = role;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.about = about;
    }
}
