package com.example.project3_algo;

public class SingleLinkedList<T extends Comparable<T>> {

	private Node<T> dummyHead;
	private Node<T> tail;

	public SingleLinkedList() {
		dummyHead = new Node<>(null);
	}

	public void insertSorted(T data) {
		Node<T> newNode = new Node<>(data);
		Node<T> curr = dummyHead;

		while (curr.getNext() != null && curr.getNext().getData().compareTo(newNode.getData()) < 0) {
			curr = curr.getNext();
		}

		newNode.setNext(curr.getNext());
		curr.setNext(newNode);

		if (newNode.getNext() == null) {
			tail = newNode;
		}
	}

	public void insertAtHead(T element) {
		Node<T> newNode = new Node<>(element);
		newNode.setNext(dummyHead.getNext());
		dummyHead.setNext(newNode);

		if (tail == null) {
			tail = newNode;
		}
	}

	public void insertAtTail(T element) {
		Node<T> newNode = new Node<>(element);
		if (tail == null) {
			dummyHead.setNext(newNode);
		} else {
			tail.setNext(newNode);
		}
		tail = newNode;
	}

	public T search(T data) {
		Node<T> curr = dummyHead.getNext();
		while (curr != null && curr.getData().compareTo(data) <= 0) {
			if (curr.getData().compareTo(data) == 0)
				return curr.getData();
			curr = curr.getNext();
		}
		return null;
	}

	public boolean delete(T data) {
		Node<T> curr = dummyHead;
		while (curr.getNext() != null && curr.getNext().getData().compareTo(data) <= 0) {
			if (curr.getNext().getData().compareTo(data) == 0) {
				curr.setNext(curr.getNext().getNext());
				if (curr.getNext() == null) {
					tail = curr;
				}
				return true;
			}
			curr = curr.getNext();
		}
		return false;
	}

	public void traverse() {
		System.out.print("Head->");
		Node<T> curr = dummyHead.getNext();
		while (curr != null) {
			System.out.print(curr.getData() + "->");
			curr = curr.getNext();
		}
		System.out.println("null");
	}

	public Node<T> getFirst() {
		return dummyHead.getNext();
	}

	public Node<T> getLast() {
		return tail;
	}

	public boolean isEmpty() {
		return dummyHead.getNext() == null;
	}

	public int getSize() {
		int count = 0;
		Node<T> curr = dummyHead.getNext();
		while (curr != null) {
			count++;
			curr = curr.getNext();
		}
		return count;
	}

	public static void main(String[] args) {
		SingleLinkedList<Integer> list = new SingleLinkedList<>();
		list.insertAtTail(3);
		list.insertAtTail(1);
		list.insertAtTail(2);
		list.insertSorted(4);
		list.insertAtHead(0);

		list.traverse();

		System.out.println("Size: " + list.getSize());

		System.out.println("Search 3: " + list.search(3));

		list.delete(2);
		list.traverse();

		System.out.println("First: " + list.getFirst().getData());
		System.out.println("Last: " + list.getLast().getData());
	}
}
