package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.test.jms.dao.EventDao;
import spring.test.jms.model.Event;
import spring.test.jms.model.impl.EventImpl;
import spring.test.jms.service.Impl.EventServiceImpl;
import spring.test.jms.util.IdGenerator;

import java.util.Arrays;
import java.util.Date;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {
    private static final long EVENT_ID = 1;
    private static final long NEW_YER_2023 = 1672524000000L;
    private static final long NEW_YER_2022 = 1640988000000L;
    private static final long NEW_YER_AFTER_PARTY_2023 = 1672610400000L;
    private static final long NEW_YER_PRE_PARTY_2023 = 1672351200000L;
    @Mock
    private IdGenerator idGenerator;
    @Mock
    private EventDao eventDao;
    @InjectMocks
    private EventServiceImpl sut;

    @Test
    void shouldReturnEventWhenExistIdPassed() {
        //given
        Event expected = createEvent("New Year", new Date(NEW_YER_2023));
        when(eventDao.getById(1)).thenReturn(Optional.ofNullable(expected));

        //when
        Event actual = sut.getById(1);

        //then
        assertThat(actual, is(expected));
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenNotExistIdPassed() {
        //given
        when(eventDao.getById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> sut.getById(10));
    }

    @Test
    void shouldDeleteEventWhenExistIdPassed() {
        //given
        when(eventDao.deleteEvent(1)).thenReturn(true);

        //when
        boolean actual = sut.deleteEvent(1);

        //then
        assertTrue(actual);
    }

    @Test
    void shouldNotDeleteEventWhenNotExistIdPassed() {
        //given
        when(eventDao.deleteEvent(anyLong())).thenReturn(false);

        //when
        boolean actual = sut.deleteEvent(10);

        //then
        assertFalse(actual);
    }

    @Test
    void shouldUpdateEventWhenExistIdPassed() {
        //given
        Event event = createEvent("New Year", new Date(NEW_YER_2022));
        Event expected = createEvent("New Year Updated", new Date(NEW_YER_2023));
        when(eventDao.updateEvent(event, event.getId())).thenReturn(Optional.ofNullable(expected));

        //when
        Event actual = sut.updateEvent(event, event.getId());

        //then
        assertThat(actual, is(expected));
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenNotExistIdPassedWhileUpdating() {
        //given
        Event event = createEvent("New Year", new Date(NEW_YER_2023));
        when(eventDao.updateEvent(any(Event.class), anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> sut.updateEvent(event, EVENT_ID));
    }

    @Test
    void shouldCreateEvent() {
        //given
        Event event = createEvent("New Year", new Date(NEW_YER_2023));

        Event expected = createEvent("New Year", new Date(NEW_YER_2023));
        expected.setId(0);

        when(eventDao.add(event)).thenReturn(expected);

        //when
        Event actual = sut.createEvent(event);

        //then
        assertThat(actual, is(expected));
    }

    @Test
    void shouldReturnNonEmptyEventsBtTitle() {
        //given
        int pageSize = 5;
        int pageNumber = 2;
        String title = "New Year";
        List<Event> expected = Arrays.asList(createEvent("New Year", new Date(NEW_YER_2023)),
                createEvent("New Year After-party", new Date(NEW_YER_AFTER_PARTY_2023)),
                createEvent("New Year Pre_party", new Date(NEW_YER_PRE_PARTY_2023)));
        when(eventDao.getEventsByTitle(title, pageSize, pageNumber)).thenReturn(expected);

        //when
        List<Event> actual = sut.getEventsByTitle(title, pageSize, pageNumber);

        //then
        assertThat(actual, is(expected));
    }

    @Test
    void shouldReturnNonEmptyEventsByDay() {
        //given
        int pageSize = 5;
        int pageNumber = 2;
        Date date = new Date(123L);
        List<Event> expected = Arrays.asList(createEvent("New Year", new Date(1L)),
                createEvent("New Year After-party", new Date(1L)),
                createEvent("New Year Pre_party", new Date(1L)));
        when(eventDao.getEventsForDay(date, pageSize, pageNumber)).thenReturn(expected);

        //when
        List<Event> actual = sut.getEventsForDay(new Date(123L), pageSize, pageNumber);

        //then
        assertThat(actual, is(expected));
    }

    private Event createEvent(String title, Date date) {
        return EventImpl.builder()
                .title(title)
                .date(date)
                .build();
    }
}
