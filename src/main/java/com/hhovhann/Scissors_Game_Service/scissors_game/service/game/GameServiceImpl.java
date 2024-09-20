package com.hhovhann.Scissors_Game_Service.scissors_game.service.game;

import com.hhovhann.Scissors_Game_Service.scissors_game.entity.Game;
import com.hhovhann.Scissors_Game_Service.scissors_game.entity.User;
import com.hhovhann.Scissors_Game_Service.scissors_game.enums.GameStatus;
import com.hhovhann.Scissors_Game_Service.scissors_game.exception.GameNotFoundException;
import com.hhovhann.Scissors_Game_Service.scissors_game.model.GameResponse;
import com.hhovhann.Scissors_Game_Service.scissors_game.repository.GameRepository;
import com.hhovhann.Scissors_Game_Service.scissors_game.service.cache.CacheService;
import com.hhovhann.Scissors_Game_Service.scissors_game.service.user.UserDetailsService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static com.hhovhann.Scissors_Game_Service.scissors_game.enums.GameResult.DRAW;
import static com.hhovhann.Scissors_Game_Service.scissors_game.enums.GameResult.LOST;
import static com.hhovhann.Scissors_Game_Service.scissors_game.enums.GameResult.WIN;


@Slf4j
@Service
public class GameServiceImpl implements GameService {
    private final CacheService cacheService;
    private final GameRepository gameRepository;
    private final UserDetailsService userDetailsService;

    public GameServiceImpl(CacheService cacheService, GameRepository gameRepository, UserDetailsService userDetailsService) {
        this.cacheService = cacheService;
        this.gameRepository = gameRepository;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public GameResponse makeMove(String userId, String userMove, String computerMove) {
        log.debug("User move received: {}, Computer move recived: {}", userMove, computerMove);

        String result = determineWinner(userMove, computerMove);

        log.info("User move: {}, Computer move: {}, Result: {}", userMove, computerMove, result);

        // Fetch the User entity based on userId
        User user = userDetailsService.findById(userId);

        Game gameToBeStored = Game.builder()
                .userMove(userMove)
                .user(user)
                .computerMove(computerMove)
                .result(result)
                .status(GameStatus.ACTIVE.getValue())
                .timestamp(LocalDateTime.now()).build();

        // Save game in database
        gameRepository.save(gameToBeStored);

        // Update statistics in cache
        cacheService.updateStatisticsInCache(userId, result);

        return new GameResponse(gameToBeStored.getId(), "You chose: " + userMove + ", Computer chose: " + computerMove + ". Result: " + result);
    }

    @Override
    public String determineWinner(String userMove, String computerMove) {
        log.debug("Determining result for User move: {} vs Computer move: {}", userMove, computerMove);
        if (userMove.equals(computerMove)) {
            return DRAW.getValue();
        } else if (isPlayerWin(userMove, computerMove)) {
            return WIN.getValue();
        } else {
            return LOST.getValue();
        }
    }

    @Override
    public Map<Object, Object> getStatistics(String userId) {
        // Fetch the User entity based on userId
        User user = userDetailsService.findById(userId);
        // Try to get cachedStatistics from Redis cache first
        Map<Object, Object> cachedStatistics = cacheService.fetchStatisticsFromCache(user.getUserId());
        if (cachedStatistics == null || cachedStatistics.isEmpty()) {
            log.info("Cache miss for user_id: {}. Fetching stats from the database.", userId);
            cachedStatistics = fetchStatisticsFromDatabase(userId);
            if (cachedStatistics != null && !cachedStatistics.isEmpty()) {
                cacheService.addStatisticsToCache(userId, cachedStatistics);
            } else {
                log.warn("Failed to fetch statistics from the database for user_id: {}.", userId);
            }
        } else {
            log.info("Cache hit for user_id: {}. Returning stats from cache.", userId);
        }
        return cachedStatistics;
    }


    @Override
    @Transactional
    public void resetGame(String userId) {
        log.info("Resetting game: clearing database records and Redis cache");

        // Fetch the User entity based on userId
        User user = userDetailsService.findById(userId);

        // Clear game data from the database
        gameRepository.deleteAllByUserId(user.getUserId());

        // Reset statistics in Redis cache
        cacheService.resetStatisticsFromCache(userId);
    }

    @Override
    public void terminateGame(Long gameId) {
        log.info("Terminating game with ID: {}", gameId);

        Optional<Game> optionalGame = gameRepository.findByIdAndStatus(gameId, "ACTIVE");
        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();
            game.setStatus(GameStatus.TERMINATED.getValue());
            gameRepository.save(game);
            log.info("Game with ID: {} terminated successfully.", gameId);
        } else {
            log.warn("Attempted to terminate a non-existent or already terminated game with ID: {}", gameId);
            throw new GameNotFoundException("Game not found or already terminated.");
        }
    }

    /**
     * Retrieves statistics from the database
     * @return the map of statistics results
     */
    private Map<Object, Object> fetchStatisticsFromDatabase(String userId) {
        log.debug("Fetching stats from the database");

        return Map.of(
                "WIN", gameRepository.countByResultAndUserUserId("WIN", userId),
                "LOST", gameRepository.countByResultAndUserUserId("LOST", userId),
                "DRAW", gameRepository.countByResultAndUserUserId("DRAW", userId)
        );
    }
}
