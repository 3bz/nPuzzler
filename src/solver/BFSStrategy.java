package solver;
import java.util.*;

public class BFSStrategy extends SearchMethod {
	
	public BFSStrategy()
	{
		code = "BFS";
		longName = "Breadth-First Search";
		Frontier = new LinkedList<PuzzleState>();
		Searched = new LinkedList<PuzzleState>();
	}
	
	protected PuzzleState popFrontier()
	{
		//remove an item from the fringe to be searched
		PuzzleState thisState = Frontier.pop();
		//Add it to the list of searched states, so that it isn't searched again
		Searched.add(thisState);
		
		return thisState;
	}
	
	@Override
	public direction[] Solve(nPuzzle puzzle) {
		//This method uses the fringe as a queue.
		//Therefore, nodes are searched in order of cost, with the lowest cost
		// unexplored node searched next.
		//-----------------------------------------
		
		//put the start state in the Fringe to get explored.
		addToFrontier(puzzle.StartState);
		
		
		ArrayList<PuzzleState> newStates = new ArrayList<PuzzleState>();
				
		while(Frontier.size() > 0)
		{
			//get the next item off the fringe
			PuzzleState thisState = popFrontier();
			
			//is it the goal item?
			if(thisState.equals(puzzle.GoalState))
			{
				//We have found a solution! return it!
				return thisState.GetPathToState();
			}
			else
			{
				//This isn't the goal, just explore the node
				newStates = thisState.explore();

				//Add newStates in order of Direction values, organised to suit our need
				for (direction d : direction.values()) {
					//BFS uses a FIFO queue
					for (PuzzleState p : newStates) {
						//Empty cell move order: UP, LEFT, DOWN, RIGHT
						if(p.PathFromParent.equals(d))
							addToFrontier(p);
					}
				}
			}
		}
		
		//No solution found and we've run out of nodes to search
		//return null.
		return null;
	}
	
	public boolean addToFrontier(PuzzleState aState)
	{
		//if this state has been found before,
		if(Searched.contains(aState) || Frontier.contains(aState))
		{
			//discard it
			return false;
		}
		else
		{
			//else put this item on the end of the queue;
			Frontier.addLast(aState);
			return true;
		}
	}

}
