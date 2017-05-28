package api.repositories;

import api.models.Course;
import api.models.Group;
import api.utils.error.EntityNotFoundException;
import api.utils.pair.Pair;
import api.utils.response.SubjectResponse;
import org.jetbrains.annotations.Nullable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

/**
 * Created by Vileven on 13.05.17.
 */
public interface CourseRepository {

    Course create(Course course);

    void updateName(long id, String name);

    Course update(Course course) throws EntityNotFoundException;

    @Nullable
    Course find(long id);

    List<Course> findByName(String name);

    List<Group> getGroups(long id);

    List<SubjectResponse> getSubjects(long id);

    List<Course> selectWithParams(Integer limit, Integer offset,@Nullable List<Pair<String, String>> orders,
                                  @Nullable List<Pair<String, String>> filters);

    void delete(long id);

    void deleteAll();

    List<Course> getAvaliableCourses(long studentId);

    RowMapper<Course> getMapper();
}
