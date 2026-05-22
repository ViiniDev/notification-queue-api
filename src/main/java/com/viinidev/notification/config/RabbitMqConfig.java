package com.viinidev.notification.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public DirectExchange notificationExchange(@Value("${app.rabbitmq.exchange}") String exchange) {
        return new DirectExchange(exchange);
    }

    @Bean
    public Queue notificationQueue(@Value("${app.rabbitmq.queue}") String queue) {
        return QueueBuilder.durable(queue).build();
    }

    @Bean
    public Binding notificationBinding(
            Queue notificationQueue,
            DirectExchange notificationExchange,
            @Value("${app.rabbitmq.routing-key}") String routingKey
    ) {
        return BindingBuilder.bind(notificationQueue).to(notificationExchange).with(routingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
