package com.example.safezone;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class homeFragment extends Fragment {

    private Intent intent;
    private String id ;
    private DatabaseReference getLocationReference;
    private cLocation updateLocationInfo;
    Button mark, addLocation;
    TextView userName, userAge, userStatus;
    Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container , false);

        bundle = this.getArguments();

        addLocation = view.findViewById(R.id.addLocation);
        userName = view.findViewById(R.id.user_name);
        userAge = view.findViewById(R.id.user_age);
        userStatus = view.findViewById(R.id.status);
        mark = view.findViewById(R.id.mark);

        userName.setText(getArguments().getString("fName") + " " + getArguments().getString("lName"));
        userAge.setText(getArguments().getString("Age"));
        userStatus.setText(getArguments().getString("Status"));
        id = getArguments().getString("userID");

        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (userStatus.getText().toString()) {
                    case "Negative": {
                        changePositiveAlert();
                        break;
                    }
                     case "Positive": {

                        break;
                    }
                }
            }
        });

        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (userStatus.getText().toString()) {
                    case "Negative":
                    intent = new Intent(getContext(), MapsActivity.class);
                    intent.putExtra("ID", id);
                    startActivity(intent);
                    break;
                    case "Positive":
                        Toast.makeText(getContext(),"Not allow to add any locations",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        return view;

    }
    private void changePositiveAlert(){

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Changing Status to Positive");
        alert.setMessage("If you change your status to positive you won't be able to add any location and change your status to negative again till 14 days");
        alert.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                markAsPositive();
                userStatus.setText("Positive");
                mark.setText("Mark as Negative");
                Toast.makeText(getContext(), "Status Changed to Positive",Toast.LENGTH_LONG).show();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.create().show();
    }
    public void markAsPositive(){
        getLocationReference = FirebaseDatabase.getInstance().getReference("User").child(id);
        getLocationReference.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                if(snapshot.exists()){
                    getLocationReference.child("status").setValue("Positive");
                    long DBCounter = (long) snapshot.child("locationCounter").getValue();
                    for(int i = 1 ; i <= DBCounter;i++) {
                        String LocationName_num = snapshot.child("locationName_"+i).getValue().toString();
                        increasePositiveCounter(LocationName_num);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void increasePositiveCounter(String locationName){
        getLocationReference = FirebaseDatabase.getInstance().getReference("Locations").child(locationName);
        getLocationReference.addListenerForSingleValueEvent(new ValueEventListener(){
            long DBPositiveCounter = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                double lat = (double) snapshot.child("Latitude").getValue();
                double lon = (double)snapshot.child("Longitude").getValue();
                DBPositiveCounter = (long) snapshot.child("PositiveCounter").getValue();
                if(snapshot.exists()){
                    if (DBPositiveCounter == 0){
                        updateLocationInfo = new cLocation(locationName , lat , lon , 1, id ,1);
                        getLocationReference = FirebaseDatabase.getInstance().getReference("Locations");
                        getLocationReference.child(locationName).setValue(updateLocationInfo);
                        Toast.makeText(getContext(),"0000",Toast.LENGTH_SHORT).show();
                    }else{
                        DBPositiveCounter += 1;
                        updateLocationInfo = new cLocation(locationName , lat , lon , 1, id ,DBPositiveCounter);
                        getLocationReference = FirebaseDatabase.getInstance().getReference("Locations");
                        getLocationReference.child(locationName).setValue(updateLocationInfo);
                        Toast.makeText(getContext(),locationName + "#"+DBPositiveCounter,Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext(),  "No Location",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
