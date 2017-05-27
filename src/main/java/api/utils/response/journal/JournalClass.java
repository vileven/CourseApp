package api.utils.response.journal;

import api.utils.info.AcceptRequestInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Vileven on 21.05.17.
 */
public class JournalClass {
    @JsonProperty("class_id")
    public final Long classId;

    @JsonProperty("class_begin_date")
    public final String beginDate;

    public void setMark(@Nullable Mark mark) {
        this.mark = mark;
    }

    private Mark mark;

    public JournalClass(Long classId, String beginDate, Mark mark) {
        this.classId = classId;
        this.beginDate = beginDate;
        this.mark = mark;
    }

    public JournalClass(Long classId, String beginDate) {
        this.classId = classId;
        this.beginDate = beginDate;
        this.mark = null;
    }

    public Mark getMark() {
        return mark;
    }
}
