package com.learn.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class ResponseDto{
    private String statusCode;
    private String statusMsg;


}