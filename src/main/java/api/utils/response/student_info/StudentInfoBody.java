package api.utils.response.student_info;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Vileven on 28.05.17.
 */
public class StudentInfoBody {
    @JsonProperty("course_id")
    public final Long courseId;

    @JsonProperty("course_name")
    public final String courseName;

    @JsonProperty("group_id")
    public final Long groupId;

    @JsonProperty("group_name")
    public final String groupName;

    @JsonProperty("subjects")
    public final List<StudentInfoBody> subjects;


    public StudentInfoBody(Long courseId, String courseName, Long groupId, String groupName, List<StudentInfoBody> subjects) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.groupId = groupId;
        this.groupName = groupName;
        this.subjects = subjects;
    }
}
