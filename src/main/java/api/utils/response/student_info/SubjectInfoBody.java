package api.utils.response.student_info;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Vileven on 28.05.17.
 */
public class SubjectInfoBody {
    @JsonProperty("subject_id")
    public final Long subjectId;

    @JsonProperty("subject_name")
    public final String subjectName;

    public SubjectInfoBody(Long subjectId, String subjectName) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
    }
}
