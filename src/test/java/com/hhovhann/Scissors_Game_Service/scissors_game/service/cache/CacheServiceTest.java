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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CacheServiceTest {

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
    void addStatisticsToCache_ShouldPutAllStatsIntoCache() {
        // Arrange
        String userId = "user123";
        Map<Object, Object> stats = Map.of("WIN", "1", "LOST", "2", "DRAW", "3");
        String cacheKey = STATISTIC_KEY + ":" + userId;

        // Act
        cacheService.addStatisticsToCache(userId, stats);

        // Assert
        verify(hashOperations).putAll(cacheKey, stats);
    }

    @Test
    void addStatisticsToCache_ShouldNotPutEmptyStats() {
        // Arrange
        String userId = "user123";
        Map<Object, Object> stats = Map.of(); // Empty stats
        String cacheKey = STATISTIC_KEY + ":" + userId;

        // Act
        cacheService.addStatisticsToCache(userId, stats);

        // Assert
        verify(hashOperations, never()).putAll(cacheKey, stats); // Verify that putAll is never called
    }

    @Test
    void updateStatisticsInCache_ShouldIncrementValueInCache() {
        // Arrange
        String userId = "user123";
        String result = "score";
        String cacheKey = STATISTIC_KEY + ":" + userId;

        // Act
        cacheService.updateStatisticsInCache(userId, result);

        // Assert
        verify(hashOperations).increment(cacheKey, result, 1);
    }

    @Test
    void fetchStatisticsFromCache_ShouldReturnCachedStats_WhenStatsExist() {
        // Arrange
        String userId = "user123";
        String cacheKey = STATISTIC_KEY + ":" + userId;
        Map<Object, Object> expectedStats = Map.of("WIN", "1", "LOST", "2", "DRAW", "3");

        when(redisTemplate.opsForHash().entries(cacheKey)).thenReturn(expectedStats);

        // Act
        Map<Object, Object> actualStats = cacheService.fetchStatisticsFromCache(userId);

        // Assert
        assertEquals(expectedStats, actualStats, "The cached statistics should match the expected statistics.");
    }

    @Test
    void fetchStatisticsFromCache_ShouldReturnNull_WhenStatsDoNotExist() {
        // Arrange
        String userId = "user123";
        String cacheKey = STATISTIC_KEY + ":" + userId;

        when(redisTemplate.opsForHash().entries(cacheKey)).thenReturn(new HashMap<>());

        // Act
        Map<Object, Object> actualStats = cacheService.fetchStatisticsFromCache(userId);

        // Assert
        assertNull(actualStats, "The result should be null when no statistics are found in the cache.");
    }

    @Test
    void resetStatisticsFromCache_ShouldDeleteCacheEntry() {
        // Arrange
        String userId = "user123";
        String cacheKey = STATISTIC_KEY + ":" + userId;

        // Act
        cacheService.resetStatisticsFromCache(userId);

        // Assert
        verify(redisTemplate, times(1)).delete(cacheKey);
    }
}
