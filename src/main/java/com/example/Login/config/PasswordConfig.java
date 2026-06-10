package com.example.Login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}

//Buat apa PasswordEncoder?
//
//Dipakai untuk:
//
//        ✔ Hash password saat register
//✔ Verifikasi password saat login

// Hash adalah proses mengubah data menjadi “sidik jari digital” dengan panjang tetap menggunakan algoritma hash

// Plaintext adalah data asli yang masih bisa dibaca manusia sebelum diamankan.

// Salt adalah data acak yang ditambahkan ke password sebelum di-hash.

//BCryptPasswordEncoder adalah class bawaan Spring Security untuk:
//
//        ✔ hash password
//✔ verifikasi password
//
//menggunakan algoritma BCrypt.

//Kenapa password harus di-hash?

//Karena password tidak boleh disimpan plaintext di database.

