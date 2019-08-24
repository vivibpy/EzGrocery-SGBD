package com.example.ezgrocery.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ezgrocery.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions().position(new LatLng(-22.9551131,-43.1714031)).title("Loja Unirio"));
        map.addMarker(new MarkerOptions().position(new LatLng(-22.9510851,-43.1862964)).title("Loja Metrô Botafogo"));
        map.addMarker(new MarkerOptions().position(new LatLng(-22.9110652,-43.1778636)).title("Loja Centro"));
    }
}
