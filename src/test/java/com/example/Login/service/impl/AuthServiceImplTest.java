package com.example.Login.service.impl;

import com.example.Login.dto.AuthResponse;
import com.example.Login.dto.LoginRequest;
import com.example.Login.dto.RegisterRequest;
import com.example.Login.entity.User;
import com.example.Login.repository.RefreshTokenRepository;
import com.example.Login.repository.UserRepository;
import com.example.Login.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.Login.enums.Role;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    void register_Should_save_user() {

        RegisterRequest registerRequest = new RegisterRequest();

        registerRequest.setUsername("fahmi");
        registerRequest.setPassword("password123");

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        authService.register(registerRequest);

        verify(userRepository).save(any(User.class));

    }

    @Test
    void login_should_return_token_when_credentials_Valid(){

        LoginRequest request = new LoginRequest();
        request.setUsername("fahmi");
        request.setPassword("password123");

        User user = User.builder()
                .username("fahmi")
                .password("encodedPassword")
                .role(Role.ROLE_USER)
                .build();

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("fahmi")
                .password("encodedPassword")
                .authorities("ROLE_USER")
                .build();

        Authentication authentication =
                mock(Authentication.class);

        when(authenticationManager.authenticate(any(
                UsernamePasswordAuthenticationToken.class
        ))).thenReturn(authentication);

        when(authentication.getPrincipal())
                .thenReturn(userDetails);

        when(userRepository.findByUsername("fahmi"))
                .thenReturn(Optional.of(user));

        when(jwtUtil.generateToken(userDetails))
                .thenReturn("jwt-token");

        AuthResponse response =
                authService.login(request);

        assertNotNull(response);

        assertEquals(
                "jwt-token",
                response.getAccessToken()
        );

        verify(authenticationManager)
                .authenticate(any(
                        UsernamePasswordAuthenticationToken.class
                ));

        verify(userRepository)
                .findByUsername("fahmi");

        verify(jwtUtil)
                .generateToken(userDetails);


    }

}
