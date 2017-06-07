package api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Vileven on 07.06.17.
 */
public class Request extends Model<Long> {

    private final Long id;

    @JsonProperty("course_id")
    private final Long courseId;

    @JsonProperty("course_name")
    private final String courseName;

    @JsonProperty("student_id")
    private final Long studentId;

    @JsonProperty("student_first")
    private final String studentFirst;

    @JsonProperty("student_last")
    private final String studentLast;

    public String getCourseName() {
        return courseName;
    }

    public String getStudentFirst() {
        return studentFirst;
    }

    public String getStudentLast() {
        return studentLast;
    }

    public Long getCourseId() {
        return courseId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public Request(Long id, Long courseId, String courseName, Long studentId, String studentFirst, String studentLast) {
        this.id = id;
        this.courseId = courseId;
        this.courseName = courseName;
        this.studentId = studentId;

        this.studentFirst = studentFirst;
        this.studentLast = studentLast;

    }

    @Override
    public Long getId() {
        return id;
    }
}
