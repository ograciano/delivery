package com.delivery.delivery.listener;

import com.delivery.delivery.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderDeliveredListener {

    private final Commons commons;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "order.delivered.queue", ackMode = "MANUAL")
    public void handleOrderDelivered(Message message, Channel channel) throws IOException {
        try {
            Order order = objectMapper.readValue(message.getBody(), Order.class);
            log.info("Received DELIVERED order event: {}", order);

            commons.updateAssignmentAndDriverStatus(order, "delivered");

            // ACK manual: confirmamos que el mensaje fue procesado correctamente
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("Error processing delivered order: {}", e.getMessage(), e);

            try {
                // NACK manual: rechazamos el mensaje, lo podemos requeuear o no
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (Exception nackEx) {
                log.error("Error sending NACK: {}", nackEx.getMessage(), nackEx);
            }
        }
    }
}

