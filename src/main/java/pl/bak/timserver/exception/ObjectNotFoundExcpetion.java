package pl.bak.timserver.exception;

public class ObjectNotFoundExcpetion extends RuntimeException {

    public ObjectNotFoundExcpetion(Class clazz, Long id) {
        super("Could not find " + clazz.getSimpleName() + " with ID: " + id);
    }
}
