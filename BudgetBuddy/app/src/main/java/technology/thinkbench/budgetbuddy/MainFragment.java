package technology.thinkbench.budgetbuddy;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import technology.thinkbench.budgetbuddy.data.DataContract;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;


public class MainFragment extends android.support.v4.app.Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>{

    /** Identifier for the expenditure data loader */
    private static final int EXPENDITURE_LOADER = 0;

    //graph parameters, for convenient tinkering
    private static final float GRAPH_TEXT_SIZE = 15;
    private static final float GRAPH_LINE_WIDTH = 3; //can be from .2 to 10
    private static final float GRAPH_DL_PARAMS[] = {8, 6, 0}; //(line length, space length, offset)

    LineChart graph;
    private double perDay[];
    private double aggregate[];
    TextView a1, a2;

    Calendar lastCheck, today;

    // data for graph
    private float saving_target;
    private float saving_amt;
    private float monthly_income;
    private float saving_amt_fixed;
    private String saving_type;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        perDay = new double[31];
        aggregate = new double[31];

        for(int i = 0; i < 31; i++){
            perDay[i] = 0;
            aggregate[i] = 0;
        }

        a1 = (TextView) rootView.findViewById(R.id.analysis_one);
        a2 = (TextView) rootView.findViewById(R.id.analysis_two);

        SharedPreferences sharedPreferences = getDefaultSharedPreferences(getContext());
        monthly_income = sharedPreferences.getFloat("Income", 0);
        saving_amt = sharedPreferences.getFloat("Target", 0);
        saving_type = sharedPreferences.getString("Type", "$");

        if(saving_type.equalsIgnoreCase("$")){
            saving_target = monthly_income - saving_amt;
            saving_amt_fixed = saving_amt;
        }else{
            saving_target = monthly_income - (monthly_income * (saving_amt/100));
            saving_amt_fixed = monthly_income * (saving_amt/100);
        }

        TextView summary = (TextView) rootView.findViewById(R.id.summary_text);
        Calendar today = Calendar.getInstance(TimeZone.getDefault());
        String topText;
        switch(today.get(Calendar.DAY_OF_WEEK)){
            case 1:
                topText = "Sunday ";
                break;
            case 2:
                topText = "Monday ";
                break;
            case 3:
                topText = "Tuesday ";
                break;
            case 4:
                topText = "Wednesday ";
                break;
            case 5:
                topText = "Thursday ";
                break;
            case 6:
                topText = "Friday ";
                break;
            case 7:
                topText = "Saturday ";
                break;
            default:
                topText = "";
                break;
        }
        switch(today.get(Calendar.MONTH)){
            case 0:
                topText += "January ";
                break;
            case 1:
                topText += "February ";
                break;
            case 2:
                topText += "March ";
                break;
            case 3:
                topText += "April ";
                break;
            case 4:
                topText += "May ";
                break;
            case 5:
                topText += "June ";
                break;
            case 6:
                topText += "July ";
                break;
            case 7:
                topText += "August ";
                break;
            case 8:
                topText += "September ";
                break;
            case 9:
                topText += "October ";
                break;
            case 10:
                topText += "November ";
                break;
            case 11:
                topText += "December ";
                break;
            default:
                topText = "";
                break;
        }
        if(!topText.equalsIgnoreCase("")){
            topText += Integer.toString(today.get(Calendar.DAY_OF_MONTH)) + "\n";
        }
        if(sharedPreferences.contains("Summary")){
            topText += sharedPreferences.getString("Summary", "Save Save Save");
        }else{
            topText += "Keep on saving!";
        }
        summary.setText(topText);

        Log.d("DEBUG", Float.toString(monthly_income));
        Log.d("DEBUG", Float.toString(saving_amt) + saving_type);
        Log.d("DEBUG", Float.toString(saving_target));

