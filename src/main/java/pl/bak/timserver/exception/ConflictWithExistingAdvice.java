package pl.bak.timserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ConflictWithExistingAdvice {

    @ResponseBody
    @ExceptionHandler(ConflictWithExistingException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String conflictsHandler(ConflictWithExistingException ex ){return ex.getMessage();}
}
