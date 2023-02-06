package spring.test.jms.util.json.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import spring.test.jms.model.Ticket;
import spring.test.jms.model.impl.TicketImpl;

import java.lang.reflect.Type;

public class TicketAdapter implements JsonDeserializer<Ticket> {
    public static final String USER_ID = "user_id";
    public static final String EVENT_ID = "event_id";
    public static final String CATEGORY = "category";
    public static final String PLACE = "place";

    @Override
    public Ticket deserialize(JsonElement jsonElement,
                              Type type,
                              JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();
        return TicketImpl.builder()
                .userId(object.get(USER_ID).getAsLong())
                .eventId(object.get(EVENT_ID).getAsLong())
                .category(Ticket.Category.valueOf(object.get(CATEGORY).getAsString()))
                .place(object.get(PLACE).getAsInt())
                .build();
    }
}
