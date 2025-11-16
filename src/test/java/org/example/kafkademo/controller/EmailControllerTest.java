package org.example.kafkademo.controller;



import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.kafkademo.entity.Mail;
import org.example.kafkademo.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class EmailControllerTest {
    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmailController emailController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(emailController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void sendEmail_validMail_shouldReturnOk() throws Exception {
        Mail mail = new Mail();
        mail.setTo("user@example.com");
        mail.setSubject("Hello");
        mail.setBody("Test message");

        mockMvc.perform(post("/sendler")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mail)))
                .andExpect(status().isOk());

        verify(emailService, times(1)).sendEmailV2(mail);
    }

    @Test
    void sendEmail_missingTo_shouldReturn400() throws Exception {
        Mail mail = new Mail();
        mail.setSubject("Hello");
        mail.setBody("Test message");

        mockMvc.perform(post("/sendler")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mail)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sendEmail_emptyBody_shouldReturn400() throws Exception {
        Mail mail = new Mail();
        mail.setTo("user@example.com");
        mail.setSubject("Hello");
        mail.setBody("");

        mockMvc.perform(post("/sendler")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mail)))
                .andExpect(status().isBadRequest());

        verify(emailService, times(0)).sendEmailV2(mail);
    }
}
