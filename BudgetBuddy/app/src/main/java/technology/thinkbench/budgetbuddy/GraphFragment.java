package technology.thinkbench.budgetbuddy;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.ParseException;
import java.util.ArrayList;

import technology.thinkbench.budgetbuddy.data.DataContract;


public class GraphFragment extends android.support.v4.app.Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>{

    /** Identifier for the expenditure data loader */
    private static final int EXPENDITURE_LOADER = 0;

    private static final float GRAPH_TEXT_SIZE = 15;
    private static final float GRAPH_VALUE_SIZE = 15;

    private PieChart breakdown, ness;

    public GraphFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_graph, container, false);

        breakdown = (PieChart) rootView.findViewById(R.id.spending_breakdown);
        ness = (PieChart) rootView.findViewById(R.id.ness_breakdown);

        breakdown.setHoleRadius(50f);
        breakdown.setTransparentCircleAlpha(0);
        breakdown.setCenterText("Your Spending");
        breakdown.setDescription(null);
        breakdown.setDrawEntryLabels(true);
        breakdown.setCenterTextSize(GRAPH_TEXT_SIZE);
        breakdown.setHoleColor(Color.LTGRAY);

        ness.setHoleRadius(50f);
        ness.setTransparentCircleAlpha(0);
        ness.setCenterText("Necessary Spending");
        ness.setDescription(null);
        ness.setDrawEntryLabels(true);
        ness.setCenterTextSize(GRAPH_TEXT_SIZE);
        ness.setHoleColor(Color.LTGRAY);

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
                DataContract.ExpenditureEntry.COLUMN_EXPENDITURE_DATE,
                DataContract.ExpenditureEntry.COLUMN_EXPENDITURE_NESS,
                DataContract.ExpenditureEntry.COLUMN_EXPENDITURE_TAG};

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
        String []tagLabels = getResources().getStringArray(R.array.tags_array);
        String []nessLabels = getResources().getStringArray(R.array.ness_array);

        float nessData[] = {0, 0, 0};
        ArrayList<Float> tagSum = new ArrayList<>();

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        ArrayList<PieEntry> nessEntries = new ArrayList<>();

        for(int i = 0; i < tagLabels.length; i++){
            tagSum.add(i, new Float(0));
        }

        Log.d("DEBUG", "tagLabels length: " + Integer.toString(tagLabels.length));
        Log.d("DEBUG", "tagSum length: " + Integer.toString(tagSum.size()));

        //load data here
        String tagTemp;
        if (data != null) {
            if (data.moveToFirst()) {
                do {
                    tagTemp = data.getString(data.getColumnIndex("necessary"));
                    switch (tagTemp){
                        case "Unspecified":
                            nessData[0] += data.getFloat(data.getColumnIndex("amount"));
                            break;
                        case "Necessary":
                            nessData[1] += data.getFloat(data.getColumnIndex("amount"));
                            break;
                        case "Discretionary":
                            nessData[2] += data.getFloat(data.getColumnIndex("amount"));
                            break;
                    }
                    tagTemp = data.getString(data.getColumnIndex("tag"));
                    for(int i = 0; i < tagLabels.length; i++){
                        if(tagLabels[i].equalsIgnoreCase(tagTemp)){
                            tagSum.set(i, new Float(tagSum.get(i).floatValue() + data.getFloat(data.getColumnIndex("amount"))));
                            Log.d("DEBUG", "new value: " + tagSum.get(i).toString());
                            break;
                        }
                    }

                } while (data.moveToNext());
            }
        }
        data.close();
        //end data loading

        for(int i=0; i < tagLabels.length; i++){
            if(tagSum.get(i) > 0){
                pieEntries.add(new PieEntry(tagSum.get(i).floatValue(), tagLabels[i]));
            }
        }
        for(int i = 0; i < nessData.length; i++){
            if(nessData[i] > 0){
                nessEntries.add(new PieEntry(nessData[i], nessLabels[i]));
            }
        }
        //create the data set
        PieDataSet pieDataSet = new PieDataSet(pieEntries,"");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(GRAPH_VALUE_SIZE);
        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GRAY);
        colors.add(Color.GREEN);
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.CYAN);
        pieDataSet.setColors(colors);
        //add legend to chart
        Legend legend = breakdown.getLegend();
        legend.setTextSize(GRAPH_TEXT_SIZE);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setXOffset(20);
        legend.setYOffset(20);
        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        breakdown.setData(pieData);
        breakdown.invalidate();
        //done

        //create the data set
        PieDataSet pieDataSet2 = new PieDataSet(nessEntries,"");
        pieDataSet2.setSliceSpace(2);
        pieDataSet2.setValueTextSize(GRAPH_VALUE_SIZE);
        //add colors to dataset
        pieDataSet2.setColors(colors);
        //add legend to chart
        Legend legend2 = ness.getLegend();
        legend2.setTextSize(GRAPH_TEXT_SIZE);
        legend2.setForm(Legend.LegendForm.CIRCLE);
        legend2.setXOffset(20);
        legend2.setYOffset(20);
        //create pie data object
        PieData pieData2 = new PieData(pieDataSet2);
        ness.setData(pieData2);
        ness.invalidate();
        //done
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }

}
