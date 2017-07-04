package com.example.nik.graphapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;


    public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static ArrayList<Vertex> list_vertices = new ArrayList<Vertex>();
    public static ArrayList<Edge> list_edges = new ArrayList<Edge>();
    public static ArrayList<Edge> way_list = new ArrayList<Edge>();
    public static boolean allow_delete_vertex = false, allow_delete_edge_first = false, allow_delete_edge_second = false, allow_add_edge_first = false, allow_add_edge_second = false, click_way1 = false, click_way2 = false, print_way = false;
    public static Button button_add_vertex, button_add_edge, button_show_graph, button_show_matrix, button_delete_vertex, button_delete_edge, algorytm_button;
    public static EditText editTextValueVertex, editTextEdge1, editTextEdge2, editTextEdgeWeight;
    public static TextView textViewVertices, textViewHint1, textViewHint2;
    public static int[][] mass_edges;
    public static int vertex1 = 0, vertex2 = 0;
    public static ArrayList<Vertex> way_list_values = new ArrayList<Vertex>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_add_vertex = (Button) findViewById(R.id.buttonAddVertex);
        button_add_vertex.setOnClickListener(this);
        button_add_edge = (Button) findViewById(R.id.buttonAddEdge);
        button_add_edge.setOnClickListener(this);
        button_show_graph = (Button) findViewById(R.id.buttonShowGraph);
        button_show_graph.setOnClickListener(this);
        button_show_matrix = (Button) findViewById(R.id.buttonShowMatrix);
        button_show_matrix.setOnClickListener(this);
        button_delete_vertex = (Button) findViewById(R.id.buttonDeleteVertex);
        button_delete_vertex.setOnClickListener(this);
        button_delete_edge = (Button) findViewById(R.id.buttonDeleteEdge);
        button_delete_edge.setOnClickListener(this);
        algorytm_button = (Button) findViewById(R.id.buttonShortestPass);
        algorytm_button.setOnClickListener(this);
        editTextValueVertex = (EditText) findViewById(R.id.editTextValue);
        editTextValueVertex.setOnClickListener(this);
        editTextEdgeWeight = (EditText) findViewById(R.id.editTextValueEdge);
        editTextEdgeWeight.setOnClickListener(this);
        textViewVertices = (TextView) findViewById(R.id.textViewVertices);
        textViewHint1 = (TextView) findViewById(R.id.textViewHint1);
        textViewHint2 = (TextView) findViewById(R.id.textViewHint2);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/Hattori_Hanzo.otf");
        textViewHint1.setTypeface(typeFace);
        textViewHint2.setTypeface(typeFace);
        editTextEdgeWeight.setTypeface(typeFace);
        editTextValueVertex.setTypeface(typeFace);
        updateTextViewVertecies();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAddVertex:
                if(!editTextValueVertex.getText().toString().equals("")) {
                    try {
                        int random_x = 0 + (int) (Math.random() * 1000);
                        int random_y = 0 + (int) (Math.random() * 1600);
                        int value = Integer.parseInt(editTextValueVertex.getText().toString());
                        boolean check_repeat = false;
                        for (int i = 0; i < list_vertices.size(); i++) {
                            if (list_vertices.get(i).value == value) check_repeat = true;
                        }
                        if (check_repeat) {
                            Toast toast = Toast.makeText(MainActivity.this, "This vertex is not unique", Toast.LENGTH_LONG);
                            toast.show();
                        }else {
                            list_vertices.add(new Vertex(new Point(random_x, random_y), value));
                            editTextValueVertex.setText("");
                            updateTextViewVertecies();
                        }
                    } catch (Exception c) {
                        c.printStackTrace();
                    }
                }
                else {
                    Toast toast = Toast.makeText(MainActivity.this, "Enter the value, please", Toast.LENGTH_LONG);
                    toast.show();
                }
                break;

            case R.id.buttonAddEdge:
                if(!editTextEdgeWeight.getText().toString().equals("")) {
                    try {
                        print_way = false;
                        click_way1 = false;
                        click_way2 = false;
                        vertex1 = 0;
                        vertex2 = 0;
                        Intent intent = new Intent(this, GraphActivity.class);
                        allow_delete_vertex = false;
                        allow_delete_edge_first = false;
                        allow_delete_edge_second = false;
                        allow_add_edge_first = true;
                        allow_add_edge_second = false;
                        startActivity(intent);
                    } catch (Exception c) {
                        c.printStackTrace();
                    }
                }
                else {
                    Toast toast = Toast.makeText(MainActivity.this, "Enter the weight, please", Toast.LENGTH_LONG);
                    toast.show();
                }
                break;

            case R.id.buttonShowGraph:
                Intent intent = new Intent(this, GraphActivity.class);
                print_way = false;
                click_way1 = false;
                click_way2 = false;
                allow_delete_vertex = false;
                allow_delete_edge_first = false;
                allow_delete_edge_second = false;
                allow_add_edge_first = false;
                allow_add_edge_second = false;
                startActivity(intent);
                break;

            case R.id.buttonShowMatrix:
                Intent intent2 = new Intent(this, ShowMatrixActivity.class);
                startActivity(intent2);
                break;

            case R.id.buttonDeleteVertex:
                if(!list_vertices.isEmpty()) {
                    Intent intent3 = new Intent(this, GraphActivity.class);
                    print_way = false;
                    click_way1 = false;
                    click_way2 = false;
                    allow_delete_vertex = true;
                    allow_delete_edge_first = false;
                    allow_delete_edge_second = false;
                    allow_add_edge_first = false;
                    allow_add_edge_second = false;
                    startActivity(intent3);
                }
                else {
                    Toast toast = Toast.makeText(MainActivity.this, "List of vertecies is empty", Toast.LENGTH_LONG);
                    toast.show();
                }
                break;

            case R.id.buttonDeleteEdge:
                if(!list_edges.isEmpty()) {
                    vertex1 = 0;
                    vertex2 = 0;
                    print_way = false;
                    Intent intent4 = new Intent(this, GraphActivity.class);
                    click_way1 = false;
                    click_way2 = false;
                    allow_delete_vertex = false;
                    allow_delete_edge_first = true;
                    allow_delete_edge_second = false;
                    allow_add_edge_first = false;
                    allow_add_edge_second = false;
                    startActivity(intent4);
                }
                else {
                    Toast toast = Toast.makeText(MainActivity.this, "List of edges is empty", Toast.LENGTH_LONG);
                    toast.show();
                }
                break;

            case R.id.buttonShortestPass:
                if(!list_edges.isEmpty() && !list_vertices.isEmpty()) {
                    try {
                        way_list.clear();
                        way_list_values.clear();
                        vertex1 = 0;
                        vertex2 = 0;
                        for (int i = 0; i < list_vertices.size(); i++) {
                            list_vertices.get(i).number = i + 1;
                        }
                        Intent intent5 = new Intent(this, GraphActivity.class);
                        print_way = false;
                        allow_delete_vertex = false;
                        allow_delete_edge_first = false;
                        allow_delete_edge_second = false;
                        allow_add_edge_first = false;
                        allow_add_edge_second = false;
                        click_way1 = true;
                        click_way2 = false;
                        startActivity(intent5);
                    } catch (Exception c) {
                        c.printStackTrace();
                    }
                }
                else {
                    Toast toast = Toast.makeText(MainActivity.this, "Not enough data", Toast.LENGTH_LONG);
                    toast.show();
                }
                break;
        }
    }

    public static void updateTextViewVertecies() {
        textViewVertices.setText("");
        for(int i = 0; i < list_vertices.size(); i++) {
            textViewVertices.setText(String.valueOf(textViewVertices.getText().toString()+" "+list_vertices.get(i).value));
        }
    }
}
