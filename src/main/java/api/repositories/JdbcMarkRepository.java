package api.repositories;

import api.models.Mark;
import api.utils.error.EntityNotFoundException;
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
 * Created by Vileven on 28.05.17.
 */
@Repository("MarkRepository")
public class JdbcMarkRepository implements MarkRepository {
    private final JdbcTemplate template;
    private final SimpleJdbcInsert markInsert;

    @Autowired
    public JdbcMarkRepository(JdbcTemplate template) {
        this.template = template;
        this.markInsert = new SimpleJdbcInsert(template).withTableName("marks").usingGeneratedKeyColumns("id");
    }

    private final RowMapper<Mark> markMapper = (rs, rowNum) -> new Mark
            (
                    rs.getLong("id"), rs.getInt("min"), rs.getInt("max"),
                    rs.getString("name"), rs.getLong("subject_id"),
                    rs.getString("subject_name")
            );


    @Nullable
    @Override
    public Mark create(Mark mark) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("min", mark.getMin());
        parameters.put("max", mark.getMax());
        parameters.put("name", mark.getName());
        parameters.put("subject_id", mark.getSubjectId());

        try {
            final Number id = markInsert.executeAndReturnKey(parameters);
            return this.find((Long)id);
        } catch (DataIntegrityViolationException e) {
            return null;
        }
    }

    @Nullable
    @Override
    public Mark find(long id) {
        try {
            final String query = "SELECT m.id, m.min, m.max, m.name, m.subject_id, s.name as subject_name FROM marks m " +
                    "JOIN subjects s ON m.subject_id = s.id WHERE m.id = ? ";
            return template.queryForObject(query, markMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Mark> findBySubject(Long subjectId) {
        return template.query("SELECT m.id, m.min, m.max, m.name, m.subject_id, s.name as subject_name " +
                "FROM marks m JOIN subjects s ON m.subject_id = s.id WHERE m.subject_id = ? " +
                        "ORDER BY m.min", markMapper, subjectId);
    }

    @Override
    public void delete(long id) {
        template.update("DELETE FROM marks WHERE id = ?", id);
    }


    @Nullable
    @Override
    public Mark update(Mark mark) throws DataIntegrityViolationException, EntityNotFoundException {
        final String query = "UPDATE marks SET " +
                "min = coalesce(?, min), max = coalesce(?, max), name = coalesce(?, name), " +
                "subject_id = coalesce(?, subject_id) " +
                "WHERE id = ? ";
        final int count = template.update(query, mark.getMin(), mark.getMax(), mark.getName(),
                mark.getSubjectId(), mark.getId());

        if (count == 0) {
            throw new EntityNotFoundException("Mark not found");
        }

        return this.find(mark.getId());
    }

    @Override
    public List<Mark> selectWithParams( Integer limit, Integer offset, @Nullable List<Pair<String, String>> orders,
                                        @Nullable List<Pair<String, String>> filters) {
        final StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT m.id, m.min, m.max, m.name, m.subject_id, s.name as subject_name ")
                .append("FROM marks AS m JOIN subjects AS s ON m.subject_id = s.id ");

        if (filters != null && !filters.isEmpty()) {
            queryBuilder.append(" WHERE ");
            for (int i = 0; i < filters.size(); i++) {
                if (filters.get(i).getKey().equals("subject_id")) {
                    queryBuilder
                            .append("m.")
                            .append(filters.get(i).getKey())
                            .append(" = ")
                            .append(filters.get(i).getValue())
                    ;
                } else {
                    queryBuilder
                            .append("m.")
                            .append(filters.get(i).getKey())
                            .append(" ~* '")
                            .append(filters.get(i).getValue())
                            .append('\'')
                    ;
                }

                if (i != filters.size() - 1) {
                    queryBuilder.append(" AND ");
                }
            }
        }

        if (orders != null && !orders.isEmpty()) {
            queryBuilder.append("ORDER BY ");

            for (int i = 1; i <= orders.size(); i++) {
                queryBuilder
                        .append("m.")
                        .append(orders.get(i-1).getKey())
                        .append(' ')
                        .append(orders.get(i-1).getValue())
                ;

                if (i != orders.size()) {
                    queryBuilder.append(", ");
                }
            }

        }
        queryBuilder.append(" LIMIT ? OFFSET ?");
        return template.query(queryBuilder.toString(), markMapper,limit, offset);
    }

    @Override
    public Integer getCount(List<Pair<String, String>> filters) {
        final StringBuilder sqlBuilder = new StringBuilder().append("SELECT count(*) FROM marks ");

        if (filters != null && !filters.isEmpty()) {
            sqlBuilder.append(" WHERE  ");
            for (int i = 0; i < filters.size(); i++) {
                if (filters.get(i).getKey().equals("subject_id")) {
                    sqlBuilder
                            .append(filters.get(i).getKey())
                            .append(" = ")
                            .append(filters.get(i).getValue())
                    ;
                } else {
                    sqlBuilder
                            .append(filters.get(i).getKey())
                            .append(" ~* '")
                            .append(filters.get(i).getValue())
                            .append('\'')
                    ;
                }
                if (i != filters.size() - 1) {
                    sqlBuilder.append(" AND ");
                }
            }
        }

        return template.queryForObject(sqlBuilder.toString(), Integer.class);
    }

    @Override
    public void deleteAll() {
        template.update("TRUNCATE TABLE marks CASCADE");
    }

    @Override
    public RowMapper<Mark> getMapper() {
        return markMapper;
    }
}
