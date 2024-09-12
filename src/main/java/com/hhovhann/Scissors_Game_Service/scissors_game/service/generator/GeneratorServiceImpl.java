package com.hhovhann.Scissors_Game_Service.scissors_game.service.generator;

import com.hhovhann.Scissors_Game_Service.scissors_game.enums.GameMove;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class GeneratorServiceImpl implements GeneratorService {

    public String generateGameMoveRandomValue() {
        Random random = new Random();

        String computerMove = GameMove.values()[random.nextInt(BOUND)].getValue();

        log.debug("Generated computer move: {}", computerMove);

        return computerMove;

    }
}

