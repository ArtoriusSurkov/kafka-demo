package org.example.kafkademo.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.kafkademo.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class KafkaConsumerTest {

    private EmailService emailService;
    private KafkaConsumer kafkaConsumer;

    @BeforeEach
    void setUp() {
        emailService = mock(EmailService.class);
        kafkaConsumer = new KafkaConsumer(emailService);
    }

    @Test
    void testListenCreate() {
        ConsumerRecord<String, String> record = new ConsumerRecord<>("kafka-test", 0, 0L, "create", "user@example.com");

        kafkaConsumer.listen(record);

        ArgumentCaptor<String> toCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);

        verify(emailService, times(1))
                .sendEmail(toCaptor.capture(), subjectCaptor.capture(), bodyCaptor.capture());

        assertEquals("user@example.com", toCaptor.getValue());
        assertEquals("Создание аккаунта.", subjectCaptor.getValue());
        assertEquals("Ваш аккаунт был успешно создан!", bodyCaptor.getValue());
    }

    @Test
    void testListenDelete() {
        ConsumerRecord<String, String> record = new ConsumerRecord<>("kafka-test", 0, 0L, "delete", "user@example.com");

        kafkaConsumer.listen(record);

        ArgumentCaptor<String> toCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);

        verify(emailService, times(1))
                .sendEmail(toCaptor.capture(), subjectCaptor.capture(), bodyCaptor.capture());

        assertEquals("user@example.com", toCaptor.getValue());
        assertEquals("Удаление аккаунта.", subjectCaptor.getValue());
        assertEquals("Ваш аккаунт был успешно удален!", bodyCaptor.getValue());
    }

    @Test
    void testListenOtherKey() {
        ConsumerRecord<String, String> record = new ConsumerRecord<>("kafka-test", 0, 0L, "update", "user@example.com");

        kafkaConsumer.listen(record);

        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
    }
}