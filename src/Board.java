import java.util.ArrayList;


public class Board {
    private final int[][] blocks;
    private int hamCount;
    private int manCount;

    public Board(int[][] blocks) {
        // construct a board from an n-by-n array of blocks
        // (where blocks[i][j] = block in row i, column j)
        if (blocks == null) {
            throw new IllegalArgumentException("blocks must not be null");
        }

        // Make a copy of the passed blocks array.
        this.blocks = new int[blocks.length][blocks.length];

        for (int i = 0; i < blocks.length; i++) {
            this.blocks[i] = blocks[i].clone();
        }

        // Update ham and man counts.
        getSearchCounts();

    }

    public int dimension() {
        // board dimension n
        return blocks.length;
    }

    public int hamming() {
        // number of blocks out of place
        return hamCount;
    }

    public int manhattan() {
        // sum of Manhattan distances between blocks and goal
        return manCount;
    }

    public boolean isGoal() {
        // is this board the goal board?
        // If hamming is equal to zero we know that all numbers are in the
        // correct cell and therefore the puzzle is solved.
        return hamming() == 0;
    }

    public Board twin() {
        // a board that is obtained by exchanging any pair of blocks

        // Make an array list to store the first two pairs of coords that do not
        // contain the empty space.
        ArrayList<Integer[]> coords = new ArrayList<>();

        // Iterate over each cell and add the cell if it does not contain 0.
        // Once two pairs of coords have been added break out of the loops.
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (blocks[i][j] != 0)
                    coords.add(new Integer[]{i, j});
                if (coords.size() > 1)
                    break;
            }
            if (coords.size() > 1)
                break;
        }

        // Grab the coords and create a new board with two coords swapped.
        int row = coords.get(0)[0];
        int col = coords.get(0)[1];
        int swapRow = coords.get(1)[0];
        int swapCol = coords.get(1)[1];

        return swapAndCreate(row, col, swapRow, swapCol);
    }

    public boolean equals(Object y) {
        // does this board equal y?
        // If y is null raise return false.
        if (y == null) {
            return false;
        }

        // Check if y and this are the same object, return true if they are.
        if (y == this)
            return true;

        // Check if y is the same class as this, if they aren't return false.
        if (y.getClass() != this.getClass())
            return false;

        // Cast the passed object to a Board.
        Board that = (Board) y;

        // Check the board dimensions, if they are different return false.
        if (that.dimension() != this.dimension())
            return false;

        // Check each space in the two boards, if they are ever not the same return false.
        // Otherwise return true after all are checked.
        for (int i = 0; i < that.dimension(); i++) {
            for (int j = 0; j < that.dimension(); j++) {
                if (that.blocks[i][j] != this.blocks[i][j])
                    return false;
            }
        }

        return true;
    }

    public Iterable<Board> neighbors() {
        // all neighboring boards

        // Create an array list to store the neighbors.
        ArrayList<Board> neighbors = new ArrayList<>();

        // Find the coords of the empty space.
        int[] emptySpace = getEmptySpace();

        // Move swap the empty space with up, down, left, or right, depending on
        // the empty space's position.
        if (emptySpace.length != 0) {
            int row = emptySpace[0];
            int col = emptySpace[1];

            // move up
            if (emptySpace[0] != 0) {
                neighbors.add(swapAndCreate(row, col, row - 1, col));
            }
            // move down
            if (emptySpace[0] != dimension() - 1) {
                neighbors.add(swapAndCreate(row, col, row + 1, col));
            }
            // move left
            if (emptySpace[1] != 0) {
                neighbors.add(swapAndCreate(row, col, row, col - 1));
            }
            // move right
            if (emptySpace[1] != dimension() - 1) {
                neighbors.add(swapAndCreate(row, col, row, col + 1));
            }
        }

        return neighbors;
    }

    public String toString() {
        // string representation of this board (in the output format specified below)
        StringBuilder returnString = new StringBuilder();

        returnString.append(dimension());
        returnString.append("\n");

        for (int i = 0; i < dimension(); i++) {
            returnString.append(" ");
            for (int j = 0; j < dimension(); j++) {
                if (blocks[i][j] < 10)
                    returnString.append(" ");
                returnString.append(blocks[i][j]);
                if (j != dimension() - 1) {
                    returnString.append(" ");
                } else {
                    returnString.append("\n");
                }
            }
        }
        return returnString.toString();
    }

    private void getSearchCounts() {
        // Iterate over the blocks arrays and get the hamming and manhattan counts.
        // Hamming is the number of spaces that are in the incorrect spot.
        // Manhattan is based on how far from the correct position the current value is.
        int expectedVal = 1;

        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (blocks[i][j] > 0) {
                    if (blocks[i][j] != expectedVal) {
                        hamCount++;
                        getManhattanCount(i, j);
                    }
                }
                expectedVal++;
            }
        }
    }

    private void getManhattanCount(int row, int col) {
        // Gets the total number of spaces away from the goal space for a given value.
        int val = blocks[row][col] - 1;
        int correctRow = val / dimension();
        int correctCol = val;

        if (correctRow > 0)
            correctCol = val % (correctRow * dimension());

        int manNum = Math.abs(correctRow - row) + Math.abs(correctCol - col);

        manCount += manNum;
    }

    private int[] getEmptySpace() {
        // Checks the current blocks array to find the empty space.
        // Returns an array of ints where int[0] = x and int[1] = y.
        // Returns an empty array if no empty space is found.

        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (blocks[i][j] == 0) {
                    return new int[] {i, j};
                }
            }
        }
        return new int[0];
    }

    private Board swapAndCreate(int row, int col, int swapRow, int swapCol) {
        // Store the original value at row/col.
        int origVal = blocks[row][col];

        // Set row/col equal to the swap row/col.
        blocks[row][col] = blocks[swapRow][swapCol];

        // Set the swap row/col equal to the original value.
        blocks[swapRow][swapCol] = origVal;

        // Create a new board with the swapped cells.
        Board b = new Board(blocks);

        // Restore blocks to its original state.
        blocks[swapRow][swapCol] = blocks[row][col];
        blocks[row][col] = origVal;

        return b;
    }
}