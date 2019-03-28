package pl.bak.timserver.exception;

public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(Class clazz, Long id) {
        super("Could not find " + clazz.getSimpleName() + " with ID: " + id);
    }
}
