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


    public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private boolean allowDeleteVertex,
            clickWay1,
            clickWay2;
    private boolean allowDeleteEdgeFirst,
            allowDeleteEdgeSecond,
            allowAddEdgeFirst,
            allowAddEdgeSecond;
    public static boolean printWay;
    private int MAX_X_VERTEX = 1000;
    private int MAX_Y_VERTEX = 1600;
    public int vertex1,
            vertex2;

    public static ArrayList<Vertex> vertices;
    public static ArrayList<Edge> edges;
    public static ArrayList<Edge> way;
    public static ArrayList<Vertex> wayValues;

    public static EditText editTextValueVertex,
            editTextEdgeWeight;
    private static TextView textViewVertices;
    private TextView textViewHint1,
            textViewHint2;
    public Button buttonAddVertex,
           buttonAddEdge,
           buttonShowGraph,
           buttonShowMatrix,
           buttonDeleteVertex,
           buttonDeleteEdge,
           algorytmButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vertices = new ArrayList<Vertex>();
        edges = new ArrayList<Edge>();
        way = new ArrayList<Edge>();
        wayValues = new ArrayList<Vertex>();

        buttonAddVertex = (Button) findViewById(R.id.buttonAddVertex);
        buttonAddVertex.setOnClickListener(this);
        buttonAddEdge = (Button) findViewById(R.id.buttonAddEdge);
        buttonAddEdge.setOnClickListener(this);
        buttonShowGraph = (Button) findViewById(R.id.buttonShowGraph);
        buttonShowGraph.setOnClickListener(this);
        buttonShowMatrix = (Button) findViewById(R.id.buttonShowMatrix);
        buttonShowMatrix.setOnClickListener(this);
        buttonDeleteVertex = (Button) findViewById(R.id.buttonDeleteVertex);
        buttonDeleteVertex.setOnClickListener(this);
        buttonDeleteEdge = (Button) findViewById(R.id.buttonDeleteEdge);
        buttonDeleteEdge.setOnClickListener(this);
        algorytmButton = (Button) findViewById(R.id.buttonShortestPass);
        algorytmButton.setOnClickListener(this);
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
                addVertex();
                break;

            case R.id.buttonAddEdge:
                addEdge();
                break;

            case R.id.buttonShowGraph:
                showGraph();
                break;

            case R.id.buttonShowMatrix:
                showMatrix();
                break;

            case R.id.buttonDeleteVertex:
                deleteVertex();
                break;

            case R.id.buttonDeleteEdge:
                deleteEdge();
                break;

            case R.id.buttonShortestPass:
                showShortestPass();
                break;
        }
    }

    private void addVertex()
    {
        if(!editTextValueVertex.getText().toString().equals("")) {
            try {
                int randomX = 0 + (int) (Math.random() * MAX_X_VERTEX);
                int randomY = 0 + (int) (Math.random() * MAX_Y_VERTEX);
                int value = Integer.parseInt(editTextValueVertex.getText().toString());
                boolean checkRepeat = false;
                for (int i = 0; i < vertices.size(); i++) {
                    if (vertices.get(i).value == value) checkRepeat = true;
                }
                if (checkRepeat) {
                    Toast.makeText(this, "This vertex is not unique", Toast.LENGTH_LONG);
                }else {
                    vertices.add(new Vertex(new Point(randomX, randomY), value));
                    editTextValueVertex.setText("");
                    updateTextViewVertecies();
                }
            } catch (Exception c) {
                c.printStackTrace();
            }
        }
        else {
            Toast.makeText(this, "Enter the value, please", Toast.LENGTH_LONG).show();
        }
    }

    private void startGraphActivity(Intent intent)
    {
        intent.putExtra("clickWay1", clickWay1);
        intent.putExtra("clickWay2", clickWay2);
        intent.putExtra("allowDeleteVertex", allowDeleteVertex);
        intent.putExtra("allowDeleteEdgeFirst", allowDeleteEdgeFirst);
        intent.putExtra("allowDeleteEdgeSecond", allowDeleteEdgeSecond);
        intent.putExtra("allowAddEdgeFirst", allowAddEdgeFirst);
        intent.putExtra("allowAddEdgeSecond", allowAddEdgeSecond);
        intent.putExtra("vertex1", vertex1);
        intent.putExtra("vertex2", vertex2);
        startActivity(intent);
    }


    private void addEdge()
    {
        if(!editTextEdgeWeight.getText().toString().equals("")) {
            try {
                Intent intent = new Intent(this, GraphActivity.class);
                printWay = false;
                clickWay1 = false;
                clickWay2 = false;
                vertex1 = 0;
                vertex2 = 0;
                allowDeleteVertex = false;
                allowDeleteEdgeFirst = false;
                allowDeleteEdgeSecond = false;
                allowAddEdgeFirst = true;
                allowAddEdgeSecond = false;
                startGraphActivity(intent);

            } catch (Exception c) {
                c.printStackTrace();
            }
        }
        else {
            Toast.makeText(this, "Enter the weight, please", Toast.LENGTH_LONG).show();
        }
    }

    private void showGraph()
    {
        Intent intent = new Intent(this, GraphActivity.class);
        printWay = false;
        clickWay1 = false;
        clickWay2 = false;
        allowDeleteVertex = false;
        allowDeleteEdgeFirst = false;
        allowDeleteEdgeSecond = false;
        allowAddEdgeFirst = false;
        allowAddEdgeSecond = false;
        startGraphActivity(intent);
    }

    private void showMatrix()
    {
        Intent intent = new Intent(this, ShowMatrixActivity.class);
        startActivity(intent);
    }

    private void deleteVertex()
    {
        if(!vertices.isEmpty()) {
            Intent intent = new Intent(this, GraphActivity.class);
            printWay = false;
            clickWay1 = false;
            clickWay2 = false;
            allowDeleteVertex = true;
            allowDeleteEdgeFirst = false;
            allowDeleteEdgeSecond = false;
            allowAddEdgeFirst = false;
            allowAddEdgeSecond = false;
            startGraphActivity(intent);
        }
        else {
            Toast.makeText(this, "List of vertecies is empty", Toast.LENGTH_LONG);
        }
    }

    private void deleteEdge()
    {
        if(!edges.isEmpty()) {
            Intent intent = new Intent(this, GraphActivity.class);
            vertex1 = 0;
            vertex2 = 0;
            printWay = false;
            clickWay1 = false;
            clickWay2 = false;
            allowDeleteVertex = false;
            allowDeleteEdgeFirst = true;
            allowDeleteEdgeSecond = false;
            allowAddEdgeFirst = false;
            allowAddEdgeSecond = false;
            startGraphActivity(intent);
        }
        else {
            Toast.makeText(this, "List of edges is empty", Toast.LENGTH_LONG);
        }
    }

    private void showShortestPass()
    {
        if(!edges.isEmpty() && !vertices.isEmpty()) {
            try {
                Intent intent = new Intent(this, GraphActivity.class);
                way.clear();
                wayValues.clear();
                vertex1 = 0;
                vertex2 = 0;
                for (int i = 0; i < vertices.size(); i++) {
                    vertices.get(i).number = i + 1;
                }
                printWay = false;
                allowDeleteVertex = false;
                allowDeleteEdgeFirst = false;
                allowDeleteEdgeSecond = false;
                allowAddEdgeFirst = false;
                allowAddEdgeSecond = false;
                clickWay1 = true;
                clickWay2 = false;
                startGraphActivity(intent);
            } catch (Exception c) {
                c.printStackTrace();
            }
        }
        else {
            Toast.makeText(this, "Not enough data", Toast.LENGTH_LONG);
        }
    }

    public static void updateTextViewVertecies() {
        textViewVertices.setText("");
        for(int i = 0; i < vertices.size(); i++) {
            textViewVertices.setText(String.valueOf(textViewVertices.getText().toString() + " " +  vertices.get(i).value));
        }
    }
}
