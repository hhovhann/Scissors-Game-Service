package com.hhovhann.Scissors_Game_Service.scissors_game.model;

import jakarta.validation.constraints.NotBlank;

public record UserResponse(@NotBlank String userId, @NotBlank String username, @NotBlank String email) {
}
