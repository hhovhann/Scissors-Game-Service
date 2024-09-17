package com.hhovhann.Scissors_Game_Service.scissors_game.service.game;

import com.hhovhann.Scissors_Game_Service.scissors_game.entity.Game;
import com.hhovhann.Scissors_Game_Service.scissors_game.enums.GameStatus;
import com.hhovhann.Scissors_Game_Service.scissors_game.exception.GameNotFoundException;
import com.hhovhann.Scissors_Game_Service.scissors_game.model.GameResponse;
import com.hhovhann.Scissors_Game_Service.scissors_game.repository.GameRepository;
import com.hhovhann.Scissors_Game_Service.scissors_game.service.cache.CacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.hhovhann.Scissors_Game_Service.scissors_game.enums.GameResult.DRAW;
import static com.hhovhann.Scissors_Game_Service.scissors_game.enums.GameResult.LOST;
import static com.hhovhann.Scissors_Game_Service.scissors_game.enums.GameResult.WIN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameServiceImplTest {

    @Mock
    private CacheService cacheService;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameServiceImpl gameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void makeMove_shouldUpdateCacheStatistics() {
        // Given
        String userId = "user1";
        String userMove = "rock";
        String computerMove = "scissors";
        String result = "WIN"; // The result returned from determineWinner

        String expectedResponse =  "You chose: " + userMove + ", Computer chose: " + computerMove + ". Result: " + result;

        // Stubbing
        when(gameRepository.save(any(Game.class))).thenReturn(new Game());
        doNothing().when(cacheService).updateStatisticsInCache(userId, result); // Use doNothing() for void methods


        // When
        GameResponse response = gameService.makeMove(userId, userMove, computerMove);

        // Then
        verify(cacheService).updateStatisticsInCache(userId, result); // Verify with exact match
        assertEquals(expectedResponse, response.result());
    }



    @Test
    void makeMove_shouldSaveGameAndUpdateCache() {
        // Given
        String userId = "user1";
        String userMove = "rock";
        String computerMove = "scissors";
        String result = WIN.getValue();

        Game game = Game.builder()
                .userMove(userMove)
                .userId(userId)
                .computerMove(computerMove)
                .result(result)
                .status(GameStatus.ACTIVE.getValue())
                .timestamp(LocalDateTime.now())
                .build();

        when(gameRepository.save(any(Game.class))).thenReturn(game);
        doNothing().when(cacheService).updateStatisticsInCache(userId, result); // Use doNothing() for void methods


        // When
        GameResponse response = gameService.makeMove(userId, userMove, computerMove);

        // Then
        verify(gameRepository).save(any(Game.class));
        verify(cacheService).updateStatisticsInCache(userId, result);
        assertEquals("You chose: rock, Computer chose: scissors. Result: WIN", response.result());
    }

    @Test
    void determineWinner_shouldReturnDraw() {
        // Given
        String userMove = "rock";
        String computerMove = "rock";

        // When
        String result = gameService.determineWinner(userMove, computerMove);

        // Then
        assertEquals(DRAW.getValue(), result);
    }

    @Test
    void determineWinner_shouldReturnWin() {
        // Given
        String userMove = "rock";
        String computerMove = "scissors";
        String expectedResult = "WIN"; // Expected result

        // When
        String result = gameService.determineWinner(userMove, computerMove);

        // Then
        assertEquals(WIN.getValue(), result);
    }

    @Test
    void determineWinner_shouldReturnLost() {
        // Given
        String userMove = "rock";
        String computerMove = "paper";

        // When
        String result = gameService.determineWinner(userMove, computerMove);

        // Then
        assertEquals(LOST.getValue(), result);
    }

    @Test
    void getStatistics_shouldReturnCachedStats() {
        // Given
        String userId = "user1";
        Map<Object, Object> cachedStats = new HashMap<>();
        cachedStats.put("WIN", 5L);
        cachedStats.put("LOST", 2L);
        cachedStats.put("DRAW", 3L);

        when(cacheService.fetchStatisticsFromCache(userId)).thenReturn(cachedStats);

        // When
        Map<Object, Object> stats = gameService.getStatistics(userId);

        // Then
        verify(cacheService).fetchStatisticsFromCache(userId);
        assertEquals(cachedStats, stats);
    }

    @Test
    void getStatistics_shouldFetchFromDatabaseOnCacheMiss() {
        // Given
        String userId = "user1";
        Map<Object, Object> dbStats = new HashMap<>();
        dbStats.put("WIN", 5L);
        dbStats.put("LOST", 2L);
        dbStats.put("DRAW", 3L);

        when(cacheService.fetchStatisticsFromCache(userId)).thenReturn(null);
        when(gameRepository.countByResultAndUserId("WIN", userId)).thenReturn(5L);
        when(gameRepository.countByResultAndUserId("LOST", userId)).thenReturn(2L);
        when(gameRepository.countByResultAndUserId("DRAW", userId)).thenReturn(3L);
        doNothing().when(cacheService).addStatisticsToCache(userId, dbStats); // Use doNothing() for void methods


        // When
        Map<Object, Object> stats = gameService.getStatistics(userId);

        // Then
        verify(cacheService).fetchStatisticsFromCache(userId);
        verify(gameRepository).countByResultAndUserId("WIN", userId);
        verify(gameRepository).countByResultAndUserId("LOST", userId);
        verify(gameRepository).countByResultAndUserId("DRAW", userId);
        verify(cacheService).addStatisticsToCache(userId, dbStats);
        assertEquals(dbStats, stats);
    }

    @Test
    void resetGame_shouldClearDatabaseRecordsAndCache() {
        // Given
        String userId = "user1";

        // When
        gameService.resetGame(userId);

        // Then
        verify(gameRepository).deleteAllByUserId(userId);
        verify(cacheService).resetStatisticsFromCache(userId);
    }

    @Test
    void terminateGame_shouldUpdateGameStatus() {
        // Given
        Long gameId = 1L;
        Game game = new Game();
        game.setId(gameId);
        game.setStatus(GameStatus.ACTIVE.getValue());

        when(gameRepository.findByIdAndStatus(gameId, "ACTIVE")).thenReturn(Optional.of(game));
        when(gameRepository.save(game)).thenReturn(game);

        // When
        gameService.terminateGame(gameId);

        // Then
        verify(gameRepository).findByIdAndStatus(gameId, "ACTIVE");
        verify(gameRepository).save(game);
        assertEquals(GameStatus.TERMINATED.getValue(), game.getStatus());
    }

    @Test
    void terminateGame_shouldThrowExceptionIfGameNotFound() {
        // Given
        Long gameId = 1L;

        when(gameRepository.findByIdAndStatus(gameId, "ACTIVE")).thenReturn(Optional.empty());

        // When & Then
        GameNotFoundException thrown = assertThrows(GameNotFoundException.class, () -> gameService.terminateGame(gameId));
        assertEquals("Game not found or already terminated.", thrown.getMessage());
    }
}
