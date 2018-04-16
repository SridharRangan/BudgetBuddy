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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void try_login(View view) {
        Boolean user = false;
        Intent intent = new Intent(getApplicationContext(), PinActivity.class);
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(this);
        if(sharedPreferences.contains("User")){
            user = sharedPreferences.getBoolean("User", false);
        }
        if(user){
            //already set a pin
            intent.putExtra("Mode", 0);
        }else{
            //no user exists
            intent.putExtra("Mode", 1);
        }
        startActivity(intent);
    }

    public void forgot_password(View view){
        Intent intent = new Intent(getApplicationContext(), ResetActivity.class);
        startActivity(intent);
    }
}
