package api.utils.response;

import api.models.Model;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Vileven on 17.05.17.
 */
public class SubjectResponse extends Model<Long> {

    private Long id;

    @JsonProperty("course_id")
    private Long courseId;

    @JsonProperty("course_name")
    private String courseName;

    private String name;

    public SubjectResponse(Long id, Long courseId, String courseName, String name) {
        this.id = id;
        this.courseId = courseId;
        this.courseName = courseName;
        this.name = name;
    }

    public SubjectResponse(Long courseId, String courseName, String name) {
        this.courseId = courseId;
        this.courseName = courseName;

        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourseName() {
        return courseName;
    }
}
