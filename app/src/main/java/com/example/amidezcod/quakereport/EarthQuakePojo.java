package com.example.amidezcod.quakereport;

import java.net.URL;

/**
 * Created by amidezcod on 2/7/17.
 */

public class EarthQuakePojo {
    private long time;
    private String placeName;
    private URL url;
    private double magnitude;
    private int tsunami;

    public EarthQuakePojo(long time, String place_name, URL url, double magnitude, int tsunami) {
        this.time = time;
        this.placeName = place_name;
        this.url = url;
        this.magnitude = magnitude;
        this.tsunami = tsunami;
    }

    public long getTime() {
        return time;
    }

    public String getPlaceName() {
        return placeName;
    }

    public URL getUrl() {
        return url;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public int getTsunami() {
        return tsunami;
    }
}
