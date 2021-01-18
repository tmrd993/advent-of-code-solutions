package myutils16;

public class DoublyLinkedList {
    private Node head;
    private Node tail;
    private int size;

    public int size() {
	return size;
    }

    public boolean isEmpty() {
	return size == 0;
    }

    public static class Node {
	public int data;
	public Node next;
	public Node previous;

	public Node(int value) {
	    data = value;
	}
    }

    public DoublyLinkedList() {
	head = null;
	tail = null;
	size = 0;
    }
    
    public int get(int index) {
	Node curr = head;
	for(int i = 0; i < index; i++) {
	    curr = curr.next;
	}
	return curr.data;
    }
    
    public Node getNode(int index) {
   	Node curr = head;
   	for(int i = 0; i < index; i++) {
   	    curr = curr.next;
   	}
   	return curr;
       }

    // adds a new node at the current position by moving the (old) current node
    // to the right
    public void addToCurrentPos(Node current, int value) {
	if (size == 0) {
	    addLast(value);
	} else {
	    if (current == head) {
		head = new Node(value);
		head.previous = null;
		head.next = current;
		current = head;
		size++;
	    } else if (current == tail) {
		current = new Node(value);
		current.next = tail;
		current.previous = tail.previous;
		tail.previous.next = current;
		tail.previous = current;
		size++;
	    } else {
		Node tmp = current;
		current = new Node(value);
		current.next = tmp;
		current.previous = tmp.previous;
		tmp.previous = current;
		current.previous.next = current;
		size++;

	    }
	}
    }

    public void removeNode(Node current) {
	if (size == 1) {
	    head = null;
	    tail = null;
	    size--;
	} else {
	    if (current == head) {
		head = head.next;
		head.previous = null;
		size--;
	    } else if (current == tail) {
		tail = tail.previous;
		tail.next = null;
		size--;
	    } else {
		current.previous.next = current.next;
		current.next.previous = current.previous;
		size--;
	    }
	}
    }

    // adds a new node to the back of the list
    public void addLast(int value) {
	if (size == 0) {
	    head = new Node(value);
	    tail = head;
	    head.next = null;
	    head.previous = null;
	    size++;
	} else {
	    Node tmp = new Node(value);
	    tmp.next = null;
	    tmp.previous = tail;
	    tail.next = tmp;
	    tail = tmp;
	    size++;
	}
    }

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder();
	for (Node tmp = head; tmp != null; tmp = tmp.next) {
	    sb.append(tmp.data + " ");
	}
	return sb.toString();
    }

    public Node getTail() {
	return this.tail;
    }

    public Node getHead() {
	return this.head;
    }
}