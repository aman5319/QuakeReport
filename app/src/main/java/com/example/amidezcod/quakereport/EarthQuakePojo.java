package com.example.amidezcod.quakereport;

import android.support.annotation.NonNull;

/**
 * Created by amidezcod on 2/7/17.
 */

 class EarthQuakePojo implements Comparable<EarthQuakePojo> {
    private long time;
    private String placeName;
    private String StringUrl;
    private double magnitude;
    private int tsunami;

     EarthQuakePojo(long time, String place_name, String url, double magnitude, int tsunami) {
        this.time = time;
        this.placeName = place_name;
        this.StringUrl = url;
        this.magnitude = magnitude;
        this.tsunami = tsunami;
    }

     long getTime() {
        return time;
    }

     String getPlaceName() {
        return placeName;
    }

     String getStringUrl() {
        return StringUrl;
    }

     double getMagnitude() {
        return magnitude;
    }

     int getTsunami() {
        return tsunami;
    }



    @Override
    public int compareTo(@NonNull EarthQuakePojo earthQuakePojo) {
        double mag2 = earthQuakePojo.getMagnitude();

        if (this.getMagnitude() == mag2)
            return 0;
        else if (this.getMagnitude() > mag2)
            return 1;
        else
            return -1;
    }
}
