import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayDeque;
import java.util.Deque;


public class Solver {
    private boolean solvable = false;
    private int moves;
    private SearchNode solution;
    private final Iterable<Board> solutionIter;

    public Solver(Board initial) {
        // find a solution to the initial board (using the A* algorithm)
        if (initial == null) {
            throw new IllegalArgumentException("initial board cannot be null.");
        }

        if (initial.isGoal()) {
            solvable = true;
            solution = new SearchNode(null, initial);
        } else {
            Board twin = initial.twin();

            MinPQ<SearchNode> origQ = new MinPQ<>();
            MinPQ<SearchNode> twinQ = new MinPQ<>();

            origQ.insert(new SearchNode(null, initial));
            twinQ.insert(new SearchNode(null, twin));

            while (!origQ.isEmpty() && !twinQ.isEmpty()) {
                SearchNode origCur = origQ.delMin();
                SearchNode twinCur = twinQ.delMin();

                if (origCur.board.isGoal()) {
                    solvable = true;
                    solution = origCur;
                    break;
                }
                if (twinCur.board.isGoal()) {
                    break;
                }

                for (Board neighbor : origCur.board.neighbors()) {
                    if (origCur.parent == null || !origCur.parent.board.equals(neighbor))
                        origQ.insert(new SearchNode(origCur, neighbor));
                }

                for (Board neighbor : twinCur.board.neighbors()) {
                    if (twinCur.parent == null || !twinCur.parent.board.equals(neighbor))
                        twinQ.insert(new SearchNode(twinCur, neighbor));
                }
            }
        }

        solutionIter = solution();

    }

    public boolean isSolvable() {
        // is the initial board solvable?
        return solvable;
    }

    public int moves() {
        // min number of moves to solve initial board; -1 if unsolvable
        if (solvable)
            return moves;
        else
            return -1;
    }

    public Iterable<Board> solution() {
        // sequence of boards in a shortest solution; null if unsolvable
        if (solvable) {
            if (solutionIter == null) {
                Deque<Board> solutionBoards = new ArrayDeque<>();
                solutionBoards.push(solution.board);

                while (solution.parent != null) {
                    solutionBoards.push(solution.parent.board);
                    solution = solution.parent;
                    moves++;
                }
                return solutionBoards;
            }
            return solutionIter;
        }
        return null;
    }

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int man;
        private final int moves;
        private final SearchNode parent;

        SearchNode(SearchNode parent, Board board) {
            this.parent = parent;
            this.board = board;

            if (parent == null)
                this.moves = 0;
            else
                this.moves = parent.moves + 1;

            this.man = board.manhattan() + moves;
        }

        public int compareTo(SearchNode that) {
            if (this.man < that.man)
                return -1;
            else if (this.man > that.man)
                return 1;
            else if (this.board.hamming() < that.board.hamming())
                return -1;
            else if (this.board.hamming() > that.board.hamming())
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

