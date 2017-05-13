package api.models;

/**
 * Created by Vileven on 13.05.17.
 */
public class Group extends Model<Long> {
    private Long id;
    private Long courseId;
    private String name;

    public Group(Long id, Long courseId, String name) {
        this.id = id;
        this.courseId = courseId;
        this.name = name;
    }

    public Group(Long courseId, String name) {
        this.courseId = courseId;
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
