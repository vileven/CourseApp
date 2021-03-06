package api.repositories;

import api.models.Group;
import api.utils.error.EntityNotFoundException;
import api.utils.pair.Pair;
import api.utils.response.UserResponseBody;
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
            rs.getLong("course_id"), rs.getString("course_name"), rs.getString("name"))));

    private final RowMapper<UserResponseBody> userMapper = (((rs, rowNum) -> new UserResponseBody(
            rs.getLong("id"), rs.getInt("role"), rs.getString("email"),
            rs.getString("first_name"), rs.getString("last_name"),
            rs.getString("about"))));

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
            return template.queryForObject("SELECT g.id, g.course_id, c.name AS course_name, g.name " +
                    "FROM groups AS g JOIN courses AS c ON g.course_id = c.id WHERE g.id = ?", groupMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Group> findByName(String name) {
        return template.query("SELECT g.id, g.course_id, c.name AS course_name, g.name " +
                "FROM groups AS g JOIN courses AS c ON g.course_id = c.id WHERE g.name = ?",groupMapper, name);
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

    @Nullable
    @Override
    public Group update(Group group) throws DataIntegrityViolationException, EntityNotFoundException {
        final String query = "UPDATE groups SET course_id = ?, name = ? WHERE id = ? ";
        final int count = template.update(query, group.getCourseId(), group.getName(), group.getId());
        if (count == 0) {
            throw new EntityNotFoundException("Subject not found");
        }
        return group;
    }

    @Override
    public List<Group> selectWithParams(Integer limit, Integer offset, @Nullable List<Pair<String, String>> orders,
                                        @Nullable List<Pair<String, String>> filters) {
        final StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT g.id, g.course_id, c.name AS course_name, g.name ")
                .append("FROM groups AS g JOIN courses AS c ON g.course_id = c.id ");

        if (filters != null && !filters.isEmpty()) {
            queryBuilder.append(" WHERE ");
            for (int i = 0; i < filters.size(); i++) {
                queryBuilder
                        .append("g.")
                        .append(filters.get(i).getKey())
                        .append(" ~* '")
                        .append(filters.get(i).getValue())
                        .append('\'')
                ;
                if (i != filters.size() - 1) {
                    queryBuilder.append(" AND ");
                }
            }
        }

        if (orders != null && !orders.isEmpty()) {
            queryBuilder.append("ORDER BY ");

            for (int i = 0; i <= orders.size() - 1; i++) {
                queryBuilder
                        .append("g.")
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
    public List<UserResponseBody> getStudents(Long id, Integer limit, Integer offset, @Nullable List<Pair<String, String>> orders,
                                              @Nullable List<Pair<String, String>> filters) {
        final StringBuilder sqlConstructor = new StringBuilder();
        sqlConstructor.append(" SELECT u.id, u.role, u.email, u.first_name, u.last_name, u.about FROM groups AS g ")
                .append("  JOIN applications AS a ON g.id = a.group_id ")
                .append("  JOIN users AS u ON a.student_id = u.id ")
                .append(" WHERE g.id = ? ");

        if (filters != null && !filters.isEmpty()) {
            sqlConstructor.append("  AND ");
            for (int i = 0; i < filters.size(); i++) {
                sqlConstructor
                        .append("u.")
                        .append(filters.get(i).getKey())
                        .append(" ~* '")
                        .append(filters.get(i).getValue())
                        .append('\'')
                ;
                if (i != filters.size() - 1) {
                    sqlConstructor.append(" AND ");
                }
            }
        }

        if (orders != null && !orders.isEmpty()) {
            sqlConstructor.append(" ORDER BY ");

            for (int i = 0; i < orders.size(); i++) {
                sqlConstructor
                        .append(orders.get(i).getKey())
                        .append(' ')
                        .append(orders.get(i).getValue())
                ;

                if (i != orders.size() - 1) {
                    sqlConstructor.append(", ");
                }
            }
        }
        sqlConstructor.append(" LIMIT ? OFFSET ?");
        return template.query(sqlConstructor.toString(), userMapper, id, limit, offset);
    }

    @Override
    public void deleteAll() {
        template.update("DELETE FROM groups");
    }

    @Override
    public RowMapper<Group> getMapper() {
        return groupMapper;
    }
}
