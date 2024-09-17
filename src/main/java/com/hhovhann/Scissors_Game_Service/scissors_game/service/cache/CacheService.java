package com.hhovhann.Scissors_Game_Service.scissors_game.service.cache;

import java.util.Map;

public interface CacheService {
    String STATISTIC_KEY = "GAME_STATISTICS";

    /**
     * Add statistics to the cache
     * @param stats the game statistics Map object
     */
    void addStatisticsToCache(String userId, Map<Object, Object> stats);

    /**
     * Increment statistics in cache
     * @param result the game data
     */
    void updateStatisticsInCache(String userId, String result);

    /**
     * Retrieves statistics from the cache
     * @return the game statistics Map object
     */
    Map<Object, Object> fetchStatisticsFromCache(String userId);

    /**
     *  Reset all statistics from the cache
     */
    void resetStatisticsFromCache(String userId);
}
