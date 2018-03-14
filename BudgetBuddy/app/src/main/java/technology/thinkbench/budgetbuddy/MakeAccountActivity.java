package technology.thinkbench.budgetbuddy;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by willis on 10/9/17.
 */

public class MakeAccountActivity extends AppCompatActivity {

    private final int LOGIN_REQUEST = 1;
    String selectedItem = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_account);
        Spinner spinner = (Spinner) findViewById(R.id.account_timezone_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.timezones_array,
                android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(7);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                selectedItem = parent.getItemAtPosition(pos).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        String response = data.getStringExtra(DownloadIntentService.RESULT_EXTRA);
        JsonObject jsonObject = (new JsonParser()).parse(response).getAsJsonObject();
        boolean success = jsonObject.get("retval").getAsBoolean();
        if(success){
            CharSequence text = "Account Created!";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            //new
            JsonObject temp = jsonObject.get("data").getAsJsonObject();
            String user = temp.get("user").getAsString();
            String tz = temp.get("tz").getAsString();
            String email = "";
            if(!temp.get("email").isJsonNull()){
                email = temp.get("email").getAsString();
            }
            SharedPreferences sharedPreferences = getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("User", user);
            editor.putString("Email", email);
            editor.putString("Timezone",tz);
            editor.putBoolean("AutoLog", false);
            editor.remove("Password");
            editor.apply();
            //end new
            Intent makeIntent = new Intent(this, CentralActivity.class);
            startActivity(makeIntent);
        }else{
            CharSequence text = jsonObject.get("message").getAsString();
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void try_make_account (View view){
        EditText temp = (EditText) findViewById(R.id.make_account_username);
        EditText pass1 = (EditText) findViewById(R.id.make_account_password);
        EditText pass2 = (EditText) findViewById(R.id.make_account_password2);

        EditText eml = (EditText) findViewById(R.id.make_account_email);

        String user = temp.getText().toString();
        String pass = pass1.getText().toString();
        String passcheck = pass2.getText().toString();
        String email = eml.getText().toString();

        //check that username is valid, pass1 matchs pass2
        if(pass.length() < 6){
            Toast toast = Toast.makeText(getApplicationContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }else if(pass.compareTo(passcheck) != 0){
            Toast toast = Toast.makeText(getApplicationContext(), "pass!=pass2", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }else if(user.length() > 20 || user.length() == 0){
            Toast toast = Toast.makeText(getApplicationContext(), "Username can be 1 to 20 characters", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        String args = "?type=newuser&user=" + user + "&pass=" + pass;
        if(selectedItem.length() > 0){
            args += "&tz=" + selectedItem;
        }
        if(email.length() > 0){
            args += "&email=" + email;
        }
        PendingIntent pendingResult = createPendingResult(LOGIN_REQUEST, new Intent(), 0);
        Intent intent = new Intent(getApplicationContext(), DownloadIntentService.class);
        intent.putExtra(DownloadIntentService.URL_EXTRA, args);
        intent.putExtra(DownloadIntentService.PENDING_RESULT_EXTRA, pendingResult);
        Log.d("DEBUG", "try_make_account: starting service");
        startService(intent);
    }

}
