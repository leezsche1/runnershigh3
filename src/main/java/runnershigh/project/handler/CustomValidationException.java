package runnershigh.project.handler;

import lombok.Getter;

@Getter
public class CustomValidationException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private CustomExceptionCode customExceptionCode;

    public CustomValidationException(String message, CustomExceptionCode customExceptionCode) {
        super(message);
        this.customExceptionCode = customExceptionCode;
    }

}
