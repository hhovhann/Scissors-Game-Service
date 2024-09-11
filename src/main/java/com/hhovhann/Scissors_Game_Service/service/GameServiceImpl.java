package com.hhovhann.Scissors_Game_Service.service;

import com.hhovhann.Scissors_Game_Service.enums.GameMove;
import org.springframework.stereotype.Service;

import java.util.Random;

import static com.hhovhann.Scissors_Game_Service.enums.GameResult.COMPUTER_WIN;
import static com.hhovhann.Scissors_Game_Service.enums.GameResult.TIE;
import static com.hhovhann.Scissors_Game_Service.enums.GameResult.USER_WIN;


@Service
public class GameServiceImpl implements GameService {
    private static int BOUND = 3;

    @Override
    public String getComputerChoice() {
        Random random = new Random();

        return GameMove.values()[random.nextInt(BOUND)].getValue();
    }

    @Override
    public String determineWinner(String userChoice, String computerChoice) {
        if (userChoice.equals(computerChoice)) {
            return TIE.getValue();
        } else if (isPlayerWin(userChoice, computerChoice)) {
            return USER_WIN.getValue();
        } else {
            return COMPUTER_WIN.getValue();
        }
    }
}
