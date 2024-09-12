package com.hhovhann.Scissors_Game_Service.scissors_game.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhovhann.Scissors_Game_Service.scissors_game.model.RequestModel;
import com.hhovhann.Scissors_Game_Service.scissors_game.service.game.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@WebMvcTest(GameController.class)
class GameControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private GameService gameService;

    @InjectMocks
    private GameController gameController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(gameController).build();
    }

    @Test
    void testMakeMove() throws Exception {
        String userMove = "rock";
        String computerMove = "scissors";
        String result = "WIN";
        RequestModel requestModel = new RequestModel(userMove);

        when(gameService.makeMove(userMove, computerMove)).thenReturn(result);

        mockMvc.perform(post("/v1/api/game/start")
                        .content(objectMapper.writeValueAsString(requestModel))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Result: " + result));
    }

    @Test
    void testGetStats() throws Exception {
        Map<Object, Object> stats = new HashMap<>();
        stats.put("WIN", 10);
        stats.put("LOSE", 5);
        stats.put("DRAW", 2);

        when(gameService.getStatistics()).thenReturn(stats);

        mockMvc.perform(get("/v1/api/game/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.WIN").value(10))
                .andExpect(jsonPath("$.LOSE").value(5))
                .andExpect(jsonPath("$.DRAW").value(2));
    }

    @Test
    void testResetGame() throws Exception {
        mockMvc.perform(delete("/v1/api/game/reset"))
                .andExpect(status().isOk())
                .andExpect(content().string("Game reset successfully."));
    }

    @Test
    void testTerminateGame() throws Exception {
        long gameId = 1L;
        mockMvc.perform(patch("/v1/api/game/terminate/{id}", gameId))
                .andExpect(status().isOk())
                .andExpect(content().string("Game terminated successfully."));
    }
}