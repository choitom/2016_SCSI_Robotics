/*
	Author : Tom Choi
	Date : 08/02/2016
	Purpose : Carleton College Summer Computer Science Institute (SCSI)
	
	Crude implementation of DFS and the shortest path from
	the source to the target based on the DFS search tree
*/

public class DFS extends Graph{
	
	private Stack stack;
	
	public DFS(Node[] nodes, int[][] edges){
		super(nodes, edges);
		stack = new Stack();
	}
	
	/*
		Traverse the tree in depth-first search order 
		and print the search result
	*/
	public void traverse(Node start){
		traverseHelper(start);
		printSearchOrder();
	}
	
	/*
		Does not necessarily find the shortest path from the source to target
		It finds the shortest path based on the depth-first search order
	*/
	public void shortestPath(Node start, Node goal){
		traverse(start);
		setGoal(goal);
		shortestPathHelper();
	}
	
	private void traverseHelper(Node s){
		setStart(s);
		start.setVisited(true);
		start.setDistance(0);
		
		// push the start node
		stack.push(s);
		
		Node current = null;
		Node[] adjacentNodes;
		
		int i;
		int distance = 0;
		int visitedIndex = 0;
		
		// while not every node is visited
		while(stack.size() != 0){
			// pop the top node in the stack
			current = stack.pop();
			distance = current.getDistance() + 1;
			
			visitedList[visitedIndex++] = current;
			
			// find its adjacent nodes and push them
			adjacentNodes = findAdjacentNodes(current);
			
			// for each adjacent node, push the ones that are not visited
			for(i = 0; i < adjacentNodes.length; i++){
				if(!adjacentNodes[i].getVisited()){
					stack.push(adjacentNodes[i]);
					adjacentNodes[i].setVisited(true);
					adjacentNodes[i].setDistance(distance);
				}
			}
		}
	}
	
	private void shortestPathHelper(){
		Queue shortestPath = new Queue();
		
		int i;
		int distance = 0;
		
		for(i = 0; i < visitedList.length; i++){
			Node n = visitedList[i];
			if(n.getDistance() == distance){
				shortestPath.enqueue(n);
				distance++;
				if (n.nodeLabel().equals(goal.nodeLabel())){
					break;
				}
				
			}
		}
		
		while(shortestPath.size() != 0){
			System.out.print(shortestPath.dequeue().nodeLabel() + " ");
		}
		System.out.println("");
	}
	
	
	public static void main(String[] args){
		Node a = new Node("a");
		Node b = new Node("b");
		Node c = new Node("c");
		Node d = new Node("d");
		Node e = new Node("e");
		Node f = new Node("f");
		Node g = new Node("g");
		Node h = new Node("h");
		Node i = new Node("i");
		
		Node[] nodes = {a, b, c, d, e, f, g, h, i};
		int[][] edges =
			{{0, 1, 1, 0, 0, 0, 1, 0, 0},
			 {1, 0, 1, 1, 1, 0, 0, 1, 0},
			 {1, 1, 0, 0, 1, 1, 0, 0, 0},
			 {0, 1, 0, 0, 0, 0, 0, 0, 1},
			 {0, 1, 1, 0, 0, 1, 0, 0, 0},
			 {0, 0, 1, 0, 1, 0, 0, 0, 0},
			 {1, 0, 0, 0, 0, 0, 0, 0, 1},
			 {0, 1, 0, 0, 0, 0, 0, 0, 1},
			 {0, 0, 0, 1, 0, 0, 1, 0, 0}};
		
		DFS dfs = new DFS(nodes, edges);
		//dfs.traverse(a);
		dfs.shortestPath(d, f);
	}
}