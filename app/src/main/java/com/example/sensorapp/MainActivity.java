package com.example.sensorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.SimpleFormatter;

import static com.example.sensorapp.App.CHANNEL_1_ID;

public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener {
    private static final int PERMISSION_FINE_LOCATION = 99;

    static ArrayList<String> a = new ArrayList<>();
    private Sensor s;
    private SensorManager sm;
    boolean g = true;
    TextView t;
    TextView t2;
    long date = System.currentTimeMillis();
    SimpleDateFormat sd = new SimpleDateFormat("hh:mm:ss");
    String time = sd.format(date);
    TextView t4;
    TextView t3;
    LocationManager lm;
    TextView t7;
    ImageView i1;
    ImageView i2;
    ImageView i3;
    MediaPlayer m;
    private NotificationManagerCompat nc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nc = NotificationManagerCompat.from(this);
        t = findViewById(R.id.textView);
        t2 = findViewById(R.id.textView2);
        t3 = findViewById(R.id.textView3);
        t4 = findViewById(R.id.textView4);
t7 = findViewById(R.id.textView7);
t7.setTypeface(null, Typeface.BOLD);
i1 = findViewById(R.id.imageView4);
        i2 = findViewById(R.id.imageView2);
        i3 = findViewById(R.id.imageView3);
i1.setVisibility(View.VISIBLE);
//permission
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sm.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null) {
            s = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            g = true;
        } else {
            g = false;

        }
/*

 */

    }


    @SuppressLint("MissingPermission")
    private void getloc() {
try{
    lm = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
    lm.requestLocationUpdates(lm.GPS_PROVIDER, 5000, 5, MainActivity.this);
}catch(Exception e){
    e.printStackTrace();
}
    }




    public void onclick(View v){


    t3.setText("Encountered on: " + a);
    if(a.isEmpty() == false) {
        getloc();
    }


    }

    public void onSensorChanged(SensorEvent event) {
        if(g == true && event.values[0] < s.getMaximumRange()){
            t.setText( "You have been exposed");
            getWindow().getDecorView().setBackgroundColor(Color.RED);
            long date2 = System.currentTimeMillis();
            SimpleDateFormat sd2 = new SimpleDateFormat("hh:mm:ss");
            String time2 = sd.format(date2);
            t2.setText(time2);
            i1.setVisibility(View.VISIBLE);
    a.add(t2.getText().toString());
    i2.setVisibility(View.VISIBLE);
    i3.setVisibility(View.GONE);
            Notification nnn = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ic_baseline_warning_24)
                    .setContentTitle("COVID-19 EXPOSURE")
                    .setContentText("Possible exposure to COVID-19: " + a)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build();
            nc.notify(1, nnn);
            if (m == null) {

                m = MediaPlayer.create(this,R.raw.sound_design_beep_censor_tone_002);


            }
            m.start();

        }else{
            t2.setText(null);
            t.setText("Keep up the social distancing bro");
            getWindow().getDecorView().setBackgroundColor(Color.BLUE);
            t3.setText(null);
            i2.setVisibility(View.GONE);
            i3.setVisibility(View.VISIBLE);
            i1.setVisibility(View.VISIBLE);
        }


        }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(g){
            sm.registerListener(this, s, sm.SENSOR_DELAY_NORMAL);
        }

    }



    @Override
    protected void onPause(){
        super.onPause();
        if(g){
            sm.unregisterListener(this);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

try{
    Geocoder g = new Geocoder(MainActivity.this, Locale.getDefault());
    List<Address> a = g.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
    String address = a.get(0).getAddressLine(0);
    t4.setText(address);
        }catch(Exception e){
    e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    //flp = LocationServices.getFusedLocationProviderClient(MainActivity.this);
    //if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

    }



