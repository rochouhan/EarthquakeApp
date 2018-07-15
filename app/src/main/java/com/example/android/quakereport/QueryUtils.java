package com.example.android.quakereport;

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
import java.util.ArrayList;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getName();

    /** Sample JSON response for a USGS query */


    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Earthquake> extractEarthquakes(String url) {
        Log.v(LOG_TAG, "Extract earthquakes");

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        URL requestURL = null;
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.
             requestURL = createURL(url);

             String jsonResponse = null;
             try {
                 jsonResponse = makeHttpRequest(requestURL);
             }
             catch (IOException e){
                 Log.e(LOG_TAG, "Error closing input stream", e);
             }
//            Convert SAMPLE_JSON_RESPONSE String into a JSONObject
            JSONObject data = new JSONObject(jsonResponse);

//            Extract “features” JSONArray
            JSONArray features = data.getJSONArray("features");
//            Loop through each feature in the array

            for (int i=0; i < features.length(); i++) {
//            Get earthquake JSONObject at position i
                JSONObject earthquake = features.getJSONObject(i);
//            Get “properties” JSONObject
                JSONObject properties = earthquake.getJSONObject("properties");

//            Extract “mag” for magnitude
                Double magnitude = (Double) properties.getDouble("mag");
//            Extract “place” for location
                String location = (String) properties.getString("place");
//            Extract “time” for time
                Long time = (Long) properties.getLong("time");
//            Extract "url" for the url
                String earthquakeUrl = (String) properties.getString("url");
//            Create Earthquake java object from magnitude, location, and time
                Earthquake earthquake_instance = new Earthquake(magnitude, location, time, earthquakeUrl);
//            Add earthquake to list of earthquakes
                earthquakes.add(earthquake_instance);
            }





        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

    protected static URL createURL(String urlString){
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Unable to create URL object from" + urlString, e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";

        if(url == null || url.toString().isEmpty()){
            return jsonResponse;
        }

        HttpURLConnection urlConnection =null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else{
                Log.e(LOG_TAG, "Error in response code: " + urlConnection.getResponseCode());
            }
        }
        catch (IOException e){
            Log.e(LOG_TAG, "Unable to receive results", e);
        }
        finally {
            if (urlConnection !=null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream)throws IOException{
        StringBuilder stream = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null){
                stream.append(line);
                line = bufferedReader.readLine();
            }
        }
        return stream.toString();
    }

}