package api.repositories;

import api.models.ClassModel;
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
 * Created by Vileven on 15.05.17.
 */
@Repository("ClassRepository")
public class JdbcClassRepository implements ClassRepository {
    private final JdbcTemplate template;
    private final SimpleJdbcInsert classInsert;

    @Autowired
    public JdbcClassRepository(JdbcTemplate template) {
        this.template = template;
        this.classInsert = new SimpleJdbcInsert(template).withTableName("classes").usingGeneratedKeyColumns("id");
    }

    private final RowMapper<ClassModel> classMapper = (((rs, rowNum) -> new ClassModel(rs.getLong("id"),
            rs.getString("topic"), rs.getLong("subject_id"), rs.getLong("group_id"),
            rs.getString("begin_time"), rs.getString("end_time"))));

    @Nullable
    @Override
    public ClassModel create(ClassModel classModel) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("topic", classModel.getTopic());
        parameters.put("subject_id", classModel.getSubject());
        parameters.put("group_id", classModel.getGroup());
        parameters.put("begin_time", classModel.getBegin());
        parameters.put("end_time", classModel.getEnd());

        try {
            final Number id = classInsert.executeAndReturnKey(parameters);
            classModel.setId((Long) id);
        } catch (DataIntegrityViolationException e) {
            return null;
        }

        return classModel;
    }

    @Nullable
    @Override
    public ClassModel find(long id) {
        final ClassModel findedClass;
        try {
            findedClass = template.queryForObject("SELECT * FROM classes WHERE id = ? ",classMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

        return findedClass;
    }

    @Override
    public ClassModel update(ClassModel classModel) throws DataIntegrityViolationException, EntityNotFoundException {
        final String query = "UPDATE classes SET topic = ?, subject_id = ?, group_id = ? ," +
                                                "begin_time = ?::TIMESTAMPTZ, end_time = ?::TIMESTAMPTZ " +
                            " WHERE id = ?";
        final int count = template.update(query, classModel.getTopic(), classModel.getSubject(), classModel.getGroup(),
                classModel.getBegin(),classModel.getEnd(), classModel.getId());
        if (count == 0) {
            throw new EntityNotFoundException("class not found");
        }
        return classModel;
    }

    @Override
    public void delete(long id) {
        template.update("DELETE FROM classes WHERE id = ?", id);
    }

    @Override
    public RowMapper<ClassModel> getMapper() {
        return classMapper;
    }

    @Override
    public List<ClassModel> selectWithParams(Integer limit, Integer offset, @Nullable List<Pair<String, String>> orders, @Nullable List<Pair<String, String>> filters) {
        final StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM classes AS cl ");

        if (filters != null && !filters.isEmpty()) {
            queryBuilder.append(" WHERE ");
            for (int i = 0; i < filters.size(); i++) {
                queryBuilder
                        .append("cl.")
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
            queryBuilder.append(" ORDER BY ");

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
        queryBuilder.append(" LIMIT ? OFFSET ? ");
        return template.query(queryBuilder.toString(),classMapper, limit, offset);
    }

    @Override
    public void deleteAll() {
        template.update("DELETE FROM classes");
    }
}
