package api.utils.response.prof_info;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Vileven on 28.05.17.
 */
public class ProfInfoBody {
    @JsonProperty("course_id")
    public final Long courseId;

    @JsonProperty("course_name")
    public final String courseName;

    @JsonProperty("subject_id")
    public final Long subjectId;

    @JsonProperty("subject_name")
    public final String subjectName;

    @JsonProperty("groups")
    public final List<GroupInfoBody> groups;

    public ProfInfoBody(Long courseId, String courseName, Long subjectId, String subjectName, List<GroupInfoBody> groups) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.groups = groups;
    }
}
