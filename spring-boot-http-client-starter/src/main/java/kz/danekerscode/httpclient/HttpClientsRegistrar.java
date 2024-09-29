package kz.danekerscode.httpclient;

import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Setter
@Slf4j
class HttpClientsRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware, ApplicationContextAware {
    private ResourceLoader resourceLoader;
    private Environment environment;
    private LoadBalancerClient lbClient;

    @Override
    public void registerBeanDefinitions(
            @NonNull AnnotationMetadata annotationMetadata,
            @NonNull BeanDefinitionRegistry registry
    ) {
        LinkedHashSet<BeanDefinition> candidateComponents = new LinkedHashSet<>();
        Map<String, Object> attrs = annotationMetadata.getAnnotationAttributes(EnableHttpClients.class.getName());

        Class<?>[] clients = attrs == null ? null : (Class<?>[]) attrs.get("clients");

        if (clients != null && clients.length != 0) {
            Arrays.stream(clients)
                    .map(AnnotatedGenericBeanDefinition::new)
                    .forEach(candidateComponents::add);
        } else {
            var scanner = getScanner();

            scanner.setResourceLoader(this.resourceLoader);
            scanner.addIncludeFilter(new AnnotationTypeFilter(HttpClient.class));

            var basePackages = this.getBasePackages(annotationMetadata);
            basePackages.stream()
                    .map(scanner::findCandidateComponents)
                    .forEach(candidateComponents::addAll);
        }

        if (candidateComponents.isEmpty()){
            log.warn("No HTTP clients found in base packages");
        }


    }

    protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attributes = importingClassMetadata.getAnnotationAttributes(EnableHttpClients.class.getCanonicalName());
        Set<String> basePackages = new HashSet<>();

        var packages = (String[]) attributes.get("basePackages");
        Arrays.stream(packages)
                .filter(StringUtils::hasText)
                .forEach(basePackages::add);

        if (basePackages.isEmpty()) {
            basePackages.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }

        return basePackages;
    }

    /**
     * ClassPathScanningCandidateComponentProvider по дефолту не признает интерфейсы
     * как кандидатов, поэтому переопределяем метод isCandidateComponent
     */
    private ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
            protected boolean isCandidateComponent(@NonNull AnnotatedBeanDefinition beanDefinition) {
                return beanDefinition.getMetadata().isIndependent() && !beanDefinition.getMetadata().isAnnotation();
            }
        };
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx)
            throws BeansException {
        this.lbClient = ctx.getBean(LoadBalancerClient.class);
    }
}
