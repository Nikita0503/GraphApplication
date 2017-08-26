package com.example.nik.graphapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nik on 29.06.2017.
 */

public class ShowMatrixActivity extends AppCompatActivity {
    TextView textViewMatrix;

    public ArrayList<Vertex> vertices;
    public ArrayList<Edge> edges;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_matrix);
        textViewMatrix = (TextView) findViewById(R.id.textViewMatrix);
        int mass_edges[][] = new int[ vertices.size()][ vertices.size()];
        String str = "        ";
        for(int i = 0; i < mass_edges.length; i++) {
            for(int j = 0; j < mass_edges.length; j++) {
                for(int z = 0; z <  edges.size(); z++) {
                    if( edges.get(z).vertex1.value ==  vertices.get(i).value &&
                            edges.get(z).vertex2.value ==  vertices.get(j).value ||
                            edges.get(z).vertex1.value == vertices.get(j).value &&
                            edges.get(z).vertex2.value ==  vertices.get(i).value) {
                        mass_edges[i][j] =  edges.get(z).weight;
                    }
                }
            }
        }
        for(int i = 0; i < vertices.size(); i++) {
            if( vertices.get(i).value<10) str += vertices.get(i).value+"   ";
            else str += vertices.get(i).value+" ";
        }
        str+="\n\n";
        for(int i = 0; i < mass_edges.length; i++) {
            if( vertices.get(i).value<10) str += vertices.get(i).value+"      ";
            else str += vertices.get(i).value+"    ";
            for(int j = 0; j < mass_edges.length; j++) {
                if(mass_edges[i][j]<10) str+=mass_edges[i][j]+"   ";
                else str+=mass_edges[i][j]+" ";
            }
            str+="\n";
        }
        textViewMatrix.setText(str);
    }
}
