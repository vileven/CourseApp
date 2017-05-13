package api.repositories;

import api.models.Group;
import api.utils.pair.Pair;
import org.jetbrains.annotations.Nullable;

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

    List<Group> selectWithParams(Integer limit, Integer offset, @Nullable List<Pair<String, String>> orders);

    void deleteAll(long id);
}
