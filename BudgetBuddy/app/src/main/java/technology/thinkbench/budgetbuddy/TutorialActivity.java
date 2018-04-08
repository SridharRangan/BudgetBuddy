package technology.thinkbench.budgetbuddy;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;


public class TutorialActivity extends AppCompatActivity {

    Tutorial1Fragment tut1 = new Tutorial1Fragment();
    Tutorial2Fragment tut2 = new Tutorial2Fragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, tut1);
        transaction.commit();
    }


}
