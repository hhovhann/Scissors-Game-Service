package com.hhovhann.Scissors_Game_Service.controller;

import com.hhovhann.Scissors_Game_Service.model.RequestModel;
import com.hhovhann.Scissors_Game_Service.model.ResponseModel;
import com.hhovhann.Scissors_Game_Service.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/game")
public class GameController {


    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    private final GameService gameService;

    @PostMapping("/start")
    public ResponseEntity<ResponseModel> playGame(@RequestBody RequestModel requestModel) {
        String computerChoice = gameService.getComputerChoice();
        String userChoice = requestModel.userChoice().toLowerCase();

        String result = gameService.determineWinner(userChoice, computerChoice);

        return ResponseEntity.ok(new ResponseModel("You chose: " + userChoice + ", Computer chose: " + computerChoice + ". Result: " + result));
    }
}
