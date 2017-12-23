package com.example.android.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.SimpleTimeZone;

/**
 * Created by sagar on 21/12/17.
 */

public class QuakeAdapter extends ArrayAdapter<QuakeDescription> {

    public QuakeAdapter(@NonNull Activity context, @NonNull ArrayList<QuakeDescription> objects) {
        super( context, 0, objects );
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from( getContext() ).inflate(
                    R.layout.item_view, parent, false );
        }

        final QuakeDescription currentDes = getItem( position );

        TextView magnitudeTv = (TextView) listItemView.findViewById( R.id.magnitude );
        TextView placeTv = (TextView) listItemView.findViewById( R.id.primary_location );
        TextView placeOffsetTv = (TextView) listItemView.findViewById( R.id.location_offset );
        TextView dateTv = (TextView) listItemView.findViewById( R.id.date );
        TextView timeTv = (TextView) listItemView.findViewById( R.id.time );

        /**
         * setting the description of the quake
         * setting the magnitude
         */
        double magnitude = currentDes.getMagnitude();
        String mag = formatMagnitude( magnitude );
        magnitudeTv.setText( mag );

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeTv.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentDes.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor( ContextCompat.getColor( getContext(), magnitudeColor ));

        /**
         * splitting the QuakePlace description into two strings
         */

        String quakePlace = currentDes.getQuakePlace();
        int index = quakePlace.indexOf( "of" );

        String place, place_offset;
        if(index == -1){
            place_offset = "Near of ";
            place = quakePlace;
        }
        else{
            place_offset = quakePlace.substring( 0, index+2 );
            place = quakePlace.substring( index+3, quakePlace.length() );
        }

        placeTv.setText( place );
        placeOffsetTv.setText( place_offset );

        /**
         * setting the date by converting the UNIX formatted milliseconds to human readable text
         */

        Date dateObject = new Date(currentDes.getmTimeInMilliSeconds());
        String formattedDate = formatDate(dateObject);
        dateTv.setText( formattedDate );

        /**
         * setting the time by converting the UNIX formatted milliseconds to human readable text
         */
        String formattedTime = formatTime(dateObject);
        timeTv.setText( formattedTime );
        return listItemView;
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat date = new SimpleDateFormat( "LLL dd, yyyy" );
        return date.format( dateObject );
    }

    private String formatTime(Date dateObject){
        SimpleDateFormat time = new SimpleDateFormat( "h:mm:ss" );
        return time.format( dateObject );
    }
    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }
    private int getMagnitudeColor(double mag){
        switch ((int) mag){
            case 1:
                return R.color.magnitude1;
            case 2:
                return R.color.magnitude2;
            case 3:
                return R.color.magnitude3;
            case 4:
                return R.color.magnitude4;
            case 5:
                return R.color.magnitude5;
            case 6:
                return R.color.magnitude6;
            case 7:
                return R.color.magnitude7;
            case 8:
                return R.color.magnitude8;
            case 9:
                return R.color.magnitude9;
            default:
                return R.color.magnitude10plus;
        }
    }
}
