package com.hhovhann.Scissors_Game_Service.service;

import com.hhovhann.Scissors_Game_Service.enums.GameMove;
import org.springframework.stereotype.Service;

import java.util.Random;

import static com.hhovhann.Scissors_Game_Service.enums.GameResult.COMPUTER_WIN;
import static com.hhovhann.Scissors_Game_Service.enums.GameResult.TIE;
import static com.hhovhann.Scissors_Game_Service.enums.GameResult.USER_WIN;


@Service
public class GameServiceImpl implements GameService {

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
