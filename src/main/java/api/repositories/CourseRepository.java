package api.repositories;

import api.models.Course;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Vileven on 13.05.17.
 */
public interface CourseRepository {

    @Nullable
    Course create(Course course);

    void updateName(long id, String name);

    @Nullable
    Course find(long id);

    Course findByName(String name);

}
