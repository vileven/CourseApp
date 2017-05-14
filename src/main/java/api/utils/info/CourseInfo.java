package api.utils.info;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Created by Vileven on 14.05.17.
 */
public class CourseInfo {

    private final String name;

    @JsonCreator
    public CourseInfo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
