package com.hhovhann.Scissors_Game_Service.scissors_game.service.user;

import com.hhovhann.Scissors_Game_Service.scissors_game.entity.User;
import com.hhovhann.Scissors_Game_Service.scissors_game.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(USER_AUTHORITIES)
                .build();
    }

    public User registerNewUser(String username, String password, String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalStateException("Username already taken");
        }
        String encodedPassword = passwordEncoder.encode(password);
        return userRepository.save(new User(username, encodedPassword, email));
    }
}
