package api.repositories;

import api.models.Mark;
import api.utils.error.EntityNotFoundException;
import api.utils.pair.Pair;
import org.jetbrains.annotations.Nullable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

/**
 * Created by Vileven on 28.05.17.
 */
public interface MarkRepository {
    @Nullable
    Mark create(Mark mark);

    @Nullable
    Mark find(long id);

    List<Mark> findBySubject(Long subjectId);

    void delete(long id);

    Integer getCount(List<Pair<String, String>> filters);

    @Nullable
    Mark update(Mark mark) throws DataIntegrityViolationException, EntityNotFoundException;

    List<Mark> selectWithParams(Integer limit, Integer offset, @Nullable List<Pair<String, String>> orders,
                                           @Nullable List<Pair<String, String>> filters);

    void deleteAll();

    RowMapper<Mark> getMapper();
}
