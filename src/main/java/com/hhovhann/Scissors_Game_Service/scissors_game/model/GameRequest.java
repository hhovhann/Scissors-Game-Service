package com.hhovhann.Scissors_Game_Service.scissors_game.model;

import jakarta.validation.constraints.NotBlank;

public record GameRequest(@NotBlank(message = "User Move cannot be blank") String userMove,
                          @NotBlank(message = "User Id cannot be blank") String userId) {

}
