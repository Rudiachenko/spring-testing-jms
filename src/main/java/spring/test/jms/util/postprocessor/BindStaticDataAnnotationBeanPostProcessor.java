package spring.test.jms.util.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import spring.test.jms.model.Identifiable;
import spring.test.jms.util.IdGenerator;
import spring.test.jms.util.annotation.BindStaticData;
import spring.test.jms.util.json.JsonMapper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class BindStaticDataAnnotationBeanPostProcessor implements BeanPostProcessor {
    private static final String START_INFO_MESSAGE = "Started binding...";
    private static final String FINISH_INFO_MESSAGE = "Successfully finished binding";
    private final JsonMapper jsonMapper;
    private final IdGenerator idGenerator;

    @Autowired
    public BindStaticDataAnnotationBeanPostProcessor(IdGenerator idGenerator) {
        this.jsonMapper = new JsonMapper();
        this.idGenerator = idGenerator;
    }

    @Override
    @Nullable
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            BindStaticData annotation = field.getAnnotation(BindStaticData.class);
            if (Objects.nonNull(annotation) && Map.class.isAssignableFrom(field.getType())) {
                log.info(START_INFO_MESSAGE);
                JSONArray jsonArray = new JSONArray(readFileFromResources(annotation.fileLocation()));
                Map<Long, Identifiable> hashMap = instantiateCollectionFromJson(jsonArray, annotation.castTo());
                field.setAccessible(true);
                ReflectionUtils.setField(field, bean, hashMap);
                log.info(FINISH_INFO_MESSAGE);
            }
        }
        return bean;
    }

    private <T extends Identifiable> Map<Long, Identifiable> instantiateCollectionFromJson(JSONArray jsonArray,
                                                                                           Class<T> clazz) {
        HashMap<Long, Identifiable> hashMap = new HashMap<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String jsonObjectAsString = jsonObject.toString();
            T entity = fromJson(clazz, jsonObjectAsString);
            long id = idGenerator.generateId(clazz);
            entity.setId(id);
            hashMap.put(entity.getId(), entity);
        }
        return hashMap;
    }

    private <T extends Identifiable> T fromJson(Class<T> clazz, String json) {
        return jsonMapper.fromJson(json, clazz);
    }

    private String readFileFromResources(String filename) {
        URL resource = BindStaticDataAnnotationBeanPostProcessor.class.getClassLoader().getResource(filename);
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(Paths.get(resource.toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return new String(bytes);
    }
}
