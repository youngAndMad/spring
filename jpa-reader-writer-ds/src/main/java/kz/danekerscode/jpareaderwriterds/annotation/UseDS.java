package kz.danekerscode.jpareaderwriterds.annotation;

import kz.danekerscode.jpareaderwriterds.config.DatasourceType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UseDS {

    DatasourceType value();

}
