package com.hhovhann.Scissors_Game_Service.scissors_game.enums;

public enum GameMove {
    ROCK("rock"),
    PAPER("paper"),
    SCISSORS("scissors");

    private final String value;

    public String getValue() {
        return this.value;
    }

    GameMove(String value) {
        this.value = value;
    }
}
