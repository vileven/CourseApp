package api.utils.info;

import api.utils.validator.FieldValidates;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Vileven on 17.05.17.
 */
public class UserUpdateInfo {
    private final Long id;
    private final Integer role;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String about;

    @JsonCreator
    public UserUpdateInfo(@JsonProperty("id") Long id, @JsonProperty("role") Integer role, @JsonProperty("email") String email,
                             @JsonProperty("first_name") String firstName,
                            @JsonProperty("last_name") String lastName, @JsonProperty("about") String about) {
        this.id = id;
        this.role = role;
        this.email = email;
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


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAbout() {
        return about;
    }

    public Long getId() {
        return id;
    }
}

