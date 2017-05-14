package api.models;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Created by Vileven on 13.05.17.
 */
public class Course extends Model<Long> {

    private Long id;
    private String name;

    public Course(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Course(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }
}
