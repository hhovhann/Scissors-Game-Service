package com.hhovhann.Scissors_Game_Service.scissors_game.service.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void addStatisticsToCache(String userId, Map<Object, Object> stats) {
        if (stats == null || stats.isEmpty()) {
            log.warn("Attempted to add empty stats to cache for user_id: {}.", userId);
            return;
        }

        log.debug("Adding stats to Redis cache for user_id: {}: {}", userId, stats);
        redisTemplate.opsForHash().putAll(STATISTIC_KEY + ":" + userId, stats);
    }


    @Override
    public void updateStatisticsInCache(String userId, String result) {
        log.debug("Updating stats in Redis cache for user_id: {} with result: {}", userId, result);
        String cacheKey = STATISTIC_KEY + ":" + userId;

        // Use Redis operations to update counts
        redisTemplate.opsForHash().increment(cacheKey, result, 1);
    }


    @Override
    public Map<Object, Object> fetchStatisticsFromCache(String userId) {
        log.debug("Fetching stats from Redis cache for user_id: {}", userId);
        Map<Object, Object> cachedStats = redisTemplate.opsForHash().entries(STATISTIC_KEY + ":" + userId);
        if (cachedStats.isEmpty()) {
            return null; // or Collections.emptyMap();
        }
        return cachedStats;
    }

    @Override
    public void resetStatisticsFromCache(String userId) {
        log.debug("Resetting stats in Redis cache for user_id: {}", userId);
        redisTemplate.opsForHash().delete(STATISTIC_KEY + ":" + userId, "someField");
    }
}
