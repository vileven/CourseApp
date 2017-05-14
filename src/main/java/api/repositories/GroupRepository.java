package api.repositories;

import api.models.Group;
import api.utils.pair.Pair;
import org.jetbrains.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

/**
 * Created by Vileven on 13.05.17.
 */
public interface GroupRepository {

    @Nullable
    Group create(Group group);

    @Nullable
    Group find(long id);

    List<Group> findByName(String name);

    void delete(long id);

    void updateName(long id, String name);

    void updateCourseId(long id, long courseId);

    Group update(Group group);

    List<Group> selectWithParams(Integer limit, Integer offset,@Nullable List<Pair<String, String>> orders,
                                 @Nullable List<Pair<String, String>> filters);

    void deleteAll();

    RowMapper<Group> getMapper();
}
