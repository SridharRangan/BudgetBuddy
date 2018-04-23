package technology.thinkbench.budgetbuddy;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import technology.thinkbench.budgetbuddy.data.DataContract.ExpenditureEntry;

public class HistoryFragment extends android.support.v4.app.Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    /** Identifier for the pet data loader */
    private static final int EXPENDITURE_LOADER = 0;

    /** Adapter for the ListView */
    ExpenditureCursorAdapter mCursorAdapter;
    ListView expenditureListView;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        // Find the ListView which will be populated with the expenditure data
        expenditureListView = (ListView) rootView.findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = rootView.findViewById(R.id.empty_view);
        expenditureListView.setEmptyView(emptyView);

        mCursorAdapter = new ExpenditureCursorAdapter(this.getContext(), null);
        expenditureListView.setAdapter(mCursorAdapter);

        //setListViewHeightBasedOnChildren(expenditureListView);

        // Kick off the loader
        getLoaderManager().initLoader(EXPENDITURE_LOADER, null, this).forceLoad();

        return rootView;
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                ExpenditureEntry._ID,
                ExpenditureEntry.COLUMN_EXPENDITURE_LABEL,
                ExpenditureEntry.COLUMN_EXPENDITURE_AMOUNT,
                ExpenditureEntry.COLUMN_EXPENDITURE_TAG,
                ExpenditureEntry.COLUMN_EXPENDITURE_NESS};

        // This loader will execute the ContentProvider's query method on a background thread
        return (new android.support.v4.content.CursorLoader(getActivity(),   // Parent activity context
                ExpenditureEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null));                  // Default sort order
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

}
