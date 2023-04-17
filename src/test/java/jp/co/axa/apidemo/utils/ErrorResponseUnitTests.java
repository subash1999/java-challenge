package jp.co.axa.apidemo.utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ErrorResponseUnitTests {

	@DisplayName("test for initialize_constructor_using_timestamp_status_error_message_and_path")
	@Test
	public void should_initialize_constructor_using_timestamp_status_error_message_and_path() {
		String timestamp = LocalDateTime.now().toString();
		int status = HttpStatus.BAD_REQUEST.value();
		String error = HttpStatus.BAD_REQUEST.getReasonPhrase();
		String message = "An error occurred";
		String path = "/test-path";
		ErrorResponse errorResponse = new ErrorResponse(timestamp, status, error, message, path);

		// Act and Assert
		assertEquals(timestamp, errorResponse.getTimestamp());
		assertEquals(status, errorResponse.getStatus());
		assertEquals(error, errorResponse.getError());
		assertEquals(message, errorResponse.getMessage());
		assertEquals(path, errorResponse.getPath());
	}

	@DisplayName("test for initialize_constructor_using_timestamp_status_error_message_and_request")
	@Test
	public void should_initialize_constructor_using_timestamp_status_error_message_and_request() {
		String timestamp = LocalDateTime.now().toString();
		int status = HttpStatus.BAD_REQUEST.value();
		String error = HttpStatus.BAD_REQUEST.getReasonPhrase();
		String message = "An error occurred";
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getRequestURI()).thenReturn("/test-path");
		ErrorResponse errorResponse = new ErrorResponse(timestamp, status, error, message, request);

		// Act and Assert
		assertEquals(timestamp, errorResponse.getTimestamp());
		assertEquals(status, errorResponse.getStatus());
		assertEquals(error, errorResponse.getError());
		assertEquals(message, errorResponse.getMessage());
		assertEquals("/test-path", errorResponse.getPath());
	}

	@DisplayName("test for initialize_constructor_using_status_error_message_and_path")
	@Test
	public void should_initialize_constructor_using_status_error_message_and_path() {
		int status = HttpStatus.BAD_REQUEST.value();
		String error = HttpStatus.BAD_REQUEST.getReasonPhrase();
		String message = "An error occurred";
		String path = "/test-path";
		ErrorResponse errorResponse = new ErrorResponse(status, error, message, path);

		// Act and Assert
		assertNotNull(errorResponse.getTimestamp());
		assertEquals(status, errorResponse.getStatus());
		assertEquals(error, errorResponse.getError());
		assertEquals(message, errorResponse.getMessage());
		assertEquals(path, errorResponse.getPath());
	}

	@DisplayName("test for initialize_constructor_using_status_error_message_and_request")
	@Test
	public void should_initialize_constructor_using_status_error_message_and_request() {
		int status = HttpStatus.BAD_REQUEST.value();
		String error = HttpStatus.BAD_REQUEST.getReasonPhrase();
		String message = "An error occurred";
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getRequestURI()).thenReturn("/test-path");
		ErrorResponse errorResponse = new ErrorResponse(status, error, message, request);

		// Act and Assert
		assertNotNull(errorResponse.getTimestamp());
		assertEquals(status, errorResponse.getStatus());
		assertEquals(error, errorResponse.getError());
		assertEquals(message, errorResponse.getMessage());
		assertEquals("/test-path", errorResponse.getPath());
	}

	@DisplayName("test for should_initialize_constructor_using_statusCode_message_and_request")
	@Test
	public void should_initialize_constructor_using_statusCode_message_and_request() {
		int statusCode = HttpStatus.BAD_REQUEST.value();
		String message = "An error occurred";
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getRequestURI()).thenReturn("/test-path");
		ErrorResponse errorResponse = new ErrorResponse(statusCode, message, request);

		// Act and Assert
		assertNotNull(errorResponse.getTimestamp());
		assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatus());
		assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorResponse.getError());
		assertEquals(message, errorResponse.getMessage());
		assertEquals("/test-path", errorResponse.getPath());
	}

	@DisplayName("test for should_initialize_constructor_using_HttpStatus_message_and_request")
	@Test
	    public void should_initialize_constructor_using_HttpStatus_message_and_request() {
	        String message = "An error occurred";
	        HttpServletRequest request = mock(HttpServletRequest.class);
	        when(request.getRequestURI()).thenReturn("/test-path");
	        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, message, request);

	        // Act and Assert
	        assertNotNull(errorResponse.getTimestamp());
	        assertEquals(HttpStatus.BAD_REQUEST.value(),errorResponse.getStatus());
	    	assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorResponse.getError());
			assertEquals(message, errorResponse.getMessage());
			assertEquals("/test-path", errorResponse.getPath());
	 }

}
