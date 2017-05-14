package api.utils.info;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Vileven on 14.05.17.
 */
public class SubjectInfo {
    private final Long id;
    private final Long courseId;
    private final String name;

    @JsonCreator
    public SubjectInfo(@JsonProperty("id") Long id, @JsonProperty("course_id") Long courseId, @JsonProperty("name") String name) {
        this.id = id;
        this.courseId = courseId;
        this.name = name;
    }

    public Long getCourseId() {
        return courseId;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}
