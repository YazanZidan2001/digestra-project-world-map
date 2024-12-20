package com.example.project3_algo;

public class Edge implements Comparable<Edge> {
	private final Vertex destination;
	private final double cost;

	public Edge(Vertex source, Vertex destination) {
		this.destination = destination;
		this.cost = calculateCost(source);
	}
	//Haversine formula
	private double calculateCost(Vertex source) {
		final double earthRadius = 6371.0; // R of the earth in k

		double radLat1 = Math.toRadians(source.getLatitude());
		double radLon1 = Math.toRadians(source.getLongitude());
		double radLat2 = Math.toRadians(destination.getLatitude());
		double radLon2 = Math.toRadians(destination.getLongitude());

		double deltaLat = radLat2 - radLat1;
		double deltaLon = radLon2 - radLon1;

		double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
				+ Math.cos(radLat1) * Math.cos(radLat2) * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return earthRadius * c;
	}

	public Vertex getDestination() {
		return destination;
	}

	public double getCost() {
		return cost;
	}

	@Override
	public int compareTo(Edge other) {
		return Double.compare(this.cost, other.cost);
	}

	@Override
	public String toString() {
		return "Edge to " + destination.getLocation() + " with cost " + cost;
	}
}
