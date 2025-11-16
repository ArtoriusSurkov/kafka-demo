package org.example.kafkademo.controller;

import org.example.kafkademo.entity.Mail;
import org.example.kafkademo.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/sendler")
    public ResponseEntity<String> sendEmail(@RequestBody Mail mail){
        if (mail == null) {
            return ResponseEntity.badRequest().body("Mail object is null");
        }
        if (mail.getTo() == null || mail.getTo().isBlank()) {
            return ResponseEntity.badRequest().body("Recipient (to) is required");
        }
        if (mail.getSubject() == null || mail.getSubject().isBlank()) {
            return ResponseEntity.badRequest().body("Subject is required");
        }
        if (mail.getBody() == null || mail.getBody().isBlank()) {
            return ResponseEntity.badRequest().body("Body is required");
        }

        System.out.println(1);
        emailService.sendEmailV2(mail);

        return ResponseEntity.status(HttpStatus.OK).body("Email sent successfully");
    }
}
