package gestionempleados.controller.advice;

import gestionempleados.exceptions.CustomEntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice {
    @ExceptionHandler(CustomEntityNotFoundException.class)
    public ResponseEntity<String> handleCustomEntityNotFoundException(CustomEntityNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body("Error: " + exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
                                                                          HttpServletRequest request) {
        List<String> errors = new ArrayList<>();
        if (!e.getFieldErrors()
              .isEmpty()) {
            e.getFieldErrors()
             .forEach(fieldError -> {
                 String defaultMessage =
                         fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage()
                                                                            .toUpperCase() : "default error message not added";
                 String message = String.format("error in field %s: %s",
                                                fieldError.getField(),
                                                defaultMessage);
                 errors.add(message);
             });
        }
        ApiError apiError = ApiError.builder()
                                    .backendMessage(e.getLocalizedMessage())
                                    .message("Error en la petición enviada")
                                    .errors(errors)
                                    .url(request.getRequestURL()
                                                .toString())
                                    .method(request.getMethod())
                                    .timestamp(LocalDateTime.now())
                                    .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(apiError);
    }

}
