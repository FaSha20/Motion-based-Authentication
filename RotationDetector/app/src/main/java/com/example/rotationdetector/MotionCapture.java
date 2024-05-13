package com.example.rotationdetector;

import android.Manifest;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class MotionCapture implements SensorEventListener {

    private static final String TAG = "MotionCapture";
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private boolean capturing;
    private int Move;
    private StringBuilder motionPatternBuilder;
    private float startX, startY, numOfZeros;
    static final int Y = 2;
    static final int X = 1;
    static final int THRESHOLD = 1;


    private float maxDistanceX, maxDistanceY;

    public MotionCapture(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        capturing = false;
        Move = 0;
        numOfZeros = 0;
        motionPatternBuilder = new StringBuilder();

    }

    public void startCapture() {
        capturing = true;
        maxDistanceX = 0;maxDistanceY = 0;// Reset max distance
        motionPatternBuilder.setLength(0); // Clear previous motion pattern
        motionPatternBuilder.append("\nX               Y");
        // Register the accelerometer sensor listener
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void stopCapture() {
        capturing = false;
        // Unregister the sensor listener to stop capturing motion
        sensorManager.unregisterListener(this);
    }

    public String getMotionPattern() {
        return motionPatternBuilder.toString();
    }

    public String getMaxDistance() {
        return "X dest: " + maxDistanceX + "\nY dest: " + maxDistanceY ;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (capturing && event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Capture accelerometer data
            float x = event.values[0];
            float y = event.values[1];

            // Append the data to the motion pattern
            motionPatternBuilder.append(String.format("\n%.2f      %.2f ", x, y));

            if(Move == 0){
               if(Math.abs(x) > 1){
                   startX = x;
                   Log.d(TAG, "X started: "+ startX);
                   Move = X;
                   numOfZeros = 0;
               }else if (Math.abs(y) > 1) {
                   startY = y;
                   Log.d(TAG, "Y started: "+ startY);
                   Move = Y;
                   numOfZeros = 0;
               }
            }
            if(Move == X){
                // Calculate distance from the starting point
                float distance = calculateDistance(startX, x);
                if (distance > maxDistanceX) {
                    maxDistanceX = distance;
                }
                if(Math.abs(x) < 1) numOfZeros += 1;
                if(numOfZeros > THRESHOLD)Move = 0;
            }
            if(Move == Y){
                float distance = calculateDistance(startY, y);
                if (distance > maxDistanceY) {
                    maxDistanceY = distance;
                }
                if(Math.abs(y) < 1) numOfZeros += 1;
                if(numOfZeros > THRESHOLD)Move = 0;
            }

        }
    }

    private float calculateDistance(float startA, float a) {
        float dist = Math.abs(a-startA);
        return dist;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used in this example
    }
}
