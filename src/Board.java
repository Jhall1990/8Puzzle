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
        return hamming() == 0;
    }

    public Board twin() {
        // a board that is obtained by exchanging any pair of blocks
        int[][] blocksCopy = new int[dimension()][dimension()];
        for (int i = 0; i < dimension(); i++) {
            blocksCopy[i] = blocks[i].clone();
        }

        ArrayList<Integer[]> coords = new ArrayList<>();

        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (blocksCopy[i][j] != 0)
                    coords.add(new Integer[]{i, j});
                if (coords.size() > 1)
                    break;
            }
            if (coords.size() > 1)
                break;
        }

        int copyBlockOrig = blocks[coords.get(0)[0]][coords.get(0)[1]];
        blocksCopy[coords.get(0)[0]][coords.get(0)[1]] = blocksCopy[coords.get(1)[0]][coords.get(1)[1]];
        blocksCopy[coords.get(1)[0]][coords.get(1)[1]] = copyBlockOrig;

        return new Board(blocksCopy);
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
        ArrayList<Board> neighbors = new ArrayList<>();
        int[] emptySpace = getEmptySpace();

        if (emptySpace.length != 0) {
            // move up
            if (emptySpace[0] != 0) {
                int movedVal = blocks[emptySpace[0] - 1][emptySpace[1]];
                blocks[emptySpace[0]][emptySpace[1]] = movedVal;
                blocks[emptySpace[0] - 1][emptySpace[1]] = 0;
                neighbors.add(new Board(blocks));
                blocks[emptySpace[0]][emptySpace[1]] = 0;
                blocks[emptySpace[0] - 1][emptySpace[1]] = movedVal;
            }

            // move down
            if (emptySpace[0] != dimension() - 1) {
                int movedVal = blocks[emptySpace[0] + 1][emptySpace[1]];
                blocks[emptySpace[0]][emptySpace[1]] = movedVal;
                blocks[emptySpace[0] + 1][emptySpace[1]] = 0;
                neighbors.add(new Board(blocks));
                blocks[emptySpace[0]][emptySpace[1]] = 0;
                blocks[emptySpace[0] + 1][emptySpace[1]] = movedVal;
            }

            // move left
            if (emptySpace[1] != 0) {
                int movedVal = blocks[emptySpace[0]][emptySpace[1] - 1];
                blocks[emptySpace[0]][emptySpace[1]] = movedVal;
                blocks[emptySpace[0]][emptySpace[1] - 1] = 0;
                neighbors.add(new Board(blocks));
                blocks[emptySpace[0]][emptySpace[1]] = 0;
                blocks[emptySpace[0]][emptySpace[1] - 1] = movedVal;
            }

            // move right
            if (emptySpace[1] != dimension() - 1) {
                int movedVal = blocks[emptySpace[0]][emptySpace[1] + 1];
                blocks[emptySpace[0]][emptySpace[1]] = movedVal;
                blocks[emptySpace[0]][emptySpace[1] + 1] = 0;
                neighbors.add(new Board(blocks));
                blocks[emptySpace[0]][emptySpace[1]] = 0;
                blocks[emptySpace[0]][emptySpace[1] + 1] = movedVal;
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
        // Returns null if no empty space is found.

        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (blocks[i][j] == 0) {
                    return new int[] {i, j};
                }
            }
        }
        return new int[0];
    }
}