package com.hhovhann.Scissors_Game_Service.enums;

public enum GameResult {
    TIE( "It's a tie!"),
    COMPUTER_WIN("Computer wins!"),
    USER_WIN("User win!");

    private String value;

    public String getValue() {
        return this.value;
    }

    GameResult(String value) {
        this.value = value;
    }
}
