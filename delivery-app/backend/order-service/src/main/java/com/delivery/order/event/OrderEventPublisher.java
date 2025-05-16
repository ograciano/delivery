package com.delivery.order.event;

import com.delivery.order.config.RabbitMQConfig;
import com.delivery.order.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishOrderCreated(Order order) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                "order.created",
                order
        );
    }

    public void publishOrderUpdated(Order order) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                "order.updated",
                order
        );
    }

    public void publishOrderPaid(Order order) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                "order.paid",
                order
        );
    }

    public void publishOrderCancelled(Order order) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                "order.cancelled",
                order
        );
    }

    public void publishOrderDelivered(Order order) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                "order.delivered",
                order
        );
    }
}

