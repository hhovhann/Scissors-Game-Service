package com.hhovhann.Scissors_Game_Service.scissors_game.service.game;

import com.hhovhann.Scissors_Game_Service.scissors_game.entity.Game;
import com.hhovhann.Scissors_Game_Service.scissors_game.enums.GameStatus;
import com.hhovhann.Scissors_Game_Service.scissors_game.repository.GameRepository;
import com.hhovhann.Scissors_Game_Service.scissors_game.service.cache.CacheService;
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

    public GameServiceImpl(CacheService cacheService, GameRepository gameRepository) {
        this.cacheService = cacheService;
        this.gameRepository = gameRepository;
    }

    @Override
    public String makeMove(String userMove, String computerMove) {
        log.debug("User move received: {}, Computer move recived: {}", userMove, computerMove);

        String result = determineWinner(userMove, computerMove);

        log.info("User move: {}, Computer move: {}, Result: {}", userMove, computerMove, result);


        Game gameToBeStored = Game.builder()
                .userMove(userMove)
                .computerMove(computerMove)
                .result(result)
                .status(GameStatus.ACTIVE.getValue())
                .timestamp(LocalDateTime.now()).build();

        // Save game in database
        gameRepository.save(gameToBeStored);

        // Update statistics in cache
        cacheService.updateStatisticsInCache(result);

        return result;
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


    public Map<Object, Object> getStatistics() {
        // Try to get cachedStatistics from Redis cache first
        Map<Object, Object> cachedStatistics = cacheService.fetchStatisticsFromCache();
        if (cachedStatistics.isEmpty()) {
            log.info("Cache miss. Fetching stats from the database.");
            cachedStatistics = fetchStatisticsFromDatabase();
            cacheService.addStatisticsToCache(cachedStatistics);
        }
        return cachedStatistics;
    }

    @Override
    public void resetGame() {
        log.info("Resetting game: clearing database records and Redis cache");

        gameRepository.deleteAll();

        cacheService.resetStatisticsFromCache();
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
            throw new IllegalStateException("Game not found or already terminated.");
        }
    }

    /**
     * Retrieves statistics from the database
     * @return the map of statistics results
     */
    private Map<Object, Object> fetchStatisticsFromDatabase() {
        log.debug("Fetching stats from the database");

        return Map.of(
                WIN, gameRepository.countByResult(WIN),
                LOST, gameRepository.countByResult(LOST),
                DRAW, gameRepository.countByResult(DRAW)
        );
    }
}
