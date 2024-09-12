package com.hhovhann.Scissors_Game_Service.scissors_game.repository;

import com.hhovhann.Scissors_Game_Service.scissors_game.entity.Game;
import com.hhovhann.Scissors_Game_Service.scissors_game.enums.GameResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByUserMove(String userMove);

    Optional<Game> findByIdAndStatus(Long id, String status); // New method to find active games

    Integer countByResult(GameResult gameMove);
}
