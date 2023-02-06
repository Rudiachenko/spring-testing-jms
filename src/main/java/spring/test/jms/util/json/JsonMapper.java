package spring.test.jms.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spring.test.jms.model.Event;
import spring.test.jms.model.Identifiable;
import spring.test.jms.model.Ticket;
import spring.test.jms.model.User;
import spring.test.jms.util.json.adapters.EventAdapter;
import spring.test.jms.util.json.adapters.TicketAdapter;
import spring.test.jms.util.json.adapters.UserAdapter;

public class JsonMapper {
    private final Gson delegate;

    private static JsonMapper withTypeAdapters() {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .disableHtmlEscaping()
                .registerTypeAdapter(Event.class, new EventAdapter())
                .registerTypeAdapter(Ticket.class, new TicketAdapter())
                .registerTypeAdapter(User.class, new UserAdapter());
        return new JsonMapper(gsonBuilder.create());
    }

    public JsonMapper(Gson delegate) {
        this.delegate = delegate;
    }

    public JsonMapper() {
        this.delegate = withTypeAdapters().delegate;
    }

    public <T extends Identifiable> T fromJson(String json, Class<T> clazz) {
        return delegate.fromJson(json, clazz);
    }
}
