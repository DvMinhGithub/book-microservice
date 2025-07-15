

package com.mdv.notice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mdv.notice.dto.request.EmailRequest;
import com.mdv.notice.dto.request.SendEmailRequest;
import com.mdv.notice.dto.request.Sender;
import com.mdv.notice.dto.response.EmailResponse;
import com.mdv.notice.exception.ApiErrorCode;
import com.mdv.notice.exception.ApiException;
import com.mdv.notice.repository.EmailClient;

import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailService {
    final EmailClient emailClient;

    @Value("${email.service.api-key}")
    String apiKey;

    @Value("${email.service.sender.email}")
    String senderEmail;

    @Value("${email.service.sender.name}")
    String senderName;

    public EmailResponse sendEmail(SendEmailRequest request) {
        
        EmailRequest emailRequest = EmailRequest.builder()
            .sender(Sender.builder()
                .email(senderEmail)
                .name(senderName)
                .build())
            .to(List.of(request.getTo()))
            .subject(request.getSubject())
            .htmlContent(request.getHtmlContent())
            .build();
            
        try {
            return emailClient.sendEmail(apiKey, emailRequest);
        } catch (FeignException e) {
            throw new ApiException(ApiErrorCode.EMAIL_SEND_FAILED, "Failed to send email", e);
        }
    }
}