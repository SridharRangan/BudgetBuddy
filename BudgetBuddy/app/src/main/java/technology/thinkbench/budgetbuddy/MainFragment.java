package technology.thinkbench.budgetbuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;


public class MainFragment extends android.support.v4.app.Fragment {

    public MainFragment() {
        // Required empty public constructor
    }
    double d[] = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
    double m[] = {800,800,800,0,0,0,0,0,0,0,0,0,0,400,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        GraphView graph = (GraphView) rootView.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0,0)

        });
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {

                new DataPoint(0,800),
                new DataPoint(31,800)

        });
        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(new DataPoint[]{



        });

        series2.setColor(RED);
        series3.setColor(GREEN);
        double y=0;
        double x=0;
        for(int i=0;i<=31;i++) {
            y = y + 51.16;
            x = i;
            series.appendData(new DataPoint(x,y),true,31);
        }
        for(int j=0;j<=10;j++) {

            series3.appendData(new DataPoint(j,m[j]),true,10);
        }



        //set Manual X Bounds
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0.0);
        graph.getViewport().setMaxX(31.0);

        //set Manual Y Bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0.0);
        graph.getViewport().setMaxY(2000.0);

        graph.addSeries(series);
        graph.addSeries(series2);
        graph.addSeries(series3);
        return rootView;
    }


}
