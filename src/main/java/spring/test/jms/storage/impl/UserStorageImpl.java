package spring.test.jms.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.test.jms.model.User;
import spring.test.jms.model.impl.UserImpl;
import spring.test.jms.storage.UserStorage;
import spring.test.jms.util.Paginator;
import spring.test.jms.util.annotation.BindStaticData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserStorageImpl implements UserStorage {
    @BindStaticData(fileLocation = "preparedUsers.json", castTo = UserImpl.class)
    private final Map<Long, User> userStorageMap = new HashMap<>();
    private final Paginator<User> paginator;

    @Autowired
    public UserStorageImpl(Paginator<User> paginator) {
        this.paginator = paginator;
    }

    @Override
    public User add(User user) {
        return userStorageMap.put(user.getId(), user);
    }

    @Override
    public Optional<User> getUserById(long userId) {
        return Optional.ofNullable(userStorageMap.get(userId));
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userStorageMap.values().stream()
                .filter(user -> Objects.equals(user.getEmail(), email))
                .findFirst();
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        List<User> usersByName = getUsersWithFilter(name);
        return paginator.paginate(usersByName, pageSize, pageNum);
    }

    @Override
    public Optional<User> updateUser(User user) {
        return Optional.ofNullable(userStorageMap.get(user.getId()));
    }

    @Override
    public boolean deleteUser(long userId) {
        if (userStorageMap.containsKey(userId)) {
            userStorageMap.remove(userId);
            return true;
        }
        return false;
    }

    private List<User> getUsersWithFilter(String name) {
        return userStorageMap.values().stream()
                .filter(user -> user.getName().equals(name))
                .collect(Collectors.toList());
    }
}
