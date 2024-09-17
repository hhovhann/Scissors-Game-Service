package com.hhovhann.Scissors_Game_Service.scissors_game.model;

import jakarta.validation.constraints.NotBlank;

public record GameResponse(@NotBlank(message = "Game Id cannot be blank") Long gameId,
                           @NotBlank(message = "Result cannot be blank") String result) {

}
