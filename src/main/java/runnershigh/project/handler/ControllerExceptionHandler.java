package runnershigh.project.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = CustomValidationException.class)
    public ResponseEntity customExceptionHandler(CustomValidationException e) {

        CommonResDTO commonResDTO = new CommonResDTO<>(e.getCustomExceptionCode().getCode(), e.getCustomExceptionCode().getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonResDTO);

    }

}
