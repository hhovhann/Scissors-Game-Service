package com.hhovhann.Scissors_Game_Service.scissors_game.service.user;

import com.hhovhann.Scissors_Game_Service.scissors_game.entity.User;
import com.hhovhann.Scissors_Game_Service.scissors_game.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

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

    @Test
    void findById_UserExists_ReturnsUser() {
        // Given
        String userId = "user123";
        User user = new User("username", "password", "email@example.com");
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));

        // When
        User foundUser = userDetailsService.findById(userId);

        // Then
        assertNotNull(foundUser);
        assertEquals("username", foundUser.getUsername());
        assertEquals("email@example.com", foundUser.getEmail());
    }

    @Test
    void findById_UserDoesNotExist_ThrowsEntityNotFoundException() {
        // Given
        String userId = "user123";
        when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> userDetailsService.findById(userId));
        assertEquals("User with id user123 isn't not found", exception.getMessage());
    }
}
