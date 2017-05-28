package api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Mark extends Model<Long> {
    private Long id;
    private Integer min;
    private Integer max;
    private String name;
    @JsonProperty("subject_id")
    private Long subjectId;
    @JsonProperty("subject_name")
    private String subjectName;


    @JsonCreator
    public Mark(@JsonProperty("id") Long id,@JsonProperty("min") Integer min,
                @JsonProperty("max") Integer max,@JsonProperty("name") String name,
                @JsonProperty("subject_id") Long subject,@JsonProperty("subject_name") String subjectName) {
        this.id = id;
        this.min = min;
        this.max = max;
        this.name = name;
        this.subjectId = subject;
        this.subjectName = subjectName;
    }

    public Mark(Integer min, Integer max, String name, Long subject, String subjectName) {
        this.min = min;
        this.max = max;
        this.name = name;
        this.subjectId = subject;
        this.subjectName = subjectName;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Long getSubjectId() {

        return subjectId;
    }

    public void setSubject(Long subject) {
        this.subjectId = subject;
    }


    public String getSubjectName() {
        return subjectName;
    }
}
