package com.viinidev.notification.service;

import com.viinidev.notification.domain.Notification;
import com.viinidev.notification.domain.NotificationStatus;
import com.viinidev.notification.dto.NotificationMessage;
import com.viinidev.notification.dto.NotificationRequest;
import com.viinidev.notification.dto.NotificationResponse;
import com.viinidev.notification.repository.NotificationRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;

    public NotificationService(
            NotificationRepository notificationRepository,
            RabbitTemplate rabbitTemplate,
            @Value("${app.rabbitmq.exchange}") String exchange,
            @Value("${app.rabbitmq.routing-key}") String routingKey
    ) {
        this.notificationRepository = notificationRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    @Transactional
    public NotificationResponse create(NotificationRequest request) {
        Notification notification = notificationRepository.save(new Notification(
                request.recipient().trim(),
                request.message().trim(),
                request.channel()
        ));
        rabbitTemplate.convertAndSend(exchange, routingKey, new NotificationMessage(notification.getId()));
        return NotificationResponse.from(notification);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> list(NotificationStatus status) {
        List<Notification> notifications = status == null
                ? notificationRepository.findAll()
                : notificationRepository.findByStatus(status);
        return notifications.stream().map(NotificationResponse::from).toList();
    }

    @Transactional
    public void process(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found."));
        notification.markAsProcessed();
        notificationRepository.save(notification);
    }
}
