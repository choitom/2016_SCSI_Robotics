/*
	Author : Tom Choi
	Date : 07/29/2016
	Purpose : Carleton College Summer Computer Science Institute (SCSI)
	
	Implementation of basic queue with FIFO (First In, First Out)
		- enqueue	: add an item to a queue in order
		- dequeue	: remove the first item in the queue
		- clear		: clear the queue
		- traverse	: print out the list of items in the queue
		- size	: get the size of the queue
*/

public class Queue{	
	
	private Node head;
	private Node current;
	private int size;
	
	public Queue(){
		this.head = null;
		this.current = null;
		this.size = 0;
	}
	
	public Queue(Node n){
		this.head = n;
		this.current = n;
		this.size = 1;
	}
	
	public void enqueue(Node n){
		if (head == null){
			head = n;
			current = head;
		}else{
			current.setNext(n);
			current = n;
		}
		size++;
	}
	
	public Node dequeue(){
		Node firstNode = head;
		
		if (firstNode == null){
			System.out.println("No node to dequeue.");
		}else{
			head = head.getNext();
			size--;
		}
		return firstNode;
	}
	
	public int size(){
		return size;
	}
	
	public void clear(){
		head = null;
		size = 0;
	}
	
	public void traverse(){
		Node temp = head;
		
		while(temp != null){
			System.out.print(temp.nodeLabel() + " ");
			temp = temp.getNext();
		}
		System.out.println("");
	}
	
	/*
	public static void main(String[] args){
		Queue q = new Queue();
		Node n1 = new Node("A");
		Node n2 = new Node("B");
		Node n3 = new Node("C");
		
		q.enqueue(n1);
		q.enqueue(n2);
		q.enqueue(n3);
		q.traverse(); // 5 4 3
		System.out.println("Queue size: " + q.getSize());
		
		q.dequeue();
		q.traverse(); // 4 3
		System.out.println("Queue size: " + q.getSize());
		
		q.dequeue();
		q.traverse(); // 3
		System.out.println("Queue size: " + q.getSize());
		q.dequeue();
		q.traverse(); // empty
		System.out.println("Queue size: " + q.getSize());
		
		q.dequeue();
		q.traverse(); // error
	}*/
}