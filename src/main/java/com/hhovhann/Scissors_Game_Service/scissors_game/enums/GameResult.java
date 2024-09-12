package com.hhovhann.Scissors_Game_Service.scissors_game.enums;

public enum GameResult {
    DRAW( "DRAW"),
    LOST("LOST"),
    WIN("WIN");

    private final String value;

    public String getValue() {
        return this.value;
    }

    GameResult(String value) {
        this.value = value;
    }
}
