package com.example.rotationdetector;

import static java.lang.Math.abs;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Dictionary;

public class MotionCapture implements SensorEventListener {

    private static final String TAG = "MotionCapture";
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;
    private boolean capturing;
    private int Move;
    private StringBuilder motionPatternBuilder;
    private float x, y, z;
    private float startX, startY, numOfZeros, numOfNonZeros;
    private Path path;

    private Dictionary<String, Integer> dict;
    static final int Y = 2;
    static final int X = 1;
    static final int THRESHOLD = 3;
    static final int THRESHOLD2 = 3;

    private float maxDistanceX, maxDistanceY;
    private long timestamp = 0; // Timestamp of the previous sensor event

    public MotionCapture(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
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
        path = new Path();
        motionPatternBuilder.append("\nX               Y                Z");
        // Register the accelerometer sensor listener
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, gyroscope, sensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public String stopCapture() {
        x = y = z = 0.0F;
        capturing = false;
        // Unregister the sensor listener to stop capturing motion
        sensorManager.unregisterListener(this);
        //Log.d(TAG, "path: \n" + path.toString());
        return path.toString();
    }

    public String getMotionPattern() {
        return motionPatternBuilder.toString();
    }

    public Path getDistancePattern() {
        return path;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (capturing) {
            if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                // Capture accelerometer data
                x = event.values[0];
                y = event.values[1];
            }
            if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
                float w = event.values[2];
                long currentTime = System.nanoTime(); // Current timestamp
                float dt = (currentTime - timestamp) / 1e9f; // Convert to seconds

                // Integrate angular rate to get rotation angle
                z += w * dt * 180 / Math.PI; // Convert to degrees

                // Update timestamp
                timestamp = currentTime;
            }
            // Append the data to the motion pattern
            motionPatternBuilder.append(String.format("\n%.2f      %.2f         %.2f", x, y,z));


            if(Move == 0){
               if(abs(x) > 1){
                   startX = x;
                   Log.d(TAG, "X started: "+ startX);
                   Move = X;
                   numOfZeros = 0;
                   numOfNonZeros = 0;
               }else if (abs(y) > 1) {
                   startY = y;
                   Log.d(TAG, "Y started: "+ startY);
                   Move = Y;
                   numOfZeros = 0;
                   numOfNonZeros = 0;
               }
            }
            if(Move == X){
                // Calculate distance from the starting point
                float distance = calculateDistance(startX, x);
                if (distance > maxDistanceX) {
                    maxDistanceX = distance;
                }
                if(abs(x) < 1) numOfZeros += 1;
                else numOfNonZeros += 1;

                if(numOfZeros > THRESHOLD){
                    Move = 0;
                    if(numOfNonZeros >= THRESHOLD2){
                        int angle = RoundingAngle(-z);
                        String direction = calculateHorizontalDirection(startX, angle);
                        path.addItem(maxDistanceX, direction, angle);
                    }
                }
            }
            if(Move == Y){
                float distance = calculateDistance(startY, y);
                if (distance > maxDistanceY) {
                    maxDistanceY = distance;
                }
                if(abs(y) < 1) numOfZeros += 1;
                else numOfNonZeros += 1;

                if(numOfZeros > THRESHOLD){
                    Move = 0;
                    if(numOfNonZeros >= THRESHOLD2){
                        int angle = RoundingAngle(-z);
                        String direction = calculateVerticalDirection(startY, angle);
                        path.addItem(maxDistanceY, direction, angle);
                    }
                }
            }

        }
    }

    private float calculateDistance(float startA, float a) {
        float dist = abs(a-startA);
        return dist;
    }
    private String calculateHorizontalDirection(float startA, float angle) {
        String direction;
        if (abs(angle) > 100 & abs(angle) < 200){
            direction = "left";
            if(startA > 0) direction = "right";
        } else if (angle > 70 & angle < 100) {
            direction = "up";
            if(startA > 0) direction = "bottom";
        }
        else if (angle > -100 & angle < -70) {
            direction = "bottom";
            if(startA > 0) direction = "up";
        }else {
            direction = "left";
            if(startA > 0) direction = "right";
        }
        return direction;
    }
    private String calculateVerticalDirection(float startA, float angle) {
        String direction;
        if (abs(angle) > 100 & abs(angle) < 200 ){
            direction = "up";
            if(startA > 0) direction = "bottom";
        } else if (angle > 70 & angle < 100) {
            direction = "left";
            if(startA > 0) direction = "right";
        }
        else if (angle > -100 & angle < -70) {
            direction = "right";
            if(startA > 0) direction = "left";
        }else {
            direction = "bottom";
            if(startA > 0) direction = "up";
        }
        return direction;
    }

    private int RoundingAngle (float angle) {
        int rounded = 0;
        if(angle > -50 & angle < 50){
            rounded = 0;
        }else if(angle > 70 & angle < 120){
            rounded = 90;
        }else if(angle > 150 & angle < 210){
            rounded = 180;
        }else if(angle > -120 & angle < -70){
            rounded = -90;
        }else if(angle > -210 & angle < -150){
            rounded = -180;
        }
        return rounded;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used in this example
    }
}
