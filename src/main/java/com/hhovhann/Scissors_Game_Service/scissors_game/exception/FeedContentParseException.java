package com.hhovhann.Scissors_Game_Service.scissors_game.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FeedContentParseException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 2172496712180575232L;

    public FeedContentParseException() {
        super();
    }

    public FeedContentParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public FeedContentParseException(String message) {
        super(message);
    }

    public FeedContentParseException(Throwable cause) {
        super(cause);
    }
}
