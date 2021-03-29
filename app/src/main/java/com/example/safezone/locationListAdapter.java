package com.example.safezone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class locationListAdapter extends RecyclerView.Adapter<locationListAdapter.locationListViewHolder> {

    private List<locationList> LocationList;
     private  Context context;
    public locationListAdapter(List<locationList> locationList, Context context) {
        this.LocationList = locationList;
        this.context=context;
    }

    public class locationListViewHolder extends RecyclerView.ViewHolder {
        TextView locationName , visitorsCounter , positiveVisitorsCounterNumber;
        CardView locationCard ;
        ProgressBar positiveVisitorsCounter;

        public locationListViewHolder(@NonNull View itemView) {
            super(itemView);
            locationName = itemView.findViewById(R.id.locationName) ;
            visitorsCounter = itemView.findViewById(R.id.visitorsCounter);
            positiveVisitorsCounter = itemView.findViewById(R.id.positiveVisitorsCounter);
            positiveVisitorsCounterNumber =itemView.findViewById(R.id.positiveVisitorsCounterNumber);
            locationCard=itemView.findViewById(R.id.cardList);
        }
    }

    @NonNull
    @Override
    public locationListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_info_list, parent, false);
        locationListViewHolder viewfinder = new locationListViewHolder(v);
        return viewfinder;
    }

    @Override
    public void onBindViewHolder(@NonNull locationListViewHolder holder, int position) {

        locationList obj = LocationList.get(position);
        holder.locationName.setText(obj.getLocationName());
        holder.visitorsCounter.setText(obj.getVisitorsCounter());
        holder.positiveVisitorsCounter.setMax(Integer.parseInt(obj.getPositiveVisitorsCounter())+Integer.parseInt(obj.getVisitorsCounter()));
        holder.positiveVisitorsCounter.setProgress(Integer.parseInt(obj.getPositiveVisitorsCounter()));
        holder.positiveVisitorsCounterNumber.setText(obj.getPositiveVisitorsCounter());
        holder.locationCard.setAnimation(AnimationUtils.loadAnimation(context,R.anim.scale));
    }

    @Override
    public int getItemCount() {
        return LocationList.size();
    }
}
