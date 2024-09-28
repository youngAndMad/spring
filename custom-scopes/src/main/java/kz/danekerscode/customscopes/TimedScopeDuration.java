package kz.danekerscode.customscopes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TimedScopeDuration {
    long value();
}