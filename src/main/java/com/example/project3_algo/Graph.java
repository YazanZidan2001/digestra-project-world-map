package com.example.project3_algo;

import java.util.ArrayList;
import java.util.List;

public class Graph {
	private final List<Vertex> vertices;

	public Graph() {
		vertices = new ArrayList<>();
	}

	public void add(Vertex vertex) {
		vertices.add(vertex);
	}

	public Vertex get(Vertex vertex) {
		for (Vertex v : vertices) {
			if (v.equals(vertex)) {
				return v;
			}
		}
		return null;
	}

	public void addEdge(Vertex vertex1, Vertex vertex2) {
		Vertex v1 = get(vertex1);
		Vertex v2 = get(vertex2);
		if (v1 != null && v2 != null) {
			v1.getEdges().insertSorted(new Edge(v1, v2));
		}
		else {
			if (v1 == null) {
				vertex1.getEdges().insertSorted(new Edge(vertex1, vertex2));
				add(vertex1);
			}
			if (v2 == null) {
				add(vertex2);
			}
		}
	}

	public List<Vertex> getVertices() {
		return vertices;
	}

	public int size() {
		return vertices.size();
	}
}
