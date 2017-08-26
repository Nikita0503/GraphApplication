package com.example.nik.graphapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity {
    private boolean allow_delete_vertex,
            allow_delete_edge_first,
            allow_delete_edge_second,
            allow_add_edge_first,
            allow_add_edge_second,
            click_way1,
            click_way2;
    int vertex1, vertex2;
    int RADIUS_OF_GRAB_VERTEX = 75;
    int RADIUS_OF_VERTEX = 100;
    int X_OF_TEXT = 10;
    int Y_OF_TEXT = 50;
    float xTouch, yTouch;
    int mass_edges[][];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent = getIntent();
        allow_delete_vertex = intent.getBooleanExtra("allowDeleteVertex", false);
        allow_add_edge_first = intent.getBooleanExtra("allowAddEdgeFirst", false);
        allow_add_edge_second = intent.getBooleanExtra("allowAddEdgeSecond", false);
        allow_delete_edge_first = intent.getBooleanExtra("allowDeleteEdgeFirst", false);
        allow_delete_edge_second = intent.getBooleanExtra("allowDeleteEdgeSecond", false);
        click_way1 = intent.getBooleanExtra("clickWay1", false);
        click_way2 = intent.getBooleanExtra("clickWay2", false);
        vertex1 = intent.getIntExtra("vertex1", 0);
        vertex2 = intent.getIntExtra("vertex2", 0);
        GraphicsView graphicsView = new GraphicsView(this);
        setContentView(graphicsView);
    }

    public class GraphicsView extends View {
        public GraphicsView(Context context) { super(context); }
        Paint p = new Paint();
        @Override
        protected void onDraw(Canvas canvas) {
            p.setTextSize(50);
            p.setStrokeWidth(10);
            p.setColor(Color.BLACK);
            if(allow_delete_vertex) {
                canvas.drawText("Please, click the vertex",  X_OF_TEXT,  Y_OF_TEXT, p);
            }

            if(allow_delete_edge_first || allow_add_edge_first || click_way1) {
                canvas.drawText("Please, click the first vertex",  X_OF_TEXT,  Y_OF_TEXT, p);
            }

            if(allow_delete_edge_second || allow_add_edge_second || click_way2) {
                canvas.drawText("Please, click the second vertex",  X_OF_TEXT,  Y_OF_TEXT, p);
            }

            for(int i = 0; i < MainActivity.edges.size(); i++) {
                canvas.drawLine(MainActivity.edges.get(i).vertex1.point.x, MainActivity.edges.get(i).vertex1.point.y,
                                MainActivity.edges.get(i).vertex2.point.x, MainActivity.edges.get(i).vertex2.point.y  ,p);
                canvas.drawText(String.valueOf(MainActivity.edges.get(i).weight),
                        (MainActivity.edges.get(i).vertex1.point.x + MainActivity.edges.get(i).vertex2.point.x)/2,
                        (MainActivity.edges.get(i).vertex1.point.y + MainActivity.edges.get(i).vertex2.point.y)/2-20, p);
            }

            if(MainActivity.printWay) {
                p.setColor(Color.GREEN);
                for(int i = 0; i < MainActivity.way.size(); i++) {
                    canvas.drawLine(MainActivity.way.get(i).vertex1.point.x, MainActivity.way.get(i).vertex1.point.y,
                                    MainActivity.way.get(i).vertex2.point.x, MainActivity.way.get(i).vertex2.point.y  ,p);
                }
            }

            for(int i = 0; i <  MainActivity.vertices.size(); i++) {
                p.setColor(Color.BLACK);
                canvas.drawCircle( MainActivity.vertices.get(i).point.x-1,  MainActivity.vertices.get(i).point.y-1, RADIUS_OF_VERTEX+2, p);
                p.setColor(Color.RED);
                canvas.drawCircle( MainActivity.vertices.get(i).point.x,  MainActivity.vertices.get(i).point.y, RADIUS_OF_VERTEX, p);
                p.setColor(Color.WHITE);
                p.setTextSize(50);
                if( MainActivity.vertices.get(i).value<10) canvas.drawText( MainActivity.vertices.get(i).value+"",
                    MainActivity.vertices.get(i).point.x-15,  MainActivity.vertices.get(i).point.y+15, p);
                else canvas.drawText(MainActivity.vertices.get(i).value+"",
                                     MainActivity.vertices.get(i).point.x-25,
                                     MainActivity.vertices.get(i).point.y+15, p);
            }
        }

        public boolean onTouchEvent(MotionEvent event) {
            xTouch = event.getX();
            yTouch = event.getY();
            if(click_way1) {
                for(int i = 0; i <  MainActivity.vertices.size(); i++) {
                    if(xTouch> MainActivity.vertices.get(i).point.x-RADIUS_OF_GRAB_VERTEX &&
                            xTouch<MainActivity.vertices.get(i).point.x+RADIUS_OF_GRAB_VERTEX &&
                            yTouch<MainActivity.vertices.get(i).point.y+RADIUS_OF_GRAB_VERTEX &&
                            yTouch>MainActivity.vertices.get(i).point.y-RADIUS_OF_GRAB_VERTEX) {
                        click_way1 = false;
                        click_way2  = true;
                        vertex1 = MainActivity.vertices.get(i).value;
                        break;
                    }
                }
            }

            if(click_way2) {
                int j;
                for(j = 0; j <  MainActivity.vertices.size(); j++) {
                    if(xTouch> MainActivity.vertices.get(j).point.x-RADIUS_OF_GRAB_VERTEX &&
                            xTouch<MainActivity.vertices.get(j).point.x+RADIUS_OF_GRAB_VERTEX &&
                            yTouch<MainActivity.vertices.get(j).point.y+RADIUS_OF_GRAB_VERTEX &&
                            yTouch>MainActivity.vertices.get(j).point.y-RADIUS_OF_GRAB_VERTEX) {
                        if(MainActivity.vertices.get(j).value != vertex1) {
                            click_way2 = false;
                            vertex2=MainActivity.vertices.get(j).value;
                            break;
                        }
                    }
                }
                Solution solution = new Solution(vertex1, vertex2);
                try {
                    solution.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if(allow_add_edge_first) {
                for(int i = 0; i <  MainActivity.vertices.size(); i++) {
                    if(xTouch> MainActivity.vertices.get(i).point.x-RADIUS_OF_GRAB_VERTEX &&
                            xTouch<MainActivity.vertices.get(i).point.x+RADIUS_OF_GRAB_VERTEX &&
                            yTouch<MainActivity.vertices.get(i).point.y+RADIUS_OF_GRAB_VERTEX &&
                            yTouch>MainActivity.vertices.get(i).point.y-RADIUS_OF_GRAB_VERTEX) {
                        allow_add_edge_first = false;
                        allow_add_edge_second  = true;
                        vertex1 = MainActivity.vertices.get(i).value;
                        break;
                    }
                }
            }

            if(allow_add_edge_second) {
                int j;
                for(j = 0; j <  MainActivity.vertices.size(); j++) {
                    if(xTouch> MainActivity.vertices.get(j).point.x-RADIUS_OF_GRAB_VERTEX &&
                            xTouch<MainActivity.vertices.get(j).point.x+RADIUS_OF_GRAB_VERTEX &&
                            yTouch<MainActivity.vertices.get(j).point.y+RADIUS_OF_GRAB_VERTEX &&
                            yTouch>MainActivity.vertices.get(j).point.y-RADIUS_OF_GRAB_VERTEX) {
                        if(MainActivity.vertices.get(j).value!=vertex1) {
                            allow_add_edge_second = false;
                            vertex2 = MainActivity.vertices.get(j).value;
                            break;
                        }
                    }
                }

                for(int i = 0; i < MainActivity.vertices.size(); i++) {
                    for(j = 0; j < MainActivity.vertices.size(); j++) {
                        if(MainActivity.vertices.get(i).value == vertex1 &&
                                MainActivity.vertices.get(j).value == vertex2 ||
                                MainActivity.vertices.get(i).value == vertex2 &&
                                        MainActivity.vertices.get(j).value == vertex1) {
                            if(!MainActivity.edges.isEmpty()) {
                                for (int z = 0; z < MainActivity.edges.size(); z++) {
                                    if (MainActivity.edges.get(z).vertex1.value == vertex1 &&
                                            MainActivity.edges.get(z).vertex2.value == vertex2 ||
                                            MainActivity.edges.get(z).vertex2.value == vertex1 &&
                                                    MainActivity.edges.get(z).vertex1.value == vertex2) {
                                        MainActivity.edges.remove(z);
                                    }
                                }
                                MainActivity.edges.add(new Edge(MainActivity.vertices.get(i), MainActivity.vertices.get(j),
                                        Integer.parseInt(MainActivity.editTextEdgeWeight.getText().toString())));
                                    MainActivity.edges.add(new Edge(MainActivity.vertices.get(j), MainActivity.vertices.get(i),
                                        Integer.parseInt(MainActivity.editTextEdgeWeight.getText().toString())));
                            }else {
                                MainActivity.edges.add(new Edge(MainActivity.vertices.get(i), MainActivity.vertices.get(j),
                                        Integer.parseInt(MainActivity.editTextEdgeWeight.getText().toString())));
                                MainActivity.edges.add(new Edge(MainActivity.vertices.get(j), MainActivity.vertices.get(i),
                                        Integer.parseInt(MainActivity.editTextEdgeWeight.getText().toString())));
                            }
                        }
                    }
                }
            }

            if(allow_delete_edge_first) {
                for(int i = 0; i <  MainActivity.vertices.size(); i++) {
                    if(xTouch> MainActivity.vertices.get(i).point.x-RADIUS_OF_GRAB_VERTEX &&
                            xTouch<MainActivity.vertices.get(i).point.x+RADIUS_OF_GRAB_VERTEX &&
                            yTouch<MainActivity.vertices.get(i).point.y+RADIUS_OF_GRAB_VERTEX &&
                            yTouch>MainActivity.vertices.get(i).point.y-RADIUS_OF_GRAB_VERTEX) {
                        allow_delete_edge_first = false;
                        allow_delete_edge_second  = true;
                        vertex1 = MainActivity.vertices.get(i).value;
                        break;
                    }
                }
            }

            if(allow_delete_edge_second) {
                int j;
                for(j = 0; j <  MainActivity.vertices.size(); j++) {
                    if(xTouch> MainActivity.vertices.get(j).point.x-RADIUS_OF_GRAB_VERTEX &&
                            xTouch<MainActivity.vertices.get(j).point.x+RADIUS_OF_GRAB_VERTEX &&
                            yTouch<MainActivity.vertices.get(j).point.y+RADIUS_OF_GRAB_VERTEX &&
                            yTouch>MainActivity.vertices.get(j).point.y-RADIUS_OF_GRAB_VERTEX) {
                        if(MainActivity.vertices.get(j).value!=vertex1)
                        {
                            allow_delete_edge_second = false;
                            vertex2=MainActivity.vertices.get(j).value;
                            break;
                        }
                    }
                }

                for(int z = 0; z < MainActivity.edges.size(); ) {
                    if(MainActivity.edges.get(z).vertex1.value==vertex1 &&
                            MainActivity.edges.get(z).vertex2.value==vertex2
                            || MainActivity.edges.get(z).vertex1.value==vertex2
                            && MainActivity.edges.get(z).vertex2.value==vertex1) {
                        MainActivity.edges.remove(z);
                    }else z++;
                }
            }

            if(allow_delete_vertex) {
                for(int i = 0; i <  MainActivity.vertices.size(); i++) {
                    if(xTouch> MainActivity.vertices.get(i).point.x-RADIUS_OF_GRAB_VERTEX &&
                            xTouch<MainActivity.vertices.get(i).point.x+RADIUS_OF_GRAB_VERTEX &&
                            yTouch<MainActivity.vertices.get(i).point.y+RADIUS_OF_GRAB_VERTEX &&
                            yTouch>MainActivity.vertices.get(i).point.y-RADIUS_OF_GRAB_VERTEX) {
                        for(int j = 0; j < MainActivity.edges.size();) {
                            if(MainActivity.vertices.get(i).value == MainActivity.edges.get(j).vertex1.value ||
                                    MainActivity.vertices.get(i).value == MainActivity.edges.get(j).vertex2.value)
                            {
                                    MainActivity.edges.remove(j);
                            }
                            else j++;
                        }
                        MainActivity.vertices.remove(i);
                        allow_delete_vertex = false;
                        MainActivity.updateTextViewVertecies();
                        break;
                    }
                }
            }

            for(int i = 0; i <  MainActivity.vertices.size(); i++) {
                if(xTouch> MainActivity.vertices.get(i).point.x-RADIUS_OF_GRAB_VERTEX &&
                        xTouch<MainActivity.vertices.get(i).point.x+RADIUS_OF_GRAB_VERTEX &&
                        yTouch<MainActivity.vertices.get(i).point.y+RADIUS_OF_GRAB_VERTEX &&
                        yTouch>MainActivity.vertices.get(i).point.y-RADIUS_OF_GRAB_VERTEX) {
                    MainActivity.vertices.get(i).point.x = (int)xTouch;
                    MainActivity.vertices.get(i).point.y = (int)yTouch;
                    break;
                }
            }
            invalidate();
            return true;
        }
    }
}
