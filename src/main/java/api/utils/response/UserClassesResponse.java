package api.utils.response;

import api.utils.response.generic.ResponseBody;

import java.util.List;

/**
 * Created by Vileven on 14.05.17.
 */
public class UserClassesResponse extends ResponseBody {

    public final List<UserClass> classes;

    public UserClassesResponse(int status, String msg, List<UserClass> classes) {
        super(status, msg);
        this.classes = classes;
    }
}
