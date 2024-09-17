package com.hhovhann.Scissors_Game_Service.scissors_game.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Table(name = "GAME_ENTITY")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "USER_ID", nullable = false)
    private String userId;
    @Column(nullable = false)
    private String userMove;
    @Column(nullable = false)
    private String computerMove;
    @Column(nullable = false)
    private String result;
    @Column(nullable = false)// WIN, LOSE, DRAW
    @Builder.Default
    private String status = "ACTIVE";   // defaulting to ACTIVE
    @Column(nullable = false)
    private LocalDateTime timestamp;
}
