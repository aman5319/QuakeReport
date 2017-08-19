
package com.example.amidezcod.quakereport.Model;

import com.google.gson.annotations.SerializedName;

public class Feature {


    @SerializedName("properties")
    private Properties properties;


    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }


}
