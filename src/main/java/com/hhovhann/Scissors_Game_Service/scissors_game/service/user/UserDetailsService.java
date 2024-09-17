package com.hhovhann.Scissors_Game_Service.scissors_game.service.user;

import com.hhovhann.Scissors_Game_Service.scissors_game.entity.User;
import com.hhovhann.Scissors_Game_Service.scissors_game.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final String USER_AUTHORITIES = "USER";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Attempting to load user by username: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        log.info("Successfully loaded user: {}", username);
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(USER_AUTHORITIES)
                .build();
    }

    public User registerNewUser(String username, String password, String email) {
        log.info("Attempting to register new user with username: {}", username);
        if (userRepository.findByUsername(username).isPresent()) {
            log.error("Username already taken: {}", username);
            throw new IllegalStateException("Username already taken");
        }
        String encodedPassword = passwordEncoder.encode(password);
        User newUser = new User(username, encodedPassword, email);
        User savedUser = userRepository.save(newUser);
        log.info("Successfully registered new user: {}", username);
        return savedUser;
    }
}
