package spring.test.jms.util;

import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Setter
@Component
public class Paginator<T> {
    private static final int MIN_PAGE_VALUE = 0;

    public List<T> paginate(List<T> entities, int pageSize, int pageNumber) {
        if (pageSize <= MIN_PAGE_VALUE || pageNumber <= MIN_PAGE_VALUE) {
            throw new IllegalArgumentException("Incorrect value for pageSize or/and pageNumber. " +
                    "Page value can't be equals or less than 0");
        }

        int startIndex = (pageNumber - 1) * pageSize;

        if (entities.size() <= startIndex) {
            return Collections.emptyList();
        }

        return entities.subList(startIndex, Math.min(startIndex + pageSize, entities.size()));
    }

}