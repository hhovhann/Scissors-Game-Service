package com.hhovhann.Scissors_Game_Service.scissors_game.service.cache;

import lombok.NoArgsConstructor;
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
    public void addStatisticsToCache(Map<Object, Object> stats) {
        log.debug("Add stats in Redis cache for result: {}", stats);

        redisTemplate.opsForHash().putAll(STATISTIC_KEY, stats);
    }

    @Override
    public void updateStatisticsInCache(String result) {
        log.debug("Updating stats in Redis cache for result: {}", result);

        redisTemplate.opsForHash().increment(STATISTIC_KEY, result, 1);
    }

    @Override
    public Map<Object, Object> fetchStatisticsFromCache() {
        log.debug("Fetching stats from Redis cache");

        return redisTemplate.opsForHash().entries(STATISTIC_KEY);
    }

    @Override
    public void resetStatisticsFromCache() {
        log.debug("Clear the game statistics from Redis cache");

        redisTemplate.delete(STATISTIC_KEY);
    }
}
