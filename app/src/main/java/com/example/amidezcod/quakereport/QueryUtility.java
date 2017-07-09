package com.example.amidezcod.quakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by amidezcod on 2/7/17.
 */

 class QueryUtility {

     QueryUtility() {
    }

     ArrayList<EarthQuakePojo> fetchRequestUrl(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonresponse = "";
        try {
            jsonresponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.v("TAG", e.getMessage());
        }
        return extractDataFromJson(jsonresponse);
    }

    private ArrayList<EarthQuakePojo> extractDataFromJson(String jsonresponse) {

        if (TextUtils.isEmpty(jsonresponse))
            return null;
        else {
            ArrayList<EarthQuakePojo> earthQuakePojos = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(jsonresponse);
                JSONArray jsonArray = jsonObject.getJSONArray("features");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("properties");
                    double mag = jsonObject2.getDouble("mag");
                    String place = jsonObject2.getString("place");
                    long time = jsonObject2.getLong("time");
                    String stringURL = jsonObject2.getString("url");
                    int tsunami = jsonObject2.getInt("tsunami");
                    EarthQuakePojo earthQuakePojo = new EarthQuakePojo(time, place, stringURL, mag, tsunami);
                    earthQuakePojos.add(earthQuakePojo);

                }

            } catch (JSONException e) {
                Log.v("TAG", e.getMessage());

            }
            return earthQuakePojos;
        }

    }

    private String makeHttpRequest(URL url) throws IOException {
        String jsonString = "";
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonString = jsonResponse(inputStream);
            } else {
                Log.v("TAG", "Response code" + httpURLConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.v("TAG", e.getMessage());

        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonString;

    }

    private String jsonResponse(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = bufferedReader.readLine();
        while (line != null) {
            stringBuilder.append(line);
            line = bufferedReader.readLine();
        }
        return stringBuilder.toString();
    }

    private URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.v("TAG", e.getMessage());
        }
        return url;
    }
}
