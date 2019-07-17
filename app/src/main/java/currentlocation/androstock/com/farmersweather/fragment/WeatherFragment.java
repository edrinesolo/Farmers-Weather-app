package currentlocation.androstock.com.farmersweather.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import currentlocation.androstock.com.farmersweather.GlobalActivity;
import currentlocation.androstock.com.farmersweather.R;
import currentlocation.androstock.com.farmersweather.activity.FirstLaunch;
import currentlocation.androstock.com.farmersweather.activity.WeatherActivity;
import currentlocation.androstock.com.farmersweather.internet.CheckConnection;
import currentlocation.androstock.com.farmersweather.internet.FetchWeather;
import currentlocation.androstock.com.farmersweather.model.Info;
import currentlocation.androstock.com.farmersweather.model.WeatherFort;
import currentlocation.androstock.com.farmersweather.model.WeatherInfo;
import currentlocation.androstock.com.farmersweather.permissions.GPSTracker;
import currentlocation.androstock.com.farmersweather.permissions.Permissions;
import currentlocation.androstock.com.farmersweather.preferences.Prefs;
import currentlocation.androstock.com.farmersweather.utils.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherFragment extends Fragment {
    Typeface weatherFont;
    @BindView(R.id.button1) TextView button;
    @BindView(R.id.iconmain) TextView icontext;
    TextView detailsField[] = new TextView[10] , weatherIcon[] = new TextView[11];
    @BindView(R.id.wind_view) TextView windView;
    @BindView(R.id.humidity_view) TextView humidityView;
    @BindView(R.id.direction_view) TextView directionView;
    @BindView(R.id.daily_view) TextView dailyView;
    @BindView(R.id.updated_field) TextView updatedField;
    @BindView(R.id.city_field) TextView cityField;
    @BindView(R.id.sunrise_view) TextView sunriseView;
    @BindView(R.id.sunset_view) TextView sunsetView;
    @BindView(R.id.sunrise_icon) TextView sunriseIcon;
    @BindView(R.id.sunriseicon) TextView sunriseIcon1;
    @BindView(R.id.sunset_icon) TextView sunsetIcon;
    @BindView(R.id.sunseticon) TextView sunsetIcon1;
    @BindView(R.id.wind_icon) TextView windIcon;
    @BindView(R.id.windicon) TextView windIcon1;
    @BindView(R.id.humidity_icon) TextView humidityIcon;
    @BindView(R.id.humidityicon) TextView humidityIcon1;
    @BindView(R.id.horizontalScrollView) HorizontalScrollView horizontalScrollView;
    double tc;
    Handler handler;
    BottomSheetDialogFragment bottomSheetDialogFragment;
    WeatherInfo json0;
    WeatherFort json1;
    @BindView(R.id.swipe) SwipeRefreshLayout swipeView;
    CheckConnection cc;
    Info json;
    MaterialDialog pd;
    FetchWeather wt;
    Prefs preferences;
    GPSTracker gps;
    View rootView;
    Permissions permission;

    public WeatherFragment() {
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        ButterKnife.bind(this , rootView);
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this.getActivity())
                .title(getString(R.string.please_wait))
                .content(getString(R.string.loading))
                .cancelable(false)
                .progress(true , 0);
        pd = builder.build();
        setHasOptionsMenu(true);
        preferences = new Prefs(getContext());
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weathericons-regular-webfont.ttf");
        Bundle bundle = getArguments();
        int mode;
        if (bundle != null)
            mode = bundle.getInt(Constants.MODE , 0);
        else
            mode = 0;
        if (mode == 0)
            updateWeatherData(preferences.getCity(), null, null);
        else
            updateWeatherData(null , Float.toString(preferences.getLatitude()) , Float.toString(preferences.getLongitude()));
        gps = new GPSTracker(getContext());
        cityField.setTextColor(ContextCompat.getColor(getContext() , R.color.textColor));
        updatedField.setTextColor(ContextCompat.getColor(getContext() , R.color.textColor));
        humidityView.setTextColor(ContextCompat.getColor(getContext() , R.color.textColor));
        sunriseIcon.setTextColor(ContextCompat.getColor(getContext() , R.color.textColor));
        sunriseIcon.setTypeface(weatherFont);
        sunriseIcon.setText(getString(R.string.sunrise_text));
        //
        icontext.setTextColor(ContextCompat.getColor(getContext(),R.color.textColor));
        icontext.setTypeface(weatherFont);
       // icontext.setText((Html.fromHtml(weather_iconText));
        //
        sunriseIcon1.setTextColor(ContextCompat.getColor(getContext() , R.color.textColor));
        sunriseIcon1.setTypeface(weatherFont);
        sunriseIcon1.setText(getString(R.string.sunrise_icon));

        sunsetIcon.setTextColor(ContextCompat.getColor(getContext() , R.color.textColor));
        sunsetIcon.setTypeface(weatherFont);
        sunsetIcon.setText(getString(R.string.sunset_text));
        //
        sunsetIcon1.setTextColor(ContextCompat.getColor(getContext() , R.color.textColor));
        sunsetIcon1.setTypeface(weatherFont);
        sunsetIcon1.setText(getString(R.string.sunset_icon));

        windIcon.setTextColor(ContextCompat.getColor(getContext() , R.color.textColor));
        windIcon.setTypeface(weatherFont);
        windIcon.setText(getString(R.string.wind_text));
        //
        windIcon1.setTextColor(ContextCompat.getColor(getContext() , R.color.textColor));
        windIcon1.setTypeface(weatherFont);
        windIcon1.setText(getString(R.string.speed_icon));

        humidityIcon.setTextColor(ContextCompat.getColor(getContext() , R.color.textColor));
        humidityIcon.setTypeface(weatherFont);
        humidityIcon.setText(getString(R.string.humidity_text));
        //
        humidityIcon1.setTextColor(ContextCompat.getColor(getContext() , R.color.textColor));
        humidityIcon1.setTypeface(weatherFont);
        humidityIcon1.setText(getString(R.string.humidity_icon));

        windView.setTextColor(ContextCompat.getColor(getContext() , R.color.textColor));
        swipeView.setColorSchemeResources(R.color.red, R.color.green , R.color.blue , R.color.yellow , R.color.orange);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        changeCity(preferences.getCity());
                    }
                });
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeView.setRefreshing(false);
                    }
                }, 10000);
            }
        });
        directionView.setTypeface(weatherFont);
        directionView.setTextColor(ContextCompat.getColor(getContext() , R.color.textColor));
        dailyView.setText(getString(R.string.daily));
        dailyView.setTextColor(ContextCompat.getColor(getContext() , R.color.textColor));
        sunriseView.setTextColor(ContextCompat.getColor(getContext() , R.color.textColor));
        sunsetView.setTextColor(ContextCompat.getColor(getContext() , R.color.textColor));
        button.setTextColor(ContextCompat.getColor(getContext() , R.color.textColor));
        pd.show();
        for (int i = 0; i < 11; ++i)
        {
            String f = "details_view" + (i + 1) , g = "weather_icon" + (i + 1);
            if (i != 10) {
                int resID = getResources().getIdentifier(f, "id", getContext().getPackageName());
                detailsField[i] = (TextView) rootView.findViewById(resID);
                detailsField[i].setTextColor(ContextCompat.getColor(getContext() , R.color.textColor));
            }
            int resIDI = getResources().getIdentifier(g, "id" , getContext().getPackageName());
            weatherIcon[i] = (TextView)rootView.findViewById(resIDI);
            weatherIcon[i].setTypeface(weatherFont);
            weatherIcon[i].setTextColor(ContextCompat.getColor(getContext() , R.color.textColor));
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu , MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_weather, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.location :
                permission = new Permissions(getContext());
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION} , Constants.READ_COARSE_LOCATION);
                break;
            case R.id.search :
                showMenuInputDialog();
                break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            cc = new CheckConnection(getContext());
            if (!cc.isNetworkAvailable())
                showNoInternet();
            else {
                pd.show();
                updateWeatherData(preferences.getCity(), null, null);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        gps.stopUsingGPS();
    }

    private void updateWeatherData(final String city, final String lat, final String lon) {
        wt = new FetchWeather(getContext());
        new Thread() {
            public void run() {
                try {
                    if (lat == null && lon == null) {
                        json = wt.execute(city).get();
                    } else if (city == null) {
                        json = wt.execute(lat, lon).get();
                    }
                }
                catch (InterruptedException iex) {
                    Log.e("InterruptedException" , "iex");
                }
                catch (ExecutionException eex) {
                    Log.e("ExecutionException" , "eex");
                }
                if (pd.isShowing())
                    pd.dismiss();
                if (swipeView != null && swipeView.isRefreshing())
                    swipeView.post(new Runnable() {
                        @Override
                        public void run() {
                            swipeView.setRefreshing(false);
                        }
                    });
                if (json == null) {
                    preferences.setCity(preferences.getLastCity());
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(),
                                    getActivity().getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                            GlobalActivity.i = 1;
                            if (!preferences.getLaunched()) {
                                FirstStart();
                            } else {
                                cc = new CheckConnection(getContext());
                                if (!cc.isNetworkAvailable()) {
                                    showNoInternet();
                                }
                                else {
                                    if (pd.isShowing())
                                        pd.dismiss();
                                    showInputDialog();
                                }
                            }
                        }
                    });
                }
                else {
                    handler.post(new Runnable() {
                        public void run() {
                            preferences.setLaunched();
                            renderWeather(json);
                            Snackbar snackbar = Snackbar.make(rootView, "Loaded Weather Data", 500);
                            snackbar.show();
                            //function();

                            if (pd.isShowing())
                                pd.dismiss();
                            preferences.setLastCity(json.day.getName());
                            ((WeatherActivity) getActivity()).createShortcuts();
                        }
                    });
                }
            }
        }.start();
    }

    public void FirstStart() {
        if (pd.isShowing())
            pd.dismiss();
        Intent intent = new Intent(getActivity(), FirstLaunch.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public List<WeatherFort.WeatherList> getDailyJson() {
        return json.fort.getList();
    }

    public void changeCity(String city)
    {
        if (!swipeView.isRefreshing())
            pd.show();
        updateWeatherData(city, null, null);
        preferences.setCity(city);
    }

    public void changeCity(String lat , String lon)
    {
        pd.show();
        updateWeatherData(null, lat, lon);
    }

    private void showInputDialog() {
        new MaterialDialog.Builder(this.getActivity())
                .title(getString(R.string.change_city))
                .content(getString(R.string.could_not_find))
                .negativeText(getString(R.string.cancel))
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog , @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .input(null, null, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, @NonNull CharSequence input) {
                        changeCity(input.toString());
                    }
                })
                .cancelable(false)
                .show();
    }



    public void showNoInternet() {
        new MaterialDialog.Builder(getContext())
                .title(getString(R.string.no_internet_title))
                .cancelable(false)
                .content(getString(R.string.no_internet_content))
                .positiveText(getString(R.string.no_internet_mobile_data))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName("com.android.settings","com.android.settings.Settings$DataUsageSummaryActivity"));
                        startActivityForResult(intent , 0);
                    }
                })
                .negativeText(getString(R.string.no_internet_wifi))
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS) , 0);
                    }
                })
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode ,
                                           @NonNull String permissions[] ,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.READ_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showCity();
                } else {
                    permission.permissionDenied();
                }
                break;
            }
        }
    }

    private void showCity() {
        gps = new GPSTracker(getContext());
        if (!gps.canGetLocation())
            gps.showSettingsAlert();
        else {
            String lat = gps.getLatitude();
            String lon = gps.getLongitude();
            changeCity(lat, lon);
        }
    }

    private void setWeatherIcon(int id , int i) {
        String icon = "";

                switch (i) {
                    case 0:
                        weatherIcon[i].setText(getText(R.string.day_1));
                        break;
                    case 1:
                        weatherIcon[i].setText(getText(R.string.day_2));
                        break;
                    case 2:
                        weatherIcon[i].setText(getText(R.string.day_3));
                        break;
                    case 3:
                        weatherIcon[i].setText(getText(R.string.day_4));
                        break;
                    case 4:
                        weatherIcon[i].setText(getText(R.string.day_5));
                        break;
                    case 5:
                        weatherIcon[i].setText(getText(R.string.day_6));
                        break;
                    case 6:
                        weatherIcon[i].setText(getText(R.string.day_7));
                        break;
                    case 7:
                        weatherIcon[i].setText(getText(R.string.day_8));
                        break;
                    case 8:
                        weatherIcon[i].setText(getText(R.string.day_9));
                        break;
                    case 9:
                        weatherIcon[i].setText(getText(R.string.day_10));
                        break;
                    case 10:
                        weatherIcon[i].setText(getText(R.string.today));


                }


    }

    private boolean checkDay() {
        Calendar c = Calendar.getInstance();
        int hours = c.get(Calendar.HOUR_OF_DAY);

        return !(hours >= 18 || hours <= 6);
    }

    private void renderWeather(Info jsonObj){
        try {
            json0 = jsonObj.day;
            json1 = jsonObj.fort;
            tc = json0.getMain().getTemp();
            preferences.setLatitude((float) json1.getCity().getCoord().getLatitude());
            preferences.setLongitude((float) json1.getCity().getCoord().getLongitude());
            preferences.setCity(json1.getCity().getName());
            int a = (int) Math.round(json0.getMain().getTemp());
            final String city = json1.getCity().getName().toUpperCase(Locale.US) +
                    ", " +
                    json1.getCity().getCountry();
            cityField.setText(city);
            cityField.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v) {
                    Snackbar.make(v, city, Snackbar.LENGTH_SHORT)
                            .show();
                }
            });
            Log.i("Location" , "Location Received");
            List<WeatherFort.WeatherList> details = json1.getList();
            for (int i = 0; i < 10; ++i)
            {
                details.set(i , json1.getList().get(i));
            }
            for (int i = 0; i < 10; ++i)
            {
                final WeatherFort.WeatherList J = details.get(i);
                long date1 = J.getDt();
                Date expiry = new Date(date1 * 1000);
                String date = new SimpleDateFormat("EE, dd" , Locale.US).format(expiry);
                SpannableString ss1 = new SpannableString(date + "\n"
                        + J.getTemp().getMax() + "°" + "      "
                        + J.getTemp().getMin() + "°" + "\n");
                ss1.setSpan(new RelativeSizeSpan(1.1f) , 0 , 7 , 0); // set size
                ss1.setSpan(new RelativeSizeSpan(1.4f) , 8 , 12 , 0);
                detailsField[i].setText(ss1);
                setWeatherIcon(J.getWeather().get(0).getId() , i);

            }
            final String d1 = new SimpleDateFormat("hh:mm a" , Locale.US).format(new Date(json0.getSys().getSunrise() * 1000));
            final String d2 = new SimpleDateFormat("hh:mm a" , Locale.US).format(new Date(json0.getSys().getSunset() * 1000));
            sunriseView.setText(d1);
            sunsetView.setText(d2);
            DateFormat df = DateFormat.getDateTimeInstance();
            String updatedOn = "Last update: " + df.format(new Date(json0.getDt() * 1000));
            updatedField.setText(updatedOn);
            final String humidity = String.format(getString(R.string.humidity_) , json0.getMain().getHumidity());
            humidityView.setText(humidity);
            humidityIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(rootView , String.format(Locale.ENGLISH , getString(R.string.humidity) , json0.getMain().getHumidity()) + " %" , Snackbar.LENGTH_SHORT).show();
                }
            });
            humidityView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(rootView , String.format(Locale.ENGLISH , getString(R.string.humidity) , json0.getMain().getHumidity()) + " %" , Snackbar.LENGTH_SHORT).show();
                }
            });
            final String wind = String.format(Locale.ENGLISH , getString(R.string.wind) , json0.getWind().getSpeed() , preferences.getUnits().equals("metric") ? getString(R.string.mps) : getString(R.string.mph));
            windView.setText(wind);
            windIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(rootView , String.format(Locale.ENGLISH , getString(R.string.wind_speed) , json0.getWind().getSpeed() , preferences.getUnits().equals("metric") ? getString(R.string.mps) : getString(R.string.mph)) , Snackbar.LENGTH_SHORT).show();
                }
            });
            windView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(rootView , String.format(Locale.ENGLISH , getString(R.string.wind_speed) , json0.getWind().getSpeed() , preferences.getUnits().equals("metric") ? getString(R.string.mps) : getString(R.string.mph)) , Snackbar.LENGTH_SHORT).show();
                }
            });
            humidityIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(rootView , String.format(Locale.ENGLISH , getString(R.string.humidity) , json0.getMain().getHumidity()) , Snackbar.LENGTH_SHORT).show();
                }
            });
            humidityView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(rootView , String.format(Locale.ENGLISH , getString(R.string.humidity) , json0.getMain().getHumidity()) , Snackbar.LENGTH_SHORT).show();
                }
            });
            sunriseIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(rootView , String.format(Locale.ENGLISH , getString(R.string.sunrise) , d1) , Snackbar.LENGTH_SHORT).show();
                }
            });
            sunriseView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(rootView , String.format(Locale.ENGLISH , getString(R.string.sunrise) , d1) , Snackbar.LENGTH_SHORT).show();
                }
            });
            sunsetIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(rootView , String.format(Locale.ENGLISH , getString(R.string.sunset) , d2) , Snackbar.LENGTH_SHORT).show();
                }
            });
            sunsetView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(rootView , String.format(Locale.ENGLISH , getString(R.string.sunset) , d2) , Snackbar.LENGTH_SHORT).show();
                }
            });
            setWeatherIcon(json0.getWeather().get(0).getId() , 10);
            weatherIcon[10].setOnClickListener(new View.OnClickListener()
            {
                public void onClick (View v)
                {
                    try {
                        String rs = json0.getWeather().get(0).getDescription();
                        String[] strArray = rs.split(" ");
                        StringBuilder builder = new StringBuilder();
                        for (String s : strArray) {
                            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
                            builder.append(cap.concat(" "));
                        }
                        Snackbar.make(v , String.format(getString(R.string.hey_there_condition) , builder.toString()), Snackbar.LENGTH_SHORT)
                                .show();
                    }
                    catch (Exception e) {
                        Log.e("Error", "Main Weather Icon OnClick Details could not be loaded");
                    }
                }
            });
            String r1 = Integer.toString(a) + "°";
            button.setText(r1);
            int deg = json0.getWind().getDirection();
            setDeg(deg);
        }catch(Exception e){
            Log.e(WeatherFragment.class.getSimpleName() , "One or more fields not found in the JSON data");
        }
    }

    private void setDeg(int deg) {
        int index = Math.abs(Math.round(deg % 360) / 45);
        switch (index) {
            case 0 : directionView.setText(getString(R.string.north_dir));
                directionView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar.make(view , getString(R.string.north) , Snackbar.LENGTH_SHORT).show();
                    }
                });
                break;
            case 1 : directionView.setText(getString(R.string.north_east_dir));
                directionView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar.make(view , getString(R.string.north_east) , Snackbar.LENGTH_SHORT).show();
                    }
                });
                break;
            case 2 : directionView.setText(getString(R.string.east));
                directionView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar.make(view , getString(R.string.east) , Snackbar.LENGTH_SHORT).show();
                    }
                });
                break;
            case 3 : directionView.setText(getString(R.string.north_dir));
                directionView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar.make(view , getString(R.string.south_east) , Snackbar.LENGTH_SHORT).show();
                    }
                });
                break;
            case 4 : directionView.setText(getString(R.string.south_dir));
                directionView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar.make(view , getString(R.string.south) , Snackbar.LENGTH_SHORT).show();
                    }
                });
                break;
            case 5 : directionView.setText(getString(R.string.south_west_dir));
                directionView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar.make(view , getString(R.string.south_west) , Snackbar.LENGTH_SHORT).show();
                    }
                });
                break;
            case 6 : directionView.setText(getString(R.string.west_dir));
                directionView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar.make(view , getString(R.string.west) , Snackbar.LENGTH_SHORT).show();
                    }
                });
                break;
            case 7 : directionView.setText(getString(R.string.north_west_dir));
                directionView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar.make(view , getString(R.string.north_west) , Snackbar.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }



    private void showMenuInputDialog() {
        new MaterialDialog.Builder(getContext())
                .title(getString(R.string.change_city))
                .content(getString(R.string.enter_zip_code))
                .negativeText(getString(R.string.cancel))
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog , @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .input(null, null, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, @NonNull CharSequence input) {
                        changeCity(input.toString());
                    }
                }).show();
    }
}
