package api.models;

import com.fasterxml.jackson.annotation.JsonProperty;


public class ClassModel extends Model<Long> {

    private Long id;
    private String topic;

    @JsonProperty("subject_id")
    private Long subjectId;

    @JsonProperty("subject_name")
    private String subjectName;

    @JsonProperty("group_id")
    private Long groupId;

    @JsonProperty("group_name")
    private String groupName;

    @JsonProperty("prof_id")
    private Long profId;

    @JsonProperty("prof_first_name")
    private String profFirstName;

    @JsonProperty("prof_last_name")
    private String profLastName;

    @JsonProperty("begin_id")
    private String begin;

    @JsonProperty("end_id")
    private String end;

    private String location;

    public ClassModel(Long id, String topic, Long subjectId, String subjectName, Long groupId, String groupName, Long profId, String profFirstName, String profLastName, String begin, String end, String location) {
        this.id = id;
        this.topic = topic;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.groupId = groupId;
        this.groupName = groupName;
        this.profId = profId;
        this.profFirstName = profFirstName;
        this.profLastName = profLastName;
        this.begin = begin;
        this.end = end;
        this.location = location;
    }

    public ClassModel(String topic, Long subjectId, String subjectName, Long groupId, String groupName, Long profId, String profFirstName, String profLastName, String begin, String end, String location) {
        this.topic = topic;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.groupId = groupId;
        this.groupName = groupName;
        this.profId = profId;
        this.profFirstName = profFirstName;
        this.profLastName = profLastName;
        this.begin = begin;
        this.end = end;
        this.location = location;
    }

    public ClassModel(String topic, Long subjectId, Long groupId, Long profId, String begin, String end, String location) {
        this.topic = topic;
        this.subjectId = subjectId;
        this.subjectName = null;
        this.groupId = groupId;
        this.groupName = null;
        this.profId = profId;
        this.profFirstName = null;
        this.profLastName = null;
        this.begin = begin;
        this.end = end;
        this.location = location;
    }

    public ClassModel(Long id, String topic, Long subjectId, Long groupId, Long profId, String begin, String end, String location) {
        this.id = id;
        this.topic = topic;
        this.subjectId = subjectId;
        this.subjectName = null;
        this.groupId = groupId;
        this.groupName = null;
        this.profId = profId;
        this.profFirstName = null;
        this.profLastName = null;
        this.begin = begin;
        this.end = end;
        this.location = location;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getGroupName() {
        return groupName;
    }

    public Long getProfId() {
        return profId;
    }

    public String getProfFirstName() {
        return profFirstName;
    }

    public String getProfLastName() {
        return profLastName;
    }
}
