package com.mdv.notice.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.mdv.notice.dto.request.EmailRequest;
import com.mdv.notice.dto.response.EmailResponse;

@FeignClient(name = "email-client", url = "${email.service.url}")
public interface EmailClient {

    @PostMapping(headers = "${email.service.api-key}", value = "/v3/smtp/email", produces  = MediaType.APPLICATION_JSON_VALUE)
    EmailResponse sendEmail(@RequestHeader("api-key") String apiKey, @RequestBody EmailRequest emailRequest);
}
