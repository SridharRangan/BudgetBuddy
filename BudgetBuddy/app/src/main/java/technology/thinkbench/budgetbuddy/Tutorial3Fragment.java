package technology.thinkbench.budgetbuddy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;


public class Tutorial3Fragment extends android.support.v4.app.Fragment {

    private Spinner spinner;
    private String type;

    EditText income;
    EditText target;

    public Tutorial3Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tutorial3, container, false);

        income = (EditText) rootView.findViewById(R.id.income_entry);
        target = (EditText) rootView.findViewById(R.id.target_entry);
        spinner = (Spinner) rootView.findViewById(R.id.type_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),R.array.type_array,
                android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                type = parent.getItemAtPosition(pos).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }

    public boolean finish_tut(){
        Log.d("DEBUG", "Began finish_tut");
        String temp = income.getText().toString();
        String temp2 = target.getText().toString();
        float monthly, saving;
        //Log.d("DEBUG", "temp: " + temp);
        //Log.d("DEBUG", "temp2: " + temp2);
        if(temp.equalsIgnoreCase("")){
            Log.d("DEBUG", "income blank");
            Toast.makeText(getContext(), "Enter your monthly income to continue", Toast.LENGTH_SHORT).show();
            income.requestFocus();
            return false;
        }else if(temp2.equalsIgnoreCase("")){
            Log.d("DEBUG", "target blank");
            Toast.makeText(getContext(), "Please pick a saving target", Toast.LENGTH_SHORT).show();
            target.requestFocus();
            return false;
        }
        monthly = Float.parseFloat(temp);
        saving = Float.parseFloat(temp2);
        if(monthly < 0 || saving < 0){
            Log.d("DEBUG", "under zero");
            Toast.makeText(getContext(), "Please enter values greater than zero", Toast.LENGTH_SHORT).show();
            return false;
        }else if(type.equalsIgnoreCase("%") && saving > 100){
            Log.d("DEBUG", "over 100%");
            Toast.makeText(getContext(), "You can't save more than 100% of your income!", Toast.LENGTH_SHORT).show();
            return false;
        }else if(type.equalsIgnoreCase("$") && saving > monthly){
            Log.d("DEBUG", "saving > monthly");
            Toast.makeText(getContext(), "You can't save more than you make!", Toast.LENGTH_SHORT).show();
            return false;
        }
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("Income", monthly);
        editor.putFloat("Target", saving);
        editor.putString("Type", type);
        editor.putBoolean("Tut", true);
        editor.apply();
        return true;
    }

}
