package com.sss.myhwmonitorapp.services;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavDeepLinkBuilder;

import com.sss.myhwmonitorapp.MainActivity;
import com.sss.myhwmonitorapp.R;
import com.sss.myhwmonitorapp.db.SensorDb;
import com.sss.myhwmonitorapp.db.SensorModel;

import static com.sss.myhwmonitorapp.utils.AppNotificationChannel.CHANNEL_ID;
import static com.sss.myhwmonitorapp.utils.SensorPreference.setServiceRunning;


public class SensorService extends Service {

    private static final String PACKAGE_NAME = "com.sss.myhwmonitorapp";
    private static final String STARTED_FROM_NOTIFICATION = PACKAGE_NAME + ".started_from_notification";

    private double xA;
    private double yA;
    private double zA;

    private double xG;
    private double yG;
    private double zG;

    private double distance;

    private double illuminance;

    private boolean isAcceleroAvailable, isGyroAvailable, isProximityAvailable, isLightAvailable;


    private SensorManager mSensorManager;
    private Sensor accelerometer, gyroscope, light, proximity;
    private SensorEventListener listener;


    private SensorDb roomDb;
    private SensorModel sensorData;

    private Handler handler;
    private Thread thread;
    private int mInterval = 30000;

    private HandlerThread mSensorThread;
    private Handler mHandler;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                getValueFromSensor();
            } finally {
                handler.postDelayed(runnable, mInterval);
            }
        }
    };

    private static final int NOTIFICATION_ID = 1001;

    private boolean isChangingConfiguration = false;

    private final IBinder mBinder = new LocalBinder();

    private static final String TAG = "SensorService";

    public SensorService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");

        roomDb = SensorDb.getInstance(this);
        thread = new Thread(runnable);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        Log.d(TAG, "onCreate: " + Thread.currentThread().getName());

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            isAcceleroAvailable = true;
        } else {
            Log.d(TAG, "onCreate: Accelero not found");
            isAcceleroAvailable = false;
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) {
            gyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            isGyroAvailable = true;
        } else {
            Log.d(TAG, "onCreate: gyroscope not found");
            isGyroAvailable = false;
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null) {
            proximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            isProximityAvailable = true;
        } else {
            Log.d(TAG, "onCreate: proximity not found");
            isProximityAvailable = false;
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null) {
            light = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            isLightAvailable = true;
        } else {
            Log.d(TAG, "onCreate: light not found");
            isLightAvailable = false;
        }

        handler = new Handler();

        mSensorThread = new HandlerThread("sensor_thread"); //$NON-NLS-1$
        mSensorThread.start();
        mHandler = new Handler(mSensorThread.getLooper());

        if (isLightAvailable && isProximityAvailable && isGyroAvailable && isAcceleroAvailable)
            runnable.run();
        else
            Log.d(TAG, "onCreate: sensor missing");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        boolean startedFromNotification = intent.getBooleanExtra(STARTED_FROM_NOTIFICATION,
                false);

        if (startedFromNotification) {
            stopSelf();
        }
        return START_NOT_STICKY;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        isChangingConfiguration = true;
    }

    private void getValueFromSensor() {
        Log.d(TAG, "getValueFromSensor: " + Thread.currentThread().getName());
        boolean isOnUiThread = Thread.currentThread() == Looper.getMainLooper().getThread();
        Log.e(TAG, "getValueFromSensor: " + isOnUiThread);
        listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                Log.d(TAG, "onSensorChanged: " + Thread.currentThread().getName());

                Sensor currentSensor = event.sensor;

                if (currentSensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    Log.d(TAG, "TYPE_ACCELEROMETER: " + event.values[0] + event.values[1] + event.values[2]);
                    xA = event.values[0];
                    yA = event.values[1];
                    zA = event.values[2];

//                    sensorData.setxAclmtr( event.values[0]);
//                    sensorData.setyAclmtr( event.values[1]);
//                    sensorData.setzAclmtr( event.values[2]);

                    mSensorManager.unregisterListener(listener, accelerometer);
                }

                if (currentSensor.getType() == Sensor.TYPE_GYROSCOPE) {
                    Log.d(TAG, "TYPE_GYROSCOPE: " + event.values[0] + event.values[1] + event.values[2]);
                    xG = event.values[0];
                    yG = event.values[1];
                    zG = event.values[2];

//                    sensorData.setxGyro( event.values[0]);
//                    sensorData.setyGyro( event.values[1]);
//                    sensorData.setzGyro( event.values[2]);

                    mSensorManager.unregisterListener(listener, gyroscope);
                }

                if (currentSensor.getType() == Sensor.TYPE_PROXIMITY) {
                    Log.d(TAG, "TYPE_PROXIMITY: " + event.values[0]);
                    distance = event.values[0];

//                    sensorData.setPrxmty_dis( event.values[0]);

                    mSensorManager.unregisterListener(listener, proximity);
                }

                if (currentSensor.getType() == Sensor.TYPE_LIGHT) {
                    Log.d(TAG, "TYPE_LIGHT: " + event.values[0]);
                    illuminance = event.values[0];

//                    sensorData.setLux( event.values[0]);

                    mSensorManager.unregisterListener(listener, light);
                }

                Long ts = System.currentTimeMillis();
//                sensorData.setTime(String.valueOf(ts));

                sensorData = new SensorModel(xA, yA, zA, xG, yG, zG, distance, illuminance, String.valueOf(ts));

                roomDb.dao().insert(sensorData);

                Log.e(TAG, "onSensorChanged: " + sensorData.getLux());

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSensorManager.unregisterListener(listener);
                        Log.d(TAG, "run: action close");
                        handler.removeCallbacks(this);

                    }
                }, 12000);

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        mSensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL, mHandler);
        mSensorManager.registerListener(listener, gyroscope, SensorManager.SENSOR_DELAY_NORMAL, mHandler);
        mSensorManager.registerListener(listener, proximity, SensorManager.SENSOR_DELAY_FASTEST, mHandler);
        mSensorManager.registerListener(listener, light, SensorManager.SENSOR_DELAY_FASTEST, mHandler);
    }


    public boolean serviceIsRunningInForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                Integer.MAX_VALUE)) {
            if (getClass().getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }
            }
        }
        return false;
    }

    public void startService() {
        Log.d(TAG, "startService: ");
        startService(new Intent(getApplicationContext(), SensorService.class));
    }

    private Notification getNotification() {
        Intent intent = new Intent(this, SensorService.class);

        // Extra to help us figure out if we arrived in onStartCommand via the notification or not.
        intent.putExtra(STARTED_FROM_NOTIFICATION, true);

        // The PendingIntent that leads to a call to onStartCommand() in this service.
        PendingIntent servicePendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent pendingIntent = new NavDeepLinkBuilder(this)
                .setComponentName(MainActivity.class)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.nav_home)
                .createPendingIntent();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .addAction(R.drawable.icon, getString(R.string.launch_activity),
                        pendingIntent)
                .setContentTitle("Sensor Monitoring")
                .setOnlyAlertOnce(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.icon)
                .setColor(getResources().getColor(R.color.purple_700))
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setChannelId(CHANNEL_ID)
                .setSound(null)
                .setWhen(System.currentTimeMillis());


        return builder.build();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "in onBind()");
        stopForeground(true);
        isChangingConfiguration = false;
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i(TAG, "in onRebind()");
        stopForeground(true);
        isChangingConfiguration = false;
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "Last client unbound from service");

        if (!isChangingConfiguration && !serviceIsRunningInForeground(this)) {
            Log.i(TAG, "Starting foreground service");
            startForeground(NOTIFICATION_ID, getNotification());
        }
        return true; // Ensures onRebind() is called when a client re-binds.
    }

    public void stopService() {
        stopSelf();
        setServiceRunning(this,false);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    public class LocalBinder extends Binder {
        public SensorService getService() {
            return SensorService.this;
        }
    }
}
