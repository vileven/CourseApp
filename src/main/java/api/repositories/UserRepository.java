package api.repositories;

import api.models.User;
import api.utils.pair.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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

    List<User> selectWithParams(Integer limit, Integer offset, @Nullable List<Pair<String, String>> orders);

    void delete(long id);

    void deleteAll();
}
