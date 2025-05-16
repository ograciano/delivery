package com.delivery.delivery.listener;

import com.delivery.delivery.model.Assignment;
import com.delivery.delivery.model.Order;
import com.delivery.delivery.repository.AssignmentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedListener {

    private final AssignmentRepository assignmentRepository;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "order.created.queue", ackMode = "MANUAL")
    public void handleOrderCreated(Message message, Channel channel) throws IOException {
        try {
            Order order = objectMapper.readValue(message.getBody(), Order.class);

            log.info("Received new order event: {}", order);

            // Creamos un Assignment con estado "PENDING"
            Assignment assignment = new Assignment();
            assignment.setOrderId(order.getId());
            assignment.setStatus("pending");

            Assignment assignmentSaved= assignmentRepository.save(assignment);
            log.info("Assignment created: {}", assignmentSaved.getOrderId());

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {
            log.error("Error processing new order: {}", e.getMessage(), e);

            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (Exception nackEx) {
                log.error("Error sending NACK: {}", nackEx.getMessage(), nackEx);
            }
        }
    }
}
