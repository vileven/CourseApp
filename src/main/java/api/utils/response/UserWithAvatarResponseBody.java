package api.utils.response;

/**
 * Created by Vileven on 13.05.17.
 */
public class UserWithAvatarResponseBody extends UserResponseBody {
    public UserWithAvatarResponseBody(Long id, Integer role, String email, String firstName, String lastName, String about) {
        super(id, role, email, firstName, lastName, about);
    }
//
//    public UserWithAvatarResponseBody(int status, String msg, Integer role, String email, String firstName, String lastName, String about) {
//        super(status, msg, role, email, firstName, lastName, about);
//    }
}
