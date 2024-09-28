package kz.danekerscode.customscopes;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.Assert;

public class CustomScopesApplication {

    public static void main(String[] args) throws InterruptedException {
        var ctx = new AnnotationConfigApplicationContext();
        ctx.addBeanFactoryPostProcessor(new TimedScopeBeanPostProcessor());
        ctx.register(SomeClassBeansConfig.class);

        ctx.refresh();

        var firstSomeClass1 = ctx.getBean("someClass1", SomeClass.class);
        Long firstSomeClass1CreatedTime = firstSomeClass1.getCreatedTime();

        Thread.sleep(3001);
        var secondSomeClass1 = ctx.getBean("someClass1", SomeClass.class);
        Long secondSomeClass1CreatedTime = secondSomeClass1.getCreatedTime();

        Assert.state(!firstSomeClass1CreatedTime.equals(secondSomeClass1CreatedTime), "The created time should be different");
    }

    static class TimedScopeBeanPostProcessor implements BeanFactoryPostProcessor {
        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            beanFactory.registerScope(TimedScope.TIMED, new TimedScope());
        }
    }


}
