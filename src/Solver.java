import edu.princeton.cs.algs4.MinPQ;

public class Solver {
    private Board initial;
    private MinPQ<Board> q;

    public Solver(Board initial) {
        // find a solution to the initial board (using the A* algorithm)
        this.initial = initial;
        int m = initial.manhattan();
        q = new MinPQ<>();
        q.insert(initial);
    }

    public boolean isSolvable() {
        // is the initial board solvable?
        return true;
    }

    public int moves() {
        // min number of moves to solve initial board; -1 if unsolvable
        return 0;
    }

    public Iterable<Board> solution() {
        // sequence of boards in a shortest solution; null if unsolvable
        return null;
    }

    public static void main(String[] args) {
        // solve a slider puzzle (given below)
    }
}