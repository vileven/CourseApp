package api.utils.info;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Vileven on 06.06.17.
 */
public class IdsInfo {

    private final List<Long> ids;

    public List<Long> getIds() {
        return ids;
    }

    @JsonCreator
    public IdsInfo(@JsonProperty("ids") List<Long> ids) {
        this.ids = ids;
    }
}
