# Scissors-Game-Service
Rock  Paper Scissors Game Service Spring Demo Application

## Software Environment

- Java 21
- MySQL database
- Flyway migration
- Spring Boot 3.2.5
- Maven 3.9.5
- Jmeter
- The Room Java Library 2.1.0

## Software Structure
- feature_name(feed)
    - controller
    - service
    - enums
    - model
    - service

## Software Behaviour

This is a Spring Boot application to create a Rest Api for the Game Rock, Paper, Scissor. 
The api takes a parameter which is the move of player 1 (Rock, Scissor, Paper). 
The Spring Boot application has a random function top generates a random move for player2. 
Then it compares both move and returns if player1 wins, loses or tie.

1. Start the game and send the POST Request to the server side.

a. TIE
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

b. User Win
```
    POST http://localhost:8080/v1/api/game/start
    {
        "userChoice": "rock"
    }
```

RESPONSE
```
    {
        "message": "You chose: rock, Computer chose: scissors. Result: User win!"
    }
```

c. Computer Win
```
    POST http://localhost:8080/v1/api/game/start
    {
        "userChoice": "rock"
    }
```

RESPONSE
```
    {
        "message": "You chose: rock, Computer chose: paper. Result: Computer wins!"
    }
```

We can also put Paper or Scissor instead Rock which will act as player1 move.

## Software Setup and Run: Docker containers
- Create .env file from the root project with
  ```
    MYSQL_DATABASE=scissors-game-service-docker-db
    MYSQL_USER=api-user
    MYSQL_PASSWORD=api-password
    MYSQL_ROOT_PASSWORD=api-password
    MYSQL_LOCAL_PORT=13306
    MYSQL_DOCKER_PORT=3306
    SPRING_APP_LOCAL_PORT=8888
    SPRING_APP_DOCKER_PORT=8080
  ```
- From root directory start containers with `docker-compose up`
- To stop the containers run `docker compose down`

## Software Setup and Run: Local Application
- Download and install [Docker Desktop](https://www.docker.com/products/docker-desktop/) if you not have it installed in your machine
- Depends on which database we are going to use, should run:
    - local MySql       ```docker run -p 3306:3306 --name =scissors-game-service-dev-db -e MYSQL_DATABASE==scissors-game-service-dev-db -e MYSQL_ROOT_PASSWORD=root  -e MYSQL_USER=api-user -e MYSQL_PASSWORD=api-password -d mysql:latest```
- Run application with bach command from project root ./scripts/run.sh
- Run the application from the IDEA itself

## Software Design and Diagram
Please check the [Rss Tracker Service Design](design/scissors-game-service-draft-design-flow.drawio)

# TODO
- Add swagger support
- Add players game results in to the Database
- Add unit testing
