package com.example.springrabbitmq.controller;

import com.example.springrabbitmq.dto.MessageDto;
import com.example.springrabbitmq.service.ProducerService;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/producer")
public class ProducerController {

    @Autowired
    private ProducerService producerService;


    /**
     * 생산자(Proceduer)가 메시지를 전송합니다.
     *
     * @param messageDto
     * @return
     */
    @PostMapping("/directMessage")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDto messageDto) {
        String result = "";

        producerService.directSendMessage(messageDto);
        ApiResponse ar = ApiResponse.builder()
                .result(result)
                .resultCode(SuccessCode.SELECT.getStatus())
                .resultMsg(SuccessCode.SELECT.getMessage())
                .build();
        return new ResponseEntity<>(ar, HttpStatus.OK);
    }

    @PostMapping("/fanoutMessage")
    public ResponseEntity<?> fanoutMessage(@RequestBody MessageDto messageDto) {
        String result = "";

        producerService.fanoutSendMessage(messageDto);
        ApiResponse ar = ApiResponse.builder()
                .result(result)
                .resultCode(SuccessCode.SELECT.getStatus())
                .resultMsg(SuccessCode.SELECT.getMessage())
                .build();
        return new ResponseEntity<>(ar, HttpStatus.OK);
    }

    @PostMapping("/headerMessage")
    public ResponseEntity<?> headerMessage(@RequestBody MessageDto messageDto) {
        String result = "";

        producerService.headerSendMessage(messageDto);
        ApiResponse ar = ApiResponse.builder()
                .result(result)
                .resultCode(SuccessCode.SELECT.getStatus())
                .resultMsg(SuccessCode.SELECT.getMessage())
                .build();
        return new ResponseEntity<>(ar, HttpStatus.OK);
    }


    @PostMapping("/topicMessage")
    public ResponseEntity<?> topicMessage(@RequestBody MessageDto messageDto) {
        String result = "";

        producerService.topicSendMessage(messageDto);
        ApiResponse ar = ApiResponse.builder()
                .result(result)
                .resultCode(SuccessCode.SELECT.getStatus())
                .resultMsg(SuccessCode.SELECT.getMessage())
                .build();
        return new ResponseEntity<>(ar, HttpStatus.OK);
    }

    @PostMapping("/deadLetterMessage")
    public ResponseEntity<?> deadLetterMessage(@RequestBody MessageDto messageDto) {
        String result = "";

        producerService.directToDeadLetterSendMessage(messageDto);
        ApiResponse ar = ApiResponse.builder()
                .result(result)
                .resultCode(SuccessCode.SELECT.getStatus())
                .resultMsg(SuccessCode.SELECT.getMessage())
                .build();
        return new ResponseEntity<>(ar, HttpStatus.OK);
    }

    @PostMapping("/queue/priority1")
    public ResponseEntity<?> sendPriority1Message(@RequestBody MessageDto messageDto) {
        String result = "";

        producerService.sendPriority1Queue(messageDto);
        ApiResponse ar = ApiResponse.builder()
                .result(result)
                .resultCode(SuccessCode.SELECT.getStatus())
                .resultMsg(SuccessCode.SELECT.getMessage())
                .build();
        return new ResponseEntity<>(ar, HttpStatus.OK);
    }

    @PostMapping("/queue/priority5")
    public ResponseEntity<?> sendPriority5Message(@RequestBody MessageDto messageDto) {
        String result = "";

        producerService.sendPriority5Queue(messageDto);
        ApiResponse ar = ApiResponse.builder()
                .result(result)
                .resultCode(SuccessCode.SELECT.getStatus())
                .resultMsg(SuccessCode.SELECT.getMessage())
                .build();
        return new ResponseEntity<>(ar, HttpStatus.OK);
    }
}


@Getter
@Setter
@Builder
class ApiResponse {
    private String result;
    private int resultCode;
    private String resultMsg;
}

@Getter
@RequiredArgsConstructor
enum SuccessCode {
    SELECT(200, "Success"),
    CREATED(201, "Created"),
    UPDATED(200, "Updated"),
    DELETED(200, "Deleted");

    private final int status;
    private final String message;
}
