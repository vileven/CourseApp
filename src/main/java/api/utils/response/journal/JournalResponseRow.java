package api.utils.response.journal;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Vileven on 21.05.17.
 */
public class JournalResponseRow {
    @JsonProperty("student_id")
    private final Long studentId;

    @JsonProperty("student_first_name")
    private final String studentFirstName;

    @JsonProperty("student_last_name")
    private final String studentLastName;

    @JsonProperty("total")
    private final Integer total;

    @JsonProperty("mark_name")
    private final String markName;

    private final List<JournalClass> classes;

    public Integer getTotal() {
        return total;
    }

    public String getMarkName() {
        return markName;
    }

    public JournalResponseRow(Long studentId, String studentFirstName, String studentLastName, Integer total, String markName, List<JournalClass> classes) {
        this.studentId = studentId;
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.total = total;

        this.markName = markName;
        this.classes = classes;
    }

    public Long getStudentId() {
        return studentId;
    }

    public String getStudentFirstName() {
        return studentFirstName;
    }

    public List<JournalClass> getClasses() {
        return classes;
    }

    public String getStudentLastName() {
        return studentLastName;
    }
}
