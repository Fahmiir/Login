package com.example.Login.service;

import com.example.Login.dto.*;

public interface AuthService {

    void register(RegisterRequest request);

    AuthResponse login (LoginRequest request);

    void logout(String username);

    void changePassword(ChangePasswordRequest request);

    void forgotPassword(ForgotPasswordRequest request);

    void resetPasswordRequest(ResetPasswordRequest request);

}
