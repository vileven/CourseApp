package api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Vileven on 07.06.17.
 */
public class Request extends Model<Long> {

    private final Long id;

    @JsonProperty("course_name")
    private final String courseName;

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

    public Request(Long id, String courseName, String studentFirst, String studentLast) {
        this.id = id;
        this.courseName = courseName;
        this.studentFirst = studentFirst;
        this.studentLast = studentLast;

    }

    @Override
    public Long getId() {
        return id;
    }
}
