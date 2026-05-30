package com.viinidev.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viinidev.notification.repository.NotificationRepository;
import com.viinidev.notification.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NotificationFlowIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Test
    void shouldQueueNotificationPublishMessageAndListByStatus() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "recipient": "vinicius@email.com",
                                  "message": "Seu pedido foi criado.",
                                  "channel": "EMAIL"
                                }
                                """))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value("QUEUED"))
                .andReturn();

        Long id = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();

        verify(rabbitTemplate).convertAndSend(eq("notifications.exchange"), eq("notifications.created"), any(Object.class));

        mockMvc.perform(get("/api/notifications")
                        .param("status", "QUEUED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == %d)]".formatted(id)).exists());
    }

    @Test
    void shouldProcessQueuedNotification() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "recipient": "cliente@email.com",
                                  "message": "Chamado atualizado.",
                                  "channel": "SMS"
                                }
                                """))
                .andExpect(status().isAccepted())
                .andReturn();

        Long id = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
        notificationService.process(id);

        var notification = notificationRepository.findById(id).orElseThrow();

        mockMvc.perform(get("/api/notifications")
                        .param("status", "PROCESSED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == %d)]".formatted(notification.getId())).exists());
    }

    @Test
    void shouldRejectInvalidNotificationRequest() throws Exception {
        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "recipient": "",
                                  "message": "",
                                  "channel": null
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }
}
