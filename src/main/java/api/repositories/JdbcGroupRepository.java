package api.repositories;

import api.models.Group;
import api.utils.pair.Pair;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vileven on 13.05.17.
 */
@Repository("GroupRepository")
public class JdbcGroupRepository implements GroupRepository {
    private final JdbcTemplate template;
    private final SimpleJdbcInsert subjectInsert;

    @Autowired
    public JdbcGroupRepository(JdbcTemplate template) {
        this.template = template;
        this.subjectInsert = new SimpleJdbcInsert(template).withTableName("groups").usingGeneratedKeyColumns("id");
    }

    private final RowMapper<Group> groupMapper = (((rs, rowNum) -> new Group(rs.getLong("id"),
            rs.getLong("course_id"), rs.getString("name"))));

    @Nullable
    @Override
    public Group create(Group group) {
        final Map<String, Object> parameters = new HashMap<>(2);
        parameters.put("course_id", group.getCourseId());
        parameters.put("name", group.getName());

        try {
            final Number id = subjectInsert.executeAndReturnKey(parameters);
            group.setId((Long) id);
        } catch (DataIntegrityViolationException e) {
            group = null;
        }
        return group;
    }

    @Nullable
    @Override
    public Group find(long id) {
        try {
            return template.queryForObject("SELECT * FROM groups WHERE id = ?", groupMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Group> findByName(String name) {
        return template.query("SELECT * FROM groups WHERE name = ?",groupMapper, name);
    }

    @Override
    public void delete(long id) {
        template.update("DELETE FROM groups WHERE id = ?", id);
    }

    @Override
    public void updateName(long id, String name) {
        template.update("UPDATE groups SET name = ? WHERE id=?", name, id);
    }

    @Override
    public void updateCourseId(long id, long courseId) throws DataIntegrityViolationException {
        template.update("UPDATE groups SET course_id = ? WHERE id = ?", courseId, id);
    }

    @Override
    public List<Group> selectWithParams(Integer limit, Integer offset, @Nullable List<Pair<String, String>> orders) {
        final StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM groups AS g ");
        if (orders != null) {
            queryBuilder.append("ORDER BY ");

            for (int i = 0; i <= orders.size() - 1; i++) {
                queryBuilder
                        .append(orders.get(i).getKey())
                        .append(' ')
                        .append(orders.get(i).getValue())
                ;

                if (i != orders.size() - 1) {
                    queryBuilder.append(", ");
                }
            }

        }
        queryBuilder.append(" LIMIT ? OFFSET ?");
        return template.query(queryBuilder.toString(), groupMapper,limit, offset);
    }

    @Override
    public void deleteAll(long id) {
        template.update("DELETE FROM groups");
    }

    @Override
    public RowMapper<Group> getMapper() {
        return groupMapper;
    }
}
