package com.hhovhann.Scissors_Game_Service.service;

import com.hhovhann.Scissors_Game_Service.enums.GameMove;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class NumberGenerationServiceImpl implements NumberGenerationService {

    private static int BOUND = 3;

    public String generateGameMoveRandomValue() {
        Random random = new Random();

        return GameMove.values()[random.nextInt(BOUND)].getValue();
    }
}

