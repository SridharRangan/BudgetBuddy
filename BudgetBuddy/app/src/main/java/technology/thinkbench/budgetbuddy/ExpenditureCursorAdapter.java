package technology.thinkbench.budgetbuddy;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.NumberFormat;

import technology.thinkbench.budgetbuddy.data.DataContract.ExpenditureEntry;

public class ExpenditureCursorAdapter extends CursorAdapter{

    public ExpenditureCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView summaryTextView = (TextView) view.findViewById(R.id.summary);

        // Find the columns of expenditure attributes that we're interested in
        int labelColumnIndex = cursor.getColumnIndex(ExpenditureEntry.COLUMN_EXPENDITURE_LABEL);
        int amountColumnIndex = cursor.getColumnIndex(ExpenditureEntry.COLUMN_EXPENDITURE_AMOUNT);

        NumberFormat nf = NumberFormat.getCurrencyInstance();

        // Read the expenditure attributes from the Cursor
        String label = cursor.getString(labelColumnIndex);
        String amount = nf.format(cursor.getDouble(amountColumnIndex));

        // Update the TextViews with the attributes for the current pet
        nameTextView.setText(label);
        summaryTextView.setText(amount);
    }

}
