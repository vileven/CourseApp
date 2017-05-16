package api.repositories;

import api.models.Course;
import api.utils.error.EntityNotFoundException;
import api.utils.pair.Pair;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vileven on 13.05.17.
 */
@Repository("CourseRepository")
public class JdbcCourseRepository implements CourseRepository {

    private final JdbcTemplate template;
    private final SimpleJdbcInsert courseInsert;

    @Autowired
    public JdbcCourseRepository(JdbcTemplate template) {
        this.template = template;
        this.courseInsert = new SimpleJdbcInsert(template).withTableName("courses").usingGeneratedKeyColumns("id");
    }

    private final RowMapper<Course> courseMapper = (((rs, rowNum) -> new Course(
            rs.getLong("id"), rs.getString("name"))));



    @Override
    public Course create(Course course) {
        final Number id = courseInsert.executeAndReturnKey(Collections.singletonMap("name", course.getName()));
        course.setId((Long)id);

        return course;
    }

    @Override
    public void updateName(long id, String name) {
        final String query = "UPDATE courses SET name=? WHERE id=? ";
        template.update(query, name, id);
    }

    @Override
    public Course update(Course course) throws EntityNotFoundException {
        final String query = "UPDATE courses SET name=? WHERE id=? ";
        final int count = template.update(query, course.getName(), course.getId());
        if (count == 0) {
            throw new EntityNotFoundException("Course not found");
        }

        return course;
    }

    @Nullable
    @Override
    public Course find(long id) {
        Course findedCourse;
        try {
            findedCourse = template.queryForObject("SELECT * FROM courses AS c WHERE c.id = ?", courseMapper, id);
        } catch (EmptyResultDataAccessException e) {
            findedCourse = null;
        }
        return findedCourse;
    }

    @Nullable
    @Override
    public List<Course> findByName(String name) {
        return template.query("SELECT * FROM courses AS c WHERE c.name = ?", courseMapper, name);
    }

    @Override
    public List<Course> selectWithParams(Integer limit, Integer offset, @Nullable List<Pair<String, String>> orders,
                                         @Nullable List<Pair<String, String>> filters) {
        final StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM courses AS c ");

        if (filters != null && !filters.isEmpty()) {
            queryBuilder.append(" WHERE ");
            for (int i = 0; i < filters.size(); i++) {
                queryBuilder
                        .append("c.")
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

            for (int i = 0; i < orders.size(); i++) {
                queryBuilder
                        .append(orders.get(i).getKey())
                        .append(" ")
                        .append(orders.get(i).getValue())
                ;

                if (i != orders.size() - 1) {
                    queryBuilder.append(", ");
                }
            }

        }
        queryBuilder.append(" LIMIT ? OFFSET ?");



        return template.query(queryBuilder.toString(), courseMapper, limit, offset);
    }

    @Override
    public void delete(long id) {
        template.update("DELETE FROM courses AS c WHERE c.id = ?", id);
    }

    @Override
    public void deleteAll() {
        template.update("DELETE FROM courses");
    }

    @Override
    public RowMapper<Course> getMapper() {
        return courseMapper;
    }
}
