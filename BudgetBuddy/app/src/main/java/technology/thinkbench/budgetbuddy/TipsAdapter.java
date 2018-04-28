package technology.thinkbench.budgetbuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by willis on 4/27/18.
 */

public class TipsAdapter extends ArrayAdapter<TipItem>{

    public TipsAdapter(Context context, ArrayList<TipItem> feedItems) {
        super(context, -1, new ArrayList<TipItem>());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.link_item, parent, false);
        }

        // Find the element at the given position in the list of earthquakes
        TipItem currentTip = getItem(position);

        // Title
        TextView titleView = (TextView) listItemView.findViewById(R.id.name);
        titleView.setText(currentTip.getTitle());

        // Body
        TextView bodyView = (TextView) listItemView.findViewById(R.id.summary);
        bodyView.setText(currentTip.getBody());

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

}
