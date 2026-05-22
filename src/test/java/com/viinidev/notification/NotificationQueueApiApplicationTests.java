package com.viinidev.notification;

import com.viinidev.notification.domain.NotificationChannel;
import com.viinidev.notification.dto.NotificationRequest;
import com.viinidev.notification.repository.NotificationRepository;
import com.viinidev.notification.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class NotificationQueueApiApplicationTests {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Test
    void shouldQueueAndProcessNotification() {
        var response = notificationService.create(new NotificationRequest(
                "vinicius@email.com",
                "Seu pedido foi criado.",
                NotificationChannel.EMAIL
        ));

        notificationService.process(response.id());

        var notification = notificationRepository.findById(response.id()).orElseThrow();

        assertThat(notification.getStatus().name()).isEqualTo("PROCESSED");
        assertThat(notification.getProcessedAt()).isNotNull();
    }
}
