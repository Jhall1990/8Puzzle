import java.util.ArrayList;
import edu.princeton.cs.algs4.MinPQ;


public class Board {
    private final int[][] blocks;
    private final int moves;
    private int hamCount;
    private int manCount;
    private MinPQ q;

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

        this.moves = 0;
        getSearchCounts();
    }

    public int dimension() {
        // board dimension n
        return blocks.length;
    }

    public int hamming() {
        // number of blocks out of place
        return hamCount + moves;
    }

    public int manhattan() {
        // sum of Manhattan distances between blocks and goal
        return manCount + moves;
    }

    public boolean isGoal() {
        // is this board the goal board?
        return hamming() - moves == 0;
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
        }

        int copyBlockOrig = blocks[coords.get(0)[0]][coords.get(0)[1]];
        blocksCopy[coords.get(0)[0]][coords.get(0)[1]] = blocksCopy[coords.get(1)[0]][coords.get(1)[1]];
        blocksCopy[coords.get(0)[0]][coords.get(0)[1]] = copyBlockOrig;

        return new Board(blocksCopy);
    }

    public boolean equals(Object y) {
        // does this board equal y?
        if (y == null) {
            throw new IllegalArgumentException("y cannot be null");
        }
        return toString().equals(y.toString());
    }

    public Iterable<Board> neighbors() {
        // all neighboring boards
        ArrayList<Board> neighbors = new ArrayList<>();
        return neighbors;
    }

    public String toString() {
        // string representation of this board (in the output format specified below)
        StringBuilder returnString = new StringBuilder();

        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
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

    private int[] getSearchCounts() {
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
        return new int[]{0, 0};
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
}