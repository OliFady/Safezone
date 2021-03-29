package com.example.safezone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePage extends AppCompatActivity {

    private static final String TAG ="homepage" ;

    Bundle bundle ;
    private BottomNavigationView.OnNavigationItemSelectedListener navListener;
    private homeFragment homeFragment;
    private LocationListFragment locationListFragment;
    private PharmacyFragment pharmacyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        navigationAction();

        homeFragment = new homeFragment();
        locationListFragment =  new LocationListFragment();
        pharmacyFragment = new PharmacyFragment();
        bundle = new Bundle();
        bundle.putString("userID",getIntent().getExtras().getString("userID"));
        bundle.putString("fName",getIntent().getExtras().getString("fName"));
        bundle.putString("lName",getIntent().getExtras().getString("lName"));
        bundle.putString("Age",getIntent().getExtras().getString("Age"));
        bundle.putString("Status",getIntent().getExtras().getString("Status"));
        homeFragment.setArguments(bundle);
        locationListFragment.setArguments(bundle);
        pharmacyFragment.setArguments(bundle);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , homeFragment).commit();
    }

private void navigationAction(){
    navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()){

                        case R.id.HomePage:
                            selectedFragment = homeFragment;
                            break;
                        case R.id.mapsActivity:
                            selectedFragment = locationListFragment;
                            break;
                        case R.id.displayPlaces:
                            selectedFragment = pharmacyFragment;
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , selectedFragment).commit();
                    return true;
                }
            };
}



/*


  @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
     NavigationView.OnNavigationItemSelectedListener navListener = new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.HomePage:{
                        intent = new Intent(HomePage.this , HomePage.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.mapsActivity:{
                        intent = new Intent(HomePage.this , MapsActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.pharmacy_search_activity:{
                        intent = new Intent(HomePage.this , pharmacy_search_activity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.displayPlaces:{
                        intent = new Intent(HomePage.this , displayPlaces.class);
                        startActivity(intent);
                        break;
                    }
                    default:{
                        break;
                    }
                }

                return true;
            }
        };

            }
        })
    public void markAsPositive(){
        getLocationReference = FirebaseDatabase.getInstance().getReference("User").child(getIntent().getExtras().getString("userID"));
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
                    }else{
                        DBPositiveCounter += 1;
                        updateLocationInfo = new cLocation(locationName , lat , lon , 1, id ,DBPositiveCounter);
                        getLocationReference = FirebaseDatabase.getInstance().getReference("Locations");
                        getLocationReference.child(locationName).setValue(updateLocationInfo);
                    }
                }else{
                    Toast.makeText(HomePage.this,  "No Location",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
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
                            ,"Positive Visitors Counter: "+DBPositiveVisitorsCounter));
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

*/
    /*
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(HomePage.this, MapsActivity.class);
                intent.putExtra("ID",id);
                startActivity(intent);
            }
        });*/
}

