package com.hhovhann.Scissors_Game_Service.scissors_game.service.user;

import com.hhovhann.Scissors_Game_Service.scissors_game.entity.User;
import com.hhovhann.Scissors_Game_Service.scissors_game.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_UserExists_ReturnsUserDetails() {
        // Given
        String username = "user1";
        String password = "password";
        User user = new User(username, password, "email@example.com");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Then
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("USER")));
    }

    @Test
    void loadUserByUsername_UserDoesNotExist_ThrowsUsernameNotFoundException() {
        // Given
        String username = "user1";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));
    }

    @Test
    void registerNewUser_UsernameNotTaken_SavesAndReturnsUser() {
        // Given
        String username = "user1";
        String password = "password";
        String email = "email@example.com";
        String encodedPassword = "encodedPassword";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        User user = new User(username, encodedPassword, email);

        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        User registeredUser = userDetailsService.registerNewUser(username, password, email);

        // Then
        assertNotNull(registeredUser);
        assertEquals(username, registeredUser.getUsername());
        assertEquals(encodedPassword, registeredUser.getPassword());
        assertEquals(email, registeredUser.getEmail());
    }

    @Test
    void registerNewUser_UsernameTaken_ThrowsIllegalStateException() {
        // Given
        String username = "user1";
        String password = "password";
        String email = "email@example.com";

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User(username, "encodedPassword", email)));

        // When & Then
        assertThrows(IllegalStateException.class, () -> userDetailsService.registerNewUser(username, password, email));
    }
}
