package api.repositories;

import api.models.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

public interface UserRepository {

    /**
     * Создать пользователя
     * @param user пользователь
     * @return вставленного пользователя
     */
    @Nullable
    User create(User user);

    /**
     * Обновить данные пользователя
     * @param user объект user идентификатором и новыми данными
     * @return код ошибки
     */
    int update(User user);

    int updateEmail(long id, String email);

    int updatePassword(long id, String password);

    @Nullable
    User find(long id);

    @Nullable
    User findWithAvatar(long id);

    @Nullable
    User findByEmail(String email);

    @Nullable
    User findByEmailWithAvatar(String email);

    void delete(long id);

    void deleteAll();
}
