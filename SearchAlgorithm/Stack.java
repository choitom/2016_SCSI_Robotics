/*
	Author	: Tom Choi
	Date	: 08/02/2016
	Purpose	: Carleton College Summer Computer Science Institute (SCSI)
	
	Implementation of basic stack with FILO (First In, Last Out)
		- push		: add an item to the stack
		- popped	: remove and return the item pushed the last
		- clear		: clear the stack
		- traverse	: print the items in the stack from top to bottom
		- size		: return the size of the stack
*/

public class Stack{
	
	private Node current;
	private Node previous;
	private int size;
	
	public Stack(){
		this.current = null;
		this.previous = null;
		this.size = 0;
	}
	
	public Stack(Node node){
		this.current = node;
		this.previous = null;
		this.size = 0;
	}
	
	public void push(Node node){
		if(size == 0){
			current = node;
		}else{
			previous = current;
			current.setNext(node);
			current = node;
			current.setPrevious(previous);
		}
		size++;
	}
	
	public Node pop(){
		Node popped = null;
		if(size == 0){
			System.err.println("The stack is empty");
		}else if (size == 1){
			previous = null;
			popped = current;
			current = null;
		}else{
			previous = previous.getPrevious();
			popped = current;
			current = current.getPrevious();
		}
		
		if (size != 0){
			size--;
		}
		
		return popped;
	}
	
	public void traverse(){
		Node start = current;
		while(start != null){
			System.out.print(start.nodeLabel() + " ");
			start = start.getPrevious();
		}
		System.out.println("");		
	}

	public void clear(){
		current = null;
		previous = null;
		size = 0;
	}
	
	public int size(){
		return size;
	}
	
	/*
	public static void main(String[] args){
		Node a = new Node("a");
		Node b = new Node("b");
		Node c = new Node("c");
		Node d = new Node("d");
		
		Stack stack = new Stack();
		
		stack.push(a);
		stack.push(b);
		stack.push(c);
		stack.push(d);
		stack.traverse();
		System.out.println(stack.size());
		
		stack.pop();
		stack.traverse();
		System.out.println(stack.size());
		
		stack.pop();
		stack.traverse();
		System.out.println(stack.size());
		
		stack.pop();
		stack.traverse();
		System.out.println(stack.size());
		
		stack.pop();
		stack.traverse();
		System.out.println(stack.size());
		
		stack.pop();
		stack.traverse();
		System.out.println(stack.size());
	}*/
}