package com.hhovhann.Scissors_Game_Service.scissors_game.repository;

import com.hhovhann.Scissors_Game_Service.scissors_game.entity.Game;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {

    Optional<Game> findByIdAndStatus(Long id, String status); // New method to find active games

    long countByResultAndUserId(String result, String userId);

    @Modifying
    @Query("DELETE FROM Game g WHERE g.userId = :userId")
    void deleteAllByUserId(@Param("userId") String userId);
}
