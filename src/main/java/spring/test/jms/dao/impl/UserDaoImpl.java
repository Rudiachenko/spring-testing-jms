package spring.test.jms.dao.impl;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import spring.test.jms.dao.UserDao;
import spring.test.jms.model.User;
import spring.test.jms.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@Setter
@Repository
public class UserDaoImpl implements UserDao {
    private final UserStorage userStorage;

    @Autowired
    public UserDaoImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User add(User user) {
        return userStorage.add(user);
    }

    @Override
    public Optional<User> getUserById(long userId) {
        return userStorage.getUserById(userId);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userStorage.getUserByEmail(email);
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        return userStorage.getUsersByName(name, pageSize, pageNum);
    }

    @Override
    public Optional<User> updateUser(User user) {
        return userStorage.updateUser(user);
    }

    @Override
    public boolean deleteUser(long userId) {
        return userStorage.deleteUser(userId);
    }
}
