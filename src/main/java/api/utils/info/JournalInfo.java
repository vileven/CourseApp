package api.utils.info;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
\\\\
 */
public class JournalInfo {
    public final Long subjectId;
    public final Long groupId;

    @JsonCreator
    public JournalInfo(@JsonProperty("subject_id") Long subjectId, @JsonProperty("group_id") Long groupId) {
        this.subjectId = subjectId;
        this.groupId = groupId;
    }
}
