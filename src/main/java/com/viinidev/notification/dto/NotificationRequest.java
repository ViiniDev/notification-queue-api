package com.viinidev.notification.dto;

import com.viinidev.notification.domain.NotificationChannel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificationRequest(
        @NotBlank String recipient,
        @NotBlank String message,
        @NotNull NotificationChannel channel
) {
}
