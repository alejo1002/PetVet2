package com.software.alejo.petvet2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.software.alejo.petvet2.Entities.VeterinariesListSerializable;
import com.software.alejo.petvet2.Entities.Veterinary;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
//FragmentActivity
    private GoogleMap mMap;
    private List<Marker> markers = new ArrayList<Marker>();

    private VeterinariesListSerializable vets = new VeterinariesListSerializable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        vets = (VeterinariesListSerializable)intent.getSerializableExtra(MainActivity.VETERINARY_LIST);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Marcador
        LatLng sena = new LatLng(6.255512, -75.575348);
        mMap.addMarker(new MarkerOptions()
                .position(sena)
                .title("SENA sede Medellín Centro")
                .snippet("Ubicación Actual")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        createMarkers();
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(sena)
                .zoom(15)
                .build();

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        googleMap.setOnInfoWindowClickListener(this);
    }

    private void createMarkers() {
        LatLng marker;
        for (Veterinary item: vets.getVeterinaries()) {
            marker = new LatLng(item.getLatitude(), item.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(marker)
                    .title(item.getName())
                    //.snippet(item.getHorario())
            );
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        for (Veterinary item: vets.getVeterinaries()){
            if (marker.getTitle().equals(item.getName())){
                InfoMapDialogFragment.newInstance(item.getName(), item.getHorario()).show(getSupportFragmentManager(), null);
            }
        }
    }
}
