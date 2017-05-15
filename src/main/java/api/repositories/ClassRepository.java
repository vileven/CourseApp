package api.repositories;

import api.models.ClassModel;
import api.utils.error.EntityNotFoundException;
import api.utils.pair.Pair;
import org.jetbrains.annotations.Nullable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

/**
 * Created by Vileven on 15.05.17.
 */
public interface ClassRepository {

    @Nullable
    ClassModel create(ClassModel classModel) ;

    @Nullable
    ClassModel find(long id);

    ClassModel update(ClassModel classModel) throws DataIntegrityViolationException, EntityNotFoundException;

    void delete(long id);

    RowMapper<ClassModel> getMapper();

    List<ClassModel> selectWithParams(Integer limit, Integer offset, @Nullable List<Pair<String, String>> orders,
                                      @Nullable List<Pair<String, String>> filters);

    void deleteAll();
}
