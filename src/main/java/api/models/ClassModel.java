package api.models;

import java.time.LocalDateTime;


public class ClassModel extends Model<Long> {

    private Long id;
    private String topic;
    private Long subject;
    private Long group;
    private LocalDateTime begin;
    private LocalDateTime end;

    public ClassModel(Long id, String topic, Long subject, Long group, LocalDateTime begin, LocalDateTime end) {
        this.id = id;
        this.topic = topic;
        this.subject = subject;
        this.group = group;
        this.begin = begin;
        this.end = end;
    }

    public ClassModel(String topic, Long subject, Long group, LocalDateTime begin, LocalDateTime end) {
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

    public LocalDateTime getBegin() {
        return begin;
    }

    public void setBegin(LocalDateTime begin) {
        this.begin = begin;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    @Override
    public Long getId() {
        return id;
    }
}
