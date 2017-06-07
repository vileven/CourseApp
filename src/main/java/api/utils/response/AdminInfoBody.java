package api.utils.response;

/**
 * Created by Vileven on 07.06.17.
 */
public class AdminInfoBody {
    public final Integer users;
    public final Integer students;
    public final Integer professors;
    public final Integer courses;
    public final Integer subjects;
    public final Integer groups;

    public AdminInfoBody(Integer users, Integer students, Integer professors, Integer courses, Integer subjects, Integer groups) {
        this.users = users;
        this.students = students;
        this.professors = professors;
        this.courses = courses;
        this.subjects = subjects;
        this.groups = groups;
    }
}
