package ru.practicum.ewm.main.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.ewm.main.CommonUtils;
import ru.practicum.ewm.main.comment.common.ClaimState;
import ru.practicum.ewm.main.user.dto.UserDto;

import java.io.Serializable;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Jacksonized
@Value
public class ClaimDto implements Serializable {
    Long id;

    UserDto author;

    Long commentId;

    ClaimState state;

    @JsonFormat(pattern = CommonUtils.DATE_FORMAT)
    LocalDateTime createdOn;

    @JsonFormat(pattern = CommonUtils.DATE_FORMAT)
    LocalDateTime allowedOn;
}