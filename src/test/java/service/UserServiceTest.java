package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.test.jms.dao.UserDao;
import spring.test.jms.model.User;
import spring.test.jms.model.impl.UserImpl;
import spring.test.jms.service.Impl.UserServiceImpl;
import spring.test.jms.util.IdGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private IdGenerator idGenerator;
    @Mock
    private UserDao userDao;
    @InjectMocks
    private UserServiceImpl sut;

    @Test
    void shouldReturnUserWhenExistIdPassed() {
        //given
        User expected = createUser("Bob", "Bob@mail.com");
        when(userDao.getUserById(1)).thenReturn(Optional.ofNullable(expected));

        //when
        User actual = sut.getUserById(1);

        //then
        assertThat(actual, is(expected));
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenNotExistIdPassed() {
        when(userDao.getUserById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> sut.getUserById(10));
    }

    @Test
    void shouldReturnUserWhenExistEmailPassed() {
        //given
        User expected = createUser("Bob", "Bob@mail.com");
        when(userDao.getUserByEmail("Bob@mail.com")).thenReturn(Optional.ofNullable(expected));

        //when
        User actual = sut.getUserByEmail("Bob@mail.com");

        //then
        assertThat(actual, is(expected));
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenNotExistEmailPassed() {
        when(userDao.getUserByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> sut.getUserByEmail("absentEmail"));
    }

    @Test
    void shouldDeleteUserWhenExistIdPassed() {
        //given
        when(userDao.deleteUser(1)).thenReturn(true);

        //when
        boolean actual = sut.deleteUser(1);

        //then
        assertTrue(actual);
    }

    @Test
    void shouldNotDeleteUserWhenNotExistIdPassed() {
        //given
        when(userDao.deleteUser(anyLong())).thenReturn(false);

        //when
        boolean actual = sut.deleteUser(10);

        //then
        assertFalse(actual);
    }

    @Test
    void shouldUpdateUserWhenExistIdPassed() {
        //given
        User user = createUser("Bob", "Bob@mail.com");
        User expected = createUser("BobUpdated", "Bob@mail.com");
        when(userDao.updateUser(user)).thenReturn(Optional.ofNullable(expected));

        //when
        User actual = sut.updateUser(user, user.getId());

        //then
        assertThat(actual, is(expected));
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenNotExistIdPassedWhileUpdating() {
        when(userDao.updateUser(any(User.class))).thenReturn(Optional.empty());
        UserImpl user = createUser("Bob", "wrongEmail");
        user.setId(1);
        assertThrows(NoSuchElementException.class, () -> sut.updateUser(user, user.getId()));
    }

    @Test
    void shouldCreateUser() {
        //given
        Optional<User> user = Optional.of(createUser("Bob", "Bob@mail.com"));
        User expected = createUser("Bob", "Bob@mail.com");
        when(userDao.add(user.get())).thenReturn(expected);

        //when
        User actual = sut.create(user.get());

        //then
        assertThat(actual, is(expected));
    }

    @Test
    void shouldReturnNonEmptyUsers() {
        //given
        int pageSize = 5;
        int pageNumber = 2;
        String name = "Bob";
        List<User> expected = Arrays.asList(createUser("Bob", "Bob@mail.com"),
                createUser("Bob22", "Bob@mail.ru"),
                createUser("Bob33", "Mimir@gmail.eu"));
        when(userDao.getUsersByName(name, pageSize, pageNumber)).thenReturn(expected);

        //when
        List<User> actual = sut.getUsersByName(name, pageSize, pageNumber);

        //then
        assertThat(actual, is(expected));
    }

    private UserImpl createUser(String name, String email) {
        return UserImpl.builder()
                .name(name)
                .email(email)
                .build();
    }
}
