package com.hhovhann.Scissors_Game_Service.scissors_game.model;

import jakarta.validation.constraints.NotBlank;

public record UserRequest (@NotBlank String username, @NotBlank String password, @NotBlank String email) {
}
