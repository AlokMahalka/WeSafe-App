package com.example.safecity;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;
import androidx.annotation.Nullable;

public class ShakeService extends Service implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI, new Handler());
        return START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f + delta;

        if (mAccel > 20) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                } else {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(SOSActivity.numbers.get(0), null, "SOS: I need your help!", null, null);
                    smsManager.sendTextMessage(SOSActivity.numbers.get(1), null, "SOS: I need your help!", null, null);
                    smsManager.sendTextMessage(SOSActivity.numbers.get(2), null, "SOS: I need your help!", null, null);
                    smsManager.sendTextMessage("+919326053407", null, "SOS: I need your help!", null, null);
                    Toast.makeText(this, "Message Sent!", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}
