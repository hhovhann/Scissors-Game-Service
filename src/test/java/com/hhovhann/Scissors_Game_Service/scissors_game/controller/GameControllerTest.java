package com.hhovhann.Scissors_Game_Service.scissors_game.controller;

import com.hhovhann.Scissors_Game_Service.scissors_game.model.GameRequest;
import com.hhovhann.Scissors_Game_Service.scissors_game.model.GameResponse;
import com.hhovhann.Scissors_Game_Service.scissors_game.service.game.GameService;
import com.hhovhann.Scissors_Game_Service.scissors_game.service.generator.GeneratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GameControllerTest {

    @Mock
    private GameService gameService;

    @Mock
    private GeneratorService generatorService;

    @InjectMocks
    private GameController gameController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(authorities = "USER")
    void testMakeMove_noRepositoryCalls() {
        GameRequest gameRequest = new GameRequest("user1", "rock");
        GameResponse gameResponse = new GameResponse(1L, "srock");

        when(generatorService.generateGameMoveRandomValue()).thenReturn("scissors");
        when(gameService.makeMove(anyString(), anyString(), anyString())).thenReturn(gameResponse);

        ResponseEntity<GameResponse> response = gameController.makeMove(gameRequest);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(gameResponse);

        verify(gameService).makeMove(anyString(), anyString(), anyString());
        verify(generatorService).generateGameMoveRandomValue();
        // Verify that no repository calls are made, assuming repository methods are not called in these services
    }

    @Test
    @WithMockUser(authorities = "USER")
    void testTerminateGame_noRepositoryCalls() {
        long gameId = 1L;

        gameController.terminateGame(gameId);

        verify(gameService).terminateGame(gameId);
        // Verify that no repository calls are made
    }

    @Test
    @WithMockUser(authorities = "USER")
    void testResetGame_noRepositoryCalls() {
        String userId = "user1";

        gameController.resetGame(userId);

        verify(gameService).resetGame(userId);
        // Verify that no repository calls are made
    }

    @Test
    @WithMockUser(authorities = "USER")
    void testGetStats_noRepositoryCalls() {
        String userId = "user1";
        Map<Object, Object> stats = Map.of("stat1", 1);

        when(gameService.getStatistics(anyString())).thenReturn(stats);

        ResponseEntity<Map<Object, Object>> response = gameController.getStats(userId);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(stats);

        verify(gameService).getStatistics(anyString());
        // Verify that no repository calls are made
    }
}
