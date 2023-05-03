package kr.co.common.aop;

import kr.co.common.exception.CustomException;
import kr.co.common.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity CustomException(CustomException e){
       return new ResponseEntity(ErrorDto.createErrorDto(e), HttpStatus.valueOf(e.getStatusCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity commonException(Exception e){
        return new ResponseEntity(ErrorDto.createErrorDto(e), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
