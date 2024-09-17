package com.hhovhann.Scissors_Game_Service.scissors_game.service.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.hhovhann.Scissors_Game_Service.scissors_game.service.cache.CacheService.STATISTIC_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class CacheServiceImplTest {

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
    void addStatisticsToCache_shouldAddStatsToCache() {
        // Given
        String userId = "user1";
        Map<Object, Object> stats = new HashMap<>();
        stats.put("key1", "value1");
        stats.put("key2", "value2");

        // When
        cacheService.addStatisticsToCache(userId, stats);

        // Then
        verify(hashOperations).putAll(STATISTIC_KEY + ":" + userId, stats);
    }

    @Test
    void addStatisticsToCache_shouldNotAddEmptyStats() {
        // Given
        String userId = "user1";
        Map<Object, Object> stats = new HashMap<>(); // empty stats

        // When
        cacheService.addStatisticsToCache(userId, stats);

        // Then
        verify(hashOperations, never()).putAll(anyString(), anyMap());
    }

    @Test
    void updateStatisticsInCache_shouldIncrementValue() {
        // Given
        String userId = "user1";
        String result = "resultKey";

        // When
        cacheService.updateStatisticsInCache(userId, result);

        // Then
        verify(hashOperations).increment(STATISTIC_KEY + ":" + userId, result, 1);
    }

    @Test
    void fetchStatisticsFromCache_shouldReturnStats() {
        // Given
        String userId = "user1";
        Map<Object, Object> stats = new HashMap<>();
        stats.put("key1", "value1");

        when(hashOperations.entries(STATISTIC_KEY + ":" + userId)).thenReturn(stats);

        // When
        Map<Object, Object> result = cacheService.fetchStatisticsFromCache(userId);

        // Then
        verify(hashOperations).entries(STATISTIC_KEY + ":" + userId);
        assertEquals(stats, result);
    }

    @Test
    void fetchStatisticsFromCache_shouldReturnNullIfEmpty() {
        // Given
        String userId = "user1";
        when(hashOperations.entries(STATISTIC_KEY + ":" + userId)).thenReturn(new HashMap<>());

        // When
        Map<Object, Object> result = cacheService.fetchStatisticsFromCache(userId);

        // Then
        verify(hashOperations).entries(STATISTIC_KEY + ":" + userId);
        assertNull(result);
    }

    @Test
    void resetStatisticsFromCache_shouldDeleteStats() {
        // Given
        String userId = "user1";

        // When
        cacheService.resetStatisticsFromCache(userId);

        // Then
        verify(hashOperations).delete(STATISTIC_KEY + ":" + userId);
    }
}
