package com.example.rotationdetector;

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
    private boolean capturing, isStarted;
    private StringBuilder motionPatternBuilder;
    private float startX, startY, startZ;


    private float maxDistanceX, maxDistanceY, maxDistanceZ;

    public MotionCapture(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        capturing = false;
        isStarted = false;
        motionPatternBuilder = new StringBuilder();

    }

    public void startCapture() {
        capturing = true;
        maxDistanceX = 0;maxDistanceY = 0;maxDistanceZ = 0; // Reset max distance
        motionPatternBuilder.setLength(0); // Clear previous motion pattern
        motionPatternBuilder.append("\nX               Y            Z");
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
        return "X dest: " + maxDistanceX + "\nY dest: " + maxDistanceY + "\nZ dest: " + maxDistanceZ;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (capturing && event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Capture accelerometer data
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Append the data to the motion pattern
            motionPatternBuilder.append(String.format("\n%.2f      %.2f     %.2f", x, y, z));

            if(!isStarted && Math.abs(x) > 1){
                startX = x;
                Log.d(TAG, "onSensorChanged: "+ startX);
                isStarted = true;
            }

            if(isStarted){
                // Calculate distance from the starting point
                float distancex = calculateDistance(startX, x);
                if (distancex > maxDistanceX) {

                    Log.d(TAG, "calculateDistance: a = " + x +" / "+ " startA = "+ startX);
                    maxDistanceX = distancex;
                    Log.d(TAG, "onSensorChanged: maxDist = " + maxDistanceX);


                }
//                float distancey = calculateDistance(startY, y);
//                if (distancey > maxDistanceY) {
//                    maxDistanceY = distancey;
//                }
//                float distancez = calculateDistance(startZ, z);
//                if (distancez > maxDistanceZ) {
//                    maxDistanceZ = distancez;
//                }

            }

            startY = y;
            startZ = z;
        }
    }

    private float calculateDistance(float startA, float a) {
        // Calculate Euclidean distance between two points in 3D space
//        float dist =  (float) Math.sqrt(Math.pow(a - startA, 2));
        float dist = Math.abs(a-startA);
        Log.d(TAG, "CalcDist: " + dist);
        return dist;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used in this example
    }
}
