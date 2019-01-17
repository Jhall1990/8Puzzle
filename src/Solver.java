import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class Solver {
    private boolean solvable;
    private int moves;
    private ArrayList<Board> solution;

    public Solver(Board initial) {
        // find a solution to the initial board (using the A* algorithm)
        solvable = false;

        if (initial.isGoal())
            solvable = true;
        else {
            moves = 0;
            Board twin = initial.twin();

            MinPQ<SearchNode> origQ = new MinPQ<>();
            MinPQ<SearchNode> twinQ = new MinPQ<>();
            solution = new ArrayList<>();
            solution.add(initial);

            origQ.insert(new SearchNode(initial, moves));
            twinQ.insert(new SearchNode(twin, moves));

            while (!origQ.isEmpty() && !twinQ.isEmpty()) {
                moves++; // can't just increment, only ++ when next level in tree.

                Board origCur = origQ.delMin().board;
                Board twinCur = twinQ.delMin().board;
                solution.add(origCur);

                if (origCur.isGoal()) {
                    solvable = true;
                    break;
                }
                if (twinCur.isGoal()) {
                    break;
                }

                for (Board neighbor : origCur.neighbors()) {
                    if (!origCur.equals(neighbor))
                        origQ.insert(new SearchNode(neighbor, moves));
                }

                for (Board neighbor : twinCur.neighbors()) {
                    if (!twinCur.equals(neighbor))
                        twinQ.insert(new SearchNode(neighbor, moves));
                }
            }
        }
    }

    public boolean isSolvable() {
        // is the initial board solvable?
        return solvable;
    }

    public int moves() {
        // min number of moves to solve initial board; -1 if unsolvable
        if (solvable) {
            return moves;
        }
        return -1;
    }

    public Iterable<Board> solution() {
        // sequence of boards in a shortest solution; null if unsolvable
        if (solvable) {
            return solution;
        }
        return null;
    }

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int man;
        private final int moves;

        SearchNode(Board board, int moves) {
            this.board = board;
            this.man = board.manhattan();
            this.moves = moves;
        }

        public int compareTo(SearchNode that) {
            if (this.man + moves < that.man + moves)
                return -1;
            else if (this.man + moves > that.man + moves)
                return 1;
            else if (this.moves < that.moves)
                return -1;
            else if (this.moves > that.moves)
                return 1;
            return 0;
        }

    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }


}