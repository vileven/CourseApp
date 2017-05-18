package api.utils.info;

import api.utils.validator.FieldValidates;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Vileven on 13.05.17.
 */
public final class UserCreationInfo {
    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;

    @ApiModelProperty(required = false)
    private final String about;

    @JsonCreator
    public UserCreationInfo( @JsonProperty("email") String email,
                            @JsonProperty("password") String password, @JsonProperty("first_name") String firstName,
                            @JsonProperty("last_name") String lastName, @JsonProperty("about") String about) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.about = about;
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
