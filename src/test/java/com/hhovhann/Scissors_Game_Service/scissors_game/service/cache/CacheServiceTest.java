package com.hhovhann.Scissors_Game_Service.scissors_game.service.cache;


import com.hhovhann.Scissors_Game_Service.scissors_game.service.cache.CacheServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CacheServiceTest {

    private static final String STATISTIC_KEY = "GAME_STATISTICS";

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @InjectMocks
    private CacheServiceImpl cacheService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    }

    @Test
    void testAddStatisticsToCache() {
        // Given
        Map<Object, Object> stats = Map.of("WIN", 10, "LOST", 5, "DRAW", 3);

        // When
        cacheService.addStatisticsToCache(stats);

        // Then
        verify(hashOperations, times(1)).putAll(STATISTIC_KEY, stats);
        verifyNoMoreInteractions(hashOperations);
    }

    @Test
    void testUpdateStatisticsInCache() {
        // Given
        String result = "WIN";

        // When
        cacheService.updateStatisticsInCache(result);

        // Then
        verify(hashOperations, times(1)).increment(STATISTIC_KEY, result, 1);
    }

    @Test
    void testFetchStatisticsFromCache() {
        // Given
        Map<Object, Object> cachedStats = Map.of("WIN", 10, "LOST", 5, "DRAW", 3);
        when(hashOperations.entries(STATISTIC_KEY)).thenReturn(cachedStats);

        // When
        Map<Object, Object> actualStats = cacheService.fetchStatisticsFromCache();

        // Then
        assertEquals(cachedStats, actualStats);
        verify(hashOperations, times(1)).entries(STATISTIC_KEY);
    }

    @Test
    void testResetStatisticsFromCache() {
        // When
        cacheService.resetStatisticsFromCache();

        // Then
        verify(redisTemplate, times(1)).delete(STATISTIC_KEY);
    }

}

