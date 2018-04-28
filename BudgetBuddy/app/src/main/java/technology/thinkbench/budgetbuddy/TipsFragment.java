package technology.thinkbench.budgetbuddy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class TipsFragment extends android.support.v4.app.Fragment {

    ArrayList<TipItem> tips;
    TipsAdapter mTipsAdapter;

    public TipsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tips, container, false);

        String titles[] = getResources().getStringArray(R.array.link_title_array);
        String bodies[] = getResources().getStringArray(R.array.link_desc_array);
        String urls[] = getResources().getStringArray(R.array.link_url_array);

        tips = new ArrayList<>(titles.length);
        if(titles.length != bodies.length || titles.length != urls.length){
            //uneven arrays
            tips.add(new TipItem("Uneven Arrays", "check values/LinksPage.xml", ""));
        }else{
            //arrays are fine
            for(int i = 0; i < titles.length; i ++){
                tips.add(i, new TipItem(titles[i], bodies[i], urls[i]));
            }
        }

        ListView tipsView = (ListView) rootView.findViewById(R.id.list);
        mTipsAdapter = new TipsAdapter(this.getContext(), null);
        tipsView.setAdapter(mTipsAdapter);
        mTipsAdapter.addAll(tips);

        tipsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                TipItem currentTip = mTipsAdapter.getItem(position);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentTip.getUrl()));
                startActivity(intent);
            }
        });

        return rootView;
    }


}
