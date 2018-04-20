package technology.thinkbench.budgetbuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by willis on 4/15/18.
 */

public class PinActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int PIN_CHECK = 0;
    private static final int PIN_SET = 1;
    private static final int PIN_VERIFY = 2;
    int mode = PIN_CHECK;
    int count = 0;
    int pin = 0, hold;
    ImageView i1, i2, i3, i4;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        mode = getIntent().getIntExtra("Mode", PIN_CHECK);

        title = (TextView) findViewById(R.id.pin_header);
        if(mode == PIN_SET){
            title.setText("Step 1: Create a PIN to secure your information");
        }else{
            title.setText("Enter your PIN");
        }



        Button one = (Button) findViewById(R.id.button1);
        one.setOnClickListener(this);
        Button two = (Button) findViewById(R.id.button2);
        two.setOnClickListener(this);
        Button three = (Button) findViewById(R.id.button3);
        three.setOnClickListener(this);
        Button four = (Button) findViewById(R.id.button4);
        four.setOnClickListener(this);
        Button five = (Button) findViewById(R.id.button5);
        five.setOnClickListener(this);
        Button six = (Button) findViewById(R.id.button6);
        six.setOnClickListener(this);
        Button seven = (Button) findViewById(R.id.button7);
        seven.setOnClickListener(this);
        Button eight = (Button) findViewById(R.id.button8);
        eight.setOnClickListener(this);
        Button nine = (Button) findViewById(R.id.button9);
        nine.setOnClickListener(this);
        Button zero = (Button) findViewById(R.id.button0);
        zero.setOnClickListener(this);

        i1 = (ImageView) findViewById(R.id.imageview_circle1);
        i2 = (ImageView) findViewById(R.id.imageview_circle2);
        i3 = (ImageView) findViewById(R.id.imageview_circle3);
        i4 = (ImageView) findViewById(R.id.imageview_circle4);
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        title.setText("Enter your PIN");
        mode = PIN_CHECK;
        count = 0;
        pin = 0;
        resetCircles();
    }

    private void resetCircles(){
        i1.setImageResource(R.drawable.circle);
        i2.setImageResource(R.drawable.circle);
        i3.setImageResource(R.drawable.circle);
        i4.setImageResource(R.drawable.circle);
    }

    private void nextActivity(){
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(this);
        Intent intent;

        if(sharedPreferences.contains("Tut") && sharedPreferences.getBoolean("Tut", false)){
            intent = new Intent(getApplicationContext(), CentralActivity.class);

        }else{
            intent = new Intent(getApplicationContext(), TutorialActivity.class);
        }

        if(false) {
            Log.d("DEBUG", "in shortcut");
            intent = new Intent(getApplicationContext(), CentralActivity.class);
        }

        startActivity(intent);
    }

    private void verify(){
        int temp;
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(this);
        count = 0;

        Log.d("PinActivity", "inside of verify()");

        switch (mode) {
            case PIN_CHECK:
                if (sharedPreferences.contains("Pin")) {
                    temp = sharedPreferences.getInt("Pin", 0);
                    if (temp == pin) {
                        Toast toast = Toast.makeText(this, "Logged on!", Toast.LENGTH_SHORT);
                        toast.show();
                        nextActivity();
                    } else {
                        pin = 0;
                        resetCircles();
                        Toast toast = Toast.makeText(this, "Incorrect PIN", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    //ERROR
                }
                break;

            case PIN_SET:
                mode = PIN_VERIFY;
                hold = pin;
                pin = 0;
                resetCircles();
                title.setText("Step 2: Re-enter your PIN");
                break;

            case PIN_VERIFY:
                if(pin == hold){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("User", true);
                    editor.putInt("Pin", pin);
                    editor.apply();
                    Toast toast = Toast.makeText(this, "PIN entered successfully", Toast.LENGTH_SHORT);
                    toast.show();
                    nextActivity();
                }else{
                    pin = 0;
                    resetCircles();
                    Toast toast = Toast.makeText(this, "PIN entries did not match, try again", Toast.LENGTH_LONG);
                    toast.show();
                    title.setText("Step 1: Create a PIN to secure your information");
                    mode = PIN_SET;
                }
                break;
        }
    }

    private void addNum(int num){
        switch (++count){
            case 1:
                pin += (num * 1000);
                i1.setImageResource(R.drawable.circle2);
                break;
            case 2:
                pin += (num * 100);
                i2.setImageResource(R.drawable.circle2);
                break;
            case 3:
                pin += (num * 10);
                i3.setImageResource(R.drawable.circle2);
                break;
            case 4:
                pin += num;
                i4.setImageResource(R.drawable.circle2);
                verify();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button1:
                addNum(1);
                break;
            case R.id.button2:
                addNum(2);
                break;
            case R.id.button3:
                addNum(3);
                break;
            case R.id.button4:
                addNum(4);
                break;
            case R.id.button5:
                addNum(5);
                break;
            case R.id.button6:
                addNum(6);
                break;
            case R.id.button7:
                addNum(7);
                break;
            case R.id.button8:
                addNum(8);
                break;
            case R.id.button9:
                addNum(9);
                break;
            case R.id.button0:
                addNum(0);
                break;
        }
    }
}