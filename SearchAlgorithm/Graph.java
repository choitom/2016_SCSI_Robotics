import java.util.ArrayList;

/*
	Author : Tom Choi
	Date : 07/29/2016
	Purpose : Carleton College Summer Computer Science Institute (SCSI)
	
	Implementation of an abstract Graph class
	for graph search algorithms such as BFS and DFS
	
	Its primary functions are
		- initiate a graph
		- find a list of nodes connected to a given node
		- connectivity of two given nodes
		- index or location of a node in a graph
*/

public abstract class Graph{
	protected Node[] nodes;
	protected Node[] visitedList;
	protected int[][] edges;

	protected Node start;
	protected Node goal;
	
	public Graph(Node[] nodes, int[][] edges){
		this.nodes = nodes;
		this.edges = edges;
		this.visitedList = new Node[nodes.length];;
	}
	
	protected void setStart(Node s){
		this.start = s;
	}
	
	protected void setGoal(Node g){
		this.goal = g;
	}
	
	protected Node[] findAdjacentNodes(Node n){
		ArrayList<Node> connectionsBuffer = new ArrayList<Node>();
		int index = findIndex(n);
		
		// loop through the connection list and find edges connected
		for(int i = 0; i < edges[index].length; i++){
			if (edges[index][i] >= 1){
				connectionsBuffer.add(nodes[i]);
			}
		}
		
		Object[] objectList = connectionsBuffer.toArray();
		Node [] nodeList = new Node[objectList.length];
		
		for (int i = 0; i < objectList.length; i++){
			nodeList[i] = (Node)objectList[i];
		}
		
		return nodeList;
	}
		
	protected int findIndex(Node n){
		int index = -1;
		for (int i = 0; i < nodes.length; i++){
			if (nodes[i].nodeLabel().equals(n.nodeLabel())){
				index = i;
			}
		}
		return index;
	}
	
	protected boolean isConnected(Node a, Node b){
		return (edges[findIndex(a)][findIndex(b)] == 1) ? true : false;
	}
	
	/*
	Print out the nodes that were searched/visited in order
	*/
	protected void printSearchOrder(){
		for (int i = 0; i < visitedList.length; i++){
			System.out.print(visitedList[i].nodeLabel() + "(" +
			visitedList[i].getDistance() + ") ");
		}
		System.out.println("");
	}
	
	public abstract void traverse(Node start);
	public abstract void shortestPath(Node start, Node goal);
}