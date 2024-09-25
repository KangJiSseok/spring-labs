package com.example.springrabbitmq.service;

import com.example.springrabbitmq.dto.MessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProducerServiceImpl implements ProducerService{

    private final RabbitTemplate rabbitTemplate;


    @Override
    @Transactional(readOnly = true)
    public void directSendMessage(MessageDto messageDto) {
        try {
            // 1. 전송하려는 객체를 문자열로 변환합니다.
            ObjectMapper objectMapper = new ObjectMapper();
            String objectToJSON = objectMapper.writeValueAsString(messageDto);

            // 2. Direct Exchange를 이용하여 라우팅 키(order.pizza)를 기반으로 queue1로 데이터를 전송합니다.
            rabbitTemplate.convertAndSend("exchange.direct", "order.pizza", objectToJSON);
        } catch (JsonProcessingException jpe) {
            System.out.println("파싱 오류 발생");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void fanoutSendMessage(MessageDto messageDto) {
        try {
            // 1. 전송하려는 객체를 문자열로 변환합니다.
            ObjectMapper objectMapper = new ObjectMapper();
            String objectToJSON = objectMapper.writeValueAsString(messageDto);

            // 2. Fanout Exchange를 이용하여 queue2, queue3로 데이터를 전송합니다.
            rabbitTemplate.convertAndSend("exchange.fanout", "", objectToJSON);
        } catch (JsonProcessingException jpe) {
            System.out.println("파싱 오류 발생");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void headerSendMessage(MessageDto messageDto) {
        try {
            // 1. 전송하려는 객체를 문자열로 변환합니다.
            ObjectMapper objectMapper = new ObjectMapper();
            String objectToJSON = objectMapper.writeValueAsString(messageDto);

            // 2. Headers Exchange를 이용하여 queue4로 데이터를 전송합니다.
            rabbitTemplate.convertAndSend("exchange.headers", "", objectToJSON);
        } catch (JsonProcessingException jpe) {
            System.out.println("파싱 오류 발생");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void topicSendMessage(MessageDto messageDto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String objectToJSON = objectMapper.writeValueAsString(messageDto);
            rabbitTemplate.convertAndSend("exchange.topic", "order.*", objectToJSON);
        } catch (JsonProcessingException jpe) {
            System.out.println("파싱 오류 발생");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void directToDeadLetterSendMessage(MessageDto messageDto) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // [STEP1] DTO -> String 직렬화 수행
            String objectToJSON = objectMapper.writeValueAsString(messageDto);

            // [STEP2] 메시지 속성 지정
            MessageProperties messageProperties = new MessageProperties();

            // [STEP3] 메시지의 Expiration 지정(TTL : 1초)
            messageProperties.setExpiration("1000");

            // [STEP4] 메시지 객체로 구성
            Message message = new Message(objectToJSON.getBytes(), messageProperties);

            // [STEP5] 라우터를 기반으로 큐에 메시지 전송
            rabbitTemplate.convertAndSend("exchange.direct.processing", "processing", message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 우선순위가 5인 메시지 전송
     */
    @Override
    @Transactional(readOnly = true)
    public void sendPriority5Queue(MessageDto messageDto) {

        // [STEP1] MessageProperties 인스턴스 구성
        MessageProperties mpb = MessagePropertiesBuilder.newInstance()
                .setPriority(5)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // [STEP2] 메시지 객체 직렬화 수행
            String objectToJSON = objectMapper.writeValueAsString(messageDto);

            // [STEP3] 직렬화 객체와 메시지 정보로 메시지 객체 구성
            Message message = new Message(objectToJSON.getBytes(), mpb);

            // [STEP4] Direct Exchange를 이용하여 Routing Key와 함께 메시지 전달
            rabbitTemplate.convertAndSend("exchange.direct.priorityQueue", "classicPriorityQueue", message);
        } catch (JsonProcessingException jpe) {
            System.out.println("파싱 오류 발생");
        }

    }

    /**
     * 우선순위가 1인 메시지 전송
     */
    @Override
    @Transactional(readOnly = true)
    public void sendPriority1Queue(MessageDto messageDto) {
        // [STEP1] MessageProperties 인스턴스 구성
        MessageProperties mpb = MessagePropertiesBuilder.newInstance()
                .setPriority(1)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // [STEP2] 메시지 객체 직렬화 수행
            String objectToJSON = objectMapper.writeValueAsString(messageDto);

            // [STEP3] 직렬화 객체와 메시지 정보로 메시지 객체 구성
            Message message = new Message(objectToJSON.getBytes(), mpb);

            // [STEP4] Direct Exchange를 이용하여 Routing Key와 함께 메시지 전달
            rabbitTemplate.convertAndSend("exchange.direct.priorityQueue", "classicPriorityQueue", message);
        } catch (JsonProcessingException jpe) {
            System.out.println("파싱 오류 발생");
        }
    }



}
