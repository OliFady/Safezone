package com.example.safezone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class displayPlaces extends AppCompatActivity {
    private static final String TAG ="homepage" ;
    RecyclerView locationListRecyclerView;
    locationListAdapter adapter;
    DatabaseReference getLocationReference;
    private ArrayList<locationList> ListOfLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_places);

        ListOfLocations = new ArrayList<>();
        displayLocations();

        locationListRecyclerView = (RecyclerView) findViewById(R.id.locationList);
        adapter = new locationListAdapter(ListOfLocations);
        locationListRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        locationListRecyclerView.setAdapter(adapter);
    }
    public void displayLocations(){
        getLocationReference = FirebaseDatabase.getInstance().getReference("User").child(getIntent().getExtras().getString("userID"));
        getLocationReference.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                if(snapshot.exists()){
                    Log.d(TAG, "data exists");
                    long DBCounter = (long) snapshot.child("locationCounter").getValue();
                    if(DBCounter == 0){
                        ListOfLocations.add(new locationList("No Locations to show"
                                ,"Negative Visitors Counter: 0"
                                , "Positive Visitors Counter: 0"));
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
                            ,"Negative Visitors Counter: 0"
                            , "Positive Visitors Counter: 0"));
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
                    ListOfLocations.add(new locationList("Location Address: "+locationName
                            ,"Negative Visitors Counter: "+DBVisitorsCounter
                            ,DBPositiveVisitorsCounter));
                    adapter.notifyDataSetChanged();
                }
                else{
                    Log.d(TAG, "no data exists 02");
                    ListOfLocations.add(new locationList("No Locations to show"
                            ,"Negative Visitors Counter: 0"
                            ,"Positive Visitors Counter: 0"));
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}