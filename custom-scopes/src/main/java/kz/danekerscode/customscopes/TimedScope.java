package kz.danekerscode.customscopes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class TimedScope implements Scope {

    public static final String TIMED = "timed";
    private static final ConcurrentHashMap<String, Long> lastCreationTimes = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Long> durations = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Object> cachedObjects = new ConcurrentHashMap<>();
    public static final long DEFAULT_BEAN_TTL = 3000L;

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        if (!cachedObjects.containsKey(name)) {
            log.info("Creating new object for {}", name);
            var object = objectFactory.getObject();
            cachedObjects.put(name, object);
            setDurationFromAnnotation(name, object);
            lastCreationTimes.put(name, System.currentTimeMillis());
        }

        var duration = durations.getOrDefault(name, DEFAULT_BEAN_TTL); // default to 3 seconds

        if (System.currentTimeMillis() - lastCreationTimes.get(name) > duration) {
            log.info("Creating new object for {}, because bean ttl was expired", name);
            Object object = objectFactory.getObject();
            cachedObjects.put(name, object);
            lastCreationTimes.put(name, System.currentTimeMillis());
        }

        return cachedObjects.get(name);
    }

    private void setDurationFromAnnotation(String name, Object object) {
        var clazz = object.getClass();
        if (clazz.isAnnotationPresent(TimedScopeDuration.class)) {
            log.info("Setting duration for {} from annotation", name);
            durations.put(name, clazz.getAnnotation(TimedScopeDuration.class).value());
            return;
        }

        Arrays.stream(clazz.getDeclaredMethods()).filter(method -> method.getName().equals(name))
                .findFirst().ifPresentOrElse(method -> {
                    var methodAnnotation = method.getAnnotation(TimedScopeDuration.class);
                    if (methodAnnotation != null) {
                        log.info("Setting duration for {} from annotation", name);
                        durations.put(name, methodAnnotation.value());
                    }
                }, () -> {
                    log.info("Setting default duration for {}", name);
                    durations.put(name, DEFAULT_BEAN_TTL); // default to 3 seconds
                });
    }

    @Override
    public Object remove(String name) {
        lastCreationTimes.remove(name);
        durations.remove(name);
        return cachedObjects.remove(name);
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        return TIMED;
    }
}