package com.example.rotationdetector;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MotionCapture implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private boolean capturing;
    private StringBuilder motionPatternBuilder;

    private float maxDistance;

    public MotionCapture(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        capturing = false;
        motionPatternBuilder = new StringBuilder();
        maxDistance = 0;
    }

    public void startCapture() {
        capturing = true;
        maxDistance = 0; // Reset max distance
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

    public float getMaxDistance() {
        return maxDistance;
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

            // Calculate distance from the starting point
//            float distance = calculateDistance(startX, x, startY, y, startZ, z);
//            if (distance > maxDistance) {
//                maxDistance = distance;
//            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used in this example
    }
}
