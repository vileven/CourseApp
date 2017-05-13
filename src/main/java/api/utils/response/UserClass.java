package api.utils.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class UserClass {

    public final Long id;

    @JsonProperty("subject_id")
    public final Long subjectId;

    @JsonProperty("subject_name")
    public final String subjectName;

    @JsonProperty("group_id")
    public final Long groupId;

    @JsonProperty("group_name")
    public final String groupName;

    @JsonProperty("begin_time")
    public final Long begin;

    @JsonProperty("end_time")
    public final Long end;

    public Long getId() {
        return id;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public Long getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public Long getBegin() {
        return begin;
    }

    public Long getEnd() {
        return end;
    }

    public UserClass(Long id, Long subjectId, String subjectName, Long groupId, String groupName, Long begin,
                     Long end) {
        this.id = id;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.groupId = groupId;
        this.groupName = groupName;
        this.begin = begin;
        this.end = end;

    }
}
