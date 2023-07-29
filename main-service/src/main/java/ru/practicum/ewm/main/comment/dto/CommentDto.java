package ru.practicum.ewm.main.comment.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.ewm.main.user.dto.UserDto;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Jacksonized
@Value
public class CommentDto implements Serializable {
    Long id;
    String text;
    UserDto author;
    Long eventId;
    LocalDateTime createdOn;
    LocalDateTime editedOn;
    LocalDateTime deletedOn;
    Integer claimCount;
}