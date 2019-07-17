package currentlocation.androstock.com.farmersweather.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import currentlocation.androstock.com.farmersweather.R;
import currentlocation.androstock.com.farmersweather.fragment.CropsFragment;
import currentlocation.androstock.com.farmersweather.fragment.WeatherFragment;
import currentlocation.androstock.com.farmersweather.preferences.Prefs;
import currentlocation.androstock.com.farmersweather.utils.Constants;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.weather_icons_typeface_library.WeatherIcons;

import butterknife.BindView;
import butterknife.ButterKnife;
import shortbread.Shortbread;
import shortbread.Shortcut;

public class WeatherActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Prefs preferences;
    WeatherFragment wf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Activity", WeatherActivity.class.getSimpleName());
        // mManager = NotificationManagerCompat.from(this);
        preferences = new Prefs(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        //  handler = new Handler();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //
        wf = new WeatherFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("mode", intent.getIntExtra(Constants.MODE, 0));
        wf.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, wf)
                .commit();

    }
    public void createShortcuts() {
        Shortbread.create(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.weather) {
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment);
            if (!(f instanceof WeatherFragment)) {
                wf = new WeatherFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, wf)
                        .commit();

            }
        }else if (id == R.id.farm) {

            Intent main=new Intent(WeatherActivity.this,MainActivity.class);
            startActivity(main);
            }
            return true;

    }
    @Override
    public void onBackPressed () {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}


