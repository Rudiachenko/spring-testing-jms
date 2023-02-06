package spring.test.jms.dao;

import spring.test.jms.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    User add(User user);

    Optional<User> getUserById(long userId);

    Optional<User> getUserByEmail(String email);

    List<User> getUsersByName(String name, int pageSize, int pageNum);

    Optional<User> updateUser(User user);

    boolean deleteUser(long userId);
}
