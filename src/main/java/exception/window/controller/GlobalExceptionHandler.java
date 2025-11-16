package exception.window.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler { // Handle all exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseException> handleAllExceptions(
            Exception ex,
            WebRequest request
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage() +", For Request: "+ request.getContextPath());
        ErrorResponseException errorResponse = new ErrorResponseException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                problemDetail,
                ex
        );

        return new ResponseEntity<>(errorResponse, errorResponse.getStatusCode());
    }
}
