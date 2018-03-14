package technology.thinkbench.budgetbuddy;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.*;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class LoginActivity extends AppCompatActivity {

    private final int LOGIN_REQUEST = 1;
    private final int LOGIN_CREATE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        String user = "";
        String pass = "";
        String tz = "";
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(this);
        if(sharedPreferences.contains("User")){
            user = sharedPreferences.getString("User", "");
            EditText temp = (EditText) findViewById(R.id.username);
            temp.setText(user);
        }
        if(sharedPreferences.getBoolean("AutoLog", false)){
            CheckBox checkBox = (CheckBox) findViewById(R.id.login_rpw);
            checkBox.setChecked(true);
            pass = sharedPreferences.getString("Password", "");
            String args = "?type=login&user=" + user + "&pass=" + pass;
            PendingIntent pendingResult = createPendingResult(LOGIN_REQUEST, new Intent(), 0);
            Intent intent = new Intent(getApplicationContext(), DownloadIntentService.class);
            intent.putExtra(DownloadIntentService.URL_EXTRA, args);
            intent.putExtra(DownloadIntentService.PENDING_RESULT_EXTRA, pendingResult);
            startService(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.d("Testing", "Started onActivityResult");
        switch(requestCode) {
            case LOGIN_REQUEST:
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                String response = data.getStringExtra(DownloadIntentService.RESULT_EXTRA);
                Log.d("Testing", "recieved string extra" + response);
                JsonObject jsonObject = (new JsonParser()).parse(response).getAsJsonObject();
                boolean success = jsonObject.get("retval").getAsBoolean();
                if (success) {
                    //save user data (inc pw if box is checked) and then log in
                    Log.d("Testing", "Starting success route");
                    JsonObject temp = jsonObject.get("data").getAsJsonObject();
                    String user = temp.get("user").getAsString();
                    String tz = temp.get("tz").getAsString();
                    String email = "";
                    if(!temp.get("email").isJsonNull()){
                        email = temp.get("email").getAsString();
                    }
                    Log.d("Testing", user);
                    Log.d("Testing", email);
                    Log.d("Testing",tz);
                    SharedPreferences sharedPreferences = getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("User", user);
                    editor.putString("Email", email);
                    editor.putString("Timezone",tz);
                    String pw;
                    CheckBox al = (CheckBox) findViewById(R.id.login_rpw);
                    if(al.isChecked()){
                        pw = temp.get("pw").getAsString();
                        editor.putString("Password", pw);
                        editor.putBoolean("AutoLog", true);
                    }else{
                        editor.putBoolean("AutoLog", false);
                        editor.remove("Password");
                    }
                    editor.apply();

                    CharSequence text = "Logged in!";
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    Intent intent = new Intent(this, CentralActivity.class);
                    startActivity(intent);
                } else {
                    CharSequence text = "User/Pass incorrect";
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                break;
            case LOGIN_CREATE:

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void try_login(View view) {
        EditText temp = (EditText) findViewById(R.id.username);
        EditText temp2 = (EditText) findViewById(R.id.password);
        String user = temp.getText().toString();
        String pass = temp2.getText().toString();

        String args = "?type=login&user=" + user + "&pass=" + pass;

        /*
        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        Bundle bundle = new Bundle();
        bundle.putString("data", args);
        loaderManager.initLoader(LOGIN_REQUEST, bundle, this);
        */

        PendingIntent pendingResult = createPendingResult(LOGIN_REQUEST, new Intent(), 0);
        Intent intent = new Intent(getApplicationContext(), DownloadIntentService.class);
        intent.putExtra(DownloadIntentService.URL_EXTRA, args);
        intent.putExtra(DownloadIntentService.PENDING_RESULT_EXTRA, pendingResult);
        startService(intent);
    }

    public void make_account(View view){
        Intent makeIntent = new Intent(this, MakeAccountActivity.class);
        startActivity(makeIntent);
    }

    public void forgot_password(View view){

    }
}
