package org.example.kafkademo.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.kafkademo.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumer {

    private final EmailService emailService;

    public KafkaConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "kafka-test", groupId = "my-consumer")
    public void listen(ConsumerRecord<String, String> consumerRecord) {
        log.info("Mode: {}, Email: {}",consumerRecord.key(),consumerRecord.value());
        if (consumerRecord.key().equals("create")){
            emailService.sendEmail(consumerRecord.value(),"Создание аккаунта.","Ваш аккаунт был успешно создан!");
        }
        else if (consumerRecord.key().equals("delete")) {
            emailService.sendEmail(consumerRecord.value(),"Удаление аккаунта.","Ваш аккаунт был успешно удален!");
        }
    }
}
