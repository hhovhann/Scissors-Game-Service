package com.hhovhann.Scissors_Game_Service.scissors_game.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhovhann.Scissors_Game_Service.scissors_game.model.RequestModel;
import com.hhovhann.Scissors_Game_Service.scissors_game.service.game.GameService;
import com.hhovhann.Scissors_Game_Service.scissors_game.service.generator.GeneratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Map;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
class GameControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;  // Jackson ObjectMapper for converting objects to JSON

    @MockBean
    private GameService gameService;

    @MockBean
    private GeneratorService generatorService;

    @InjectMocks
    private GameController gameController;

    @BeforeEach
    void setUp() {
        // No need for explicit setup when using @WebMvcTest and @MockBean,
        // services will be injected automatically.
    }

    @Test
    void testMakeMove() throws Exception {
        // Given
        String userMove = "rock";
        String computerMove = "scissors";
        String result = "You win!";

        // Creating RequestModel object
        RequestModel requestModel = new RequestModel(userMove);

        // Mocking service method calls
        when(generatorService.generateGameMoveRandomValue()).thenReturn(computerMove);
        when(gameService.makeMove(userMove, computerMove)).thenReturn(result);

        // Serialize the request model to JSON
        String requestBody = objectMapper.writeValueAsString(requestModel);

        // When & Then
        mockMvc.perform(post("/v1/api/game/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))  // Pass serialized JSON
                .andExpect(status().isOk())
                .andExpect(content().string("You chose: rock, Computer chose: scissors. Result: You win!"));
    }

    @Test
    void testTerminateGame() throws Exception {
        // Given
        Long gameId = 1L;

        doNothing().when(gameService).terminateGame(gameId);

        // When & Then
        mockMvc.perform(patch("/v1/api/game/terminate/{id}", gameId))
                .andExpect(status().isOk())
                .andExpect(content().string("Game terminated successfully."));
    }

    @Test
    void testTerminateGame_NotFound() throws Exception {
        // Given
        Long gameId = 1L;

        doThrow(new IllegalStateException("Game not found")).when(gameService).terminateGame(gameId);

        // When & Then
        mockMvc.perform(patch("/v1/api/game/terminate/{id}", gameId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Game not found"));
    }

    @Test
    void testResetGame() throws Exception {
        // Given
        doNothing().when(gameService).resetGame();

        // When & Then
        mockMvc.perform(delete("/v1/api/game/reset"))
                .andExpect(status().isOk())
                .andExpect(content().string("Game reset successfully."));
    }

    @Test
    void testGetStatistics() throws Exception {
        // Given
        Map<Object, Object> statistics = Collections.singletonMap("totalGames", 10);

        when(gameService.getStatistics()).thenReturn(statistics);

        // When & Then
        mockMvc.perform(get("/v1/api/game/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalGames").value(10));
    }
}
