package com.example.amidezcod.quakereport.Model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Properties implements Comparable<Properties> {

    @SerializedName("mag")

    private Float mag;
    @SerializedName("place")

    private String place;
    @SerializedName("time")
    private Long time;

    @SerializedName("url")
    private String url;


    public Float getMag() {
        return mag;
    }

    public void setMag(Float mag) {
        this.mag = mag;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int compareTo(@NonNull Properties properties) {
        double mag2 = properties.getMag();

        if (this.getMag() == mag2)
            return 0;
        else if (this.getMag() > mag2)
            return 1;
        else
            return -1;
    }
}
