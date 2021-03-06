package api.utils.info;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Vileven on 13.05.17.
 */
public class ValueInfo {
    private final String value;

    @JsonCreator
    ValueInfo(
            @JsonProperty(value = "value", required = true) String value
    ) {
        this.value = value;
    }

    public String getValue() { return value; }
}

