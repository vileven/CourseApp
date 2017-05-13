package api.models;

public class Mark extends Model<Long> {
    private Long id;
    private Integer min;
    private Integer max;
    private String name;
    private Long subject;

    public Mark(Long id, Integer min, Integer max, String name, Long subject) {
        this.id = id;
        this.min = min;
        this.max = max;
        this.name = name;
        this.subject = subject;
    }

    public Mark(Integer min, Integer max, String name, Long subject) {
        this.min = min;
        this.max = max;
        this.name = name;
        this.subject = subject;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSubject() {
        return subject;
    }

    public void setSubject(Long subject) {
        this.subject = subject;
    }


}
