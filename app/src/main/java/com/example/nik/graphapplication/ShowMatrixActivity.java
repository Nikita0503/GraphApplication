package com.example.nik.graphapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Nik on 29.06.2017.
 */

public class ShowMatrixActivity extends AppCompatActivity {
    TextView textViewMatrix;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix);
        textViewMatrix = (TextView) findViewById(R.id.textViewMatrix);
        int mass_edges[][] = new int[ MainActivity.list_vertices.size()][ MainActivity.list_vertices.size()];
        String str = "        ";
        for(int i = 0; i < mass_edges.length; i++)
        {
            for(int j = 0; j < mass_edges.length; j++)
            {
                for(int z = 0; z <  MainActivity.list_edges.size(); z++)
                {
                    if( MainActivity.list_edges.get(z).vertex1.value ==  MainActivity.list_vertices.get(i).value &&  MainActivity.list_edges.get(z).vertex2.value ==  MainActivity.list_vertices.get(j).value ||  MainActivity.list_edges.get(z).vertex1.value == MainActivity.list_vertices.get(j).value &&  MainActivity.list_edges.get(z).vertex2.value ==  MainActivity.list_vertices.get(i).value)
                    {
                        mass_edges[i][j] =  MainActivity.list_edges.get(z).weight;
                    }
                }
            }
        }
        for(int i = 0; i < MainActivity.list_vertices.size(); i++)
        {
            if( MainActivity.list_vertices.get(i).value<10) str += MainActivity.list_vertices.get(i).value+"   ";
            else str += MainActivity.list_vertices.get(i).value+" ";
        }
        str+="\n\n";
        for(int i = 0; i < mass_edges.length; i++)
        {
            if( MainActivity.list_vertices.get(i).value<10) str += MainActivity.list_vertices.get(i).value+"      ";
            else str += MainActivity.list_vertices.get(i).value+"    ";
            for(int j = 0; j < mass_edges.length; j++)
            {
                if(mass_edges[i][j]<10) str+=mass_edges[i][j]+"   ";
                else str+=mass_edges[i][j]+" ";
            }
            str+="\n";
        }
        textViewMatrix.setText(str);
    }
}
