package technology.thinkbench.budgetbuddy;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import technology.thinkbench.budgetbuddy.data.DataContract.ExpenditureEntry;

public class AddActivity extends AppCompatActivity{


    private EditText mLabelEditText;
    private EditText mAmountEditText;
    private String mTagEditText;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Find all relevant views that we will need to read user input from
        mLabelEditText = (EditText) findViewById(R.id.edit_expenditure_label);
        mAmountEditText = (EditText) findViewById(R.id.edit_expenditure_amount);
        spinner = (Spinner) findViewById(R.id.tag_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.tags_array,
                android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(4);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                mTagEditText = parent.getItemAtPosition(pos).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    public void saveExpenditure(View view) {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mLabelEditText.getText().toString().trim();
        String amountString = mAmountEditText.getText().toString().trim();
        String tagString = mTagEditText;


        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(ExpenditureEntry.COLUMN_EXPENDITURE_LABEL, nameString);
        values.put(ExpenditureEntry.COLUMN_EXPENDITURE_AMOUNT, amountString);
        values.put(ExpenditureEntry.COLUMN_EXPENDITURE_TAG, tagString);

        // This is a NEW expenditure, so insert a new pet into the provider
        Uri newUri = getContentResolver().insert(ExpenditureEntry.CONTENT_URI, values);

        // Show a toast message depending on whether or not the insertion was successful.
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, "Whoops! Something went wrong!", Toast.LENGTH_SHORT).show();

        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, "Added Expenditure", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, CentralActivity.class);
            startActivity(intent);
        }
    }

}
