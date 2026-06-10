package com.example.Login.service;

public interface EmailService {

    void sendForgotPasswordEmail(
            String to,
            String resetLink
    );

}
