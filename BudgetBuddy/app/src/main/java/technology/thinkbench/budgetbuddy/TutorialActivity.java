package technology.thinkbench.budgetbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class TutorialActivity extends AppCompatActivity {

    Tutorial1Fragment tut1 = new Tutorial1Fragment();
    Tutorial2Fragment tut2 = new Tutorial2Fragment();
    Tutorial3Fragment tut3 = new Tutorial3Fragment();
    Button button;
    int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        button = (Button) findViewById(R.id.tut_next_button);
        state = 0;
        draw_state();
    }

    public void draw_state(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (state){
            case 0:
                button.setText("Tell me more!");
                transaction.replace(R.id.fragment_container, tut1);
                transaction.commit();
                break;
            case 1:
                button.setText("What's next?");
                transaction.replace(R.id.fragment_container, tut2);
                transaction.commit();
                break;
            case 2:
                button.setText("Take me to the app!");
                transaction.replace(R.id.fragment_container, tut3);
                transaction.commit();
                break;
        }

    }

    public void next_tut(View view){
        switch (state){
            case 0:
                Log.d("DEBUG", "state 0");
                state++;
                draw_state();
                break;
            case 1:
                Log.d("DEBUG", "state 1");
                state++;
                draw_state();
                break;
            case 2:
                Log.d("DEBUG", "state 2");
                if(tut3.finish_tut()){
                    Intent intent = new Intent(getApplicationContext(), CentralActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

}
