package com.example.safezone;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;

public class PharmacyFragment extends Fragment {

    private RecyclerView pharmacyRecyclerViewer;
    private ArrayList<Pharmacy> pharmacies = new ArrayList<Pharmacy>();
    private EditText searchPharmicies;
    private static final String TAG = "MainActivity";
    private static final int erorrDialogRequest = 9001;
    private AppCompatButton mapsButton ;
    private View view ;
    private  Bundle bundle ;
    String id ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_pharmacy_search_activity, container , false);
        pharmacyRecyclerViewer = view.findViewById(R.id.pharmacyRecyclerViewer);
        bundle=this.getArguments();
        id = getArguments().getString("userID");
        setPharmacyRecyclerViewer();
        openMaps();
        return view;
    }

    public void openMaps(){
        mapsButton = view.findViewById(R.id.nearabyPharmcyMaps);
        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapsActivity = new Intent(getActivity().getApplicationContext(),MapsActivity.class);
                mapsActivity.putExtra("ID",id);
                startActivity(mapsActivity);
            }
        });
    }

    public boolean isServicesOk() {
        Log.d(TAG, "isServicesOk: checking google services");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());
        if(available== ConnectionResult.SUCCESS){
            Log.d(TAG, "isServicesOk: Services is Working");
            return  true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG, "isServicesOk: an erorr ocurred");
            Dialog dialog =GoogleApiAvailability.getInstance().getErrorDialog(getActivity(),available,erorrDialogRequest);
            dialog.show();
        }
        else{
            Toast.makeText(getContext(),"You cannot make map Requests",Toast.LENGTH_SHORT).show();
        }
        return  false;
    }

    public void setPharmacyRecyclerViewer(){
        PharmacyRecyclerView pharmacyRecyclerView = new PharmacyRecyclerView(getContext());
        pharmacyRecyclerViewer.setAdapter(pharmacyRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        pharmacyRecyclerViewer.setLayoutManager(linearLayoutManager);
        pharmacyRecyclerViewer.hasFixedSize();
        setPharmacies();
        pharmacyRecyclerView.setPharmacies(pharmacies);
        searchPharmicies= view.findViewById(R.id.searchPharmacy);
        searchPharmicies.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pharmacyRecyclerView.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void setPharmacies(){
        pharmacies.add(new Pharmacy("Elezaby",35,R.drawable.elezaby,"19600"));
        pharmacies.add(new Pharmacy("Seif",30,R.drawable.seif,"19199"));
        pharmacies.add(new Pharmacy("Masr",27,R.drawable.masr,"19110"));
        pharmacies.add(new Pharmacy("EzzEldin",40,R.drawable.ezzeldin2,"15055"));
        pharmacies.add(new Pharmacy("19011",25,R.drawable.nintennzero,"19011"));
    }

}
