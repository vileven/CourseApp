package api.utils.info;

import api.utils.validator.FieldValidates;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Vileven on 13.05.17.
 */
public final class UserCreationInfo {
    private final Integer role;
    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final String about;

    @JsonCreator
    public UserCreationInfo(@JsonProperty("role") Integer role,@JsonProperty("email") String email,
                            @JsonProperty("password") String password, @JsonProperty("first_name") String firstName,
                            @JsonProperty("last_name") String lastName, @JsonProperty("about") String about) {
        this.role = role;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.about = about;
    }

    public Integer getRole() {
        return role;
    }

    @FieldValidates(validators = {"email"})
    public String getEmail() {
        return email;
    }

    @FieldValidates(validators = {"password"})
    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAbout() {
        return about;
    }
}
