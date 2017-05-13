package api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;

/**
 * Модель пользователя
 */
public class User extends Model<Long> {
    private Long id;
    private Integer role;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private byte[] avatar;
    private String about;

    public User(Long id, Integer role, String email, String password, String firstName, String lastName,
                @Nullable byte[] avatar, String about) {
        this.id = id;
        this.role = role;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
        this.about = about;
    }

    public User(Integer role, String email, String password, String firstName, String lastName,
                @Nullable byte[] avatar, String about) {
        this.role = role;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
        this.about = about;
    }

    @Override
    public Long getId() {
        return id;
    }

    public Integer getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public String getAbout() {
        return about;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
