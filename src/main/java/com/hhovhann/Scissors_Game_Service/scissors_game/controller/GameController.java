package com.hhovhann.Scissors_Game_Service.scissors_game.controller;

import com.hhovhann.Scissors_Game_Service.scissors_game.model.GameRequest;
import com.hhovhann.Scissors_Game_Service.scissors_game.model.GameResponse;
import com.hhovhann.Scissors_Game_Service.scissors_game.service.game.GameService;
import com.hhovhann.Scissors_Game_Service.scissors_game.service.generator.GeneratorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Validated
@RestController
@Tag(name = "Game endpoints")
@PreAuthorize("hasAuthority('USER')")
@RequestMapping("/v1/api/game")
public class GameController {
    public GameController(GameService gameService, GeneratorService generatorService) {
        this.gameService = gameService;
        this.generatorService = generatorService;
    }

    private final GameService gameService;
    private final GeneratorService generatorService;


    @PostMapping("/start")
    public ResponseEntity<GameResponse> makeMove(@RequestBody GameRequest gameRequest) {
        String userMove = gameRequest.userMove().toLowerCase();

        log.info("Received move request with move: {}", userMove);

        String computerMove = generatorService.generateGameMoveRandomValue();
        GameResponse result = gameService.makeMove(gameRequest.userId(), userMove, computerMove);

        log.info("Move processed. Result: {}", result);

        return ResponseEntity.ok(result);
    }

    @PatchMapping("/terminate/{game_id}")
    public ResponseEntity<String> terminateGame(@NotNull @PathVariable Long game_id) {
        log.info("Received request to terminate game with ID: {}", game_id);

        try {
            gameService.terminateGame(game_id);
            log.info("Game terminated successfully with ID: {}", game_id);

            return ResponseEntity.ok("Game terminated successfully.");
        } catch (IllegalStateException e) {
            log.error("Error terminating game with ID: {}", game_id, e);

            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/reset/{user_id}")
    public ResponseEntity<String> resetGame(@NotBlank @PathVariable("user_id") String  userId) {
        log.info("Received request to reset the game");
        gameService.resetGame(userId);
        log.info("Game reset successfully");

        return ResponseEntity.ok("Game reset successfully.");
    }

    @GetMapping("/statistics/{user_id}")
    public ResponseEntity<Map<Object, Object>> getStats(@NotBlank @PathVariable("user_id") String userId) {
        log.info("Received request for game statistics for user_id: {}", userId);

        Map<Object, Object> statistics = gameService.getStatistics(userId);
        if (statistics.isEmpty()) {
            log.warn("No statistics found for user_id: {}", userId);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(statistics);
    }
}
