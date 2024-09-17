package com.hhovhann.Scissors_Game_Service.scissors_game.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhovhann.Scissors_Game_Service.ScissorsGameServiceApplication;
import com.hhovhann.Scissors_Game_Service.scissors_game.model.GameRequest;
import com.hhovhann.Scissors_Game_Service.scissors_game.model.GameResponse;
import com.hhovhann.Scissors_Game_Service.scissors_game.service.game.GameService;
import com.hhovhann.Scissors_Game_Service.scissors_game.service.generator.GeneratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WithMockUser(authorities = "USER")
@SpringBootTest(classes = ScissorsGameServiceApplication.class)
public class GameControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private GeneratorService generatorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void makeMove_shouldReturnGameResponse() throws Exception {
        // Given
        String userId = "user1";
        String userMove = "rock";
        String computerMove = "scissors";
        String result = "WIN";

        GameResponse gameResponse = new GameResponse(1L, "You chose: rock, Computer chose: scissors. Result: WIN");

        when(generatorService.generateGameMoveRandomValue()).thenReturn(computerMove);
        when(gameService.makeMove(anyString(), anyString(), anyString())).thenReturn(gameResponse);

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/api/game/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(new GameRequest(userId, userMove))))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(gameResponse)));
    }

    @Test
    void terminateGame_shouldReturnSuccessMessage() throws Exception {
        // Given
        Long gameId = 1L;

        // When
        mockMvc.perform(MockMvcRequestBuilders.patch("/v1/api/game/terminate/{game_id}", gameId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Game terminated successfully."));
    }

    @Test
    void resetGame_shouldReturnSuccessMessage() throws Exception {
        // Given
        String userId = "user1";

        // When
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/api/game/reset/{user_id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Game reset successfully."));
    }

    @Test
    void getStats_shouldReturnStatistics() throws Exception {
        // Given
        String userId = "user1";
        Map<Object, Object> stats = new HashMap<>();
        stats.put("WIN", 10);
        stats.put("LOST", 5);
        stats.put("DRAW", 2);

        when(gameService.getStatistics(anyString())).thenReturn(stats);

        // When
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/game/statistics/{user_id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(stats)));
    }

    @Test
    void getStats_shouldReturnNoContent() throws Exception {
        // Given
        String userId = "user1";

        when(gameService.getStatistics(anyString())).thenReturn(new HashMap<>());

        // When
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/game/statistics/{user_id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    // Helper method to convert objects to JSON string
    private static String asJsonString(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
