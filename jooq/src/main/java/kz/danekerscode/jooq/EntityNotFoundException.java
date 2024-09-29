package kz.danekerscode.jooq;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Class<?> entityClass, Object id) {
        super(String.format("Entity %s with id %s not found", entityClass.getSimpleName(), id));
    }
}
