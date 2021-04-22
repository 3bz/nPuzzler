package solver;

import java.util.Comparator;

public class PuzzleComparator implements Comparator<PuzzleState>
{
	
	@Override
	public int compare(PuzzleState state1, PuzzleState state2) 
	{
		int result = state1.getEvaluationFunction() - state2.getEvaluationFunction();

		if (result != 0)
			return result;

		else
			return state1.PathFromParent.compareTo(state2.PathFromParent);
	}
}

