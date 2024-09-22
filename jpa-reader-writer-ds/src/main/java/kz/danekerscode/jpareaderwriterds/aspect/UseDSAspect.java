package kz.danekerscode.jpareaderwriterds.aspect;

import jakarta.annotation.PostConstruct;
import kz.danekerscode.jpareaderwriterds.annotation.UseDS;
import kz.danekerscode.jpareaderwriterds.config.DatasourceTypeContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class UseDSAspect {

    @PostConstruct
    private void init() {
        log.info("UseDSAspect initialized");
    }

    @Before("@annotation(useDS)")
    public void beforeUseDS(UseDS useDS) {
        log.info("UseDSAspect.beforeUseDS: {}", useDS.value());
        DatasourceTypeContextHolder.setDatasourceType(useDS.value());
    }

    @After("@annotation(useDS)")
    public void afterUseDS(UseDS useDS) {
        DatasourceTypeContextHolder.clear();
        log.info("UseDSAspect context cleared after: {}", useDS.value());
    }
}
