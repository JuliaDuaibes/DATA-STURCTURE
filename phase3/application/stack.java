package application;

public class stack<T> {
	private Node<T> top;
	private int size;

	public stack() {
		top = null;
		size = 0;
	}

	public void push(T value) {
		Node<T> newNode = new Node<>(value);
		newNode.next = top;
		top = newNode;
		size++;
	}

	public T pop() {
		if (isEmpty()) {
			throw new RuntimeException("Stack is empty");
		}
		T value = top.value;
		top = top.next;
		size--;
		return value;
	}

	public boolean isEmpty() {
		return top == null;
	}

	public int size() {
		return size;
	}

	public T peek() {
		if (!isEmpty())
			return top.value;
		else
			throw new IllegalStateException("Stack is empty");
	}

	public void clear() {
		top = null;
		size = 0;
	}
	

	private static class Node<T> {
		private T value;
		private Node<T> next;

		public Node(T value) {
			this.value = value;
			this.next = null;
		}
	}
}