package currentlocation.androstock.com.farmersweather.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import currentlocation.androstock.com.farmersweather.R;
import currentlocation.androstock.com.farmersweather.fragment.FirstLaunchFragment;

public class FirstLaunch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launch);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment , new FirstLaunchFragment())
                .commit();
//        setSupportActionBar(toolbar);
    }
}
