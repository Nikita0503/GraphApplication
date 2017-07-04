package com.example.nik.graphapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity {
    float xTouch, yTouch;
    int mass_edges[][];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GraphicsView graphicsView = new GraphicsView(this);
        setContentView(graphicsView);
    }

    public class GraphicsView extends View
    {
        public GraphicsView(Context context) { super(context); }
        Paint p;

        @Override
        protected void onDraw(Canvas canvas)
        {
            p = new Paint();
            p.setTextSize(50);
            p.setStrokeWidth(10);
            p.setColor(Color.BLACK);
            if(MainActivity.allow_delete_vertex)
            {
                canvas.drawText("Please, click the vertex",  10,  50, p);
            }

            if(MainActivity.allow_delete_edge_first || MainActivity.allow_add_edge_first || MainActivity.click_way1)
            {
                canvas.drawText("Please, click the first vertex",  10,  50, p);
            }

            if(MainActivity.allow_delete_edge_second || MainActivity.allow_add_edge_second || MainActivity.click_way2)
            {
                canvas.drawText("Please, click the second vertex",  10,  50, p);
            }

            for(int i = 0; i < MainActivity.list_edges.size(); i++)
            {
                canvas.drawLine(MainActivity.list_edges.get(i).vertex1.point.x, MainActivity.list_edges.get(i).vertex1.point.y, MainActivity.list_edges.get(i).vertex2.point.x, MainActivity.list_edges.get(i).vertex2.point.y  ,p);
                canvas.drawText(String.valueOf(MainActivity.list_edges.get(i).weight),  (MainActivity.list_edges.get(i).vertex1.point.x + MainActivity.list_edges.get(i).vertex2.point.x)/2,  (MainActivity.list_edges.get(i).vertex1.point.y + MainActivity.list_edges.get(i).vertex2.point.y)/2-20, p);
            }

            if(MainActivity.print_way)
            {
                p.setColor(Color.GREEN);
                for(int i = 0; i < MainActivity.way_list.size(); i++)
                {
                    canvas.drawLine(MainActivity.way_list.get(i).vertex1.point.x, MainActivity.way_list.get(i).vertex1.point.y, MainActivity.way_list.get(i).vertex2.point.x, MainActivity.way_list.get(i).vertex2.point.y  ,p);
                }
            }

            for(int i = 0; i <  MainActivity.list_vertices.size(); i++)
            {
                p.setColor(Color.BLACK);
                canvas.drawCircle( MainActivity.list_vertices.get(i).point.x-1,  MainActivity.list_vertices.get(i).point.y-1, 102, p);
                p.setColor(Color.RED);
                canvas.drawCircle( MainActivity.list_vertices.get(i).point.x,  MainActivity.list_vertices.get(i).point.y, 100, p);
                p.setColor(Color.WHITE);
                p.setTextSize(50);
                if( MainActivity.list_vertices.get(i).value<10)
                    canvas.drawText( MainActivity.list_vertices.get(i).value+"",  MainActivity.list_vertices.get(i).point.x-15,  MainActivity.list_vertices.get(i).point.y+15, p);
                else
                    canvas.drawText( MainActivity.list_vertices.get(i).value+"",  MainActivity.list_vertices.get(i).point.x-25,  MainActivity.list_vertices.get(i).point.y+15, p);
            }

        }
        public boolean onTouchEvent(MotionEvent event)
        {
            xTouch = event.getX();
            yTouch = event.getY();
            if(MainActivity.click_way1)
            {
                for(int i = 0; i <  MainActivity.list_vertices.size(); i++)
                {
                    if(xTouch> MainActivity.list_vertices.get(i).point.x-75 && xTouch<MainActivity.list_vertices.get(i).point.x+75 && yTouch<MainActivity.list_vertices.get(i).point.y+75 && yTouch>MainActivity.list_vertices.get(i).point.y-75)
                    {
                        MainActivity.click_way1 = false;
                        MainActivity.click_way2  = true;
                        MainActivity.vertex1 = MainActivity.list_vertices.get(i).value;
                        break;
                    }
                }
            }

            if(MainActivity.click_way2)
            {
                int j;
                for(j = 0; j <  MainActivity.list_vertices.size(); j++)
                {
                    if(xTouch> MainActivity.list_vertices.get(j).point.x-75 && xTouch<MainActivity.list_vertices.get(j).point.x+75 && yTouch<MainActivity.list_vertices.get(j).point.y+75 && yTouch>MainActivity.list_vertices.get(j).point.y-75)
                    {
                        if(MainActivity.list_vertices.get(j).value!=MainActivity.vertex1)
                        {
                            MainActivity.click_way2 = false;
                            MainActivity.vertex2=MainActivity.list_vertices.get(j).value;
                            break;
                        }
                    }
                }
                Solution solution = new Solution();
                try {
                    solution.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if(MainActivity.allow_add_edge_first)
            {
                for(int i = 0; i <  MainActivity.list_vertices.size(); i++)
                {
                    if(xTouch> MainActivity.list_vertices.get(i).point.x-75 && xTouch<MainActivity.list_vertices.get(i).point.x+75 && yTouch<MainActivity.list_vertices.get(i).point.y+75 && yTouch>MainActivity.list_vertices.get(i).point.y-75)
                    {
                        MainActivity.allow_add_edge_first = false;
                        MainActivity.allow_add_edge_second  = true;
                        MainActivity.vertex1 = MainActivity.list_vertices.get(i).value;
                        break;
                    }
                }
            }

            if(MainActivity.allow_add_edge_second)
            {
                int j;
                for(j = 0; j <  MainActivity.list_vertices.size(); j++)
                {
                    if(xTouch> MainActivity.list_vertices.get(j).point.x-75 && xTouch<MainActivity.list_vertices.get(j).point.x+75 && yTouch<MainActivity.list_vertices.get(j).point.y+75 && yTouch>MainActivity.list_vertices.get(j).point.y-75)
                    {
                        if(MainActivity.list_vertices.get(j).value!=MainActivity.vertex1)
                        {
                            MainActivity.allow_add_edge_second = false;
                            MainActivity.vertex2=MainActivity.list_vertices.get(j).value;
                            break;
                        }

                    }
                }

                for(int i = 0; i < MainActivity.list_vertices.size(); i++)
                {
                    for(j = 0; j < MainActivity.list_vertices.size(); j++)
                    {
                        if(MainActivity.list_vertices.get(i).value == MainActivity.vertex1 && MainActivity.list_vertices.get(j).value == MainActivity.vertex2 || MainActivity.list_vertices.get(i).value == MainActivity.vertex2 && MainActivity.list_vertices.get(j).value == MainActivity.vertex1) {
                            if(!MainActivity.list_edges.isEmpty()) {
                                for (int z = 0; z < MainActivity.list_edges.size(); z++)
                                {
                                    if (MainActivity.list_edges.get(z).vertex1.value == MainActivity.vertex1 && MainActivity.list_edges.get(z).vertex2.value == MainActivity.vertex2 || MainActivity.list_edges.get(z).vertex2.value == MainActivity.vertex1 && MainActivity.list_edges.get(z).vertex1.value == MainActivity.vertex2) {
                                        MainActivity.list_edges.remove(z);
                                    }
                                }
                                    MainActivity.list_edges.add(new Edge(MainActivity.list_vertices.get(i), MainActivity.list_vertices.get(j), Integer.parseInt(MainActivity.editTextEdgeWeight.getText().toString())));
                                    MainActivity.list_edges.add(new Edge(MainActivity.list_vertices.get(j), MainActivity.list_vertices.get(i), Integer.parseInt(MainActivity.editTextEdgeWeight.getText().toString())));
                            }else
                            {
                                MainActivity.list_edges.add(new Edge(MainActivity.list_vertices.get(i), MainActivity.list_vertices.get(j), Integer.parseInt(MainActivity.editTextEdgeWeight.getText().toString())));
                                MainActivity.list_edges.add(new Edge(MainActivity.list_vertices.get(j), MainActivity.list_vertices.get(i), Integer.parseInt(MainActivity.editTextEdgeWeight.getText().toString())));
                            }
                        }
                    }
                }
            }

            if(MainActivity.allow_delete_edge_first)
            {
                for(int i = 0; i <  MainActivity.list_vertices.size(); i++)
                {
                    if(xTouch> MainActivity.list_vertices.get(i).point.x-75 && xTouch<MainActivity.list_vertices.get(i).point.x+75 && yTouch<MainActivity.list_vertices.get(i).point.y+75 && yTouch>MainActivity.list_vertices.get(i).point.y-75)
                    {
                        MainActivity.allow_delete_edge_first = false;
                        MainActivity.allow_delete_edge_second  = true;
                        MainActivity.vertex1 = MainActivity.list_vertices.get(i).value;
                        break;
                    }
                }
            }

            if(MainActivity.allow_delete_edge_second)
            {
                int j;
                for(j = 0; j <  MainActivity.list_vertices.size(); j++)
                {
                    if(xTouch> MainActivity.list_vertices.get(j).point.x-75 && xTouch<MainActivity.list_vertices.get(j).point.x+75 && yTouch<MainActivity.list_vertices.get(j).point.y+75 && yTouch>MainActivity.list_vertices.get(j).point.y-75)
                    {
                        if(MainActivity.list_vertices.get(j).value!=MainActivity.vertex1)
                        {
                            MainActivity.allow_delete_edge_second = false;
                            MainActivity.vertex2=MainActivity.list_vertices.get(j).value;
                            break;
                        }
                    }
                }

                for(int z = 0; z < MainActivity.list_edges.size(); )
                {
                    if(MainActivity.list_edges.get(z).vertex1.value==MainActivity.vertex1 && MainActivity.list_edges.get(z).vertex2.value==MainActivity.vertex2 || MainActivity.list_edges.get(z).vertex1.value==MainActivity.vertex2 && MainActivity.list_edges.get(z).vertex2.value==MainActivity.vertex1)
                    {
                        MainActivity.list_edges.remove(z);
                    }else z++;
                }
            }

            if(MainActivity.allow_delete_vertex)
            {
                for(int i = 0; i <  MainActivity.list_vertices.size(); i++)
                {
                    if(xTouch> MainActivity.list_vertices.get(i).point.x-75 && xTouch<MainActivity.list_vertices.get(i).point.x+75 && yTouch<MainActivity.list_vertices.get(i).point.y+75 && yTouch>MainActivity.list_vertices.get(i).point.y-75)
                    {
                        for(int j = 0; j < MainActivity.list_edges.size();)
                        {
                            if(MainActivity.list_vertices.get(i).value == MainActivity.list_edges.get(j).vertex1.value || MainActivity.list_vertices.get(i).value == MainActivity.list_edges.get(j).vertex2.value)
                            {
                                    MainActivity.list_edges.remove(j);
                            }
                            else j++;
                        }
                        MainActivity.list_vertices.remove(i);
                        MainActivity.allow_delete_vertex = false;
                        MainActivity.updateTextViewVertecies();
                        break;
                    }
                }
            }

            for(int i = 0; i <  MainActivity.list_vertices.size(); i++)
            {
                if(xTouch> MainActivity.list_vertices.get(i).point.x-75 && xTouch<MainActivity.list_vertices.get(i).point.x+75 && yTouch<MainActivity.list_vertices.get(i).point.y+75 && yTouch>MainActivity.list_vertices.get(i).point.y-75)
                {
                    MainActivity.list_vertices.get(i).point.x = (int)xTouch;
                    MainActivity.list_vertices.get(i).point.y = (int)yTouch;
                    break;
                }
            }
            invalidate();
            return true;
        }
    }
}
