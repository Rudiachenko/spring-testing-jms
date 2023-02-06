package spring.test.jms.util.json.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import spring.test.jms.model.impl.UserImpl;

import java.lang.reflect.Type;

public class UserAdapter implements JsonDeserializer<UserImpl> {
    private static final String NAME = "name";
    private static final String EMAIL = "email";

    @Override
    public UserImpl deserialize(JsonElement jsonElement,
                                Type type,
                                JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();
        return UserImpl.builder()
                .name(object.get(NAME).getAsString())
                .email(object.get(EMAIL).getAsString())
                .build();
    }
}