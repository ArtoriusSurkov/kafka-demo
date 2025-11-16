package org.example.kafkademo.service;

import com.icegreen.greenmail.store.FolderException;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import jakarta.mail.internet.MimeMessage;
import org.example.kafkademo.entity.Mail;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class EmailServiceTest {

    private static GreenMail greenMail;

    @Autowired
    private EmailService emailService;

    @BeforeAll
    static void setUp() {
        // SMTP сервер GreenMail на порту 3025
        greenMail = new GreenMail(new ServerSetup(3025, null, ServerSetup.PROTOCOL_SMTP));
        greenMail.start();
    }

    @BeforeEach
    void clearEmails() throws FolderException {
        greenMail.purgeEmailFromAllMailboxes();
    }

    @AfterAll
    static void tearDown() {
        greenMail.stop();
    }

    @Test
    void testSendEmail() throws Exception {
        emailService.sendEmail("Name@mail.ru", "Test Subject", "Hello!");

        Thread.sleep(500);

        MimeMessage[] messages = greenMail.getReceivedMessages();
        assertEquals(1, messages.length);

        MimeMessage message = messages[0];

        assertEquals("Test Subject", message.getSubject());
        assertEquals("Name@mail.ru", message.getAllRecipients()[0].toString());
        assertTrue(message.getContent().toString().contains("Hello!"));
    }

    @Test
    void testSendEmailV2() throws Exception {
        Mail mail = new Mail();
        mail.setTo("user@example.com");
        mail.setSubject("Order created");
        mail.setBody("Your order was successfully created!");

        emailService.sendEmailV2(mail);

        Thread.sleep(500);

        MimeMessage[] messages = greenMail.getReceivedMessages();
        assertEquals(1, messages.length);

        MimeMessage message = messages[0];

        assertEquals("Order created", message.getSubject());
        assertEquals("user@example.com", message.getAllRecipients()[0].toString());
        assertTrue(message.getContent().toString().contains("successfully created"));
    }

}
