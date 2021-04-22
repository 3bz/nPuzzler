package solver;
import java.io.*;
import java.util.*;

/**
 * nPuzzler --- main class/initiate search methods/read problem file/etc.
 * @author    COS30019
 */
class nPuzzler
{
	
	//the number of methods programmed into nPuzzler
	public static final int METHOD_COUNT = 4;
	public static nPuzzle gPuzzle;
	public static SearchMethod[] lMethods;
	
	public static void main(String[] args)
	{
		//Create method objects
		InitMethods();
		
		//args contains:
		//  [0] - filename containing puzzle(s)
		//  [1] - method name
		
		if(args.length < 2) {
			System.out.println("Usage: nPuzzler <filename> <search-method>.");
			System.exit(1);			
		}
		
		//Get the puzzle from the file / Start and End states in 2d array forms
		gPuzzle = readProblemFile(args[0]);
		
		String method = args[1];
		SearchMethod thisMethod = null;
		
		//determine which method the user wants to use to solve the puzzles
		for(int i = 0; i < METHOD_COUNT; i++)
		{
			//do they want this one?
			if(lMethods[i].code.compareTo(method) == 0)
			{
				//yes, use this method.
				thisMethod = lMethods[i];
			}
		}
		
		//Has the method been implemented?
		if(thisMethod == null)
		{
			//No, give an error
			System.out.println("Search method identified by " + method + " not implemented. Methods are case sensitive.");
			//todo methods should not be case sensitive - remove user error by using ToUpper
			System.exit(1);
		}
		
		//Solve the puzzle, using the method that the user chose
		direction[] thisSolution = thisMethod.Solve(gPuzzle);
		
		//Print information about this solution
		//todo string concat neatly
		System.out.println(args[0] + "   " + method + "   " + thisMethod.Frontier.size());
		if(thisSolution == null)
		{
			//No solution found :(
			System.out.println("No solution found.");
		}
		else
		{
			//We found a solution, print all the steps to success!
			for(int j = 0; j < thisSolution.length; j++)
			{
				System.out.print(thisSolution[j].toString() + ";");
			}
			System.out.println();
		}
		//Reset the search method for next use.
		thisMethod.reset();
		System.exit(0);
	}
	
	private static void InitMethods()
	{
		lMethods = new SearchMethod[METHOD_COUNT];
		lMethods[0] = new BFSStrategy();
		lMethods[1] = new GreedyBestFirstStrategy();
		lMethods[2] = new DFSStrategy();
		lMethods[3] = new AStarStrategy();
		//todo only create what is needed at runtime
	}
	
	private static nPuzzle readProblemFile(String fileName) // this allow only one puzzle to be specified in a problem file 
	{
		
		try
		{
			//create file reading objects
			FileReader reader = new FileReader(fileName);
			BufferedReader puzzle = new BufferedReader(reader);
			nPuzzle result;
			
			String puzzleDimension = puzzle.readLine();
			//split the string by letter "x"
			String[] bothDimensions = puzzleDimension.split("x");
		
			//work out the "physical" size of the puzzle
			//MxN is Rows by Columns, so we consider the Y value first
			int puzzleY = Integer.parseInt(bothDimensions[0]);
			int puzzleX = Integer.parseInt(bothDimensions[1]);

			int[][] startPuzzleGrid = new int[puzzleX][puzzleY];
			int[][] goalPuzzleGrid = new int[puzzleX][puzzleY];
			
			//fill in the start state
			String startStateString = puzzle.readLine();
			startPuzzleGrid = ParseStateString(startStateString, startPuzzleGrid, puzzleX, puzzleY);

			//fill in the end state
			String goalStateString = puzzle.readLine();
			goalPuzzleGrid = ParseStateString(goalStateString, goalPuzzleGrid, puzzleX, puzzleY);
			
			//create the nPuzzle object...
			result = new nPuzzle(startPuzzleGrid, goalPuzzleGrid);
						
			puzzle.close();
			return result;
		}
		catch(FileNotFoundException ex)
		{
			//The file didn't exist, show an error
			System.out.println("Error: File \"" + fileName + "\" not found.");
			System.out.println("Please check the path to the file.");
			System.exit(1);
		}
		catch(IOException ex)
		{
			//There was an IO error, show and error message
			System.out.println("Error in reading \"" + fileName + "\". Try closing it and programs that may be accessing it.");
			System.out.println("If you're accessing this file over a network, try making a local copy.");
			System.exit(1);
		}
		catch(InvalidPuzzleException ex) {
			System.out.println("FIle input does not match required format\n"+
					"Ensure the number of tiles fits the dimensions");
		}

		//this code should be unreachable. This statement is simply to satisfy Eclipse.
		return null;
	}
	
	private static int[][] ParseStateString(String stateString, int[][] puzzleGrid, int puzzleX, int puzzleY) throws InvalidPuzzleException {
		//Parse state string converts the text file's format for each puzzle into
		// multidimensional arrays.
		
		//split the string by spaces
		String[] tileLocations = stateString.split(" ");

		//ensure formatting is correct
		if(!isValidPuzzleInput(tileLocations, puzzleX*puzzleY))
			throw new InvalidPuzzleException();

		// the top-left corner of the puzzle has a coordinate of [0,0]
		int x = 0;	
		int y = 0;
		
		for(int i = 0; i < tileLocations.length; i++)
		{
			//tileLocations[i] holds the (i + 1)th tile
			int tileNumber = Integer.parseInt(tileLocations[i]);
			
			//now, check the location of this tile
			//todo ensure puzzle value cannot exceed inputted puzzle size (3x3 board with a '10')
			if (x >= puzzleX) {
				//reset x to 0 and go to next row (increase y by 1)
				x = 0;
				y++;
			}
			
			puzzleGrid[x][y] = tileNumber;
			x++;
		}
		
		return puzzleGrid;
	}

	private static Boolean isValidPuzzleInput(String[] tiles, int gridSize){
		boolean isValid = true;
		for(String tile : tiles) {
			if(Integer.parseInt(tile) >= gridSize) {
				isValid = false;
				break;
			}
		}
		return (isValid && tiles.length == gridSize);
	}
}