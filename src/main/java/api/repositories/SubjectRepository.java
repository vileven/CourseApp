package api.repositories;

import api.models.Subject;
import api.utils.pair.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by Vileven on 13.05.17.
 */
public interface SubjectRepository {
    @Nullable
    Subject create(Subject subject);

    @Nullable
    Subject find(long id);

    List<Subject> findByName(String name);

    void delete(long id);

    void updateName(long id, String name);

    void updateCourseId(long id, long courseId);

    List<Subject> selectWithParams(Integer limit, Integer offset, @Nullable List<Pair<String, String>> orders);

    void deleteAll(long id);
}
