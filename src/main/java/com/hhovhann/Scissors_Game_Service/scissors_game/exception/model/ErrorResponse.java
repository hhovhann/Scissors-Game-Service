package com.hhovhann.Scissors_Game_Service.scissors_game.exception.model;

import java.util.List;

public record ErrorResponse (String message, List<String> details ) {
}
