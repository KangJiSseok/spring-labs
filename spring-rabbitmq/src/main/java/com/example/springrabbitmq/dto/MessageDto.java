package com.example.springrabbitmq.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private String title;
    private String message;



}
