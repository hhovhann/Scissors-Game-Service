package com.hhovhann.Scissors_Game_Service.enums;

public enum GameMove {
    ROCK("rock"),
    PAPER("paper"),
    SCISSORS("scissors");

    private String value;

    public String getValue() {
        return this.value;
    }

    GameMove(String value) {
        this.value = value;
    }
}
