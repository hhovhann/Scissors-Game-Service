# Scissors-Game-Service
Rock  Paper Scissors Game Service Spring Demo Application
This is a Spring Boot application to create a Rest Api for the Game Rock, Paper, Scissor. 
The api takes a parameter which is the move of player 1 (Rock, Scissor, Paper). 
The Spring Boot application has a random function top generates a random move for player2. 
Then it compares both move and returns if player1 wins, loses or tie.

1. Start the game and send the POST Request to the server side.

REQUEST

```
POST http://localhost:8080/v1/api/game/start
{
    "userChoice": "rock"
}
```

RESPONSE
```
    {
        "message": "You chose: rock, Computer chose: rock. Result: It's a tie!"
    }
```

We can also put Paper or Scissor instead Rock which will act as player1 move.


# How to run the application
- Run via Intellij IDEA
- Run from `run.sh` scripts

# TODO
- Add swagger support
- Add docker support
- Add unit testing
