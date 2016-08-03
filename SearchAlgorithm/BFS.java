import java.util.ArrayList;

/*
	Author	: Tom Choi
	Date	: 07/31/2016
	Purpose : Carleton College Summer Computer Science Institute (SCSI)
	
	Crude implementation of BFS and its shortest path algorithm
*/

public class BFS extends Graph{
	
	private ArrayList<ArrayList<Node>> tree;
	private Queue queue;
	
	public BFS(Node[] nodes, int[][] edges){
		super(nodes, edges);
		tree = null;
		queue = new Queue();
	}
	
	/*
		Traverse the graph in breath-first algorithm
	*/
	public void traverse(Node start){
		traverseHelper(start);
		printSearchOrder();
	}
	
	/*
		If not BFS traversed before, traverse the graph
		then, find the shortest path to the goal
	*/
	public void shortestPath(Node start, Node goal){
		traverse(start);
		setGoal(goal);
		shortestPathHelper();
	}
	
	/*
		Visit every adjacent node using queue
		Keep the distance from the start to each node
	*/
	private void traverseHelper(Node s){
		setStart(s);
		start.setVisited(true);
		start.setDistance(0);
		
		// enqueue the start node
		queue.enqueue(start);
		
		Node current = null;
		Node[] adjacentNodes;
		
		int i;
		int distance;
		int visitedIndex = 0;
		
		// while not every node is visited
		while(queue.size() != 0){
			// dequeue a node from the queue
			current = queue.dequeue();
			distance = current.getDistance() + 1;
			
			visitedList[visitedIndex++] = current;
			
			// find its adjacent nodes and enqueue them
			adjacentNodes = findAdjacentNodes(current);
			
			// for each adjacent node, enqueue the ones that are not visited
			for(i = 0; i < adjacentNodes.length; i++){
				if (!adjacentNodes[i].getVisited()){
					queue.enqueue(adjacentNodes[i]);
					adjacentNodes[i].setVisited(true);
					adjacentNodes[i].setDistance(distance);
				}
			}
		}
	}
	
	/*
		Make layers of a tree based on the distance from the start
		Backtrace the path from the goal to the start
	*/
	private void shortestPathHelper(){
		generateTree();
		backTrace();
	}
	
	
	/*
		Find the shortest path from the goal to the start in the tree
	*/
	private void backTrace(){
		ArrayList<Node> shortestPath = new ArrayList<Node>();
		ArrayList<Node> upperLevel;
		
		Node current = goal;
		shortestPath.add(current);
		
		int height = current.getDistance() - 1;
		
		do{
			upperLevel = tree.get(height);
			
			for(int i = 0; i < upperLevel.size(); i++){
				Node node = upperLevel.get(i);
				if(isConnected(node, current)){
					current = node;
					shortestPath.add(current);
					height = current.getDistance() - 1;
					break;
				}
			}
		}while(height >= 0);
			
		for(int i = shortestPath.size()-1; i >= 0; i--){
			System.out.print(shortestPath.get(i).nodeLabel() + " ");
		}System.out.println("");
	}
			
	/*
		Put nodes into each list in accordance with their distance from the start
	*/
	private void generateTree(){
		tree = new ArrayList<ArrayList<Node>>();
		ArrayList<Node> currentHeight = new ArrayList<Node>();
		
		int index = 0;
		int currentLevel = 0;
		int maxLevel = visitedList[visitedList.length - 1].getDistance();
		Node currentNode;
		
		while(index < visitedList.length){
			currentNode = visitedList[index++];
			
			// If no change in distance, keep storing a node
			if (currentNode.getDistance() == currentLevel){
				currentHeight.add(currentNode);
			}
			
			// If the distance changed, add the layer that has been
			// storing nodes to the tree and create a new one
			else{
				tree.add(currentHeight);
				currentHeight = new ArrayList<Node>();
				currentHeight.add(currentNode);
				currentLevel = currentNode.getDistance();
			}
		}
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
			{{0,1,0,0,0,0,0,0,0},
			 {1,0,1,0,0,0,0,0,0},
			 {0,1,0,1,0,0,0,0,0},
			 {0,0,1,0,1,1,0,0,0},
			 {0,0,0,1,0,0,0,0,0},
			 {0,0,0,1,0,0,1,0,0},
			 {0,0,0,0,0,1,0,1,0},
			 {0,0,0,0,0,0,1,0,1},
			 {0,0,0,0,0,0,0,1,0}};
		
		BFS bfs = new BFS(nodes, edges);
		//bfs.traverse(d);
		bfs.shortestPath(a, i);
	}
}