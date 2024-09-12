package com.hhovhann.Scissors_Game_Service.scissors_game.service.cache;

import java.util.Map;

public interface CacheService {
    String STATISTIC_KEY = "game_statistics";

    /**
     * Add statistics to the cache
     * @param stats
     */
    void addStatisticsToCache(Map<Object, Object> stats);

    /**
     * Increment statistics in cache
     * @param result
     */
    void updateStatisticsInCache(String result);

    /**
     * Retrieves statistics from the cache
     * @return
     */
    Map<Object, Object> fetchStatisticsFromCache();

    /**
     *  Reset all statistics from the cache
     */
    void resetStatisticsFromCache();
}
