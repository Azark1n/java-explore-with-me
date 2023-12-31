package ru.practicum.ewm.main.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;
import org.springframework.http.HttpStatus;
import ru.practicum.ewm.main.CommonUtils;

import java.time.LocalDateTime;
import java.util.List;

@Value
public class ApiError {
    String message;

    String reason;

    String status;

    @JsonFormat(pattern = CommonUtils.DATE_FORMAT)
    LocalDateTime timestamp;

    List<StackTraceElement> errors;

    public ApiError(String message, Exception exception, HttpStatus status) {
        this.message = message;
        this.reason = exception.getMessage();
        this.status = status.name();
        this.timestamp = LocalDateTime.now();
        this.errors = List.of(exception.getStackTrace());
    }
}
