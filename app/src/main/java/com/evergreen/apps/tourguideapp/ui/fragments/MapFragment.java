package com.evergreen.apps.tourguideapp.ui.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.example.tourguideapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = MapFragment.class.getSimpleName();
    private GoogleMap mMap;
    private double mLat;
    private double mLong;


    public MapFragment() {
    }

    public static MapFragment newInstance(double lat, double lng){
        MapFragment mapFragment = new MapFragment();
        Bundle args = new Bundle();
        args.putDouble("LAT", lat);
        args.putDouble("LNG", lng);
        mapFragment.setArguments(args);
        return mapFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null){
            Bundle args = getArguments();
            mLat  = args.getDouble("LAT");
            mLong = args.getDouble("LNG");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng newLoc = new LatLng(mLat, mLong);
        mMap.addMarker(new MarkerOptions()
                .position(newLoc).title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(newLoc));

        mMap.setBuildingsEnabled(true);
        mMap.setMinZoomPreference(16);
    }
}
