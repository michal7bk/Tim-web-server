package pl.bak.timserver.exception;

public class objectNotFoundExcpetion extends RuntimeException {

    public objectNotFoundExcpetion(Class clazz, Long id) {
        super("Could not find " + clazz.getSimpleName() + " with ID: " + id);
    }
}
