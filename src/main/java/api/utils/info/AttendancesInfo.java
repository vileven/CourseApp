package api.utils.info;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Vileven on 16.05.17.
 */
public class AttendancesInfo {
    public final Long classId;
    public final Long studentId;
    public final Integer mark;
    public final String comment;
    public final boolean attendance;

    @JsonCreator
    public AttendancesInfo(@JsonProperty("class_id") Long classId, @JsonProperty("student_id") Long studentId,
                           @JsonProperty("mark") Integer mark, @JsonProperty("comment") String comment,
                           @JsonProperty("attendance") boolean attendance) {
        this.classId = classId;
        this.studentId = studentId;
        this.mark = mark;
        this.comment = comment;
        this.attendance = attendance;
    }
}
