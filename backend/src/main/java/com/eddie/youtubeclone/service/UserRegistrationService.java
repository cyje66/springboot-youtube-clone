package com.eddie.youtubeclone.service;

import com.eddie.youtubeclone.dto.UserInfoDTO;
import com.eddie.youtubeclone.model.User;
import com.eddie.youtubeclone.repository.UserRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    @Value("${auth0.userinfoEndpoint}")
    private String userInfoEndpoint;

    private final UserRepository userRepository;

    public String registerUser(String tokenValue) {
        // Make a call to the userInfo Endpoint
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(userInfoEndpoint))
                .setHeader("Authorization", String.format("Bearer %s", tokenValue))
                .build();
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String body = response.body();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            UserInfoDTO userInfoDTO = objectMapper.readValue(body, UserInfoDTO.class);

            Optional<User> userBySubject = userRepository.findBySub(userInfoDTO.getSub());

            if (userBySubject.isPresent()) {
                return userBySubject.get().getId();
            } else {
                User user = new User();
                user.setFirstname(userInfoDTO.getGivenName());
                user.setLastName(userInfoDTO.getFamilyName());
                user.setFullName(userInfoDTO.getName());
                user.setEmailAddress(userInfoDTO.getEmail());
                user.setSub(userInfoDTO.getSub());

                return userRepository.save(user).getId();
            }
        } catch (IOException e) {
            throw new RuntimeException("Exception occurred while registering user", e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Fetch user details and save them to the database
    }
}
