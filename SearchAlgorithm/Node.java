/*
	Author : Tom Choi
	Date : 07/29/2016
	Purpose : Carleton College Summer Computer Science Institute (SCSI)
	
	Implementation of a node with
		- label
		- double link (previous, next)
		- distance from the source node in a graph
		- whether the node is visited or not while traversing
*/

public class Node{
	private Node next;
	private Node previous;
	private String label;
	private int distance;
	private boolean visited;
	
	public Node(String s){
		initVars(s, null, null);
	}
	
	public Node(String s, Node next){
		initVars(s, next, null);
	}
	
	public Node(String s, Node next, Node previous){
		initVars(s, next, previous);
	}
	
	private void initVars(String s, Node next, Node previous){
		this.label = s;
		this.previous = previous;
		this.next = next;
		this.visited = false;
		this.distance = 0;
	}
	
	public void setNext(Node node){
		this.next = node;
	}
	
	public void setPrevious(Node node){
		this.previous = node;
	}
	
	public void setVisited(boolean visited){
		this.visited = visited;
	}
	
	public void setDistance(int distance){
		this.distance = distance;
	}
	
	public Node getNext(){
		return this.next;
	}
	
	public Node getPrevious(){
		return this.previous;
	}
	
	public int getDistance(){
		return this.distance;
	}
	
	public boolean getVisited(){
		return this.visited;
	}
	public String nodeLabel(){
		return this.label;
	}
}