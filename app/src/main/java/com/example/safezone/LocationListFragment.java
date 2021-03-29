package com.example.safezone;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LocationListFragment extends Fragment {

    private static final String TAG ="homepage" ;
    RecyclerView locationListRecyclerView;
    locationListAdapter adapter;
    DatabaseReference getLocationReference;
    private ArrayList<locationList> ListOfLocations;
    private Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_display_places, container , false);
        bundle = this.getArguments();
        ListOfLocations = new ArrayList<>();
        displayLocations();
        locationListRecyclerView = (RecyclerView) view.findViewById(R.id.locationList);
        adapter = new locationListAdapter(ListOfLocations,this.getActivity());
        locationListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        locationListRecyclerView.setAdapter(adapter);
        return view;
    }
    public void displayLocations(){
        getLocationReference = FirebaseDatabase.getInstance().getReference("User").child(getArguments().getString("userID"));
        getLocationReference.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                if(snapshot.exists()){
                    Log.d(TAG, "data exists");
                    long DBCounter = (long) snapshot.child("locationCounter").getValue();
                    if(DBCounter == 0){
                        ListOfLocations.add(new locationList("No Locations to show"
                                ,"0"
                                , "0"));
                        adapter.notifyDataSetChanged();
                    }else{
                        for(int i = 1 ; i <= DBCounter;i++) {
                            String LocationName = snapshot.child("locationName_"+i).getValue().toString();
                            getPositiveCounter(LocationName);
                            adapter.notifyDataSetChanged();
                        }

                    }
                }else{
                    Log.d(TAG, "no locations (else)");
                    ListOfLocations.add(new locationList("No Locations to show"
                            ,"1"
                            , "1"));
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getPositiveCounter(String locationName){
        Log.d(TAG, "connecting to firebase 02");
        getLocationReference = FirebaseDatabase.getInstance().getReference("Locations").child(locationName);
        getLocationReference.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                if(snapshot.exists()){
                    Log.d(TAG, "geting location info");
                    String DBVisitorsCounter = snapshot.child("Counter").getValue().toString();
                    String DBPositiveVisitorsCounter = snapshot.child("PositiveCounter").getValue().toString();
                    Log.d(TAG, "initialize the list ");
                    ListOfLocations.add(new locationList(locationName
                            ,DBVisitorsCounter
                            ,DBPositiveVisitorsCounter));
                    adapter.notifyDataSetChanged();
                }
                else{
                    Log.d(TAG, "no data exists 02");

                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
