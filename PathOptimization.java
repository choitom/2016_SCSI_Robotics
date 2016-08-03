/*
Implementation of path optimazation of left-hand maze solving problem

Inspired by the post 'Maze Solving Robot' by patrickmccb
from http://www.instructables.com/id/Maze-Solving-Robot/

Implemented by Tom Choi on 07/20/2016 for
Carleton College Summer Computer Science Institute program
*/

public class PathOptimization{
	
	// the original path that the robot has taken
	private char[] originalPath;
	
	// the shortest path in the maze
	private char[] optPath;
	
	// path conversion factor
	public final char LBR = 'B';
	public final char LBS = 'R';
	public final char RBL = 'B';
	public final char SBL = 'R';
	public final char SBS = 'B';
	public final char LBL = 'S';
	
	
	
	/**************** PRIVATE METHODS ****************/
	/*
		Print out an error message
		and exit the program
	*/
	private void quit(String message){
		System.out.println(message);
		return;
	}
	
	/*
		Find and return the first index of back command(B)
		If it doesn't exist, return -1
	*/
	private int getFirstBIndex(char[] optimizablePath){
		int BIndex = -1;
		for(int i = 0; i < optimizablePath.length; i++){
			if(optimizablePath[i] == ('B')){
				BIndex = i;
				break;
			}
		}
		
		return BIndex;
	}
	
	/*
		Return the optimized path
		given a sequence of optimizable instructions
	*/
	private char fetchOptimizedPortion(String optimizablePortion){
		if (optimizablePortion.equals("LBR") || optimizablePortion.equals("RBL") ||
			optimizablePortion.equals("SBS")){
			return 'B';
		}else if (optimizablePortion.equals("LBS") || optimizablePortion.equals("SBL")){
			return 'R';
		}else{
			return 'S';
		}
	}
	
	
	/*
		While no more B left in the command array
		Locate B and substitute to one of conversion factors
		Then, return the optimized path
	*/
	private void optimizeHelper(){
		char[] optimizablePath = this.originalPath;
		
		StringBuilder sb = new StringBuilder();
		String optimizablePortion = "";
		char optimizedPortion = ' ';
		
		boolean isBLeft = false;
		int BIndex = getFirstBIndex(optimizablePath);
		
		if (BIndex == -1){
			isBLeft = true;
		}else if (BIndex + 1 > optimizablePath.length || BIndex - 1 < 0){
				quit("There is an error in an array index");
		}
		
		while(!isBLeft){
			
			// find the string portion to optimized
			optimizablePortion = sb.append(optimizablePath[BIndex - 1]).append(optimizablePath[BIndex]).
									append(optimizablePath[BIndex + 1]).toString();
			
			optimizedPortion = fetchOptimizedPortion(optimizablePortion);
			
			// re-set the string builder
			sb.delete(0, sb.length());
			
			// replace the optimizablePortion with optimizedPortion
			optimizablePath = updateOptimization(optimizablePath, optimizedPortion, BIndex);
			
			BIndex = getFirstBIndex(optimizablePath);
			
			if (BIndex == -1){
				isBLeft = true;
			}else if (BIndex + 1 > optimizablePath.length || BIndex - 1 < 0){
				quit("There is an error in an array index");
			}
		}
		optPath = optimizablePath;
	}
	
	/*
		Substitute a series of redundant command with
		its corresponding conversion factor and return
		the updated command list
	*/
	private char[] updateOptimization(char[] optimizablePath, char optimizedPortion, int BIndex){
		char[] updated = new char[optimizablePath.length - 2];
		int index = BIndex;
		int upperBoundary = index + 2;
		try{
			if (index -1 == 0){
				updated[0] = optimizedPortion;
				for (int i = 1; i < updated.length; i++){
					updated[i] = optimizablePath[upperBoundary++];
				}
			}else if (upperBoundary == optimizablePath.length){
				for (int i = 0; i < (BIndex - 1); i++){
					updated[i] = optimizablePath[i];
				}
				updated[updated.length-1] = optimizedPortion;
			}else{
				for (int i = 0; i < (BIndex - 1); i++){
					updated[i] = optimizablePath[i];
				}
				
				updated[BIndex-1] = optimizedPortion;
				
				for (int i = BIndex; i < updated.length; i++){
					updated[i] = optimizablePath[upperBoundary++];
				}
			}
		}catch(IndexOutOfBoundsException e){
			e.printStackTrace();
		}

		return updated;
	}
	
	
	/**************** PUBLIC METHODS ****************/
	public PathOptimization(String path){
		this.originalPath = path.toCharArray();
	}
	
	public char[] getPath(){
		return originalPath;
	}
	
	public char[] getOptPath(){
		return optPath;
	}
	
	public void setPath(String path){
		this.originalPath = path.toCharArray();
	}
	
	/*
		See if the path can be optimized by looking at its length
	*/
	public boolean isLengthOptimizable(){
		return (this.originalPath.length > 2) ? true : false;
	}
	
	/*
		See if there is any optimizable command in the path
	*/
	public boolean isSymbolOptimizable(){
		boolean isOpt = false;
		
		for (int i = 0; i < originalPath.length; i++){
			if (originalPath[i] == ('B')){
				isOpt = true;
				break;
			}
		}
		return isOpt;
	}
	
	/*
		Finds the shortest path
	*/
	public void optimize(){
		if (!isLengthOptimizable()){
			quit("The path cannot be optimized any further");
		}
		if (!isSymbolOptimizable()){
			quit("The path cannot be optimized any further");
		}
				
		optimizeHelper();
	}
	
	
	public static void main(String[] args){
		// sample path 1
		PathOptimization po = new PathOptimization("LLLBLLLRBLLBSRSRS");
		po.optimize();
		char[] c = po.getOptPath();
		System.out.println(new String(c));
		
		// sample path 2
		po.setPath("LBLLBSR");
		po.optimize();
		c = po.getOptPath();
		System.out.println(new String(c));
	}
}