package kz.danekerscode.customscopes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class SomeClassBeansConfig {

    @Bean
    @Scope(TimedScope.TIMED)
    @TimedScopeDuration(1000)
    public SomeClass someClass1() {
        return new SomeClass();
    }

    @Bean
    @Scope(TimedScope.TIMED)
    public SomeClass someClass2() {
        return new SomeClass();
    }
}
