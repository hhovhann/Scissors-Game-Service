package com.hhovhann.Scissors_Game_Service.scissors_game.service.game;

import com.hhovhann.Scissors_Game_Service.scissors_game.repository.GameRepository;
import com.hhovhann.Scissors_Game_Service.scissors_game.service.cache.CacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Disabled

class GameServiceTest {
    @InjectMocks
    private GameService gameService;

    @InjectMocks
    private CacheService cacheService;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMakeMove() {
        String userMove = "ROCK";
        String computerMove = "PAPER";
        String result = "LOSE";

        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));
        when(redisTemplate.opsForHash().entries(anyString())).thenReturn(new HashMap<>());

        String moveResult = gameService.makeMove(userMove, computerMove);

        assertThat(moveResult).isEqualTo(result);
        verify(redisTemplate.opsForHash(), times(1)).increment("game_stats", result, 1);
    }

    @Test
    void testGetStats() {
        Map<Object, Object> stats = new HashMap<>();
        stats.put("WIN", 10);
        stats.put("LOSE", 5);
        stats.put("DRAW", 2);

        when(redisTemplate.opsForHash().entries(anyString())).thenReturn(stats);

        Map<Object, Object> retrievedStats = gameService.getStatistics();

        assertThat(retrievedStats).isEqualTo(stats);
    }
}