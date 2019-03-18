package pl.bak.timserver.exception;

public class ConflictWithExistingException extends RuntimeException {

    public ConflictWithExistingException(Class clazz, Long id) {
        super("Resources : " + clazz.getSimpleName() + "with ID : " + id + "confflict with existing resources.");
    }
}
