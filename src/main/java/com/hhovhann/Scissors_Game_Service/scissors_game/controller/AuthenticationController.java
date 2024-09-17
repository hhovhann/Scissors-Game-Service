package com.hhovhann.Scissors_Game_Service.scissors_game.controller;

import com.hhovhann.Scissors_Game_Service.scissors_game.entity.User;
import com.hhovhann.Scissors_Game_Service.scissors_game.model.UserRequest;
import com.hhovhann.Scissors_Game_Service.scissors_game.model.UserResponse;
import com.hhovhann.Scissors_Game_Service.scissors_game.service.user.UserDetailsService;
import com.hhovhann.Scissors_Game_Service.scissors_game.util.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@RestController
@Tag(name = "User Authentication endpoints")
@RequestMapping("/v1/api/user")
public class AuthenticationController {

    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthenticationController(UserDetailsService userDetailsService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRequest userRequest) {
        try {
            log.info("Registering new user: {}", userRequest.username());
            User user = userDetailsService.registerNewUser(userRequest.username(), userRequest.password(), userRequest.email());
            UserResponse response = new UserResponse(user.getUserId(), user.getUsername(), user.getEmail());
            log.info("User registered successfully: {}", userRequest.username());
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            log.error("Error registering user: %s", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody UserRequest userRequest) {
        try {
            log.info("Attempting to authenticate user: {}", userRequest.username());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userRequest.username(), userRequest.password()));
            String jwtToken = jwtUtil.generateToken(userRequest.username());
            log.info("User authenticated successfully: {}", userRequest.username());
            return ResponseEntity.ok(jwtToken);
        } catch (AuthenticationException e) {
            log.warn("Authentication failed for user: {}", userRequest.username());
            return ResponseEntity.status(UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
