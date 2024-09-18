package com.hhovhann.Scissors_Game_Service.scissors_game.controller;

import com.hhovhann.Scissors_Game_Service.scissors_game.entity.User;
import com.hhovhann.Scissors_Game_Service.scissors_game.model.UserRequest;
import com.hhovhann.Scissors_Game_Service.scissors_game.model.UserResponse;
import com.hhovhann.Scissors_Game_Service.scissors_game.service.user.UserDetailsService;
import com.hhovhann.Scissors_Game_Service.scissors_game.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

class AuthenticationControllerTest {

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_success() {
        UserRequest userRequest = new UserRequest("username", "password", "email@example.com");
        User user = new User("username", "password", "email@example.com");

        when(userDetailsService.registerNewUser(anyString(), anyString(), anyString())).thenReturn(user);

        ResponseEntity<UserResponse> response = authenticationController.register(userRequest);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().username()).isEqualTo("username");
        assertThat(response.getBody().email()).isEqualTo("email@example.com");

        verify(userDetailsService).registerNewUser(anyString(), anyString(), anyString());
    }

    @Test
    void testRegister_failure() {
        UserRequest userRequest = new UserRequest("username", "password", "email@example.com");

        when(userDetailsService.registerNewUser(anyString(), anyString(), anyString()))
                .thenThrow(new IllegalStateException("User already exists"));

        ResponseEntity<UserResponse> response = authenticationController.register(userRequest);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isNull();

        verify(userDetailsService).registerNewUser(anyString(), anyString(), anyString());
    }

    @Test
    void testAuthenticate_success() {
        UserRequest userRequest = new UserRequest("username", "password", "email@example.com");
        Authentication authentication = mock(Authentication.class);
        String jwtToken = "jwtToken";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtil.generateToken(anyString())).thenReturn(jwtToken);

        ResponseEntity<String> response = authenticationController.authenticate(userRequest);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(jwtToken);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil).generateToken(anyString());
    }

    @Test
    void testAuthenticate_failure() {
        UserRequest userRequest = new UserRequest("username", "password", "email@example.com");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Invalid credentials") {});

        ResponseEntity<String> response = authenticationController.authenticate(userRequest);

        assertThat(response.getStatusCodeValue()).isEqualTo(UNAUTHORIZED.value());
        assertThat(response.getBody()).isEqualTo("Invalid credentials");

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil, never()).generateToken(anyString());
    }
}
