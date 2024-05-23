package com.example.rotationdetector;

import static java.lang.Math.abs;

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
    public ArrayList<PathItem> path = new ArrayList<>();

    public void addItem(float distance, String direction, int angle){
        path.add(new PathItem(distance, direction, angle));
    }

    public boolean isMatch(Path pattern, int threshold){
        ArrayList<PathItem> pis = pattern.path;
        if(pis.size() != path.size()){
            Log.d(TAG, "isMath: Path sizes are not match "+ pis.size() + " - " + path.size());
            return false;
        }
        for (int i  =  0 ; i < pis.size(); i++) {
            if (path.get(i).angle != pis.get(i).angle |
                    path.get(i).direction != pis.get(i).direction){
                Log.d(TAG, "isMath: Path directions or angles are not match  "+ pis.get(i).direction + " - " + path.get(i).direction + " or " + pis.get(i).angle + " - " + path.get(i).angle);
                return false;
            }
            if( abs(path.get(i).distance - pis.get(i).distance) > threshold){
                Log.d(TAG, "isMath: Path distances are not match  "+ pis.get(i).distance + " - " + path.get(i).distance);
                return false;
            }
        }
        return true;
    }

    public String getJsonFormat() {

        try {
            JSONObject config1 = new JSONObject();
            JSONObject path_json = new JSONObject();

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


