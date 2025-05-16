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
public class OrderUpdatedListener {

    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "order.updated.queue", ackMode = "MANUAL")
    public void handleOrderUpdated(Message message, Channel channel) throws IOException {
        try {
            Order order = objectMapper.readValue(message.getBody(), Order.class);
            log.info("Received updated order event: {}", order);

            // Aquí puedes agregar la lógica para manejar la actualización del pedido
            // Por ejemplo, actualizar el estado del pedido en la base de datos

            // ACK manual: confirmamos que el mensaje fue procesado correctamente
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("Error processing updated order: {}", e.getMessage(), e);

            try {
                // NACK manual: rechazamos el mensaje, lo podemos requeuear o no
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (Exception nackEx) {
                log.error("Error sending NACK: {}", nackEx.getMessage(), nackEx);
            }
        }
    }
}
