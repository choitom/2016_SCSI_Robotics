import java.util.ArrayList;

/*
	Author	: Tom Choi
	Date	: 08/03/2016
	Purpose	: Carleton College Summer Computer Science Institute (SCSI)
	
	Implementation of Dijsktra's shortest path algorithm
		1. traverse 
			- prints out the shortest distance from the source to each node
		2. shortestPath
			- prints out the shortest path and distance from the source to the target
*/

public class Dijsktra extends Graph{
	
	private int[] distanceArray;
	
	public Dijsktra(Node[] nodes, int[][] edges){
		super(nodes, edges);
		distanceArray = new int[nodes.length];
	}
	
	/*
		Find and print the shortest distance from the source to each node in the graph
	*/
	public void traverse(Node s){
		setStart(s);
		initDistanceArray();
		
		// find the index of the start node and set its distance to 0
		int index = findIndex(start);
		distanceArray[index] = 0;
		
		// factor out variables to prevent redundant initializations
		int i;
		int j;
		int minIndex;
		int updatedWeight;
		
		for(i = 0; i < distanceArray.length - 1; i++){
			// find the index of the min node and set it visited
			minIndex = minDistance();
			Node minNode = nodes[minIndex];
			minNode.setVisited(true);	
			
			Node[] adjacent = findAdjacentNodes(minNode);
			
			// for each adjacent node
			for(j = 0; j < adjacent.length; j++){
				Node n = adjacent[j];
				index = findIndex(n);
				updatedWeight = distanceArray[minIndex] + edges[minIndex][index];
				index = findIndex(adjacent[j]);
				
				// if the total weight of path from the start to the node
				// is less than the current weight of the node,
				// update its weight and set its previous node for back-tracking
				if(	distanceArray[index] > updatedWeight){
					distanceArray[index] = updatedWeight;
					n.setPrevious(minNode);
				}
			}
		}
		printShortestDistance();
	}
	
	/*
		Find and print the shortest path from the start to the goal
	*/
	public void shortestPath(Node s, Node goal){
		traverse(s);
		findPathToGoal(goal);
	}
	
	/*
		Find the distance from the start to the goal and
		Back traces the path from the goal to the start
	*/
	private void findPathToGoal(Node goal){
		int index = findIndex(goal);
		int shortestDistance = distanceArray[index];
		
		ArrayList<String> path = new ArrayList<String>();
		Node current = goal;
		
		while(current != null){
			path.add(current.nodeLabel());
			current = current.getPrevious();
		}
		
		System.out.print("The shortest path from \'" + start.nodeLabel() + "\' to \'" +
							goal.nodeLabel() + "\' is: ");
		for(int i = path.size()-1; i >= 0; i--){
			System.out.print(path.get(i) + " ");
		}System.out.println("\nAnd the distance to the goal is: " + shortestDistance);
	}
	
	private void printShortestDistance(){
		int i;
		for(i = 0; i < distanceArray.length; i++){
			if(distanceArray[i] == 0){
				System.out.println(nodes[i].nodeLabel() + "(source):" + distanceArray[i]);
			}else{
				System.out.println(nodes[i].nodeLabel() + ":" + distanceArray[i]);
			}
		}
	}
	
	/*
		Find the unvisited node that has the minimum distance from the visited nodes
	*/
	private int minDistance(){
		int min = Integer.MAX_VALUE;
		int minIndex = -1;
		int i;
		
		for(i = 0; i < distanceArray.length; i++){
			if (distanceArray[i] <= min && nodes[i].getVisited() == false){
				min = distanceArray[i];
				minIndex = i;
			}
		}
		return minIndex;
	}
	
	/*
		Initialize all distances with infinite numbers
	*/
	private void initDistanceArray(){
		int i;
		for(i = 0; i < distanceArray.length; i++){
			distanceArray[i] = Integer.MAX_VALUE;
		}
	}
	
	public static void main(String[] args){
		String[] nodeNames = {"a","b","c","d","e","f","g","h","i"};
		Node[] nodes = new Node[nodeNames.length];
		
		// generating nodes
		int i;
		for(i = 0; i < nodeNames.length; i++){
			nodes[i] = new Node(nodeNames[i]);
		}
		
		// weighted edges
		int[][] edges = {{0, 4, 0, 0, 0, 0, 0, 8, 0},
						{4, 0, 8, 0, 0, 0, 0, 11, 0},
						{0, 8, 0, 7, 0, 4, 0, 0, 2},
						{0, 0, 7, 0, 9, 14, 0, 0, 0},
						{0, 0, 0, 9, 0, 10, 0, 0, 0},
						{0, 0, 4, 0, 10, 0, 2, 0, 0},
						{0, 0, 0, 14, 0, 2, 0, 1, 6},
						{8, 11, 0, 0, 0, 0, 1, 0, 7},
						{0, 0, 2, 0, 0, 0, 6, 7, 0}};
		
		Dijsktra dijsktra = new Dijsktra(nodes, edges);
		dijsktra.shortestPath(nodes[0], nodes[4]);
	}
}