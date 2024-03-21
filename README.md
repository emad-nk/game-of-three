# Game of Three
## Implementation Details

The service is a hybrid of HTTP and Websocket.

Game updates are sent via websocket (pusher.com service) to the UI. This is to prevent players keep polling to check if it's their turn to play.
There are 2 types of websocket messages. One is to inform the other player that a player has joined the game and the other one to inform that the other player has moved/played.

Starting and moving in the game is done via HTTP POST requests.

This hybrid mechanism has made the implementation easier.


## Endpoints

Here is the Swagger documentation:

[Swagger endpoint](http://localhost:8080/swagger-ui/index.html)

Endpoint ``/starts`` does two things, either it starts a new game or when a 2nd player wants to start a game he/she automatically joins an existing game which is in `WAITING` state that has been already created by another player.
Ideally it would be better to have an extra endpoint to `join` an existing game or create a new game, however for simplicity and due to time constraints that has been skipped.

## Tests

As many tests as possible have been tried to be put, however most important aspects of the code are being covered by the tests, and the coverage is not 100% due to time constraint.

Integration tests have the naming convention to end with `*IT.java` and unit tests have the naming convention to end with `*Test.java`.

## Starting the application

Initially, execute `./start-deps.sh` to initiate the dependencies. Subsequently, invoke `StartApplication` in the **test** package to launch the application with the `local` profile.

When running tests, ensure that the docker dependencies are active by executing `./start-deps.sh`.

To terminate the dependencies, use the command `./stop-deps.sh`.

## Tech Stack

- Spring boot 3.x
- Java 17
- Postgres
- Redis
- docker
- pusher.com (external Websocket service)

## Future Development

- Sending metrics of failures/successes
- More test coverage
- Workflow diagram
- Handling when a player gets disconnected
- Players can have statistics of their wins/loses
- Security for authenticating players
