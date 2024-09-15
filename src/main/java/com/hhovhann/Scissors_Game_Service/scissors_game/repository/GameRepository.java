package com.hhovhann.Scissors_Game_Service.scissors_game.repository;

import com.hhovhann.Scissors_Game_Service.scissors_game.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {

    Optional<Game> findByIdAndStatus(Long id, String status); // New method to find active games

    Integer countByResult(String move);
}
