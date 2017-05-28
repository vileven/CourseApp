package api.utils.response.prof_info;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Vileven on 28.05.17.
 */
public class GroupInfoBody {
    @JsonProperty("group_id")
    public final Long groupId;

    @JsonProperty("group_name")
    public final String groupName;

    public GroupInfoBody(Long groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }
}
