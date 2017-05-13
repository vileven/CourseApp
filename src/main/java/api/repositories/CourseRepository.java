package api.repositories;

import api.models.Course;
import api.utils.pair.Pair;
import org.jetbrains.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

/**
 * Created by Vileven on 13.05.17.
 */
public interface CourseRepository {

    @Nullable
    Course create(Course course);

    void updateName(long id, String name);

    @Nullable
    Course find(long id);

    List<Course> findByName(String name);

    List<Course> selectWithParams(Integer limit, Integer offset,@Nullable List<Pair<String, String>> orders);

    void delete(long id);

    void deleteAll();

    RowMapper<Course> getMapper();
}
