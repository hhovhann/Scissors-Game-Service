package com.hhovhann.Scissors_Game_Service.scissors_game.enums;

public enum GameStatus {
    ACTIVE("ACTIVE"),
    TERMINATED("TERMINATED");

    private final String value;

    public String getValue() {
        return this.value;
    }

    GameStatus(String value) {
        this.value = value;
    }
}
