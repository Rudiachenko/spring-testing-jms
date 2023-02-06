package spring.test.jms.util.annotation;

import spring.test.jms.model.impl.UserImpl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindStaticData {
    String fileLocation();

    Class<UserImpl> castTo();
}
