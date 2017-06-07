package api.utils.response.journal;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Vileven on 07.06.17.
 */
public class JournalFinalResponse {

    @JsonProperty("group_name")
    public final String groupName;

    @JsonProperty("subject_name")
    public final String subjectName;

    private final List<JournalResponseRow> entries;

    public JournalFinalResponse(String groupName, String subjectName, List<JournalResponseRow> entries) {
        this.groupName = groupName;
        this.subjectName = subjectName;
        this.entries = entries;
    }

    public List<JournalResponseRow> getEntries() {
        return entries;
    }
}
