package com.delivery.delivery.event;

import com.delivery.delivery.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DeliveryEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publishDriverAssigned(String orderId, String driverId) {
        Map<String, String> publishMessage = new HashMap<>();
        publishMessage.put("orderId", orderId);
        publishMessage.put("driverId", driverId);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                "driver.assigned",
                publishMessage

        );
    }
}
