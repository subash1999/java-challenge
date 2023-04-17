package jp.co.axa.apidemo.exceptions;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import jp.co.axa.apidemo.utils.ErrorResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * GlobalExceptionHandlerToHandle the exceptions
 *
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception ex, HttpServletRequest request) throws Exception {
		log.error("Path : "+request.getRequestURI()+" An exception occurred: ", ex);
		throw ex;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {

		List<String> errors = ex.getBindingResult().getAllErrors().stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());

		String message = "Validation failed. " + String.join(", ", errors);
		log.error("Path : "+request.getRequestURI()+" "+message);
		return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message, request);
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorResponse handleException(HttpMessageNotReadableException ex, HttpServletRequest request) {
		log.error("Path : "+request.getRequestURI()+" "+ex.getLocalizedMessage());
		return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getLocalizedMessage(), request);
    }
	
	@ExceptionHandler(value = { JsonMappingException.class })
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleJsonMappingException(JsonMappingException ex, HttpServletRequest request) {
		String message = "Json Mapping Exception : "  + ex.getPath().get(0).getFieldName();
		log.error("Path : "+request.getRequestURI()+" "+message);
		return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message, request);
	}

	@ExceptionHandler(InvalidFormatException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleInvalidFormatException(InvalidFormatException ex, HttpServletRequest request) {
		String message = "Invalid value for field " + ex.getPath().get(0).getFieldName();
		log.error("Path : "+request.getRequestURI()+" "+message);
		return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message, request);
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handleInvalidFormatException(EntityNotFoundException ex, HttpServletRequest request) {
		String message =ex.getMessage();
		log.error("Path : "+request.getRequestURI()+" "+message);
		return new ErrorResponse(HttpStatus.NOT_FOUND, message, request);
	}
	
	@ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex,HttpServletRequest request) {
        if (ex.getStatus() == HttpStatus.NOT_FOUND) {
            ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "Not Found",request);
            log.error("Path : "+request.getRequestURI()+" "+ex.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }	
        else {
        	log.error("Path : "+request.getRequestURI()+" "+ex.getMessage());
            ErrorResponse error = new ErrorResponse(ex.getStatus(), ex.getMessage(),request);
            return new ResponseEntity<>(error, ex.getStatus());
        }
    }

}
