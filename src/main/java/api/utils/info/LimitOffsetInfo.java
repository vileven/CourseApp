package api.utils.info;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Vileven on 07.06.17.
 */
public class LimitOffsetInfo {
    public final Integer limit;
    public final Integer offset;

    @JsonCreator
    public LimitOffsetInfo(@JsonProperty("limit") Integer limit, @JsonProperty("offset") Integer offset) {
        this.limit = limit;
        this.offset = offset;
    }
}
