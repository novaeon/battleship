public class Player
{
    private static final int[] SHIP_LENGTHS = {2, 3, 3, 4, 5};
    private static final int NUM_SHIPS = 5;
    private int opponentScore = 0;
    public static final int lengthSum = 17;
   

    
    private Grid myGrid;
    private Grid opponentGrid;
    private Ship[] myShips;
    private int shipCount;

    
 
    
    public Player(){
    myShips = new Ship[SHIP_LENGTHS.length];
    myGrid = new Grid();
    opponentGrid = new Grid();
    shipCount = 0;
    
    for(int i = 0; i < NUM_SHIPS; i++){
       myShips[i] = new Ship(SHIP_LENGTHS[i]);
       //Adds 5 ships with different sizes
    }
        
    }
    
    public void chooseShipLocation(Ship s, int row, int col, int direction){
        
        if(shipCount < NUM_SHIPS){
        s.setDirection(direction);
        s.setLocation(row, col);
        myGrid.addShip(s);
        shipCount++;
        }
      
    }

    public Grid myGrid(){
        return myGrid;
    }
    
    public int opponentScore(){
        return opponentScore;
    }
    
    // Print your ships on the grid
    public void printMyShips(){
        myGrid.printShips();
    }

    // Print opponent guesses
    public void printMyGuesses(){
        opponentGrid.printMyGuesses();
    }

    // Print your guesses
    public void printMyGrid(){
        myGrid.printStatus();
    }
    
    //The winning condition is if you have hit every battleship location.

   public boolean isOpponentWinner(){
       if(opponentScore == lengthSum){
        return true;
     }
       return false;
    } 

    public int returnOpponentScore(){
        return opponentScore;
    }

    // Record a guess from the opponent
    public void recordOpponentGuess(int row, int col, boolean hit){
        if(hit){
            myGrid.markHit(row, col);
            opponentScore++;
        }
        else{
            myGrid.markMiss(row, col);
        }
        
    }

    public void recordMyGuess(int row, int col, boolean hit){
        if(hit){
            opponentGrid.markHit(row, col);
        }
        
        else{
            opponentGrid.markMiss(row, col);
        }
        
    }
    
    
     
    }