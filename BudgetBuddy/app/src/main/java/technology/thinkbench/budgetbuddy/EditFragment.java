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


public class EditFragment extends android.support.v4.app.Fragment {

    private Spinner spinner;

    private String type_original, type_new;
    private float income_original, target_original;

    EditText income;
    EditText target;

    public EditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit, container, false);

        SharedPreferences sharedPreferences = getDefaultSharedPreferences(getContext());
        income_original = sharedPreferences.getFloat("Income", 0);
        target_original = sharedPreferences.getFloat("Target", 0);
        type_original = sharedPreferences.getString("Type", "$");

        income = (EditText) rootView.findViewById(R.id.edit_income);
        target = (EditText) rootView.findViewById(R.id.edit_savings);
        spinner = (Spinner) rootView.findViewById(R.id.edit_spinner);

        income.setText(Float.toString(income_original));
        target.setText(Float.toString(target_original));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),R.array.type_array,
                android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        switch(type_original){
            case "$":
                spinner.setSelection(0);
                type_new = spinner.getItemAtPosition(0).toString();
                break;
            case "%":
                spinner.setSelection(1);
                type_new = spinner.getItemAtPosition(1).toString();
                break;
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                type_new = parent.getItemAtPosition(pos).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }

    public void submit_changes(View view){
        //check for validity
        boolean changes = false;
        Float income_new, target_new;
        income_new = Float.parseFloat(income.getText().toString());
        target_new = Float.parseFloat(target.getText().toString());

        if(income_new < 0){
            Log.d("DEBUG", "income under zero");
            Toast.makeText(getContext(), "Your income has to be greater than zero", Toast.LENGTH_SHORT).show();
            //income.setText(Float.toString(income_original));
            income.requestFocus();
            return;
        }else if(target_new < 0){
            Log.d("DEBUG", "target under zero");
            Toast.makeText(getContext(), "You have to save a positive amount", Toast.LENGTH_SHORT).show();
            //target.setText(Float.toString(target_original));
            target.requestFocus();
            return;
        }else if(type_new.equalsIgnoreCase("%") && target_new > 100){
            Log.d("DEBUG", "over 100%");
            Toast.makeText(getContext(), "You can't save more than 100% of your income", Toast.LENGTH_SHORT).show();
            target.requestFocus();
            return;
        }else if(type_new.equalsIgnoreCase("$") && target_new > income_new){
            Log.d("DEBUG", "saving > monthly");
            Toast.makeText(getContext(), "You can't save more than you make", Toast.LENGTH_SHORT).show();
            target.requestFocus();
            return;
        }

        //do the actual updating
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(income_new != income_original){
            editor.putFloat("Income", income_new);
            income_original = income_new;
            changes = true;
        }
        if(target_new != target_original){
            editor.putFloat("Target", target_new);
            target_original = target_new;
            changes = true;
        }
        if(!type_new.equalsIgnoreCase(type_original)){
            editor.putString("Type", type_new);
            type_original = type_new;
            changes = true;
        }
        editor.apply();
        if(changes){
            Toast.makeText(getContext(), "Changes Applied", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(), "You didn't make any changes", Toast.LENGTH_SHORT).show();
        }

    }


}
