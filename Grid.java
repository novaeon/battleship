public class Grid {
    // Write your Grid class here
    private Location[][] grid;
    public static final int UNGUESSED = 0;
    public static final int HIT = 1;
    public static final int MISSED = 2;
    public static char[] letters = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J' };

    // Constants for number of rows and columns.
    public static final int NUM_ROWS = 10;
    public static final int NUM_COLS = 10;

    // Create a new Grid. Initialize each Location in the grid
    // to be a new Location object.
    public Grid() {
        grid = new Location[NUM_ROWS][NUM_COLS];

        for (int i = 0; i < NUM_ROWS; i++) {
            for (int a = 0; a < NUM_COLS; a++) {
                grid[i][a] = new Location();
            }
        }

    }

    // Mark a hit in this location by calling the markHit method
    // on the Location object.
    public void markHit(int row, int col) {
        grid[row][col].markHit();
    }

    // Mark a miss on this location.
    public void markMiss(int row, int col) {
        grid[row][col].markMiss();
    }

    // Set the status of this location object.
    public void setStatus(int row, int col, int status) {
        grid[row][col].setStatus(status);
    }

    // Get the status of this location in the grid
    public int getStatus(int row, int col) {
        return grid[row][col].getStatus();
    }

    // Return whether or not this Location has already been guessed.
    public boolean alreadyGuessed(int row, int col) {
        if (grid[row][col].getStatus() == UNGUESSED) {
            return false;
        }
        return true;
    }

    // Set whether or not there is a ship at this location to the val
    public void setShip(int row, int col, boolean val) {
        grid[row][col].setShip(val);
    }

    // Return whether or not there is a ship here
    public boolean hasShip(int row, int col) {
        if (grid[row][col].hasShip()) {
            return true;
        }

        return false;
    }

    // Get the Location object at this row and column position
    public Location get(int row, int col) {
        return grid[row][col];
    }

    // Return the number of rows in the Grid
    public int numRows() {
        return NUM_ROWS;
    }

    // Return the number of columns in the grid
    public int numCols() {
        return NUM_COLS;
    }

    // Radar Grid
    public void printStatus() {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        for (int i = 0; i < NUM_ROWS; i++) {
            System.out.print(letters[i] + " ");
            for (int a = 0; a < NUM_COLS; a++) {
                if (grid[i][a].hasShip() && grid[i][a].getStatus() == HIT) {
                    System.out.print("X ");
                }
                else if (grid[i][a].hasShip() && grid[i][a].getStatus() == UNGUESSED) {
                    System.out.print("# ");
                }
                else if (grid[i][a].getStatus() == MISSED) {
                    System.out.print("O ");
                }
                else {
                    System.out.print("- ");
                }

            }
            System.out.println();
        }

    }

    public void printMyGuesses() {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        for (int i = 0; i < NUM_ROWS; i++) {
            System.out.print(letters[i] + " ");
            for (int a = 0; a < NUM_COLS; a++) {
                if (grid[i][a].getStatus() == HIT) {
                    System.out.print("X ");
                }
                else if (grid[i][a].getStatus() == MISSED) {
                    System.out.print("O ");
                }
                else {
                    System.out.print("- ");
                }

            }
            System.out.println();
        }

    }

    // My Grid
    public void printShips() {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        for (int i = 0; i < NUM_ROWS; i++) {
            System.out.print(letters[i] + " ");

            for (int a = 0; a < NUM_COLS; a++) {
                if (hasShip(i, a)) {
                    System.out.print("X ");
                }

                else {
                    System.out.print("- ");
                }

            }
            System.out.println();
        }
    }

    public void addShip(Ship s) {
        int row = s.getRow();
        int col = s.getCol();
        int length = s.getLength();
        int direction = s.getDirection();
        int HORIZONTAL = 0;
        int VERTICAL = 1;

        if (direction == HORIZONTAL) {
            for (int i = col; i < col + length; i++) {
                setShip(row, i, true);
            }
        }

        else if (direction == VERTICAL) {
            for (int i = row; i < row + length; i++) {
                setShip(i, col, true);
            }
        }
    }

}