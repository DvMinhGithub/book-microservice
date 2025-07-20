package com.mdv.notice.controller;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.mdv.event.dto.NotificationEvent;
import com.mdv.notice.dto.request.Recipient;
import com.mdv.notice.dto.request.SendEmailRequest;
import com.mdv.notice.service.EmailService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal =  true)
@Slf4j
public class NotificationController {

    EmailService emailService;
    
    @KafkaListener(topics = "notification-delivery")
    public void listen(NotificationEvent message) {
        log.info("Received message: {}", message.getRecipient());
        SendEmailRequest request = SendEmailRequest.builder()
            .to(Recipient.builder().email(message.getRecipient()).build())
            .subject(message.getSubject())
            .htmlContent(message.getBody())
            .build();

        emailService.sendEmail(request);
    }
}
