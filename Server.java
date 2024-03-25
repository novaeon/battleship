import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    // Server socket that accepts client and handles data flow.
    private ServerSocket serverSocket;
    private ClientHandler player1;
    private ClientHandler player2;
    private int[] guess;
    private int status;

    // List of client handlers
    private List<ClientHandler> clientHandlers = new ArrayList<>();

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() throws InterruptedException {
        try {
            while (!serverSocket.isClosed()) {

                if (clientHandlers.size() == 2) { // Size of the clientHandlers list is 2, so 2 players are connected, we can start the game.
                    System.out.println("Game is starting...");
                    startGame();
                }
                else {
                    System.out.println("Waiting... " + (2 - clientHandlers.size()) + " more player(s) needed to start the game."); // Waiting for 2 players to connect.
                }

                Socket socket = serverSocket.accept(); // returns a new socket object representing the connection and a variable holding the connection address of the client.
                ClientHandler clientHandler = new ClientHandler(socket);

                clientHandlers.add(clientHandler); // Adds the new client to the list

                Thread thread = new Thread(clientHandler); // Start a new thread (lane) for new connected client.
                thread.start(); // Starts the thread.
            }
        } catch (IOException e) { // This just has to be here otherwise java complains, basically says if some error happens close the socket because something is wrong.
            closeServerSocket();
        }
    }

    public void startGame() throws IOException, InterruptedException {
        player1 = clientHandlers.get(0); // Player 1 is the first client in the list
        player2 = clientHandlers.get(1); // Player 2 is the second client in the list

        Thread thread1 = new Thread(player1); // Start a new thread/lane for player 1
        thread1.start(); // Start the thread

        Thread thread2 = new Thread(player2); // Same for player 2
        thread2.start(); 

        // Player 1 and 2 pick their ships
        player1.pickShips();
        player2.pickShips();

        while (!(player1.shipsPlaced() && player2.shipsPlaced())) { // Wait for both players to place their ships
            Thread.sleep(1000); // Waits for 1000 milliseconds or 1 second
        }

        // After both players have picked their ships, the server sends a message to start the game
        player1.startGame(); 
        player2.startGame();

        while (!(player1.amReady() && player2.amReady())) { // Wait for both players to have sent the ready for turn to start message
            Thread.sleep(1000);
        }

        while (true) { // It's okay that this is an infinite loop because we will break out of it when a player has won.

            // Player 1's turn to guess
            player1.yourTurn();
            player2.notYourTurn();

            guess = player1.askForGuess(); // Get guess from player 1

            // 2 means win, 1 means a hit and 0 means a miss. 
            if ((status = player2.sendGuess(guess[0], guess[1])) == 2) { 
                player1.winner();
                System.exit(0);
            } else if (status == 1) {
                player1.turnSuccesful(true);
            } else if (status == 0) {
                player1.turnSuccesful(false);
            }

            // Wait for both players to be ready for the next turn
            player1.waitForTurnOver();
            player2.waitForTurnOver();

            while (!(player1.turnOver() && player2.turnOver())) { 
                Thread.sleep(1000);
            }

            // Now it's player 2's turn to guess
            player1.notYourTurn();
            player2.yourTurn();

            guess = player2.askForGuess(); // All this is identical to the code above, just for player 2

            if ((status = player1.sendGuess(guess[0], guess[1])) == 2) {
                player2.winner();
                System.exit(0);
            } else if (status == 1) {
                player2.turnSuccesful(true);
            } else if (status == 0) {
                player2.turnSuccesful(false);
            }

            while (!(player1.turnOver() && player2.turnOver())) {
                Thread.sleep(1000);
            }
        }

    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket socket = new ServerSocket(2007);
        Server server = new Server(socket);
        server.startServer();
    }

}