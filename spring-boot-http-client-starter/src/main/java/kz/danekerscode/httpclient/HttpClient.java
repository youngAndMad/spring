package kz.danekerscode.httpclient;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface HttpClient {

    String name();

    String baseUrl() default "";

    String url() default "";

}
