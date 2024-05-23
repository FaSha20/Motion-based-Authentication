package com.example.rotationdetector;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
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

    private static final int THRESHOLD = 3;

    private String AuthPattern, motionPattern;

    private Path authPath, initialPath;

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
        Button startAuthentication = findViewById(R.id.startAuthButton);
        Button stopAuthentication = findViewById(R.id.stopAuthButton);
        Button checkPattern = findViewById(R.id.checkAuthButton);
        TextView patternTextView = findViewById(R.id.patternTextView);
        TextView distanceTextView = findViewById(R.id.maxDistView);
        TextView errorTextView = findViewById(R.id.errorResultView);
        TextView validTextView = findViewById(R.id.validResultView);

        // Set click listeners for the buttons
        startCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patternTextView.setText("");
                distanceTextView.setText("");
                errorTextView.setText("");
                validTextView.setText("");
                // Start capturing motion
                motionCapture.startCapture();
            }

        });

        stopCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Stop capturing motion and get the captured pattern
                motionPattern = motionCapture.getMotionPattern();
                initialPath = motionCapture.getDistancePattern();
                // Stop capturing motion
                String jsonStr = motionCapture.stopCapture();
                SavaJsonFile(jsonStr);
            }
        });

        startAuthentication.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                patternTextView.setText("");
                distanceTextView.setText("");
                errorTextView.setText("");
                validTextView.setText("");
                motionCapture.startCapture();
            }
        });

        stopAuthentication.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AuthPattern = motionCapture.getMotionPattern();
                authPath = motionCapture.getDistancePattern();
                motionCapture.stopCapture();
                // Display the pattern, for example, in a TextView
                patternTextView.setText("Captured Auth Pattern: " + AuthPattern);
                distanceTextView.setText("The authentication path is: \n" + authPath.toString());

            }
        });



        checkPattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(initialPath.isMatch(authPath, THRESHOLD)){
                    validTextView.setText("Welcome!\n Your authentication was successful.");
                }else {
                    errorTextView.setText("Authentication failed.");
                }
            }
        });

        showPatternButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the captured motion pattern
                String motionPattern = motionCapture.getMotionPattern();
                // Display the pattern, for example, in a TextView
                patternTextView.setText("Captured Motion Pattern: " + motionPattern);
            }
        });

        showDistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                distanceTextView.setText("The captured path is: \n" + initialPath.toString());
            }
        });
    }

    public void SavaJsonFile(String jsonStr){
        try {
            Log.d(TAG, "SavaJsonFile: jsonStr\n"+jsonStr);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput("path.json", Context.MODE_PRIVATE));
            outputStreamWriter.write(jsonStr);
            outputStreamWriter.close();
            Log.d(TAG, "SavaJsonFile: done");
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}


