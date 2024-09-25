package com.example.springrabbitmq.service;

import com.example.springrabbitmq.dto.MessageDto;

/**
 * 메시지 생성자의 Exchange 별 서비스 처리
 */
public interface ProducerService {

    void directSendMessage(MessageDto messageDto);      // Direct Exchange 방식 이용

    void fanoutSendMessage(MessageDto messageDto);      // Fanout Exchange 방식 이용

    void headerSendMessage(MessageDto messageDto);      // Header Exchange 방식 이용

    void topicSendMessage(MessageDto messageDto);       // Topic Exchange 방식 이용

    void directToDeadLetterSendMessage(MessageDto messageDto);  // Direct Exchange 방식을 이용하며 Dead Letter 테스트 하기 위해 이용

    void sendPriority5Queue(MessageDto messageDto);
    void sendPriority1Queue(MessageDto messageDto);

}