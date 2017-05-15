package api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;


public class ClassModel extends Model<Long> {

    private Long id;
    private String topic;

    @JsonProperty("subject_id")
    private Long subject;

    @JsonProperty("group_id")
    private Long group;

    @JsonProperty("begin_id")
    private String begin;

    @JsonProperty("end_id")
    private String end;

    public ClassModel(Long id, String topic, Long subject, Long group, String begin, String end) {
        this.id = id;
        this.topic = topic;
        this.subject = subject;
        this.group = group;
        this.begin = begin;
        this.end = end;
    }

    public ClassModel(String topic, Long subject, Long group, String begin, String end) {
        this.topic = topic;
        this.subject = subject;
        this.group = group;
        this.begin = begin;
        this.end = end;
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

    public Long getSubject() {
        return subject;
    }

    public void setSubject(Long subject) {
        this.subject = subject;
    }

    public Long getGroup() {
        return group;
    }

    public void setGroup(Long group) {
        this.group = group;
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
}
