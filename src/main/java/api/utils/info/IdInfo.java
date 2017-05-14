package api.utils.info;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Vileven on 14.05.17.
 */
public class IdInfo {
    public Long getId() {
        return id;
    }

    private final Long id;

    @JsonCreator
    public IdInfo(@JsonProperty Long id) {
        this.id = id;
    }
}
