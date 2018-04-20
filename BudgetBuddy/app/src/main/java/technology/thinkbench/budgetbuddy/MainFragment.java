package technology.thinkbench.budgetbuddy;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import technology.thinkbench.budgetbuddy.data.DataContract;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.preference.PreferenceManager.getDefaultSharedPreferences;


public class MainFragment extends android.support.v4.app.Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>{

    /** Identifier for the pet data loader */
    private static final int EXPENDITURE_LOADER = 0;

    /** Adapter for the ListView */
    ExpenditureCursorAdapter mCursorAdapter;

    GraphView graph;
    private double perDay[];
    private double aggregate[];

    // data for graph
    private double saving_target;
    private float saving_amt;
    private float monthly_income;
    private String saving_type;

    public MainFragment() {
        // Required empty public constructor
    }
<<<<<<< HEAD
    double d[] = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
    double m[] = {800,800,800,0,0,0,0,0,1000,0,0,0,0,400,0,0,0,0,0,0,0,200,0,0,20,0,0,0,0,0,0};
=======
>>>>>>> willis

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

<<<<<<< HEAD
        GraphView graph = (GraphView) rootView.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0,0)

        });
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {

                new DataPoint(0,1600),
                new DataPoint(31,1600)

=======
        perDay = new double[31];
        aggregate = new double[31];

        for(int i = 0; i < 31; i++){
            perDay[i] = 0;
            aggregate[i] = 0;
        }

        SharedPreferences sharedPreferences = getDefaultSharedPreferences(getContext());
        monthly_income = sharedPreferences.getFloat("Income", 0);
        saving_amt = sharedPreferences.getFloat("Target", 0);
        saving_type = sharedPreferences.getString("Type", "$");

        if(saving_type.equalsIgnoreCase("$")){
            saving_target = monthly_income - saving_amt;
        }else{
            saving_target = monthly_income * (saving_amt/100);
        }

        Log.d("DEBUG", Float.toString(monthly_income));
        Log.d("DEBUG", Float.toString(saving_amt) + saving_type);
        Log.d("DEBUG", Double.toString(saving_target));

        mCursorAdapter = new ExpenditureCursorAdapter(this.getContext(), null);
        graph = (GraphView) rootView.findViewById(R.id.graph);

        graph.setTitle("Spending");
        // set manual X bounds
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(1); // set the min value of the viewport of x axis
        graph.getViewport().setMaxX(31); // set the max value of the viewport of x-axix
        // set manual Y bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);  // set the min value of the viewport of y axis
        graph.getViewport().setMaxY(monthly_income);    // set the max value of the viewport of y-axis

        // Kick off the loader
        getLoaderManager().initLoader(EXPENDITURE_LOADER, null, this).forceLoad();

        return rootView;
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                DataContract.ExpenditureEntry._ID,
                DataContract.ExpenditureEntry.COLUMN_EXPENDITURE_LABEL,
                DataContract.ExpenditureEntry.COLUMN_EXPENDITURE_AMOUNT,
                DataContract.ExpenditureEntry.COLUMN_EXPENDITURE_DATE};

        // This loader will execute the ContentProvider's query method on a background thread
        return (new android.support.v4.content.CursorLoader(getActivity(),   // Parent activity context
                DataContract.ExpenditureEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null));                  // Default sort order
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        graph.removeAllSeries();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        try {
            if (data != null) {
                if (data.moveToFirst()) {
                    do {
                        Date date = formatter.parse(data.getString(data.getColumnIndex("date")));
                        Log.d("DEBUG", date.toString());
                        Log.d("DEBUG", Integer.toString(date.getDate()));
                        perDay[date.getDate() - 1] += data.getDouble(data.getColumnIndex("amount"));
                    } while (data.moveToNext());
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            data.close();
        }

        aggregate[0] = perDay[0];
        Log.d("DEBUG", Double.toString(perDay[0]));
        for(int i = 1; i < day; i++){
            Log.d("DEBUG", Double.toString(perDay[i]));
            aggregate[i] = aggregate[i-1] + perDay[i];
        }

        LineGraphSeries<DataPoint> target = new LineGraphSeries<>(new DataPoint[] {
            new DataPoint(0,saving_target),
            new DataPoint(31,saving_target)
        });

        LineGraphSeries<DataPoint> agg = new LineGraphSeries<>(new DataPoint[] {
            new DataPoint(0, 0)
>>>>>>> willis
        });
        for(int i =0; i < day; i++){
            agg.appendData(new DataPoint((i + 1), aggregate[i]), true, day);
        }

<<<<<<< HEAD


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
        series3.appendData(new DataPoint(0,0),true,1);
        for(int j=0;j<=30;j++) {

            series3.appendData(new DataPoint(j+1,m[j]),true,31);
        }



        //set Manual X Bounds
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0.0);
        graph.getViewport().setMaxX(31.0);

        //set Manual Y Bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0.0);
        graph.getViewport().setMaxY(2000.0);
=======
        LineGraphSeries<DataPoint> projection = new LineGraphSeries<>(new DataPoint[]{

            new DataPoint(day, aggregate[day - 1]),
            new DataPoint(31, saving_target)

        });
>>>>>>> willis

        LineGraphSeries<DataPoint> trace = new LineGraphSeries<>(new DataPoint[]{

                new DataPoint(0, 0),
                new DataPoint(31, saving_target)

        });

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setColor(RED);
        paint.setPathEffect(new DashPathEffect(new float[]{8, 5}, 0));
        projection.setDrawAsPath(true);
        projection.setCustomPaint(paint);

        Paint paint2 = new Paint();
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeWidth(4);
        paint2.setColor(BLUE);
        paint2.setPathEffect(new DashPathEffect(new float[]{8, 5}, 0));
        trace.setDrawAsPath(true);
        trace.setCustomPaint(paint2);

        agg.setColor(GREEN);

        graph.addSeries(target);
        graph.addSeries(agg);
        graph.addSeries(projection);
        graph.addSeries(trace);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

}
