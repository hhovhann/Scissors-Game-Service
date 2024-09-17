package com.hhovhann.Scissors_Game_Service.scissors_game.controller;

import com.hhovhann.Scissors_Game_Service.scissors_game.entity.User;
import com.hhovhann.Scissors_Game_Service.scissors_game.model.UserRequest;
import com.hhovhann.Scissors_Game_Service.scissors_game.model.UserResponse;
import com.hhovhann.Scissors_Game_Service.scissors_game.service.user.UserDetailsService;
import com.hhovhann.Scissors_Game_Service.scissors_game.util.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            User user = userDetailsService.registerNewUser(userRequest.username(), userRequest.password(), userRequest.email());
            return ResponseEntity.ok(new UserResponse(user.getUserId(), user.getUsername(), user.getEmail()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody UserRequest userRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userRequest.username(), userRequest.password()));
            String jwtToken = jwtUtil.generateToken(userRequest.username());
            return ResponseEntity.ok(jwtToken);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
