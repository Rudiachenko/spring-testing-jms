package util;

import org.junit.jupiter.api.Test;
import spring.test.jms.model.impl.UserImpl;
import spring.test.jms.util.Paginator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PaginatorTest {
    private final Paginator<UserImpl> sut = new Paginator<>();

    @Test
    void shouldPaginateEntities() {
        //given
        List<UserImpl> users = Arrays.asList(createUser("Bob1"),
                createUser("Bob2"),
                createUser("Bob3"),
                createUser("Bob4"),
                createUser("Bob5"),
                createUser("Bob6"),
                createUser("Bob7"),
                createUser("Bob8"),
                createUser("Bob9"),
                createUser("Bob10"));

        List<UserImpl> expected1 = Arrays.asList(createUser("Bob4"),
                createUser("Bob5"),
                createUser("Bob6")
        );

        List<UserImpl> expected2 = List.of(createUser("Bob10"));

        List<UserImpl> expected3 = Arrays.asList(createUser("Bob7"),
                createUser("Bob8"),
                createUser("Bob9"),
                createUser("Bob10"));

        //when
        List<UserImpl> actual1 = sut.paginate(users, 3, 2);
        List<UserImpl> actual2 = sut.paginate(users, 3, 4);
        List<UserImpl> actual3 = sut.paginate(users, 6, 2);
        List<UserImpl> actual4 = sut.paginate(users, 6, 3);

        //then
        assertThat(actual1, is(expected1));
        assertThat(actual2, is(expected2));
        assertThat(actual3, is(expected3));
        assertThat(actual4, is(Collections.emptyList()));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenInvalidPageSizePassed() {
        assertThrows(IllegalArgumentException.class, () -> sut.paginate(Collections.emptyList(), 0, 10));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenInvalidPageNumberPassed() {
        assertThrows(IllegalArgumentException.class, () -> sut.paginate(Collections.emptyList(), 2, -2));
    }

    private UserImpl createUser(String name) {
        return UserImpl.builder()
                .name(name)
                .email("BobAlison@Gmail.com")
                .build();
    }
}
