package pl.codecity.module.users.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import pl.codecity.module.users.dto.ErrorDTO;

@EnableWebMvc
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity notFountGlobal(Exception ex){
        ErrorDTO errorDTO = new ErrorDTO(ex.getMessage());
        return new ResponseEntity(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
