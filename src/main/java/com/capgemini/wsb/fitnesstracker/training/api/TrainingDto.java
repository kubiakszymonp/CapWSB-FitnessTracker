package com.capgemini.wsb.fitnesstracker.training.api;

import com.capgemini.wsb.fitnesstracker.training.internal.ActivityType;
import com.capgemini.wsb.fitnesstracker.user.api.UserDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

public record TrainingDto(
        @Nullable Long id,
        @Nullable Long userId,
        @Nullable UserDto user,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS+00:00")
        Date startTime,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS+00:00")
        Date endTime,
        ActivityType activityType,
        double distance,
        double averageSpeed
) {
}
