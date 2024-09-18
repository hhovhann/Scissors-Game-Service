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
    - filter
    - model
    - repository
    - service
      - cache
      - game
      - generator
      - user
    - util

## Software Behaviour

This is a Spring Boot web application to create a Rest Api for the Game Rock, Paper, Scissor.
Supported Features:
 - Start: The `POST /v1/api/game/start` endpoint allows a game to be started by specifying the userMove as a request body parameter (rock | )
 - Terminate: The `PATCH /terminate/{game_id}` endpoint allows a game to be terminated by specifying its ID. If the game is not found or already terminated, an error message is returned.
 - Reset: The `DELETE /v1/api/game/reset/{user_id}`  endpoint allows resetting all game data
 - Statistics: The `/v1/api/game/statistics/{user_id}` endpoint returns the game data statistics

## Software Manual Testing
- Testing the Game with Authentication:
  -You can now test the following scenarios:
  - Register a User: Send a POST /register request with a username and password.
    ```
      curl -X POST http://localhost:8080/register \
           -H "Content-Type: application/json" \
           -d '{"username": "user123", "password": "password123"}'
    ```
    ```
      {
        "userId": "1982c718-d8df-44fe-a01e-188a501d00e5",
        "username": "hhovhann",
        "email": "haik.hovhannisyan@gmail.com"
      }
    ```
- Authenticate a User: Send a POST /authenticate request with valid credentials to receive a JWT token.
    ```
    curl -X POST http://localhost:8080/authenticate \
         -H "Content-Type: application/json" \
         -d '{"username": "user123", "password": "password123"}'
    ```
    ```eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoaG92aGFubiIsImlhdCI6MTcyNjYwNjYzOCwiZXhwIjoxNzI2NjQyNjM4fQ.OT2hT41R2Opi4dZA0uOrHK0OZKqGg-Iq2511sqZozLI```
- Play a Game: Send a POST /game/play request with the user move (e.g., "rock", "paper", "scissors") along with the JWT token in the Authorization header.
    ```
    curl -X POST http://localhost:8080/v1/api/game/start \
         -H "Content-Type: application/json" \
         -d '{"userId": "user123", "userMove": "rock"}'
    ```
  - Terminate Game: Send a PATCH /v1/api/game/terminate/{game_id} request with the game id along with the JWT token in the Authorization header.
    ```
    curl -X PATCH http://localhost:8080/v1/api/game/terminate/cb99d696-1a8a-4692-a3f6-43b6530e7b7e \
         -H "Content-Type: application/json"
    ```
  - Reset a Game: Send a DELETE /v1/api/game/reset/{user_id} request with the user id along with the JWT token in the Authorization header.
    ```
    DELETE /v1/api/game/reset/{user_id}
    ```
  - Get a Game Statistics:  Send a GET /v1/api/game/statistics/{user_id} request with the user id with the JWT token in the Authorization header.
    ```
    curl -X GET http://localhost:8080/v1/api/game/statistics/user123 \
         -H "Content-Type: application/json"
    ```
    ```
    {
    "WIN": 0,
    "LOST": 0,
    "DRAW": 0
    }
    ```
    
The api takes a parameter which is the move of user1, user2, userN (Rock, Scissor, Paper). 
The Spring Boot application has a random function top generates a random move for player2. 
Then it compares both move and returns if user1, user2, userN wins, loses or draw.

## Software Design
Please check the [Rss Tracker Service Design](design/scissors-game-service-draft-design-flow.drawio)

## Software Documentation
- Open Api/Swagger Support - http://localhost:8080/swagger-ui/index.html

We can also put Paper or Scissor instead Rock which will act as player1 move.

## Software Setup and Run: Local Application
- Download and install [Docker Desktop](https://www.docker.com/products/docker-desktop/) if you not have it installed in your machine
- Depends on which database we are going to use, should run:
    - local PostgreSQL  ```docker run --name scissors-game-service-dev-db -e POSTGRES_USER=api-username -e POSTGRES_PASSWORD=api-password -e POSTGRES_DB=scissors-game-service-dev-db -p 5432:5432 -d postgres:latest```
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
    GRAFANA_LOCAL_PORT=3000
    GRAFANA_DOCKER_PORT=3000
    PROMETHEUS_LOCAL_PORT=9090
    PROMETHEUS_DOCKER_PORT=9090
  ```
- From root directory start containers with `docker-compose up --build`
- To stop the containers run `docker compose down`
- 
# Nice To Have
- Add  integration (need to add testcontainers), performance(Jmetter) testing
- Rate limiting could be added to prevent the server from the high load
- For high user loads add horizontal scaling
