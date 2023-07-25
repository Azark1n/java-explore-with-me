package ru.practicum.ewm.main.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ErrorHandlingControllerAdvice {
    @ExceptionHandler({DataIntegrityViolationException.class, ForbiddenException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(final RuntimeException exception) {
        log.error(exception.toString());
        return new ApiError("Integrity constraint has been violated", exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(final RuntimeException exception) {
        log.error("Error 400: {}", exception.getMessage(), exception);
        return new ApiError("Unhandled exception", exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({BadRequestException.class, MissingServletRequestParameterException.class, HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError onBadRequestException(Exception e) {
        log.error(e.toString());
        return new ApiError("Request is incorrect", e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError onConstraintValidationException(ConstraintViolationException e) {
        String violationsInfo = e.getConstraintViolations().stream()
                .map(violation -> "Field" + violation.getPropertyPath() + " - " + violation.getMessage())
                .collect(Collectors.joining(", "));

        log.error(violationsInfo);
        return new ApiError(violationsInfo, e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String violationsInfo = e.getBindingResult().getFieldErrors().stream()
                .map(violation -> violation.getField() + " - " + violation.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.error(violationsInfo);
        return new ApiError(violationsInfo, e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError onNotFoundException(RuntimeException e) {
        log.error(e.toString());
        return new ApiError("The required object was not found", e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({StatsInternalException.class, ResourceAccessException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError onStatsInternalException(RuntimeException e) {
        log.error(e.toString());
        return new ApiError("Stats problem", e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
