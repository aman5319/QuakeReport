package com.example.amidezcod.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by amidezcod on 2/7/17.
 */

public class EarthQuakeClassAdapter extends RecyclerView.Adapter<EarthQuakeClassAdapter.MyViewHolder> {
    private final String LOCATION_SEPERATOR = "of";
    int lastPosition = -1;
    private Context mContext;
    private ArrayList<EarthQuakePojo> mEarthQuakePojoArrayList;


    public EarthQuakeClassAdapter(Context mContext, ArrayList<EarthQuakePojo> mEarthQuakePojoArrayList) {
        this.mContext = mContext;
        this.mEarthQuakePojoArrayList = mEarthQuakePojoArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_earthquake, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onViewDetachedFromWindow(MyViewHolder holder) {
        holder.itemView.clearAnimation();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        EarthQuakePojo earthQuakePojo = mEarthQuakePojoArrayList.get(position);

        //for magnitude
        String formattedMagnitude = formattedMagnitude(earthQuakePojo.getMagnitude());
        holder.mag.setText(formattedMagnitude);
        GradientDrawable gradientDrawable = (GradientDrawable) holder.mag.getBackground();
        gradientDrawable.setColor(getMagnitudecolor(earthQuakePojo.getMagnitude()));

        //for location
        String Location = earthQuakePojo.getPlaceName();
        String primaryLocation;
        String locationOffset;
        if (Location.contains(LOCATION_SEPERATOR)) {
            String[] newString = Location.split("of");
            locationOffset = newString[0] + LOCATION_SEPERATOR;
            primaryLocation = newString[1];
        } else {
            locationOffset = mContext.getString(R.string.near_by);
            primaryLocation = Location;
        }
        holder.locationOffset.setText(locationOffset);
        holder.primaryLocation.setText(primaryLocation);

        //for date and time
        long epochtime = earthQuakePojo.getTime();
        Date date = new Date(epochtime);
        holder.date.setText(formatDate(date));
        holder.time.setText(formatTime(date));
        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            //use animation class to load default animations
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            //start the animation
            viewToAnimate.startAnimation(animation);
            //update the lastPosition
            lastPosition = position;
        }
    }

    private String formatTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    private String formatDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("LLL dd, yyyy", Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    public void swapData(ArrayList<EarthQuakePojo> earthQuakePojos) {
        this.mEarthQuakePojoArrayList = earthQuakePojos;
    }

    private int getMagnitudecolor(double magnitude) {
        int mColorResourceid;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 1:
                mColorResourceid = R.color.magnitude1;
                break;
            case 2:
                mColorResourceid = R.color.magnitude2;
                break;
            case 3:
                mColorResourceid = R.color.magnitude3;
                break;
            case 4:
                mColorResourceid = R.color.magnitude4;
                break;
            case 5:
                mColorResourceid = R.color.magnitude5;
                break;
            case 6:
                mColorResourceid = R.color.magnitude6;
                break;
            case 7:
                mColorResourceid = R.color.magnitude7;
                break;
            case 8:
                mColorResourceid = R.color.magnitude8;
                break;
            case 9:
                mColorResourceid = R.color.magnitude9;
                break;
            default:
                mColorResourceid = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(mContext, mColorResourceid);

    }


    private String formattedMagnitude(double magnitude) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        return decimalFormat.format(magnitude);
    }

    @Override
    public int getItemCount() {
        return mEarthQuakePojoArrayList.size();
    }

    public void clear() {
        if (mEarthQuakePojoArrayList != null && !mEarthQuakePojoArrayList.isEmpty()) {
            int sise = mEarthQuakePojoArrayList.size();
            mEarthQuakePojoArrayList.clear();
            notifyItemRangeRemoved(0, sise);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        TextView mag, locationOffset, primaryLocation, date, time;

        private MyViewHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.cardview_elements);
            mag = itemView.findViewById(R.id.magnitude);
            locationOffset = itemView.findViewById(R.id.location_offset);
            primaryLocation = itemView.findViewById(R.id.primary_location);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);

        }
    }
}
