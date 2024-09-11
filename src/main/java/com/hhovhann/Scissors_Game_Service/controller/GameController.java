package com.hhovhann.Scissors_Game_Service.controller;

import com.hhovhann.Scissors_Game_Service.model.RequestModel;
import com.hhovhann.Scissors_Game_Service.model.ResponseModel;
import com.hhovhann.Scissors_Game_Service.service.GameService;
import com.hhovhann.Scissors_Game_Service.service.NumberGenerationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/game")
public class GameController {


    public GameController(GameService gameService, NumberGenerationService numberGenerationService) {
        this.gameService = gameService;
        this.numberGenerationService = numberGenerationService;
    }

    private final GameService gameService;
    private final NumberGenerationService numberGenerationService;

    @PostMapping("/start")
    public ResponseEntity<ResponseModel> playGame(@RequestBody RequestModel requestModel) {
        String computerChoice = numberGenerationService.generateGameMoveRandomValue();
        String userChoice = requestModel.userChoice().toLowerCase();

        String result = gameService.determineWinner(userChoice, computerChoice);

        return ResponseEntity.ok(new ResponseModel("You chose: " + userChoice + ", Computer chose: " + computerChoice + ". Result: " + result));
    }
}
