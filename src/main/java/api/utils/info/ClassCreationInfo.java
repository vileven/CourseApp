package api.utils.info;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Vileven on 19.05.17.
 */
public class ClassCreationInfo {

    public String getTopic() {
        return topic;
    }

    public Long getSubject() {
        return subject;
    }

    public List<Long> getGroups() {
        return groups;
    }

    public String getBegin() {
        return begin;
    }

    public String getEnd() {
        return end;
    }

    private final String topic;
    private final Long subject;
    private final List<Long> groups;
    private final Long prof;
    private final String begin;
    private final String end;
    private final String location;
    private final Integer amount;
    private final Integer weekOffset;

    @JsonCreator
    public ClassCreationInfo(@JsonProperty("topic") String topic, @JsonProperty("subject_id") Long subject,
                             @JsonProperty("group_ids") List<Long> groups, @JsonProperty("prof_id") Long prof,
                             @JsonProperty("begin_time") String begin, @JsonProperty("end_time") String end,
                             @JsonProperty("location") String location, @JsonProperty("amount") Integer amount,
                             @JsonProperty("week_offset") Integer weekOffset) {
        this.topic = topic;
        this.subject = subject;
        this.groups = groups;
        this.prof = prof;
        this.begin = begin;
        this.end = end;
        this.location = location;
        this.amount = amount;

        this.weekOffset = weekOffset;
    }

    public String getLocation() {
        return location;
    }

    public Long getProf() {
        return prof;
    }

    public Integer getAmount() {
        return amount;
    }

    public Integer getWeekOffset() {
        return weekOffset;
    }
}
