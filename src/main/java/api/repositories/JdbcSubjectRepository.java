package api.repositories;

import api.models.Subject;
import api.utils.error.EntityNotFoundException;
import api.utils.pair.Pair;
import api.utils.response.SubjectResponse;
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
@Repository("SubjectRepository")
public class JdbcSubjectRepository implements SubjectRepository {

    private final JdbcTemplate template;
    private final SimpleJdbcInsert subjectInsert;

    @Autowired
    public JdbcSubjectRepository(JdbcTemplate template) {
        this.template = template;
        this.subjectInsert = new SimpleJdbcInsert(template).withTableName("subjects").usingGeneratedKeyColumns("id");
    }

    private final RowMapper<SubjectResponse> subjectMapper = (((rs, rowNum) -> new SubjectResponse(rs.getLong("id"),
            rs.getLong("course_id"), rs.getString("course_name"), rs.getString("name"))));

    private final RowMapper<UserResponseBody> userMapperWithotPassword = (((rs, rowNum) ->
            new UserResponseBody(rs.getLong("id"), rs.getInt("role"),
                    rs.getString("email"),
                    rs.getString("first_name"), rs.getString("last_name"),
                    rs.getString("about"))
    ));

    @Nullable
    @Override
    public SubjectResponse create(SubjectResponse subject) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("course_id", subject.getCourseId());
        parameters.put("name", subject.getName());

        try {
            final Number id = subjectInsert.executeAndReturnKey(parameters);
            return this.find((Long) id);
        } catch (DataIntegrityViolationException e) {
            return null;
        }
    }

    @Nullable
    @Override
    public SubjectResponse find(long id) {
        try {
            return template.queryForObject("SELECT s.id, s.course_id, c.name AS course_name, s.name FROM " +
                                                    "subjects AS s " +
                                                "JOIN courses AS c ON s.course_id = c.id" +
                                                " WHERE s.id = ? ", subjectMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<SubjectResponse> findByName(String name) {
        return template.query(
                "SELECT s.id, s.course_id, c.name AS course_name, s.name " +
                "FROM subjects AS s " +
                "JOIN courses AS c ON s.course_id = c.id " +
                "WHERE s.name = ?", subjectMapper, name);
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
    public SubjectResponse update(SubjectResponse subject) throws DataIntegrityViolationException, EntityNotFoundException {
        final String query = "UPDATE subjects SET course_id = ?, name = ? WHERE id = ? ";
        final int count = template.update(query, subject.getCourseId(), subject.getName(), subject.getId());
        if (count == 0) {
            throw new EntityNotFoundException("Subject not found");
        }
        return subject;
    }

    @Override
    public List<SubjectResponse> selectWithParams(Integer limit, Integer offset, @Nullable List<Pair<String, String>> orders,
                                                  @Nullable List<Pair<String, String>> filters) {
        final StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT s.id, s.course_id, c.name AS course_name, s.name ")
            .append("FROM subjects AS s JOIN courses AS c ON c.id = s.course_id ");

        if (filters != null && !filters.isEmpty()) {
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

        if (orders != null && !orders.isEmpty()) {
            queryBuilder.append("ORDER BY ");

            for (int i = 1; i <= orders.size(); i++) {
                queryBuilder
                        .append("s.")
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

    @Override
    public List<UserResponseBody> getProfessors(long id) {
        final String sql =
                "SELECT " +
                "  u.id, " +
                "  u.role, " +
                "  u.email, " +
                "  u.password, " +
                "  u.first_name, " +
                "  u.last_name, " +
                "  u.about " +
                "FROM " +
                "  professors AS pr " +
                "  JOIN users u ON pr.prof_id = u.id  " +
                "WHERE pr.subject_id = ? ";

        return template.query(sql, userMapperWithotPassword, id);
    }
}
