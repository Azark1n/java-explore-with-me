package ru.practicum.ewm.main.comment.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.ewm.main.comment.common.ClaimState;
import ru.practicum.ewm.main.user.dto.UserDto;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Jacksonized
@Value
public class ClaimDto implements Serializable {
    Long id;
    UserDto author;
    Long commentId;
    ClaimState state;
    LocalDateTime allowedOn;
    UserDto allowedAuthor;
}