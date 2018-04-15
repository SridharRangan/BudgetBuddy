package technology.thinkbench.budgetbuddy;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import technology.thinkbench.budgetbuddy.data.DataContract;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;


public class MainFragment extends android.support.v4.app.Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>{

    /** Identifier for the pet data loader */
    private static final int EXPENDITURE_LOADER = 0;

    /** Adapter for the ListView */
    ExpenditureCursorAdapter mCursorAdapter;

    GraphView graph;

    // Data for graph
    int x[] = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
    int y[] = {800,800,800};

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mCursorAdapter = new ExpenditureCursorAdapter(this.getContext(), null);
        graph = (GraphView) rootView.findViewById(R.id.graph);

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
                DataContract.ExpenditureEntry.COLUMN_EXPENDITURE_AMOUNT };

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

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 3),
                new DataPoint(2, 9)
        });
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 2),
                new DataPoint(2, 4)
        });
        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(new DataPoint[]{

                new DataPoint(0, 1),
                new DataPoint(1, 3),
                new DataPoint(2, 2)

        });
        series2.setColor(RED);
        series3.setColor(GREEN);

        graph.addSeries(series);
        graph.addSeries(series2);
        graph.addSeries(series3);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

}
