package api.utils.info;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Vileven on 15.05.17.
 */
public class ClassInfo {
    public Long getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public Long getSubject() {
        return subject;
    }

    public Long getGroup() {
        return group;
    }

    public String getBegin() {
        return begin;
    }

    public String getEnd() {
        return end;
    }

    private final Long id;
    private final String topic;
    private final Long subject;
    private final Long group;
    private final String begin;
    private final String end;
    private final String location;

    @JsonCreator
    public ClassInfo(@JsonProperty("id") Long id, @JsonProperty("topic") String topic,
                     @JsonProperty("subject_id") Long subject, @JsonProperty("group_id") Long group,
                     @JsonProperty("begin_time") String begin, @JsonProperty("end_time") String end,
                     @JsonProperty("location") String location) {
        this.id = id;
        this.topic = topic;
        this.subject = subject;
        this.group = group;
        this.begin = begin;
        this.end = end;
        this.location = location;
    }

    public String getLocation() {
        return location;
    }
}
