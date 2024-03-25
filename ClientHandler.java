import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String messageFromClient;
    private boolean shipsPlaced = false;
    private boolean turnOver = false;
    private boolean ready = false;

    public void startGame() {
        sendMessage("startgame");
    }

    public void yourTurn() {
            sendMessage("yourturn");
    }

    public void notYourTurn() {
        sendMessage("notyourturn");
    }

    public void turnSuccesful(boolean success) {
        if (success) {
            sendMessage("hit");
        } else {
            sendMessage("miss");
        }
    }

    public void winner() {
        sendMessage("win");
    }

    public void yourTurn(boolean turn) {
        if (turn) {
            sendMessage("yourturn");
        } else {
            sendMessage("notyourturn");
        }
    }
    
    public void pickShips() {
        sendMessage("pickships");
    }

    public boolean turnOver() {
        return turnOver;
    }

    public int[] askForGuess() throws IOException {
        while (socket.isConnected()) { 
                messageFromClient = bufferedReader.readLine();
                if (messageFromClient.startsWith("guess")) { break; } // wait until we get a message that starts with "guess"
        }
        String[] parts = messageFromClient.split(" "); // split the message into parts to get the row and col
        int row = Integer.parseInt(parts[1]); // the row is the second part of the message, 1 because it starts at 0
        int col = Integer.parseInt(parts[2]); // the row is the third part of the message
        return new int[]{row, col};
    }

    public void waitForReady() throws IOException {
        while (socket.isConnected()) {  
            messageFromClient = bufferedReader.readLine();
            if (messageFromClient.equals("ready")) { break; } // wait until we get a message that says "ready"
        }
        ready = true; // set ready to true
    }

    public void waitForTurnOver() throws IOException {
        while (socket.isConnected()) { 
            messageFromClient = bufferedReader.readLine();
            if (messageFromClient.equals("turnover")) { break; } // wait until we get a message that says "turnover"
        }
        turnOver = true;
    }

    public int sendGuess(int row, int col) throws IOException {
        sendMessage("guess " + row + " " + col); // send the guess to the opponent
        while (socket.isConnected()) {
            messageFromClient = bufferedReader.readLine(); 
            if (messageFromClient.equalsIgnoreCase("hit") || messageFromClient.equalsIgnoreCase("miss") || messageFromClient.equalsIgnoreCase("win")) { break; } // wait until we get a message that says "hit", "miss" or "win"
        }
        if (messageFromClient.equalsIgnoreCase("win")) {
            return 2; // return 2 if the player has won 
        } else if (messageFromClient.equalsIgnoreCase("hit")) {
            return 1; // return 1 if the player has hit a ship
        } else {
            return 0; // return 0 if the player has missed
        }
    }

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            closeAll(socket, bufferedReader, bufferedWriter); // Close this client handler.
        }
    }

    @Override
    public void run() {
        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                if (messageFromClient.equals("shipsplaced")) {
                        shipsPlaced = true;
                        break;
                } else if (messageFromClient.equals("ready")) {
                        ready = true;
                        break;    
                } else if (messageFromClient.equals("turnover")) {
                        turnOver = true;
                        break;  
                } else if (messageFromClient.equals("disconnect")) {
                    closeAll(socket, bufferedReader, bufferedWriter);
                    break;        
                } else {
                        System.out.println("Unhandled message:" + messageFromClient);
                        break;
                }
            } catch (IOException e) {
                closeAll(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void sendMessage(String messageToSend) {
        try {
            this.bufferedWriter.write(messageToSend);
            this.bufferedWriter.newLine();
            this.bufferedWriter.flush();
        } catch (IOException e) {
            closeAll(socket, bufferedReader, bufferedWriter);
        }
    }

    public boolean shipsPlaced() {
        return shipsPlaced;
    }

    public boolean amReady() {
        return ready;
    }

    public void closeAll(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}