package com.delivery.delivery.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "deliveryapp.exchange";
    public static final String DEAD_LETTER_EXCHANGE = "deadletter.exchange";

    public static final String DRIVER_ASSIGNED_QUEUE = "driver.assigned.queue";
    public static final String DEAD_LETTER_QUEUE = "deadletter.queue";

    public static final String DRIVER_ASSIGNED_ROUTING_KEY = "driver.assigned";
    public static final String DEAD_LETTER_ROUTING_KEY = "deadletter";

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setClassMapper(classMapper());
        return converter;
    }

    @Bean
    public ClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();

        // 👇 Aquí defines cada tipo de mensaje que quieras recibir
        idClassMapping.put("com.delivery.order.model.Order", com.delivery.delivery.model.Order.class);
        idClassMapping.put("com.delivery.order.model.Product", com.delivery.delivery.model.Product.class);
        // Si en el futuro tienes más clases, solo agregas aquí:
        // idClassMapping.put("com.delivery.order.model.XYZ", com.delivery.delivery.model.XYZ.class);

        classMapper.setIdClassMapping(idClassMapping);
        return classMapper;
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
    public Queue driverAssignedQueue() {
        return QueueBuilder.durable(DRIVER_ASSIGNED_QUEUE)
                .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }


    @Bean
    public Binding bindingDriverAssigned() {
        return BindingBuilder.bind(driverAssignedQueue()).to(appExchange()).with(DRIVER_ASSIGNED_ROUTING_KEY);
    }

    @Bean
    public Binding deadLetterBinding(DirectExchange deadLetterExchange, Queue deadLetterQueue) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange).with(DEAD_LETTER_ROUTING_KEY);
    }

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }



}

