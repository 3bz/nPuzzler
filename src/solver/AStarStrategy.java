package solver;

import java.util.LinkedList;

public class AStarStrategy extends SearchMethod {

    public AStarStrategy()
    {
        code = "A*";
        longName = "A-Star Search";
        Frontier = new LinkedList<PuzzleState>();
        Searched = new LinkedList<PuzzleState>();
    }


    @Override
    public direction[] Solve(nPuzzle aPuzzle) {
        return new direction[0];
    }

    @Override
    public boolean addToFrontier(PuzzleState aState) {
        return false;
    }

    @Override
    protected PuzzleState popFrontier() {
        return null;
    }
}
