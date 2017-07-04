package com.example.nik.graphapplication;

/**
 * Created by Nik on 28.06.2017.
 */

public class Edge {
    public Vertex vertex1, vertex2;
    public int weight;
    public Edge(Vertex vertex1, Vertex vertex2, int weight) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.weight = weight;
    }
}
