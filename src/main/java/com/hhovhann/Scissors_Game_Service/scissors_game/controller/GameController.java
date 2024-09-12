package com.hhovhann.Scissors_Game_Service.scissors_game.controller;

import com.hhovhann.Scissors_Game_Service.scissors_game.model.RequestModel;
import com.hhovhann.Scissors_Game_Service.scissors_game.service.game.GameService;
import com.hhovhann.Scissors_Game_Service.scissors_game.service.generator.GeneratorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
@Tag(name = "Order endpoints")
@RequestMapping("/v1/api/game")
public class GameController {
    public GameController(GameService gameService, GeneratorService generatorService) {
        this.gameService = gameService;
        this.generatorService = generatorService;
    }

    private final GameService gameService;
    private final GeneratorService generatorService;


    @PostMapping("/start")
    public ResponseEntity<String> makeMove(@RequestBody RequestModel requestModel) {
        String userMove = requestModel.userMove().toLowerCase();

        log.info("Received move request with move: {}", userMove);

        String computerMove = generatorService.generateGameMoveRandomValue();
        String result = gameService.makeMove(userMove, computerMove);

        log.info("Move processed. Result: {}", result);

        return ResponseEntity.ok("You chose: " + userMove + ", Computer chose: " + computerMove + ". Result: " + result);
    }

    @PatchMapping("/terminate/{id}")
    public ResponseEntity<String> terminateGame(@NotNull @PathVariable Long id) {
        log.info("Received request to terminate game with ID: {}", id);

        try {
            gameService.terminateGame(id);
            log.info("Game terminated successfully with ID: {}", id);

            return ResponseEntity.ok("Game terminated successfully.");
        } catch (IllegalStateException e) {
            log.error("Error terminating game with ID: {}", id, e);

            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/reset")
    public ResponseEntity<String> resetGame() {
        log.info("Received request to reset the game");
        gameService.resetGame();
        log.info("Game reset successfully");

        return ResponseEntity.ok("Game reset successfully.");
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<Object, Object>> getStats() {
        log.info("Received request for game statistics");

        return ResponseEntity.ok(gameService.getStats());
    }
}
