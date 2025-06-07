package com.delivery.delivery.listener;

import com.delivery.delivery.model.Order;
import com.delivery.delivery.repository.AssignmentRepository;
import com.delivery.delivery.repository.DriverRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderCancelledListener {

    private final Commons commons;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "order.cancelled.queue", ackMode = "MANUAL")
    public void handleOrderCancelled(Message message, Channel channel) throws IOException {
        try {
            Order order = objectMapper.readValue(message.getBody(), Order.class);
            log.info("Received CANCELLED order event: {}", order);

            commons.updateAssignmentAndDriverStatus(order, "cancelled");

            // ACK manual: confirmamos que el mensaje fue procesado correctamente
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("Error processing cancelled order: {}", e.getMessage(), e);

            try {
                // NACK manual: rechazamos el mensaje, lo podemos requeuear o no
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (Exception nackEx) {
                log.error("Error sending NACK: {}", nackEx.getMessage(), nackEx);
            }
        }
    }
}
