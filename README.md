# Battleship Game

This repository contains a simple implementation of the classic Battleship game. The game can be played between two players over a network, with one player acting as the server and the other as the client.

## Table of Contents

- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [How to Play](#how-to-play)
- [Classes and Files](#classes-and-files)
- [Contributing](#contributing)

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- An IDE or text editor (e.g., Visual Studio Code, IntelliJ IDEA)

### Running the Game

1. Clone the repository:
    ```sh
    git clone https://github.com/yourusername/battleship-game.git
    cd battleship-game
    ```

2. Compile the Java files:
    ```sh
    javac *.java
    ```

3. Start the server:
    ```sh
    java Main
    ```
    When prompted, enter `server`.

4. Start the client in a new terminal or on a different machine:
    ```sh
    java Main
    ```
    When prompted, enter `client`.

## Project Structure

```
BattleshipClient.java
ClientHandler.java
ConsoleProgram.java
Grid.java
Location.java
Main.java
Player.java
Server.java
Ship.java
```

## How to Play

1. The server waits for a client to connect.
2. Once connected, both players place their ships on their respective grids.
3. Players take turns guessing the location of the opponent's ships.
4. The game continues until one player sinks all of the opponent's ships.

## Classes and Files

- **[BattleshipClient.java](BattleshipClient.java)**: Handles the client-side logic of the game.
- **[ClientHandler.java](ClientHandler.java)**: Manages communication between the server and each client.
- **[ConsoleProgram.java](ConsoleProgram.java)**: Provides utility methods for console input and output.
- **[Grid.java](Grid.java)**: Represents the game grid and manages the status of each cell.
- **[Location.java](Location.java)**: Represents a single cell in the grid.
- **[Main.java](Main.java)**: Entry point for starting the server or client.
- **[Player.java](Player.java)**: Manages the player's ships and guesses.
- **[Server.java](Server.java)**: Handles server-side logic and manages the game flow.
- **[Ship.java](Ship.java)**: Represents a ship and its properties.

## Contributing

Contributions are welcome! Please open an issue or submit a pull request for any improvements or bug fixes.