package api.utils.info;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Vileven on 18.05.17.
 */
public class AcceptRequestInfo {
    public final Long id;
    public final Long groupId;

    @JsonCreator
    public AcceptRequestInfo(@JsonProperty("request_id") Long id, @JsonProperty("group_id") Long groupId) {
        this.id = id;
        this.groupId = groupId;
    }
}
