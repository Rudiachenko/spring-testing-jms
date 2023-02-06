package spring.test.jms.service;

import spring.test.jms.model.User;

import java.util.List;

public interface UserService {
    User create(User user);

    User getUserById(long userId);

    User getUserByEmail(String email);

    List<User> getUsersByName(String name, int pageSize, int pageNum);

    User updateUser(User user, long id);

    boolean deleteUser(long userId);
}
