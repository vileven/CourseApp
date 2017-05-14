package api.repositories;

import api.models.Subject;
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
@Repository("SubjectRepository")
public class JdbcSubjectRepository implements SubjectRepository {

    private final JdbcTemplate template;
    private final SimpleJdbcInsert subjectInsert;

    @Autowired
    public JdbcSubjectRepository(JdbcTemplate template) {
        this.template = template;
        this.subjectInsert = new SimpleJdbcInsert(template).withTableName("subjects").usingGeneratedKeyColumns("id");
    }

    private final RowMapper<Subject> subjectMapper = (((rs, rowNum) -> new Subject(rs.getLong("id"),
            rs.getLong("course_id"), rs.getString("name"))));

    @Nullable
    @Override
    public Subject create(Subject subject) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("course_id", subject.getCourseId());
        parameters.put("name", subject.getName());

        try {
            final Number id = subjectInsert.executeAndReturnKey(parameters);
            subject.setId((Long) id);
        } catch (DataIntegrityViolationException e) {
            subject = null;
        }
        return subject;
    }

    @Nullable
    @Override
    public Subject find(long id) {
        try {
            return template.queryForObject("SELECT * FROM subjects WHERE id = ?", subjectMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Subject> findByName(String name) {
        return template.query("SELECT * FROM subjects WHERE name = ?",subjectMapper, name);
    }

    @Override
    public void delete(long id) {
        template.update("DELETE FROM subjects WHERE id = ?", id);
    }

    @Override
    public void updateName(long id, String name) {
        template.update("UPDATE subjects SET name = ? WHERE id=?", name, id);
    }

    @Override
    public void updateCourseId(long id, long courseId) throws DataIntegrityViolationException {
        template.update("UPDATE subjects SET course_id = ? WHERE id = ?", courseId, id);
    }

    @Nullable
    @Override
    public Subject update(Subject subject) {
        try {
            final String query = "UPDATE subjects SET course_id = ?, name = ? WHERE id = ? ";
            final int count =template.update(query, subject.getCourseId(), subject.getName(), subject.getId());
            if (count == 0) {
                return null;
            }
        } catch (DataIntegrityViolationException e) {
            return null;
        }
        return subject;
    }

    @Override
    public List<Subject> selectWithParams(Integer limit, Integer offset, @Nullable List<Pair<String, String>> orders,
                                          @Nullable List<Pair<String, String>> filters) {
        final StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM subjects AS s ");

        if (filters != null) {
            queryBuilder.append(" WHERE ");
            for (int i = 0; i < filters.size(); i++) {
                queryBuilder
                        .append("s.")
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

        if (orders != null) {
            queryBuilder.append("ORDER BY ");

            for (int i = 1; i <= orders.size(); i++) {
                queryBuilder
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
        return template.query(queryBuilder.toString(),subjectMapper,limit, offset);
    }

    @Override
    public void deleteAll() {
        template.update("DELETE FROM subjects");
    }
}
