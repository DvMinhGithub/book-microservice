package com.mdv.notice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mdv.notice.dto.request.SendEmailRequest;
import com.mdv.notice.dto.response.ApiResponse;
import com.mdv.notice.dto.response.EmailResponse;
import com.mdv.notice.service.EmailService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailController {
    EmailService emailService;

    @PostMapping("/email/send")
    public ApiResponse<EmailResponse> sendEmail(@RequestBody SendEmailRequest request) {
        return ApiResponse.<EmailResponse>builder()
            .result(emailService.sendEmail(request))
            .message("Email sent successfully")
            .code(HttpStatus.OK.value())
            .build();
    }

    @KafkaListener(topics = "user-created")
    public void listen(String message) {
        System.out.println("Received message: " + message);
    }
}