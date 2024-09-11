package com.hhovhann.Scissors_Game_Service.service;


import com.hhovhann.Scissors_Game_Service.enums.GameMove;

public interface GameService {
    String getComputerChoice();

    String determineWinner(String userChoice, String computerChoice);


    default boolean isPlayerWin(String userChoice, String computerChoice) {
        return userChoice.equals(GameMove.ROCK.getValue()) && computerChoice.equals(GameMove.SCISSORS.getValue())
                || (userChoice.equals(GameMove.SCISSORS.getValue()) && computerChoice.equals(GameMove.PAPER.getValue()))
                || (userChoice.equals(GameMove.PAPER.getValue()) && computerChoice.equals(GameMove.ROCK.getValue()));
    }
}
