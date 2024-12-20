package com.example.project3_algo;

public class Node<T> {
    private final T data;
    private Node<T> next;

    public Node(T data) {
        this.data = data;
    }


    public T getData() {
        return data;
    }


    public Node<T> getNext() {
        return next;
    }


    public void setNext(Node<T> next) {
        this.next = next;
    }
}




//	void glowObject(javafx.scene.Node o) {
//		DropShadow glow = new DropShadow();
//		glow.setWidth(5);
//		glow.setHeight(5);
//		glow.setRadius(5);
//		glow.setSpread(0.2);
//		glow.setColor(Color.BLACK); // Neon color
//		o.setEffect(glow);
//
//	}
//
//	DropShadow glow(Color color) {
//		DropShadow glow = new DropShadow();
//		glow.setWidth(25);
//		glow.setHeight(25);
//		glow.setRadius(15);
//		glow.setSpread(0.5);
//		glow.setColor(color); // Neon color
//		return glow;
//
//	}