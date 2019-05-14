package pl.bak.timserver.exception;

public class ConflictWithExistingException extends RuntimeException {

    public ConflictWithExistingException(Class clazz, Long id) {
        super("Resources : " + clazz.getSimpleName() + " with ID : " + id + " conflict with existing resources.");
    }

    public ConflictWithExistingException(String conflictField, String conflictValue){
        super("Resources : " + conflictField + " with value: " + conflictValue + " conflict with existing resources.");
    }
}
