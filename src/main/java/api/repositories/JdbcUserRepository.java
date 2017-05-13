package api.repositories;

import api.models.User;
import api.utils.pair.Pair;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vileven on 13.05.17.
 */
@Repository("UserRepository")
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate template;
    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate template) {
        this.template = template;
        this.insertUser = new SimpleJdbcInsert(template).withTableName("users").usingGeneratedKeyColumns("id");
    }

    private final RowMapper<User> userMapperWithAvatar = ((rs, rowNum) -> new User(rs.getLong("id"),
            rs.getInt("role"), rs.getString("email"),
            rs.getString("password"), rs.getString("first_name"),
            rs.getString("last_name"), rs.getBytes("avatar"),
            rs.getString("about")));

    private final RowMapper<User> userMapper = ((rs, rowNum) -> new User(rs.getLong("id"),
            rs.getInt("role"), rs.getString("email"),
            rs.getString("password"), rs.getString("first_name"),
            rs.getString("last_name"), null,
            rs.getString("about")));

    @Nullable
    @Override
    public User create(User user) {

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("role", user.getRole());
        parameters.put("email", user.getEmail());
        parameters.put("password", user.getPassword());
        parameters.put("first_name", user.getFirstName());
        parameters.put("last_name", user.getLastName());
        parameters.put("avatar", user.getAvatar());
        parameters.put("about", user.getAbout());

        try {
            final Number id = insertUser.executeAndReturnKey(parameters);
            user.setId((Long) id);
        } catch (DuplicateKeyException e) {
            user = null;
        }

        return user;
    }

    @Override
    public int update(User user) {
        final String sql =  "UPDATE users " +
                "SET role = ?, email = ?, password = ?, first_name = ?, " +
                "last_name = ?, avatar = ?, about = ? WHERE id = ? ";
        try {
            template.update(sql, user.getRole(), user.getEmail(), user.getPassword(), user.getFirstName(),
                    user.getLastName(), user.getAvatar(), user.getAbout(), user.getId());
        } catch (DuplicateKeyException e) {
            return Code.ERR_DUPLICATE;
        }

        return Code.OK;
    }

    @Override
    public int updateEmail(long id, String email) {
        final String query = "UPDATE users SET email = ? WHERE id = ?";
        try {
            template.update(query, email, id);
        } catch (DuplicateKeyException e) {
            return Code.ERR_DUPLICATE;
        }

        return Code.OK;
    }

    @Override
    public int updatePassword(long id, String password) {
        final String query = "UPDATE users SET password = ? WHERE id = ?";
        try {
            template.update(query, password, id);
        } catch (DuplicateKeyException e) {
            return Code.ERR_DUPLICATE;
        }

        return Code.OK;
    }

    @Nullable
    @Override
    public User find(long id) {
        final String query = "SELECT u.id, u.role, u.email, u.password, u.first_name, u.last_name, u.about FROM users AS u WHERE id = ?";

        User findedUser;
        try {
            findedUser = template.queryForObject(query, userMapper, id);
        } catch (EmptyResultDataAccessException e) {
            findedUser = null;
        }

        return findedUser;
    }

    @Nullable
    @Override
    public User findWithAvatar(long id) {
        final String query = "SELECT * FROM users WHERE id = ?";

        User findedUser;
        try {
            findedUser = template.queryForObject(query, userMapperWithAvatar, id);
        } catch (EmptyResultDataAccessException e) {
            findedUser = null;
        }

        return findedUser;
    }

    @Nullable
    @Override
    public User findByEmail(String email) {
        final String query = "SELECT u.id, u.role, u.email, u.password, u.first_name, u.last_name, u.about FROM users AS u WHERE email = ?";

        User findedUser;
        try {
            findedUser = template.queryForObject(query, userMapper, email);
        } catch (EmptyResultDataAccessException e) {
            findedUser = null;
        }

        return findedUser;
    }

    @Nullable
    @Override
    public User findByEmailWithAvatar(String email) {
        final String query = "SELECT * FROM users WHERE email = ?";

        User findedUser;
        try {
            findedUser = template.queryForObject(query, userMapperWithAvatar, email);
        } catch (EmptyResultDataAccessException e) {
            findedUser = null;
        }

        return findedUser;
    }

    @Override
    public List<User> selectWithParams(Integer limit, Integer offset, @Nullable List<Pair<String, String>> orders) {
        final StringBuilder sqlConstructor = new StringBuilder();
        sqlConstructor.append("SELECT u.id, u.role, u.email, u.password, u.first_name, u.last_name, u.about FROM users AS u ");

        if (orders != null) {
            sqlConstructor.append("ORDER BY ");

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

        return template.query(sqlConstructor.toString(), userMapper, limit, offset);
    }

    @Override
    public void delete(long id) {
        final String query = "DELETE FROM users AS u WHERE u.id = ?";
        this.template.update(query, id);
    }

    @Override
    public void deleteAll() {
        this.template.update("DELETE FROM users");
    }


}
