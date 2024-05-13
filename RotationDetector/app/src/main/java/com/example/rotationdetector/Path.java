package com.example.rotationdetector;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Path {

    private static final String TAG = "Path";
    private class PathItem{
        private float distance;
        private int angle;
        private String direction;

        public PathItem(float distance, String direction, int angle) {
            this.distance = distance;
            this.angle = angle;
            this.direction = direction;
        }
    }
    private ArrayList<PathItem> path = new ArrayList<>();

    public void addItem(float distance, String direction, int angle){
        path.add(new PathItem(distance, direction, angle));
    }

    public String getJsonFormat() {

        try {
            JSONObject config1 = new JSONObject();
            JSONObject path_json = new JSONObject();

//            for (PathItem item : path) {
//                config1.put("Distance", item.distance);
//                config1.put("Direction", item.direction);
//                config1.put("Angle", item.angle);
//                path_json.put()
//            }
            return path_json.toString();
        }catch (Exception e) {
            e.printStackTrace();
        }



        return null;
    }
    @NonNull
    @Override
    public String toString() {
        String str = "";
        for (PathItem item : path) {
            str += ("Distance:" +  item.distance + "\n");
            str += ("Direction:" +  item.direction + "\n");
            str += ("Angle:" +  item.angle + "\n");
            str += ("-------------------\n");
        }
        return str;
    }
}


