import java.util.Scanner;


public class Ship
{
    private static final int UNSET = -1;
    private static final int HORIZONTAL = 0;
    private static final int VERTICAL = 1;
    
    private int row = UNSET;
    private int col = UNSET;
    private int length = UNSET;
    private int direction = UNSET;
    Scanner sc = new Scanner(System.in);

public Ship(int length){
    this.length = length;
   
}

//Has the location been initialized?
public boolean isLocationSet(){
   if(row == UNSET || col == UNSET){
       return false;
}
        return true;
    
}


// Has the direction been initialized?
public boolean isDirectionSet(){
    if(direction == UNSET){
        return false;
    }
        return true;
}

// Set the location of the ship
public void setLocation(int row, int col){
    this.row = row;
    this.col = col;
   
}

// Set the direction of the ship
public void setDirection(int direction){
    this.direction = direction;
}

// Getter for the row value
public int getRow(){
    return row;
}

// Getter for the column value
public int getCol(){
    return col;
}

// Getter for the length of the ship
public int getLength(){
    return length;
}

// Getter for the direction
public int getDirection(){
    return direction;
}

// Helper method to get a string value from the direction
private String directionToString(){
    if(direction == HORIZONTAL){
        return "horizontal";

    }
    else if(direction == VERTICAL){
        return "vertical";
    }
    
    return "unset";
}

// Helper method to get a (row, col) string value from the location
private String locationToString(){
    if(isLocationSet()){
    return "(" + row + ", " + col + ")";
    }
    
    return "(unset location)";
}

// toString value for this Ship
public String toString(){
    if(direction == UNSET){
    return directionToString() + " direction ship of length " + length + " at " + locationToString();
    }
    return directionToString() + " ship of length " + length + " at " + locationToString();
}

}