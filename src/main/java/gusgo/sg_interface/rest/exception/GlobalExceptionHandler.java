package gusgo.sg_interface.rest.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import gusgo.sg_interface.rest.resource.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    protected static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class.getName());

    @ExceptionHandler(RestException.class)
    public ResponseEntity<ErrorResponseDTO> handleException(RestException exception) {
        logger.log(Level.SEVERE, exception.getMessage(), exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponseDTO.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .build());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDTO> handleException(BusinessException exception) {
        logger.log(Level.SEVERE, exception.getMessage(), exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponseDTO.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .build());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDTO> badRequestException(final BadRequestException e) {
        log.error("Bad request", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponseDTO.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> illegalArgumentException(final IllegalArgumentException e) {
        log.error("Illegal argument", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponseDTO.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .build());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> dataIntegrityViolationException(final DataIntegrityViolationException e) {
        log.error("Data integrity violation", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponseDTO.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleException(MethodArgumentNotValidException exception) {

        final var result = exception.getBindingResult();
        final var fieldErrors = result.getFieldErrors();

        logger.log(Level.SEVERE, "MethodArgumentNotValidException", exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(processFieldErrors(fieldErrors));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleException(HttpMessageNotReadableException exception) {

        String message = "Request body is missing or with error";

        Throwable root = exception.getRootCause();
        if (root != null) {
            if (root instanceof InvalidFormatException invalidFormatException) {
                message = String.format("Value '%s' is not a valid for '%s'.",
                        invalidFormatException.getValue(),
                        invalidFormatException.getPath().get(0).getFieldName());
            } else {
                message = root.getMessage();
            }
        }

        logger.log(Level.SEVERE, message, exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponseDTO.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .build());
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErrorResponseDTO> handleException(DateTimeParseException exception) {

        logger.log(Level.SEVERE, exception.getMessage(), exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponseDTO.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception exception) {
        logger.log(Level.SEVERE, exception.getMessage(), exception);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponseDTO.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .build());
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponseDTO> handleException(MissingRequestHeaderException exception) {
        logger.log(Level.SEVERE, exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(ErrorResponseDTO.builder()
                .code(HttpStatus.PRECONDITION_FAILED.value())
                .message(exception.getMessage())
                .build());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDTO> handleException(MissingServletRequestParameterException exception) {
        logger.log(Level.SEVERE, exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.PRECONDITION_REQUIRED).body(ErrorResponseDTO.builder()
                .code(HttpStatus.PRECONDITION_REQUIRED.value())
                .message(exception.getMessage())
                .build());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleException(MethodArgumentTypeMismatchException exception) {
        logger.log(Level.SEVERE, exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponseDTO.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(removeJavaLang(exception.getMessage()))
                .build());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleException(NoHandlerFoundException exception) {
        logger.log(Level.SEVERE, exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponseDTO.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .build());
    }

    private ErrorResponseDTO processFieldErrors(final List<FieldError> fieldErrors) {
        final var errors = new ArrayList<>();
        for (final var fieldError : fieldErrors) {
            final var error = ErrorResponseDTO.FieldError.builder().field(fieldError.getField())
                    .message(fieldError.getDefaultMessage()).build();
            errors.add(error);
        }
        return ErrorResponseDTO.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("validation error")
                .details(errors)
                .build();
    }

    private String removeJavaLang(final String input) {
        final var pattern = Pattern.compile("java.lang.", Pattern.MULTILINE);
        final var matcher = pattern.matcher(input);
        return matcher.replaceAll("");
    }

}
