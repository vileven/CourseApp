package api.repositories;

import api.models.Subject;
import api.utils.error.EntityNotFoundException;
import api.utils.pair.Pair;
import api.utils.response.SubjectResponse;
import org.jetbrains.annotations.Nullable;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

/**
 * Created by Vileven on 13.05.17.
 */
public interface SubjectRepository {
    @Nullable
    SubjectResponse create(SubjectResponse subject);

    @Nullable
    SubjectResponse find(long id);

    List<SubjectResponse> findByName(String name);

    void delete(long id);

    void updateName(long id, String name);

    void updateCourseId(long id, long courseId);

    @Nullable
    SubjectResponse update(SubjectResponse subject) throws DataIntegrityViolationException, EntityNotFoundException;

    List<SubjectResponse> selectWithParams(Integer limit, Integer offset, @Nullable List<Pair<String, String>> orders,
                                           @Nullable List<Pair<String, String>> filters);

    void deleteAll();
}
