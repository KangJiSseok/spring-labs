package com.example.springrabbitmq.config;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ의 메시지 생성자(Message Producer) 설정파일 입니다.
 * - 기본적인 큐를 구성하고 오류를 강제로 발생 시켜서 DeadLetter을 테스트
 */
@Configuration
public class RabbitMqDeadLetterConfig {

    /**
     * Queue 구성 : Dead Letter Queue로 이용
     * - 성공적으로 처리하지 못한 메시지가 해당 큐에 들어옵니다.
     */
    @Bean
    public Queue deadLetterQueue() {
        return new Queue("deadLetterQueue", true);
    }

    /**
     * Queue 구성 : processingQueue 이름의 큐를 구성
     * - 해당 큐에서는 속성 값으로 x-dead-letter-exchange가 발생시 deadLetterExchange로 라우팅 됩니다
     * - 해당 큐에서는 속성 값으로 x-dead-letter-routing-key를 통해 Direct Queue의 라우팅 키를 전달하여 라우팅 됩니다.
     */
    @Bean
    public Queue processingQueue() {
        return QueueBuilder.durable("processingQueue")
                .withArgument("x-dead-letter-exchange", "exchange.direct.deadLetter")
                .withArgument("x-dead-letter-routing-key", "deadLetter")
                .build();
    }

    /**
     * Direct Exchange 구성 : Dead Letter Exchange로 라우팅을 하는데 사용
     * - 성공적으로 처리하지 못한 메시지를 메시지 큐(deadLetterQueue)로 전달하는 역할을 수행합니다.
     */
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange("exchange.direct.deadLetter");
    }

    /**
     * Direct Exchange 구성 : processingQueue를 라우팅 하는데 사용
     */
    @Bean
    public DirectExchange processingExchange() {
        return new DirectExchange("exchange.direct.processing");
    }

    /**
     * Direct Exchange 와 deadLetterQueue 간의 바인딩을 수행합니다.
     * - Direct Exchange 방식으로 deadLetterQueue와 라우팅 키(Routing key)를 기반으로 바인딩 수행.
     */
    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, DirectExchange deadLetterExchange) {
        return BindingBuilder
                .bind(deadLetterQueue)
                .to(deadLetterExchange)
                .with("deadLetter");
    }

    /**
     * Direct Exchange 와 processingQueue 간의 바인딩을 수행합니다.
     */
    @Bean
    public Binding processingBinding(Queue processingQueue, DirectExchange processingExchange) {
        return BindingBuilder
                .bind(processingQueue)
                .to(processingExchange)
                .with("processing");
    }

}
