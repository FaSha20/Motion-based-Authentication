package com.example.rotationdetector;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Paths;
public class MainActivity extends AppCompatActivity {

    private MotionCapture motionCapture;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize MotionCapture object
        motionCapture = new MotionCapture(this);

        // Get references to the buttons
        Button startCaptureButton = findViewById(R.id.startCaptureButton);
        Button stopCaptureButton = findViewById(R.id.stopCaptureButton);
        Button showPatternButton = findViewById(R.id.showPatternButton);
        Button showDistButton = findViewById(R.id.showDistButton);

        // Set click listeners for the buttons
        startCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start capturing motion
                motionCapture.startCapture();
            }

        });

        stopCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Stop capturing motion and get the captured pattern
                String motionPattern = motionCapture.getMotionPattern();
                // Do something with the motion pattern, such as saving it
                Log.d("Motion Pattern", motionPattern);
                // Stop capturing motion
                String jsonStr = motionCapture.stopCapture();
                SavaJsonFile(jsonStr);
            }
        });

        showPatternButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the captured motion pattern
                String motionPattern = motionCapture.getMotionPattern();
                // Display the pattern, for example, in a TextView
                TextView patternTextView = findViewById(R.id.patternTextView);
                patternTextView.setText("Captured Motion Pattern: " + motionPattern);
            }
        });

        showDistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String distances = motionCapture.getDistancePattern();
                TextView distanceTextView = findViewById(R.id.maxDistView);
                distanceTextView.setText("The captured path is: \n" + distances);
            }
        });
    }

    public void SavaJsonFile(String jsonStr){
//        try {
//            File root = new File("RotationDetector/app/src/main/java/com/example/rotationdetector"); // Specify the directory path
//            File gpxfile = new File(root, "pathItem.json"); // Specify the file name
//            FileWriter writer = new FileWriter(gpxfile);
//            writer.append(jsonStr);
//            writer.flush();
//            writer.close();
//
//            Log.d(TAG, "SavaJsonFile: added to json file\n" + jsonStr);
//        }
//        catch (IOException e) {
//            Log.e("Exception", "File write failed: " + e.toString());
//        }
    }
}




//public class MainActivity extends Activity implements SensorEventListener {
//    private float mLastX, mLastY, mLastZ;
//    private boolean mInitialized;
//    private SensorManager mSensorManager;
//    private Sensor mAccelerometer;
//    private final float NOISE = (float) 2.0;
//
//    private static final String TAG = "MainActivity";
//
//    TextView tvX= (TextView)findViewById(R.id.xValue);
//    TextView tvY= (TextView)findViewById(R.id.yValue);
//    TextView tvZ= (TextView)findViewById(R.id.zValue);
//
//    /** Called when the activity is first created. */
//
//    @Override
//
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        mInitialized = false;
//        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
//
//    }
//
//    @Override
//    public void onSensorChanged(SensorEvent event) {
////        Log.d(TAG, "onSensorChanged: x = " + event.values[0] + "- y = "+ event.values[1] + " - z = "+ event.values[2]);
//
//        float x = event.values[0];
//        float y = event.values[1];
//        float z = event.values[2];
//        if (!mInitialized) {
//            mLastX = x;
//            mLastY = y;
//            mLastZ = z;
//            tvX.setText("0.0");
//            tvY.setText("0.0");
//            tvZ.setText("0.0");
//            mInitialized = true;
//            Log.d(TAG, "onSensorChanged: first time+++++++++");
//
//
//        } else {
//            float deltaX = (mLastX - x);
//            float deltaY = (mLastY - y);
//            float deltaZ = (mLastZ - z);
//            if (Math.abs(deltaX) < NOISE) deltaX = (float)0.0;
//            if (Math.abs(deltaY) < NOISE) deltaY = (float)0.0;
//            if (Math.abs(deltaZ) < NOISE) deltaZ = (float)0.0;
//            mLastX = x;
//            mLastY = y;
//            mLastZ = z;
//            tvX.setText(Float.toString(deltaX));
//            tvY.setText(Float.toString(deltaY));
//            tvZ.setText(Float.toString(deltaZ));
////            if(Math.abs(deltaX) > NOISE || Math.abs(deltaY) > NOISE || Math.abs(deltaZ) > NOISE){
////                Log.d(TAG, "delta-x= " + Float.toString(deltaX) + "delta-Y= " + Float.toString(deltaY) );
////            }
//
//        }
//    }
//
//    protected void onResume() {
//        super.onResume();
//        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
//    }
//    protected void onPause() {
//        super.onPause();
//        mSensorManager.unregisterListener(this);
//    }
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//// can be safely ignored for this demo
//    }
//}
//public class MainActivity extends AppCompatActivity {
//
//    // create variables of the two class
//    private Accelerometer accelerometer;
//    private Gyroscope gyroscope;
//
//    private static final String TAG = "MainActivity";
//    TextView xValue, yValue, zValue;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // instantiate them with this as context
//        accelerometer = new Accelerometer(this);
//        gyroscope = new Gyroscope(this);
//
//        xValue = (TextView) findViewById(R.id.xValue);
//        yValue = (TextView) findViewById(R.id.yValue);
//        zValue = (TextView) findViewById(R.id.zValue);
//
//
//        // create a listener for accelerometer
//        accelerometer.setListener(new Accelerometer.Listener() {
//            //on translation method of accelerometer
//            @Override
//            public void onTranslation(float tx, float ty, float ts) {
//                Log.d(TAG, "onTranslation: x= " + tx + "Y= " + ty + "Z= " + ts);
//                // set the color red if the device moves in positive x axis
//                if (tx > 1.0f) {
//                    getWindow().getDecorView().setBackgroundColor(Color.RED);
//                }
//                // set the color blue if the device moves in negative x axis
//                else if (tx < -1.0f) {
//                    getWindow().getDecorView().setBackgroundColor(Color.BLUE);
//                }
//                else if (ty > 1.0f) {
//                    getWindow().getDecorView().setBackgroundColor(Color.CYAN);
//                }
//                // set the color blue if the device moves in negative y axis
//                else if (ty < -1.0f) {
//                    getWindow().getDecorView().setBackgroundColor(Color.GRAY);
//                }
//
//                xValue.setText("X = " + tx);
//                yValue.setText("Y = " + ty);
//
//
//            }
//        });
//
//        // create a listener for gyroscope
//        gyroscope.setListener(new Gyroscope.Listener() {
//            // on rotation method of gyroscope
//            @Override
//            public void onRotation(float rx, float ry, float rz) {
//                // set the color green if the device rotates on positive z axis
//                if (rz > 1.0f) {
//                    getWindow().getDecorView().setBackgroundColor(Color.GREEN);
//                }
//                // set the color yellow if the device rotates on positive z axis
//                else if (rz < -1.0f) {
//                    getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
//                }
//                zValue.setText("Z = " + rz);
//            }
//        });
//    }
//
//    // create on resume method
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        // this will send notification to
//        // both the sensors to register
//        accelerometer.register();
//        gyroscope.register();
//    }
//
//    // create on pause method
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        // this will send notification in
//        // both the sensors to unregister
//        accelerometer.unregister();
//        gyroscope.unregister();
//    }
//}
