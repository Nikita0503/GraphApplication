package com.example.nik.graphapplication;

/**
 * Created by Nik on 02.07.2017.
 */

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Solution {
    private static int INF = Integer.MAX_VALUE / 2;
    private int n;
    private int m;
    private int vertex1, vertex2;
    private ArrayList<Integer> adj[];
    private ArrayList<Integer> weight[];
    private boolean used[];
    private int dist[];
    private int pred[];
    int start;

    public Solution(int vertex1, int vertex2)
    {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
    }

    private void dejkstra(int s) {
        dist[s] = 0;
        for (int iter = 0; iter < n; ++iter) {
            int v = -1;
            int distV = INF;
            for (int i = 0; i < n; ++i) {
                if (used[i]) {
                    continue;
                }
                if (distV < dist[i]) {
                    continue;
                }
                v = i;
                distV = dist[i];
            }
            for (int i = 0; i < adj[v].size(); ++i) {
                int u = adj[v].get(i);
                int weightU = weight[v].get(i);
                if (dist[v] + weightU < dist[u]) {
                    dist[u] = dist[v] + weightU;
                    pred[u] = v;
                }
            }
            used[v] = true;
        }
    }

    private void readData() throws IOException {
        n = MainActivity.vertices.size();
        m = MainActivity.edges.size();
        for(int i = 0; i < MainActivity.vertices.size(); i++) {
            if(MainActivity.vertices.get(i).value == vertex1) {
                MainActivity.wayValues.add(MainActivity.vertices.get(i));
                start = MainActivity.vertices.get(i).number - 1;
            }
        }

        adj = new ArrayList[n];
        for (int i = 0; i < n; ++i) {
            adj[i] = new ArrayList<Integer>();
        }

        weight = new ArrayList[n];
        for (int i = 0; i < n; ++i) {
            weight[i] = new ArrayList<Integer>();
        }

        for (int i = 0; i < m; ++i) {
            int u = 0;
            for(int j = 0; j < MainActivity.vertices.size(); j++) {
                if(MainActivity.edges.get(i).vertex1.value == MainActivity.vertices.get(j).value) {
                    u = MainActivity.vertices.get(j).number;
                }
            }
            int v = 0;
            for(int j = 0; j < MainActivity.vertices.size(); j++) {
                if(MainActivity.edges.get(i).vertex2.value == MainActivity.vertices.get(j).value) {
                    v = MainActivity.vertices.get(j).number;
                }
            }
            int w = MainActivity.edges.get(i).weight;
            u--;
            v--;
            adj[u].add(v);
            weight[u].add(w);
        }
        used = new boolean[n];
        Arrays.fill(used, false);
        pred = new int[n];
        Arrays.fill(pred, -1);
        dist = new int[n];
        Arrays.fill(dist, INF);

    }

    void printWay(int v) {
        if (v == -1) {
            return;
        }
        printWay(pred[v]);
        for(int i = 0; i < MainActivity.vertices.size(); i++) {
            if(MainActivity.vertices.get(i).number == v + 1) {
                MainActivity.wayValues.add(MainActivity.vertices.get(i));
                Log.d("GRAPH", MainActivity.vertices.get(i).value + " " );
            }
        }
    }
    private void printData() throws IOException {
        for(int i = 0; i < MainActivity.vertices.size(); i++) {
            if(vertex2 == MainActivity.vertices.get(i).value) {
                printWay(MainActivity.vertices.get(i).number-1);
            }
        }
        for(int i = 0; i < MainActivity.wayValues.size()-1; i++) {
            MainActivity.way.add(new Edge(MainActivity.wayValues.get(i), MainActivity.wayValues.get(i+1), 0));
        }
        MainActivity.printWay = true;
    }

    public void run() throws IOException {
        readData();
        dejkstra(start);
        printData();
    }
}