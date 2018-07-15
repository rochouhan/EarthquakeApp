package com.example.android.quakereport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import android.graphics.drawable.GradientDrawable;

public class ListAdapter extends ArrayAdapter {

    public ListAdapter(Context context, int resource, ArrayList<Earthquake> earthquakes){
        super(context, 0, earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Earthquake earthquake = (Earthquake) getItem(position);

        TextView magnitude = (TextView) listItemView.findViewById(R.id.magnitude);
        TextView locationView = (TextView) listItemView.findViewById(R.id.primary_location);
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        TextView offsetView = (TextView) listItemView.findViewById(R.id.location_offset);
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();

        int magnitudeColor = getMagnitudeColor(earthquake.getMagnitude());
        magnitudeCircle.setColor(magnitudeColor);

        String place = earthquake.getLocation();
        ArrayList<String> placeList = splitString(place);
        String offset = placeList.get(0);
        String location = placeList.get(1);

        Date date = new Date(earthquake.getDate());

        dateView.setText(formatDate(date));
        timeView.setText(formatTime(date));
        magnitude.setText(formatMagnitude(earthquake.getMagnitude()));
        locationView.setText(location);
        offsetView.setText(offset);


        return listItemView;
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    private String formatMagnitude(double magnitude){
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        return decimalFormat.format(magnitude);
    }

    private ArrayList<String> splitString(String place){
        String offset = "Near";
        String location;
        int starting_index = place.indexOf(" of ");
        if (starting_index != -1){
            offset = place.substring(0, starting_index + 4);
            location = place.substring(starting_index + 4);
        }
        else{
            location = place;
        }
        ArrayList<String> strings = new ArrayList<String>();
        strings.add(offset);
        strings.add(location);
        return strings;
    }

    private int getMagnitudeColor(double magnitude){
        int color;
//        Log.v("int magnitude ", ma)
        switch ((int)(magnitude % 10)){
            case 0:
                color = R.color.magnitude1;
                break;
            case 1:
                color = R.color.magnitude1;
                break;
            case 2:
                color = R.color.magnitude2;
                break;
            case 3:
                color = R.color.magnitude3;
                break;
            case 4:
                color = R.color.magnitude4;
                break;
            case 5:
                color = R.color.magnitude5;
                break;
            case 6:
                color = R.color.magnitude6;
                break;
            case 7:
                color = R.color.magnitude7;
                break;
            case 8:
                color = R.color.magnitude8;
                break;
            case 9:
                color = R.color.magnitude9;
                break;
            default:
                color = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), color);
    }
}
