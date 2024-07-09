package com.ecom.webshop.store.exceptions;

import com.ecom.webshop.store.dtos.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger= LoggerFactory.getLogger(GlobalExceptionHandler.class);
    //Handle RNF Exception
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){

       logger.info("Exception handler method is called");
       ApiResponseMessage response= ApiResponseMessage.builder().message(ex.getMessage()).status((HttpStatus.NOT_FOUND)).success(true).build();

       return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){

      List<ObjectError> allErrors =ex.getBindingResult().getAllErrors();
      Map<String,Object> response=new HashMap<>();
      allErrors.stream().forEach(objectError -> {
          String message=objectError.getDefaultMessage();
          String field=((FieldError)objectError).getField();
          response.put(field,message);
      });
      return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ApiResponseMessage> propertyReferenceExceptionHandler(PropertyReferenceException ex){

        ApiResponseMessage response= ApiResponseMessage.builder().message(ex.getMessage()).status((HttpStatus.INTERNAL_SERVER_ERROR)).success(true).build();

        return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadApiRequestException.class)
    public ResponseEntity<ApiResponseMessage> HandleBadApiRequest(BadApiRequestException ex){

        logger.info("Bad APi Request");
        ApiResponseMessage response= ApiResponseMessage.builder().message(ex.getMessage()).status((HttpStatus.BAD_REQUEST)).success(true).build();

        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> FileNotFoundEx(FileNotFoundException ex){


        ApiResponseMessage response= ApiResponseMessage.builder().message(ex.getMessage()).status((HttpStatus.NOT_FOUND)).success(true).build();

        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }
}
