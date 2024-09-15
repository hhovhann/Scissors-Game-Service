package com.hhovhann.Scissors_Game_Service.scissors_game.service;


import com.hhovhann.Scissors_Game_Service.scissors_game.enums.GameMove;

import java.util.Map;

public interface GameService {

    /**
     * Retrieves the game statistics
     * @return
     */
    Map<Object, Object> getStatistics();

    /**
     *
     * @param userMove
     * @return
     */
    String makeMove(String userMove, String computerMove);

    /**
     *
     */
    void resetGame();

    /**
     * Terminate the game session
     * @param gameId
     */
    void terminateGame(Long gameId) ;


    /**
     * Determines the game winner
     * @param userMove user move choice
     * @param computerMove computer move choice
     * @return the winner
     */
    String determineWinner(String userMove, String computerMove);

    /**
     * Checks is the player the winner, otherwise return false
     * @param userMove user move choice
     * @param computerMove computer move choice
     * @return true if the player wins, otherwise false
     */
    default boolean isPlayerWin(String userMove, String computerMove) {
        return userMove.equals(GameMove.ROCK.getValue()) && computerMove.equals(GameMove.SCISSORS.getValue())
                || (userMove.equals(GameMove.SCISSORS.getValue()) && computerMove.equals(GameMove.PAPER.getValue()))
                || (userMove.equals(GameMove.PAPER.getValue()) && computerMove.equals(GameMove.ROCK.getValue()));
    }
}
