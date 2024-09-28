package kz.danekerscode.customscopes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@Bean
@Scope(TimedScope.TIMED)

public @interface TimedScopeBean {
}
