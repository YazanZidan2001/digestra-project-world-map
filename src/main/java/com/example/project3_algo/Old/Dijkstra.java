package com.example.project3_algo.Old;


import com.example.project3_algo.*;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

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

        public TableEntry(Vertex vertex) {
            this.source = vertex;
            this.edges = vertex.getEdges();
            this.known = false;
            this.distance = Double.MAX_VALUE;
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
        PriorityQueue<TableEntry> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(sourceEntry);

        while (!priorityQueue.isEmpty()) {
            TableEntry currentEntry = priorityQueue.poll();

            if (currentEntry.source.equals(destination)) break;

            currentEntry.known = true;

            Node<Edge> currentNode = currentEntry.edges.getFirst();
            while (currentNode != null) {
                TableEntry adjacentEntry = getTableEntry(currentNode.getData().getDestination());

                if (!adjacentEntry.known) {
                    double newDist = currentEntry.distance + currentNode.getData().getCost();
                    if (newDist < adjacentEntry.distance) {
                        adjacentEntry.distance = newDist;
                        adjacentEntry.previous = currentEntry.source;
                        priorityQueue.add(adjacentEntry);
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

