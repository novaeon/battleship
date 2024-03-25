import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class BattleshipClient extends ConsoleProgram {
    // Local Variables
    private static final int UNSET = -1;
    private static final int HORIZONTAL = 0;
    private static final int VERTICAL = 1;
    public static final int UNGUESSED = 0;
    public static final int HIT = 1;
    public static final int MISSED = 2;
    String dInput = "";
    private int row;
    private int col;
    private boolean myturn;

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String message;

    Scanner sc = new Scanner(System.in); // New Scanner
    Random random = new Random(); // New Randomizer
    ArrayList<String> letters = new ArrayList<String>();
    ArrayList<Integer> numbers = new ArrayList<Integer>();
    private static final int NUM_SHIPS = 5;
    public static final int[] SHIP_LENGTHS = { 2, 3, 3, 4, 5 };
    public static int TOTAL_SHIPS = Arrays.stream(SHIP_LENGTHS).sum(); //The total number of ships i.e. 17

    public BattleshipClient() throws InterruptedException {
        for (char c = 'a'; c <= 'j'; c++) {
            letters.add(String.valueOf(c));
        }

        for (int i = 1; i <= 10; i++) {
            numbers.add(i);
        }

        try {
            socket = new Socket("localhost", 2007);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("Connected to server.");
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Start of RUN

    public void run() throws IOException, InterruptedException {

        Player person = new Player();
    
        System.out.println("Waiting for second player to join...");

        while ((message = bufferedReader.readLine()) != null) { // Wait for pickships to start picking ships
            if (message.equals("pickships")) {
                break;
            }
        }

        placePlayerShips(person); // Place the ships

        sendServerMessage("shipsplaced");

         // Wait for opponent to place ships
         System.out.println("Waiting for your opponent to place their ships...");

        while ((message = bufferedReader.readLine()) != null) { // Wait for startgame to start the game
            if (message.equals("startgame")) {
                break; //break out of the waiting loop
            }
        }

        System.out.println("Your opponent has placed their ships. The game is starting.");

        while (true) {
            sendServerMessage("ready"); // Tell the server that you are ready for the turn to start
            while ((message = bufferedReader.readLine()) != null) { // wait to be assigned either yourturn or notyourturn from the server
                if (message.equals("yourturn")) {
                    myturn = true;
                    break;
                } else if (message.equals("notyourturn")) {
                    myturn = false;
                    break;
                }
            }

            if (myturn) { // if its my turn then get a guess and send it to the server
                System.out.println("Your turn to guess.");
                askForGuess();
                sendServerMessage("guess " + row + " " + col); // send a guess in the format "guess row col"
                while ((message = bufferedReader.readLine()) != null) {
                    if (message.equals("hit")) {
                        System.out.println("You hit your opponent's ship!");
                        person.recordMyGuess(row, col, true);
                        break;
                    } else if (message.equals("miss")) {
                        System.out.println("You missed your opponent's ship :(");
                        person.recordMyGuess(row, col, false);
                        break;
                    } else if (message.equals("win")) {
                        System.out.println("You win!");
                        System.exit(0); // Exit the game
                    }
                }
                System.out.println("----- Your guesses: -----");
                person.printMyGuesses();
                sendServerMessage("turnover"); // Tell the server that your turn is over

            } else { // if its not my turn then wait for the opponent to guess
                System.out.println("Your opponent is guessing.");
                while ((message = bufferedReader.readLine()) != null) {
                    if (message.startsWith("guess")) { // if the message is a guess from the opponent
                        String[] parts = message.split(" "); // split the message into parts to get the row and col
                        row = Integer.parseInt(parts[1]); // the row is the second part of the message, 1 because it starts at 0
                        col = Integer.parseInt(parts[2]);// the row is the third part of the message
                        if (person.myGrid().hasShip(row, col)) {
                            person.recordOpponentGuess(row, col, true); // record the guess as true (hit)
                            if (person.opponentScore() == TOTAL_SHIPS) { // if the opponent has hit all the ships
                                sendServerMessage("win"); // send a message to the server that your opponent has won
                                System.out.println("You lose!"); 
                                System.exit(0); // exit the game
                            } else {
                                sendServerMessage("hit"); // if the score is not equal to the total ships then send a hit message
                            }
                        } else {
                            sendServerMessage("miss"); // if the guess doesnt have a ship then send a miss message
                            person.recordOpponentGuess(row, col, false); // record the guess as false (miss)
                        }
                    }
                    System.out.println("----- Your grid: -----"); // print the grid after the guess
                    person.printMyGrid();
                    sendServerMessage("turnover"); // tell the server that the turn is over
                    break; // break out of the while loop
                }

            }
        }

    } // End of RUN

    private void sendServerMessage(String message){
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void placePlayerShips(Player person) {
        System.out.println("First you need to chose the location of your ships.");
        for (int i = 0; i < NUM_SHIPS; i++) {
            System.out.println("Your current grid of ships.");
            person.printMyShips();

            System.out.println("Ship length: " + SHIP_LENGTHS[i]);
            Ship s = new Ship(SHIP_LENGTHS[i]); // THIS IS WHERE THE SHIP IS INITIALIZED
            askForShips(person, s);

        }
    }

    // Collects row and col player is guessing + asks for direction
    public void askForShips(Player p, Ship s) {
        int row;
        int col;
        int direction = UNSET;
        dInput = "";

        do {
            row = askForRow();
            col = askForCol();
            direction = askForDir();

            s.setDirection(direction);
            s.setLocation(row, col);

        } while (!shipFits(s) || shipIntersects(p, s));

        p.chooseShipLocation(s, row, col, direction);
    }

    private int askForDir() {
        int direction = UNSET;
        String dInput = "";

        while (!dInput.toLowerCase().equals("h") && !dInput.toLowerCase().equals("v")) {
            System.out.println("Horizontal or vertical?");
            dInput = sc.next();
        }

        if (dInput.toLowerCase().equals("h")) {
            direction = HORIZONTAL;
        } else {
            direction = VERTICAL;
        }
        return direction;
    }

    private int askForRow() {
        String rowLetter = "";
        while (!letters.contains(rowLetter.toLowerCase())) {
            System.out.println("Which row? (A-J)");
            rowLetter = sc.next();
            try {
                row = numbers.get(letters.indexOf(rowLetter.toLowerCase())) - 1;
            } catch (Exception e) {
                System.out.println("Invalid row. Try again.");
            }
        }
        return row;
    }

    private int askForCol() {
        int col = -1;
        while (col < 0 || col > 9) {
            System.out.println("Which column? (1-10)");
            try {
                col = sc.nextInt() - 1;
                if (col < 0 || col > 9) {System.out.println("Invalid column. Try again.");}
            } catch (Exception e) {
                System.out.println("Invalid column. Try again.");
                sc.next();
            }
        }
        return col;
    }

    public void askForGuess() {
        row = askForRow();
        col = askForCol();
    }

    public boolean shipFits(Ship s) {
        if (s.getDirection() == HORIZONTAL) {
            if ((s.getCol() + s.getLength() - 1) < 10) {
                return true;
            }

            else {
                return false;
            }
        }

        else {
            if ((s.getRow() + s.getLength() - 1) < 10) {
                return true;
            }

            else {
                return false;
            }
        }
    }

    public boolean shipIntersects(Player p, Ship s) {
        int statusCheck = 0;

        if (s.getDirection() == HORIZONTAL) {
            for (int i = s.getCol(); i < s.getCol() + s.getLength(); i++) {
                if (!p.myGrid().hasShip(s.getRow(), i)) {
                    statusCheck++;
                }

            }
        }

        else {
            for (int i = s.getRow(); i < s.getRow() + s.getLength(); i++) {
                if (!p.myGrid().hasShip(i, s.getCol())) {
                    statusCheck++;
                }

            }
        }

        if (statusCheck == s.getLength()) {
            return false;
        } else {
            return true;
        }
    }

}
