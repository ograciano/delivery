package com.delivery.order.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "deliveryapp.exchange";
    public static final String DEAD_LETTER_EXCHANGE = "deadletter.exchange";

    public static final String ORDER_CREATED_QUEUE = "order.created.queue";
    public static final String ORDER_UPDATED_QUEUE = "order.updated.queue";
    public static final String ORDER_PAID_QUEUE = "order.paid.queue";
    public static final String ORDER_CANCELLED_QUEUE = "order.cancelled.queue";
    public static final String ORDER_DELIVERED_QUEUE = "order.delivered.queue";
    public static final String DEAD_LETTER_QUEUE = "deadletter.queue";

    public static final String ORDER_CREATED_ROUTING_KEY = "order.created";
    public static final String ORDER_UPDATED_ROUTING_KEY = "order.updated";
    public static final String ORDER_PAID_ROUTING_KEY = "order.paid";
    public static final String ORDER_CANCELLED_ROUTING_KEY = "order.cancelled";
    public static final String ORDER_DELIVERED_ROUTING_KEY = "order.delivered";
    public static final String DEAD_LETTER_ROUTING_KEY = "deadletter";

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public TopicExchange appExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE);
    }

    @Bean
    public Queue orderCreatedQueue() {
        return QueueBuilder.durable(ORDER_CREATED_QUEUE)
                .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue orderUpdatedQueue() {
        return QueueBuilder.durable(ORDER_UPDATED_QUEUE)
                .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue orderPaidQueue() {
        return QueueBuilder.durable(ORDER_PAID_QUEUE)
                .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue orderCancelledQueue() {
        return QueueBuilder.durable(ORDER_CANCELLED_QUEUE)
                .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue orderDeliveredQueue() {
        return QueueBuilder.durable(ORDER_DELIVERED_QUEUE)
                .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    @Bean
    public Binding bindingOrderCreated() {
        return BindingBuilder.bind(orderCreatedQueue()).to(appExchange()).with(ORDER_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding bindingOrderUpdated() {
        return BindingBuilder.bind(orderUpdatedQueue()).to(appExchange()).with(ORDER_UPDATED_ROUTING_KEY);
    }

    @Bean
    public Binding bindingOrderPaid() {
        return BindingBuilder.bind(orderPaidQueue()).to(appExchange()).with(ORDER_PAID_ROUTING_KEY);
    }

    @Bean
    public Binding bindingOrderCancelled() {
        return BindingBuilder.bind(orderCancelledQueue()).to(appExchange()).with(ORDER_CANCELLED_ROUTING_KEY);
    }

    @Bean
    public Binding bindingOrderDelivered() {
        return BindingBuilder.bind(orderDeliveredQueue()).to(appExchange()).with(ORDER_DELIVERED_ROUTING_KEY);
    }

    @Bean
    public Binding deadLetterBinding(DirectExchange deadLetterExchange, Queue deadLetterQueue) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange).with(DEAD_LETTER_ROUTING_KEY);
    }

    // Acknowledge mode MANUAL
    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }

}
