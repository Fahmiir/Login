package com.example.Login.service.impl;

import com.example.Login.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {


    private final JavaMailSender mailSender;


    @Override
    public void sendForgotPasswordEmail(String to, String resetLink) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Reset Password");
        message.setText(                "Klik link berikut untuk reset password:\n\n"
                + resetLink);
        mailSender.send(message);
    }
}
