package com.hhovhann.Scissors_Game_Service.scissors_game.service.generator;

public interface GeneratorService {
    /**
     * Bound Default value
     */
    int BOUND = 3;

    /**
     * Generates Random Game Move
     * @return generated random game move
     */
    String generateGameMoveRandomValue();
}
