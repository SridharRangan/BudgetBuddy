package technology.thinkbench.budgetbuddy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import technology.thinkbench.budgetbuddy.data.DataContract;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by willis on 4/15/18.
 */

public class ResetActivity extends AppCompatActivity {
    Boolean repeat = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

    }

    public void confirm_delete(View view){
        Log.d("ResetActivity", "Are you sure?");
        if(repeat){
            resetEverything();
            Toast toast = Toast.makeText(this, "Data reset", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }else{
            TextView textView = (TextView) findViewById(R.id.reset_text);
            textView.setText("Remember that deleting your data is permanent! (Hit the back button to go back to the title screen)");
            Button button = (Button) findViewById(R.id.reset_button);
            button.setText("I'm sure");
            repeat = true;
        }
    }

    private void resetEverything(){
        int rowsDeleted = getContentResolver().delete(DataContract.ExpenditureEntry.CONTENT_URI, null, null);
        Log.d("ResetActivity", rowsDeleted + " rows deleted from expenditure database");
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("User", false);
        editor.putBoolean("Tut", false);
        editor.remove("Income");
        editor.remove("Target");
        editor.remove("Type");
        editor.putInt("Pin", 99999);
        editor.apply();
    }
}
