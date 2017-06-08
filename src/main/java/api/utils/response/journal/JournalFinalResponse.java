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

    @JsonProperty("marks")
    private final List<api.models.Mark> marks;

    private final List<JournalResponseRow> entries;

    public String getGroupName() {
        return groupName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public List<api.models.Mark> getMarks() {
        return marks;
    }

    public JournalFinalResponse(String groupName, String subjectName, List<api.models.Mark> marks, List<JournalResponseRow> entries) {
        this.groupName = groupName;
        this.subjectName = subjectName;
        this.marks = marks;

        this.entries = entries;
    }

    public List<JournalResponseRow> getEntries() {
        return entries;
    }
}
