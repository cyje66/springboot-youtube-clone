package com.eddie.youtubeclone.controller;

import com.eddie.youtubeclone.dto.VideoDto;
import com.eddie.youtubeclone.service.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRegistrationService userRegistrationService;

    @GetMapping("/register")
    public String register(Authentication authentication) {
        Jwt jwt = (Jwt)authentication.getPrincipal();

        userRegistrationService.registerUser(jwt.getTokenValue());
        return "User Registration Successful";
    }
}
