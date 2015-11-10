package com.trekcoders.safar.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.trekcoders.safar.R;

public class TrailMapActivity extends AppCompatActivity {

    static final LatLng Pos = new LatLng(27.962869 , 86.911469);
    private GoogleMap googleMap;
    Marker marker;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trail_map);

        initToolbar();

        try {
            if (googleMap == null) {

                googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            }

            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            googleMap.setMyLocationEnabled(true);

            googleMap.getUiSettings().setZoomControlsEnabled(true);

            googleMap.setBuildingsEnabled(true);

            googleMap.setTrafficEnabled(true);

            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(Pos, 15);
            googleMap.animateCamera(yourLocation);

            marker = googleMap.addMarker(new MarkerOptions().position(Pos).title("Safar Checkpoint"));

        }

        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_trailmap);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbar.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_logo);
    }
}
