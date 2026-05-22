package com.viinidev.notification.messaging;

import com.viinidev.notification.dto.NotificationMessage;
import com.viinidev.notification.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    private final NotificationService notificationService;

    public NotificationConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    public void consume(NotificationMessage message) {
        notificationService.process(message.notificationId());
    }
}
