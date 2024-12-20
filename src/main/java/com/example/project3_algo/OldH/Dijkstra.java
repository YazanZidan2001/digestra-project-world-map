package com.example.project3_algo.OldH;


import com.example.project3_algo.*;

import java.util.ArrayList;
import java.util.List;

public class Dijkstra {
    private List<TableEntry> table;
    private final Graph graph;

    public Dijkstra(Graph graph) {
        this.graph = graph;
    }

    private static class TableEntry implements Comparable<TableEntry> {
        private final Vertex source;
        private final SingleLinkedList<Edge> edges;
        private boolean known;
        private double distance;
        private Vertex previous;
        private int heapIndex;

        public TableEntry(Vertex vertex) {
            this.source = vertex;
            this.edges = vertex.getEdges();
            this.known = false;
            this.distance = Double.MAX_VALUE;
            this.heapIndex = -1;
        }

        @Override
        public int compareTo(TableEntry other) {
            return Double.compare(this.distance, other.distance);
        }
    }

    public List<Vertex> getShortestPath(Vertex source, Vertex destination) {
        initializeTable();
        TableEntry sourceEntry = getTableEntry(source);
        if (sourceEntry == null) {
            throw new IllegalArgumentException("Source vertex not found in the graph");
        }
        sourceEntry.distance = 0;
        MinHeap<TableEntry> heap = new MinHeap<>();
        sourceEntry.heapIndex = heap.add(sourceEntry);

        while (!heap.isEmpty()) {
            TableEntry currentEntry = heap.delete();
            System.out.println("Processing vertex: " + currentEntry.source.getLocation());

            if (currentEntry.source.equals(destination)) break;

            currentEntry.known = true;

            Node<Edge> currentNode = currentEntry.edges.getFirst();
            while (currentNode != null) {
                TableEntry adjacentEntry = getTableEntry(currentNode.getData().getDestination());

                if (!adjacentEntry.known) {
                    double newDist = currentEntry.distance + currentNode.getData().getCost();
                    if (newDist < adjacentEntry.distance) {
                        System.out.println("Updating distance for vertex: " + adjacentEntry.source.getLocation() + " to " + newDist);
                        adjacentEntry.distance = newDist;
                        adjacentEntry.previous = currentEntry.source;
                        if (adjacentEntry.heapIndex != -1) {
                            heap.decreaseKey(adjacentEntry.heapIndex, adjacentEntry);
                        } else {
                            adjacentEntry.heapIndex = heap.add(adjacentEntry);
                        }
                    }
                }
                currentNode = currentNode.getNext();
            }
        }

        return buildPath(destination);
    }

    private void initializeTable() {
        table = new ArrayList<>();
        for (Vertex vertex : graph.getVertices()) {
            table.add(new TableEntry(vertex));
        }
    }

    private TableEntry getTableEntry(Vertex vertex) {
        for (TableEntry entry : table) {
            if (entry.source.equals(vertex)) {
                return entry;
            }
        }
        return null;
    }

    private List<Vertex> buildPath(Vertex destination) {
        List<Vertex> path = new ArrayList<>();
        TableEntry currentEntry = getTableEntry(destination);
        while (currentEntry != null) {
            path.add(currentEntry.source);
            currentEntry = getTableEntry(currentEntry.previous);
        }
        return path;
    }

    public double getCost(Vertex vertex) {
        TableEntry entry = getTableEntry(vertex);
        if (entry == null) {
            throw new IllegalArgumentException("Vertex not found in the table");
        }
        return entry.distance;
    }
}

