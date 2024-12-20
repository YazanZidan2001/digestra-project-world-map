package com.example.project3_algo;

public class Vertex implements Comparable<Vertex> {
	private final Double latitude;
	private final Double longitude;
	private final String location;
	private final SingleLinkedList<Edge> edges;

	public Vertex(Double latitude, Double longitude, String location) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.location = location;
		this.edges = new SingleLinkedList<>();
	}

	public Vertex(String location) {
		this(null, null, location);
	}

	public Double getLatitude() {
		return latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public String getLocation() {
		return location;
	}

	public SingleLinkedList<Edge> getEdges() {
		return edges;
	}

	@Override
	public String toString() {
		return location;
	}

	@Override
	public int hashCode() {
		return location.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Vertex vertex = (Vertex) obj;
		return location.equals(vertex.location);
	}

	@Override
	public int compareTo(Vertex other) {
		return this.location.compareTo(other.location);
	}
}
