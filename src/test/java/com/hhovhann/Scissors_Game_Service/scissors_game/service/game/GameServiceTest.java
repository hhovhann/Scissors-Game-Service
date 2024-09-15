package com.hhovhann.Scissors_Game_Service.scissors_game.service.game;

import com.hhovhann.Scissors_Game_Service.scissors_game.entity.Game;
import com.hhovhann.Scissors_Game_Service.scissors_game.enums.GameStatus;
import com.hhovhann.Scissors_Game_Service.scissors_game.repository.GameRepository;
import com.hhovhann.Scissors_Game_Service.scissors_game.service.cache.CacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static com.hhovhann.Scissors_Game_Service.scissors_game.enums.GameResult.DRAW;
import static com.hhovhann.Scissors_Game_Service.scissors_game.enums.GameResult.LOST;
import static com.hhovhann.Scissors_Game_Service.scissors_game.enums.GameResult.WIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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
    void testMakeMove_PlayerWins() {
        // Given
        String userMove = "rock";
        String computerMove = "scissors";
        String expectedResult = WIN.getValue();

        // Mock repository and cache interactions
        doNothing().when(cacheService).updateStatisticsInCache(expectedResult);

        // When
        String actualResult = gameService.makeMove(userMove, computerMove);

        // Then
        assertEquals(expectedResult, actualResult);
        verify(gameRepository, times(1)).save(any(Game.class));
        verify(cacheService, times(1)).updateStatisticsInCache(expectedResult);
    }

    @Test
    void testMakeMove_Draw() {
        // Given
        String userMove = "rock";
        String computerMove = "rock";
        String expectedResult = DRAW.getValue();

        // When
        String actualResult = gameService.makeMove(userMove, computerMove);

        // Then
        assertEquals(expectedResult, actualResult);
        verify(gameRepository, times(1)).save(any(Game.class));
        verify(cacheService, times(1)).updateStatisticsInCache(expectedResult);
    }

    @Test
    void testMakeMove_PlayerLoses() {
        // Given
        String userMove = "rock";
        String computerMove = "paper";
        String expectedResult = LOST.getValue();

        // When
        String actualResult = gameService.makeMove(userMove, computerMove);

        // Then
        assertEquals(expectedResult, actualResult);
        verify(gameRepository, times(1)).save(any(Game.class));
        verify(cacheService, times(1)).updateStatisticsInCache(expectedResult);
    }

    @Test
    void testTerminateGame_Success() {
        // Given
        Long gameId = 1L;
        Game game = Game.builder()
                .id(gameId)
                .status(GameStatus.ACTIVE.getValue())
                .timestamp(LocalDateTime.now())
                .build();
        when(gameRepository.findByIdAndStatus(gameId, "ACTIVE")).thenReturn(Optional.of(game));

        // When
        gameService.terminateGame(gameId);

        // Then
        assertEquals(GameStatus.TERMINATED.getValue(), game.getStatus());
        verify(gameRepository, times(1)).save(game);
    }

    @Test
    void testTerminateGame_GameNotFound() {
        // Given
        Long gameId = 1L;
        when(gameRepository.findByIdAndStatus(gameId, "ACTIVE")).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(IllegalStateException.class, () -> gameService.terminateGame(gameId));
        assertEquals("Game not found or already terminated.", exception.getMessage());
    }

    @Test
    void testResetGame() {
        // When
        gameService.resetGame();

        // Then
        verify(gameRepository, times(1)).deleteAll();
        verify(cacheService, times(1)).resetStatisticsFromCache();
    }

    @Test
    void testGetStatistics_CacheHit() {
        // Given
        Map<Object, Object> cachedStats = Map.of("WIN", 10, "LOST", 5, "DRAW", 3);
        when(cacheService.fetchStatisticsFromCache()).thenReturn(cachedStats);

        // When
        Map<Object, Object> actualStats = gameService.getStatistics();

        // Then
        assertEquals(cachedStats, actualStats);
        verify(cacheService, times(1)).fetchStatisticsFromCache();
        verifyNoMoreInteractions(gameRepository);
    }

    @Test
    void testGetStatistics_CacheMiss() {
        // Given
        Map<Object, Object> dbStats = Map.of("WIN", 10, "LOST", 5, "DRAW", 3);
        when(cacheService.fetchStatisticsFromCache()).thenReturn(Map.of());
        when(gameRepository.countByResult("WIN")).thenReturn(10);
        when(gameRepository.countByResult("LOST")).thenReturn(5);
        when(gameRepository.countByResult("DRAW")).thenReturn(3);

        // When
        Map<Object, Object> actualStats = gameService.getStatistics();

        // Then
        assertEquals(dbStats, actualStats);
        verify(cacheService, times(1)).fetchStatisticsFromCache();
        verify(cacheService, times(1)).addStatisticsToCache(dbStats);
    }
}
