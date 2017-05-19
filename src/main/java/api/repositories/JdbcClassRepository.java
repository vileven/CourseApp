package api.repositories;

import api.models.ClassModel;
import api.utils.error.EntityNotFoundException;
import api.utils.pair.Pair;
import api.utils.response.UserClass;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

    private final RowMapper<ClassModel> classMapper = (((rs, rowNum) ->
            new ClassModel(rs.getLong("id"),
            rs.getString("topic"), rs.getLong("subject_id"),
            rs.getString("subject_name"), rs.getLong("group_id"),
            rs.getString("group_name"), rs.getLong("prof_id"),
            rs.getString("prof_first_name"), rs.getString("prof_last_name"),
            rs.getString("begin_time"), rs.getString("end_time"),
            rs.getString("location"))
    ));

    private final RowMapper<UserClass> userClassMapper = (((rs, rowNum) -> new UserClass(rs.getLong("id"),
            rs.getLong("subject_id"), rs.getString("subject_name"),
            rs.getLong("group_id"), rs.getString("group_name"),
            rs.getString("professors"), rs.getString("topic"), rs.getString("begin_time"),
            rs.getString("end_time"))));

    @Nullable
    @Override
    public ClassModel create(ClassModel classModel) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("topic", classModel.getTopic());
        parameters.put("subject_id", classModel.getSubjectId());
        parameters.put("group_id", classModel.getGroupId());
        parameters.put("begin_time", classModel.getBegin());
        parameters.put("end_time", classModel.getEnd());
        parameters.put("prof_id", classModel.getProfId());
        parameters.put("location", classModel.getLocation());


        try {
//            final Number id = classInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
//            return find((Long) id);

            final Long id = template.queryForObject( "INSERT INTO classes (topic, subject_id, group_id, begin_time, end_time, location, prof_id)" +
                    "VALUES (?,?,?,?::TIMESTAMPTZ,?::TIMESTAMPTZ,?,?) RETURNING id ",Long.class, classModel.getTopic(), classModel.getSubjectId(),
                    classModel.getGroupId(), classModel.getBegin(), classModel.getEnd(), classModel.getLocation(), classModel.getProfId());
            return find(id);
        } catch (DataIntegrityViolationException e) {
            return null;
        }
    }

    @Nullable
    @Override
    public ClassModel find(long id) {
        final ClassModel findedClass;
        try {
            final String sql =
                    "SELECT " +
                    "cl.id," +
                            "cl.topic, cl.location, cl.prof_id, pr.first_name as prof_first_name, " +
                            " pr.last_name as prof_last_name, cl.subject_id, s.name as  subject_name, " +
                            " cl.group_id, g.name as group_name, cl.begin_time, cl.end_time " +
                            "FROM " +
                            "classes cl " +
                            "JOIN subjects s ON cl.subject_id = s.id " +
                            "JOIN users pr ON cl.prof_id = pr.id " +
                            "JOIN groups g ON cl.group_id = g.id " +
                            "WHERE cl.id = ? ";

            findedClass = template.queryForObject(sql,classMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

        return findedClass;
    }

    @Override
    public ClassModel update(ClassModel classModel) throws DataIntegrityViolationException, EntityNotFoundException {
        final String query = "UPDATE classes SET topic = ?, subject_id = ?, group_id = ? , prof_id = ?, location = ?, " +
                                                "begin_time = ?::TIMESTAMPTZ, end_time = ?::TIMESTAMPTZ " +
                            " WHERE id = ?";
        final int count = template.update(query, classModel.getTopic(), classModel.getSubjectId(), classModel.getGroupId(),
                classModel.getProfId(), classModel.getLocation(), classModel.getBegin(),classModel.getEnd(), classModel.getId());
        if (count == 0) {
            throw new EntityNotFoundException("class not found");
        }
        return find(classModel.getId());
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
        final StringBuilder queryBuilder = new StringBuilder().append("SELECT ")
                .append("cl.id,")
                .append("cl.topic, cl.location, cl.prof_id, pr.first_name as prof_first_name, ")
                .append(" pr.last_name as prof_last_name, cl.subject_id, s.name as  subject_name, ")
                .append(" cl.group_id, g.name as group_name, cl.begin_time, cl.end_time ")
                .append("FROM ").append("classes cl ").append("JOIN subjects s ON cl.subject_id = s.id ")
                .append("JOIN users pr ON cl.prof_id = pr.id ").append("JOIN groups g ON cl.group_id = g.id ")
                ;

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

//        queryBuilder.append(" GROUP BY cl.id, s.name, g.name ");

        if (orders != null && !orders.isEmpty()) {
            queryBuilder.append(" ORDER BY ");

            for (int i = 1; i <= orders.size(); i++) {
                queryBuilder
                        .append("cl.")
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
        return template.query(queryBuilder.toString(), classMapper, limit, offset);
    }

    @Override
    public void deleteAll() {
        template.update("DELETE FROM classes");
    }
}
