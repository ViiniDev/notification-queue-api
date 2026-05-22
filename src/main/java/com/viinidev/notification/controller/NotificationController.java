package com.viinidev.notification.controller;

import com.viinidev.notification.domain.NotificationStatus;
import com.viinidev.notification.dto.NotificationRequest;
import com.viinidev.notification.dto.NotificationResponse;
import com.viinidev.notification.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public NotificationResponse create(@RequestBody @Valid NotificationRequest request) {
        return notificationService.create(request);
    }

    @GetMapping
    public List<NotificationResponse> list(@RequestParam(required = false) NotificationStatus status) {
        return notificationService.list(status);
    }
}
