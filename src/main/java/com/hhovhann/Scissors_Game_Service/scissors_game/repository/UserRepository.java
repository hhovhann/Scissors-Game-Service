package com.hhovhann.Scissors_Game_Service.scissors_game.repository;

import com.hhovhann.Scissors_Game_Service.scissors_game.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
}
