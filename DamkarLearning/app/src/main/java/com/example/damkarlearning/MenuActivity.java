package com.example.damkarlearning;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.net.Uri;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.damkarlearning.tabs.Tab1Play;
import com.example.damkarlearning.tabs.Tab2Friends;
import com.example.damkarlearning.tabs.Tab3Profile;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;

import java.util.List;

import static com.example.damkarlearning.R.id.locationText;

public class MenuActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, SensorEventListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    private int field = 0x00000020;
    private SensorManager SM;
    private Sensor proxSensor;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    TextView tvLatlong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        SM = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (SM.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null) {
            // success! we have an gravity_meter
            proxSensor = SM.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            SM.registerListener(this,proxSensor,SensorManager.SENSOR_DELAY_NORMAL);

        } else {
            // fail! we dont have an gravity_meter!
        }





        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tvLatlong = (TextView) findViewById(locationText);
        buildGoogleApiClient();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        } else {
            Toast.makeText(this, "Not connected...", Toast.LENGTH_SHORT).show();
        }

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);


        /*
        TextView settingsTextView = (TextView) findViewById(R.id.settingsContent);
        settingsTextView.setText(builder.toString());
        */

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setType("plain/text");
                sendIntent.setData(Uri.parse("test@gmail.com"));
                sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "test@gmail.com" });
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "test");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(sendIntent);
                */
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboard.setText("faza.thirafi@gmail.com");
                Snackbar.make(view, "Send to faza.thirafi@gmail.com, saved to clibboard", Snackbar.LENGTH_LONG)
                      .setAction("Action", null).setDuration(100000).show();
                String url = "https://mail.google.com/mail/u/0/?view=cm&fs=1&to=faza.thirafi@gmail.com&su=[DamKar+Feedback]&body=&tf=1";
                String URL = "https://mail.google.com/mail/u/0/x/?&v=b&eot=1&pv=tl&cs=b";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(URL));
                startActivity(i);

            }
        });

//        try {
//            final RequestQueue requestQueue = Volley.newRequestQueue(MenuActivity.this);
//            String url = "https://damkar-learning.herokuapp.com/user";
//            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    Log.i("LOG_VOLLEY", response);
//                    System.out.println(response);
//                    //response isinya JSON dalam bentuk string dari data friends
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.e("LOG_VOLLEY", error.toString());
//                }
//            });
//
//            requestQueue.add(stringRequest);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
            // Yeah, this is hidden field.
            field = PowerManager.class.getClass().getField("PROXIMITY_SCREEN_OFF_WAKE_LOCK").getInt(null);
        } catch (Throwable ignored) {
        }

        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(field, getLocalClassName());


    }

    @Override
    public void onPause() {
        super.onPause();
        startService(new Intent(getApplicationContext(),MyService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MenuActivity.this, SettingActivity.class));

            return true;
        } else if (id == R.id.send_mail) {
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setType("plain/text");
            sendIntent.setData(Uri.parse("test@gmail.com"));
            sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
            sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "test@gmail.com" });
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "test");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "hello. this is a message sent from my demo app :-)");
            startActivity(sendIntent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            GlobalVariableSingleton globalVar = GlobalVariableSingleton.getInstance();
            globalVar.locationX = String.valueOf(mLastLocation.getLatitude());
            globalVar.locationY = String.valueOf(mLastLocation.getLongitude());
        }





    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection suspended...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Failed to connect...", Toast.LENGTH_SHORT).show();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void turnOnScreen(){
        // turn on screen
        Log.v("ProximityActivity", "ON!");
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag");
        wakeLock.acquire();
    }

    public void turnOffScreen(){
        // turn off screen
        Log.v("ProximityActivity", "OFF!");
        wakeLock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "tag");
        wakeLock.acquire();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float distance = event.values[0];
        if (distance==0.0f) {
            turnOffScreen();
        }
        else {
            turnOnScreen();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /*
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Tab1Play tab1 = new Tab1Play();
                    return tab1;
                case 1:
                    Tab2Friends tab2 = new Tab2Friends();
                    return tab2;
                case 2:
                    Tab3Profile tab3 = new Tab3Profile();
                    return tab3;
                default:
                    return null;
            }

        }
        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "PLAY";
                case 1:
                    return "FRIENDS";
                case 2:
                    return "PROFILE";
            }
            return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SM.registerListener(this, proxSensor, SensorManager.SENSOR_DELAY_NORMAL);

        Toolbar LL = (Toolbar) findViewById(R.id.toolbar);
        //ImageView II = (ImageView) findViewById(R.id.mr_art);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String value = sharedPrefs.getString("appColor", "");



        switch (value) {
            case "Red" :
            {

                LL.setBackgroundColor(Color.RED);
                break;
            }

            case "Blue" :
            {
                LL.setBackgroundColor(0x7f01009b);
                break;
            }

            case "Black" :
            {
                LL.setBackgroundColor(Color.BLACK);
                break;
            }

            case "Green" :
            {
                LL.setBackgroundColor(Color.GREEN);
                break;
            }
            default: break;
        }

    }


}