        //General LineChart setup
        graph = (LineChart) rootView.findViewById(R.id.graph);
        graph.setDescription(null);
        graph.setDragEnabled(false);
        graph.setDoubleTapToZoomEnabled(false);
        graph.setPinchZoom(false);
        graph.setDrawGridBackground(false);
        graph.setDrawMarkers(false);
        graph.getLegend().setTextSize(GRAPH_TEXT_SIZE);

        // set Y axis stuff
        graph.getAxisLeft().setAxisMinimum(0);
        graph.getAxisLeft().setAxisMaximum(monthly_income);
        graph.getAxisLeft().setTextSize(GRAPH_TEXT_SIZE);
        graph.getAxisLeft().setDrawGridLines(false);
        graph.getAxisRight().setEnabled(false);
        // set X axis stuff
        graph.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        graph.getXAxis().setDrawGridLines(false);
        graph.getXAxis().setAxisMinimum(0);
        graph.getXAxis().setAxisMaximum(31);
        graph.getXAxis().setTextSize(GRAPH_TEXT_SIZE);

        // Kick off the loader
        getLoaderManager().initLoader(EXPENDITURE_LOADER, null, this).forceLoad();

        return rootView;
    }

    private void resetDB(){
        int rowsDeleted = getContext().getContentResolver().delete(DataContract.ExpenditureEntry.CONTENT_URI, null, null);
        Log.d("ResetActivity", rowsDeleted + " rows deleted from expenditure database");
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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        graph.getXAxis().setAxisMaximum(daysInMonth);
        graph.clear();
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
        for(int i = 1; i < 31; i++){
            Log.d("DEBUG", Double.toString(perDay[i]));
            aggregate[i] = aggregate[i-1] + perDay[i];
        }

        //checking for time period rollover
        boolean reset = false;
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        today = Calendar.getInstance(TimeZone.getDefault());
        lastCheck = Calendar.getInstance(TimeZone.getDefault());
        if(sharedPreferences.contains("Month")){
            int month = sharedPreferences.getInt("Month", today.get(Calendar.MONTH));
            int year = sharedPreferences.getInt("Year", today.get(Calendar.YEAR));
            if(today.get(Calendar.YEAR) > year || today.get(Calendar.MONTH) > month){
                //OMG Reset
                String summary = "";
                Float margin;
                NumberFormat nf = NumberFormat.getCurrencyInstance();
                if(aggregate[30] > saving_target){
                    margin = (float)aggregate[30] - saving_target;
                    summary = "Last month you wanted to save " + nf.format(saving_amt_fixed) + ", and unfortunately you missed your target by " + nf.format(margin) + ".";
                }else{
                    margin = saving_target - (float)aggregate[30];
                    summary = "Congratulations! Last month you wanted to save " + nf.format(saving_amt_fixed) + ", and you managed to save " + nf.format(margin + saving_amt_fixed) + "!";
                }
                editor.putString("Summary", summary);
                resetDB();
                reset = true;
                //end reset
            }
        }
        editor.putInt("Month", today.get(Calendar.MONTH));
        editor.putInt("Year", today.get(Calendar.YEAR));
        editor.apply();
        if(reset){
            getLoaderManager().restartLoader(EXPENDITURE_LOADER, null, this).forceLoad();
        }

        //rescale graph if aggregate is higher than income
        graph.getAxisLeft().setAxisMaximum((float)Math.max(monthly_income, aggregate[day-1]));

        //target line
        LineDataSet target = new LineDataSet(null, "Target");
        target.addEntry(new Entry((float)0, saving_target));
        target.addEntry(new Entry((float)daysInMonth, saving_target));
        target.setAxisDependency(YAxis.AxisDependency.LEFT);
        target.setColor(Color.RED);
        target.setDrawValues(false);
        target.setLineWidth(GRAPH_LINE_WIDTH);

        //initial trace
        LineDataSet trace = new LineDataSet(null, "Trace");
        trace.addEntry(new Entry((float)0, 0));
        trace.addEntry(new Entry((float)daysInMonth, saving_target));
        trace.setAxisDependency(YAxis.AxisDependency.LEFT);
        trace.setColor(Color.BLACK);
        trace.enableDashedLine(GRAPH_DL_PARAMS[0], GRAPH_DL_PARAMS[1], GRAPH_DL_PARAMS[2]);
        trace.setDrawValues(false);
        trace.setLineWidth(GRAPH_LINE_WIDTH);

        //aggregate spending
        LineDataSet agg = new LineDataSet(null, "Spending");
        agg.addEntry(new Entry(0, 0));
        for(int i =0; i < day; i++){
            agg.addEntry(new Entry((float)(i + 1), (float)aggregate[i]));
        }
        agg.setAxisDependency(YAxis.AxisDependency.LEFT);
        agg.setColor(Color.GREEN);
        agg.setDrawValues(false);
        agg.setDrawCircles(false);
        agg.setLineWidth(GRAPH_LINE_WIDTH);

        //current projection
        LineDataSet projection = new LineDataSet(null, "Projection");
        projection.addEntry(new Entry((float)day, (float)aggregate[day - 1]));
        projection.addEntry(new Entry((float)daysInMonth, saving_target));
        projection.setAxisDependency(YAxis.AxisDependency.LEFT);
        projection.setColor(Color.BLUE);
        projection.enableDashedLine(GRAPH_DL_PARAMS[0], GRAPH_DL_PARAMS[1], GRAPH_DL_PARAMS[2]);
        projection.setDrawValues(false);
        projection.setLineWidth(GRAPH_LINE_WIDTH);

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(target);
        dataSets.add(trace);
        dataSets.add(agg);
        dataSets.add(projection);

        LineData graphPrep = new LineData(dataSets);
        graph.setData(graphPrep);
        graph.invalidate(); // refresh

        //do analysis here
        double traceSlope, projectionSlope, remainingSpace, avgSpending, avgDiff;
        int daysLeft;
        boolean over = false, met = false;
        daysLeft = daysInMonth - day + 1; //add 1 to count current day
        traceSlope = (saving_target)/(daysInMonth);
        projectionSlope = ((saving_target - aggregate[day])/(daysLeft));
        avgSpending = (aggregate[day]/day);
        remainingSpace = (saving_target - aggregate[day]);
        avgDiff = avgSpending - projectionSlope;
        //now do something with this info
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        String aOne = "", aTwo = "";
        if(remainingSpace > 0){
            aOne = "You have " + Integer.toString(daysLeft) + " days left in the month, and you can only spend " + nf.format(remainingSpace) + " more. ";
            aOne += "This means you have to keep your spending below an average of " + nf.format(projectionSlope) + " a day.";
        }else if(remainingSpace == 0){
            over = true;
            met = true;
            aOne = "You have " + Integer.toString(daysLeft) + " days left in the month, and you have exactly met your spending target.";
        }else{
            over = true;
            aOne = "You have " + Integer.toString(daysLeft) + " days left in the month, and you have already gone over your saving target by " + nf.format((remainingSpace * -1)) + ".";
        }
        if(avgDiff > 0 && !over){
            aTwo = "Your spending has averaged " + nf.format(avgSpending) + " a day, which is " + nf.format(avgDiff) + " above what it needs to be to hit your target. ";
            aTwo += "Try to keep your discretionary purchases to a minimum for the rest of the month.";
        }else if (!over){
            aTwo = "Your spending has averaged " + nf.format(avgSpending) + " a day, which is in the range it needs to be to hit your target. Keep up the good work!";
        }else if(met){
            aTwo = "Try to hold off on purchases if you can, you're so close to meeting your goal!";
        }else{
            aTwo = "Even though you are past your target, you can still keep discretionary purchases to a minimum. Not hitting your target doesn't mean you should spend the rest of your money anyway.";
        }

        a1.setText(aOne);
        a2.setText(aTwo);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }

}
