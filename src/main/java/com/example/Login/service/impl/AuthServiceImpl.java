package com.example.Login.service.impl;

import com.example.Login.dto.*;
import com.example.Login.entity.PasswordResetToken;
import com.example.Login.entity.RefreshToken;
import com.example.Login.entity.User;
import com.example.Login.enums.Role;
import com.example.Login.repository.PasswordResetTokenRepository;
import com.example.Login.repository.RefreshTokenRepository;
import com.example.Login.repository.UserRepository;
import com.example.Login.security.JwtUtil;
import com.example.Login.service.AuthService;
import com.example.Login.service.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordResetTokenRepository  passwordResetTokenRepository;
    private final EmailService emailService;


    @Override
    public void register(RegisterRequest request) {

        if(userRepository.existsByUsername(request.getUsername())){
            throw new RuntimeException("Username Already Exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);

    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        ); // spring mengecek ke db

        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal(); // UserDetails dipakai Spring Security sebagai format standar untuk “user login”.

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow();

        refreshTokenRepository.deleteByUser(user);

        refreshTokenRepository.flush();

        String accessToken = jwtUtil.generateToken(userDetails);

        String refreshTokenValue = UUID.randomUUID().toString();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenValue)
                .user(user)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .build();

        refreshTokenRepository.save(refreshToken);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenValue)
                .tokenType("Bearer")
                .build();
    }

    @Override
    public void logout(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();

        refreshTokenRepository.deleteByUser(user);
    }

    @Override
    public void changePassword(ChangePasswordRequest request){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//SecurityContextHolder adalah class di Spring Security yang tugasnya:
//
//🔥 Menyimpan informasi user yang sedang login selama request berjalan.
        String username = authentication.getName();

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("user not found"));

        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())){
            throw new RuntimeException("Old Password Incorect");
        }

        if(!request.getNewPassword().equals(request.getConfirmPassword())){
            throw new RuntimeException("Password confirmation mismatch");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);

    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        PasswordResetToken resetToken =
                passwordResetTokenRepository.findByUser(user)
                        .orElse(new PasswordResetToken());

        String token = UUID.randomUUID().toString();

        resetToken.setUser(user);
        resetToken.setToken(token);
        resetToken.setExpiryDate(
                LocalDateTime.now().plusMinutes(15)
        );

        passwordResetTokenRepository.save(resetToken);

        String resetLink =
                "http://localhost:8082/api/reset-password.html?token="
                        + token;

        emailService.sendForgotPasswordEmail(
                user.getEmail(),
                resetLink
        );    }

    @Override
    @Transactional
    public void resetPasswordRequest(ResetPasswordRequest request) {

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.getToken()).orElseThrow(()->new RuntimeException("invalid token"));

        if(resetToken.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Token Expired");
        }

//        if(!request.getNewPassword().equals(request.getConfirmPassword())){
//            throw new RuntimeException("Password Confirmation mismatch");
//        }

        User user = resetToken.getUser();

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);



    }
}

// . Authentication itu apa?
//
//Authentication adalah object yang merepresentasikan:
//
//siapa usernya
//sudah login atau belum
//punya role apa


// AuthenticationManager itu apa?
//
//Ini "mesin login"-nya.
//
//Tugasnya:
//
//menerima username/password
//↓
//cek ke database
//↓
//cek password bcrypt
//↓
//kalau valid → return Authentication
//kalau salah → exception

        /*
        Kenapa pakai SecurityContextHolder?

        Karena user sudah login lewat JWT.

        Di JwtAuthenticationFilter, kamu sudah set:

        SecurityContextHolder.getContext()
        .setAuthentication(authenticationToken);

        jadi saat request masuk:

        Spring sudah tahu user login siapa
        tinggal ambil:
        authentication.getName()

        hasilnya:

        username user login
         */
