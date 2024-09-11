package com.hhovhann.Scissors_Game_Service.service;


import com.hhovhann.Scissors_Game_Service.enums.GameMove;

public interface GameService {

    /**
     * Determines the game winner
     * @param userChoice user move choice
     * @param computerChoice computer move choice
     * @return the winner
     */
    String determineWinner(String userChoice, String computerChoice);

    /**
     * Checks is the player the winner, otherwise return false
     * @param userChoice user move choice
     * @param computerChoice computer move choice
     * @return true if the player wins, otherwise false
     */
    default boolean isPlayerWin(String userChoice, String computerChoice) {
        return userChoice.equals(GameMove.ROCK.getValue()) && computerChoice.equals(GameMove.SCISSORS.getValue())
                || (userChoice.equals(GameMove.SCISSORS.getValue()) && computerChoice.equals(GameMove.PAPER.getValue()))
                || (userChoice.equals(GameMove.PAPER.getValue()) && computerChoice.equals(GameMove.ROCK.getValue()));
    }
}
