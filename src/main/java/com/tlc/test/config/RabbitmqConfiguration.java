package com.tlc.test.config;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitmqConfiguration {

    private static final String queueName = "test_queue";

    @Bean
    Queue queue() {
        return new Queue(queueName, true);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                  MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        rabbitTemplate.setEncoding(String.valueOf(StandardCharsets.UTF_8));
        rabbitTemplate.setUserCorrelationId(true);
        rabbitTemplate.setUseDirectReplyToContainer(true);
        rabbitTemplate.setUseTemporaryReplyQueues(true);
        rabbitTemplate.setReplyTimeout(TimeUnit.SECONDS.toMillis(30));
        rabbitTemplate.setReceiveTimeout(TimeUnit.SECONDS.toMillis(30));
        rabbitTemplate.setBeforePublishPostProcessors(message -> {
            System.out.println("SEND body: " + new String(message.getBody()));
            System.out.println("SEND properties: " + message.getMessageProperties().toString());
            return message;
        });
        rabbitTemplate.addAfterReceivePostProcessors(message -> {
            System.out.println("RECEIVE body: " + new String(message.getBody()));
            System.out.println("RECEIVE properties: " + message.getMessageProperties().toString());
            return message;
        });
        return rabbitTemplate;
    }

    @Bean
    MessageConverter messageConverter() {
        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter();
        messageConverter.setCreateMessageIds(true);
        messageConverter.setDefaultCharset("UTF-8");

        DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();
        javaTypeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
        javaTypeMapper.addTrustedPackages("*");
        messageConverter.setJavaTypeMapper(javaTypeMapper);

        return messageConverter;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return factory;
    }
}
