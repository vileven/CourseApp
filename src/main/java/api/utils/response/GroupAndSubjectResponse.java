package api.utils.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Vileven on 16.05.17.
 */
public class GroupAndSubjectResponse {
    @JsonProperty("subject_id")
    public final Long subjectId;

    @JsonProperty("subject_name")
    public final String subjectName;

    @JsonProperty("group_id")
    public final Long groupId;

    @JsonProperty("group_name")
    public final String groupName;

    @JsonCreator
    public GroupAndSubjectResponse(Long subjectId, String subjectName, Long groupId, String groupName) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.groupId = groupId;
        this.groupName = groupName;
    }
}
