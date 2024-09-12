package com.hhovhann.Scissors_Game_Service.scissors_game.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String userMove;
    @Column(nullable = false)
    private String computerMove;
    @Column(nullable = false)
    private String result;
    @Column(nullable = false)// WIN, LOSE, DRAW
    private String status = "ACTIVE";   // defaulting to ACTIVE
    @Column(nullable = false)
    private LocalDateTime timestamp;
}
