package myutils2k17;

public class CircularLinkedList<T> {

    private Node head;
    private Node tail;
    private int size;

    public CircularLinkedList() {
	head = null;
	tail = null;
	size = 0;
    }

    public CircularLinkedList(CircularLinkedList<T> toCopy) {
	head = null;
	tail = null;
	size = 0;

	Node tmp = toCopy.head;
	for (int i = 0; i < toCopy.size; i++) {
	    addLast(tmp.data);
	    tmp = tmp.next;
	}
    }

    /**
     *
     * @param startIndex
     *            index 0 of sublist to reverse
     * @param length
     *            number of elements in the sublist to reverse
     */
    public void reverse(int startIndex, int length) {
	if (size == 0 || size == 1 || length > size || startIndex < 0 || startIndex >= size) {
	    return;
	}

	CircularLinkedList<T> tmp = new CircularLinkedList<>();
	Node tmpNode = getNode(startIndex);

	for (int i = 0; i < length; i++) {
	    tmp.addFirst(tmpNode.data);
	    tmpNode = tmpNode.next;
	}

	tmpNode = tmp.head;
	for (int i = 0; i < length; i++) {
	    getNode(startIndex).data = tmpNode.data;
	    tmpNode = tmpNode.next;
	    startIndex = (startIndex + 1) % size;
	}
    }

    public void addLast(T item) {
	if (size == 0) {
	    head = new Node(item);
	    tail = head;
	    head.next = tail;
	    tail.next = head;
	    size++;
	} else {
	    Node oldTail = tail;
	    tail = new Node(item);
	    tail.next = head;
	    oldTail.next = tail;
	    size++;
	}
    }

    public void addFirst(T item) {
	if (size == 0) {
	    head = new Node(item);
	    tail = head;
	    head.next = tail;
	    tail.next = head;
	    size++;
	} else {
	    Node oldHead = head;
	    head = new Node(item);
	    head.next = oldHead;
	    tail.next = head;
	    size++;
	}
    }

    public T get(int index) {
	index = index % size;

	Node tmp = head;
	for (int i = 0; i < index; i++) {
	    tmp = tmp.next;
	}
	return tmp.data;
    }

    public boolean isEmpty() {
	return size == 0;
    }

    public int size() {
	return size;
    }

    /**
     *
     * @param amount
     *            amount of elements to rotate to the front of the list
     *
     *            rotates the list by moving elements from the end to the front
     */
    public void rotateFromTail(int amount) {
	if (amount < 0 || amount > size) {
	    throw new IllegalArgumentException();
	}

	int translationAmount = size - amount;
	for (int i = 0; i < translationAmount; i++) {
	    head = head.next;
	    tail = tail.next;
	}
    }

    @SuppressWarnings("unused")
    private void printList() {
	Node tmp = head;
	for (int i = 0; i < size; i++) {
	    System.out.print(tmp.data + " ");
	    tmp = tmp.next;
	}
	System.out.println();
    }

    private Node getNode(int index) {
	index = index % size;
	Node tmp = head;
	for (int i = 0; i < index; i++) {
	    tmp = tmp.next;
	}
	return tmp;
    }

    @Override
    public String toString() {
	StringBuilder result = new StringBuilder("[");
	Node tmpNode = head;
	for (int i = 0; i < size; i++) {
	    result.append(tmpNode.data + ", ");
	    tmpNode = tmpNode.next;
	}
	result.deleteCharAt(result.length() - 2);
	result.deleteCharAt(result.length() - 1);
	result.append("]");
	return result.toString();
    }

    public Node head() {
	return head;
    }

    // adds a new node between the current node and the next
    public void addToCurrentPosition(Node current, T value) {
	if (size == 0) {
	    addLast(value);
	} else {
	    if (current == tail) {
		Node newHead = new Node(value);
		newHead.next = head;
		tail.next = newHead;
		tail = newHead;
		size++;
	    } else {
		Node toInsert = new Node(value);
		toInsert.next = current.next;
		current.next = toInsert;
		size++;
	    }
	}
    }

    public class Node {
	public T data;
	public Node next;

	public Node(T data) {
	    this.data = data;
	}
    }

}
