# scissors-game-service
Rock  Paper Scissors Game Service Spring Demo Application

## Software Environment

- Java 21
- MySQL database
- Flyway migration
- Spring Boot 3.3.3
- Maven 3.9.5

## Software Structure
- feature_name(scissors_game)
    - configuration
    - controller
    - entity
    - enums
    - exception
    - model
    - repository
    - service
      - cache
      - game
      - generator

## Software Behaviour

This is a Spring Boot web application to create a Rest Api for the Game Rock, Paper, Scissor.
Supported Features:
 - Start: The `POST /v1/api/game/start` endpoint allows a game to be started by specifying the userMove as a request body parameter (rock | )
 - Terminate: The `PATCH /terminate/{id}` endpoint allows a game to be terminated by specifying its ID. If the game is not found or already terminated, an error message is returned.
 - Reset: The `DELETE /v1/api/game/reset`  endpoint allows resetting all game data
 - Statistics: The `/v1/api/game/statistics` endpoint returns the game data statistics

The api takes a parameter which is the move of player 1 (Rock, Scissor, Paper). 
The Spring Boot application has a random function top generates a random move for player2. 
Then it compares both move and returns if player1 wins, loses or tie.

## Software Design
Please check the [Rss Tracker Service Design](design/scissors-game-service-draft-design-flow.drawio)

## Software Documentation
- Open Api/Swagger Support - http://localhost:8080/swagger-ui/index.html

1. Start the game and send the POST Request to the server side.

a. TIE
REQUEST

```
    POST http://localhost:8080/v1/api/game/start
    {
        "userMove": "rock"
    }
```

RESPONSE
```
    {
        "message": "You chose: rock, Computer chose: rock. Result: DRAW"
    }
```

b. User Win
```
    POST http://localhost:8080/v1/api/game/start
    {
        "userMove": "rock"
    }
```

RESPONSE
```
    {
        "message": "You chose: rock, Computer chose: scissors. Result: WIN"
    }
```

c. Computer Win
```
    POST http://localhost:8080/v1/api/game/start
    {
        "userMove": "rock"
    }
```

RESPONSE
```
    {
        "message": "You chose: rock, Computer chose: paper. Result: LOST"
    }
```


2. Terminate the game
REQUEST
```
    PATCH http://localhost:8080/v1/api/game/terminate/1
```

RESPONSE
```
    Game terminated successfully.
```

RESPONSE ERROR
```
    Game not found or already terminated.
```

3. Reset the game
REQUEST
```
    DELETE http://localhost:8080/v1/api/game/reset
```

```
    Game reset successfully.
```

4. Get the game statistics:

REQUEST 
```
    POST http://localhost:8080/v1/api/game/statistics
    {
        "userMove": "rock"
    }
```

RESPONSE
```
    {
        "User wins!": 4,
        "Computer wins!": 3,
        "It's a draw!": 5
    }
```

We can also put Paper or Scissor instead Rock which will act as player1 move.

## Software Setup and Run: Local Application
- Download and install [Docker Desktop](https://www.docker.com/products/docker-desktop/) if you not have it installed in your machine
- Depends on which database we are going to use, should run:
    - local PostgreSQL  ```docker run --name scissors-game-service-dev-db -e POSTGRES_USER=api-username -e POSTGRES_PASSWORD=api-password -e POSTGRES_DB=scissors-game-service-dev-db -p 5432:5432 -d postgres```
    - local Redis       ```docker run -p 6379:6379 --name=scissors-game-service-dev-redis -d redis:latest```
- Run application with bach command from project root ./scripts/run.sh
- Run the application from the IDEA itself


## Software Setup and Run: Docker containers:
- Create .env file from the root project with
  ```
    POSTGRES_DATABASE=scissors-game-service-docker-db
    POSTGRES_USER=api-username
    POSTGRES_PASSWORD=api-password
    POSTGRES_LOCAL_PORT=5432
    POSTGRES_DOCKER_PORT=5432
    REDIS_LOCAL_PORT=6379
    REDIS_DOCKER_PORT=6379
    REDIS_DOCKER_HOST=redis_cache
    SPRING_APP_LOCAL_PORT=8888
    SPRING_APP_DOCKER_PORT=8080
    SPRING_DATASOURCE_URL=jdbc:postgresql://postgres_db:5432/scissors-game-service-docker-db
  ```
- From root directory start containers with `docker-compose up --build`
- To stop the containers run `docker compose down`
- 
# Nice To Have
- Add monitoring tools (Grafana, Prometheus)
- Add  integration(need to add testcontainers), performance(Jmetter) testing
- Rate limiting could be added to prevent the server from the high load
- For high user loads add horizontal scaling
- The application security: authentication and authorisation
