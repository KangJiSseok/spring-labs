package com.example.springrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.QueueBuilder;

/**
 * 클래식 큐 중 우선순위를 두어서 처리
 */
@Configuration
public class RabbitMqClassicQueueConfig {

    /**
     * 클래식 큐 내에 최대 우선순위를 지정하여, 메시지의 우선순위에 따라 처리 합니다.
     */
    @Bean
    public Queue classicPriorityQueue() {
        return QueueBuilder
                .durable("classicPriorityQueue")
                .maxPriority(255)
                .build();
    }

    /**
     * Direct Exchange
     */
    @Bean
    public DirectExchange classicPriorityExchange() {
        return ExchangeBuilder.directExchange("exchange.direct.priorityQueue").build();
    }

    /**
     * classicPriorityQueue와 classicPriorityExchange를 바인딩합니다.
     */
    @Bean
    Binding classicPriorityBind(DirectExchange classicPriorityExchange, Queue classicPriorityQueue) {
        return BindingBuilder
                .bind(classicPriorityQueue)
                .to(classicPriorityExchange)
                .with("classicPriorityQueue");     // 라우팅 키 (Routing key)
    }

}
