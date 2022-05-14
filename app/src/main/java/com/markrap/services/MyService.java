package com.markrap.services;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.renderscript.RenderScript;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.app.preferences.SavePreferences;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.markrap.R;
import com.markrap.communication.CallBack;
import com.markrap.communication.ServerHandler;
import com.markrap.dashboard.MainActivity;
import com.markrap.gpstracter.GpsTracker;
import com.markrap.login.LoginActivity;
import com.markrap.utility.AppConstants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MyService extends Service {
    String latitude="0.0",longitude="0.0";
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1001,getNotification());
        getLocation();
        return super.onStartCommand(intent, flags, startId);

    }



    public Notification getNotification() {
        String channel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            channel = createChannel();
        else {
            channel = "";
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channel).setSmallIcon(android.R.drawable.ic_menu_mylocation).setContentTitle("Background Location service");
        Notification notification = mBuilder

                .setCategory(Notification.CATEGORY_SERVICE)
                .build();


        return notification;
    }

    @NonNull
    @TargetApi(26)
    private synchronized String createChannel() {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        String name = "Background Location Service ";
        int importance = NotificationManager.IMPORTANCE_LOW;

        NotificationChannel mChannel = new NotificationChannel("markrap", name, importance);

        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        if (mNotificationManager != null) {
            mNotificationManager.createNotificationChannel(mChannel);
        } else {
            stopSelf();
        }
        return "markrap";
    }



    FusedLocationProviderClient mFusedLocationClient;
    public void getLocation() {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        requestNewLocationData();
          callHandler(1000);
    }

    final Handler handler = new Handler();
    private void callHandler(int timer)
    {
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {

                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(MainActivity.mBroadcastStringAction);
                sendBroadcast(broadcastIntent);
                requestNewLocationData();
                if(!latitude.equalsIgnoreCase("0.0")) {
                    updateLocation(latitude + "", longitude + "");
                }
                System.out.println("Lat lnt===>>" + latitude + "==" + longitude);
                callHandler(60000*2);
            }
        }, timer);
    }



    public void updateLocation(String lat,String lng)
    {
        LinkedHashMap<String, String> m = new LinkedHashMap<>();
        m.put("user_id", getLoginData("id"));
        m.put("latitude", lat);
        m.put("longitude", lng);


        System.out.println("before==="+m);
        Map<String, String> headerMap = new HashMap<>();

        new ServerHandler().sendToServer(this, AppConstants.apiUlr+"livelocation", m, 1, headerMap, 20000, R.layout.loader_dialog, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    System.out.println("Login===="+dta);
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getInt("result")>0)
                    {
                        System.out.println("location msg=s=="+obj.getString("msg"));
                    }
                    else
                    {
                        System.out.println("location msg==="+obj.getString("msg"));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public String getLoginData(String dataType) {
        try {
            JSONObject data = new JSONObject(new SavePreferences().reterivePreference(this, AppConstants.logindata).toString());
            return data.getString(dataType);
            }
        catch (Exception e) {
            e.printStackTrace();
            }

        return  "";
    }




    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult)
        {
            Location mLastLocation = locationResult.getLastLocation();

            latitude=mLastLocation.getLatitude()+"";
            longitude=mLastLocation.getLongitude()+"";

        }
    };




}