package api.utils.info;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Vileven on 16.05.17.
 */
public class TimeInfo {
    public final String from;
    public final String to;

    @JsonCreator
    public TimeInfo(@JsonProperty("from") String from, @JsonProperty("to") String to) {
        this.from = from;
        this.to = to;
    }
}
