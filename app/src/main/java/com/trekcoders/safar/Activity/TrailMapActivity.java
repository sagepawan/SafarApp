package com.trekcoders.safar.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.trekcoders.safar.R;
import com.trekcoders.safar.utils.GMapV2GetRouteDirection;

import org.w3c.dom.Document;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TrailMapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    Toolbar mToolbar;

    private MapFragment mapFragment;

    Location location;
    LocationManager locationManager;


    GoogleMap map;
    MarkerOptions toMarker, fromMarker, marker;
    Document document;
    double toLat, toLong, fromLat, fromLong;
    LatLng fromPosition, toPosition;

    GMapV2GetRouteDirection gMapV2GetRouteDirection;

    public final static double AVERAGE_RADIUS_OF_EARTH = 6371;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trail_map);

        initToolbar();


        if (!isGooglePlayServicesAvailable()) {
            finish();
        }

        fromLat = 27.7003548;
        fromLong = 85.3106189;
        toLat = 27.7075505;
        toLong = 85.3351872;

        fromPosition = new LatLng(fromLat, fromLong);
        toPosition = new LatLng(toLat, toLong);

        toMarker = new MarkerOptions();
        fromMarker = new MarkerOptions();
        marker = new MarkerOptions();
        mapFragment = (MapFragment) this.getFragmentManager().findFragmentById(R.id.map);
        gMapV2GetRouteDirection = new GMapV2GetRouteDirection();

        map = mapFragment.getMap();

        getCurrentLocation();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(true);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(true);

        new GetRoute().execute();
    }


    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    //get Address from longitude and latitude
    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                if (address.getSubLocality() != null)
                    result.append(address.getSubLocality()).append(", ");//sanepa
                result.append(address.getLocality()).append(", ");//lalitpur
                result.append(address.getCountryName());//nepal
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        } catch (NullPointerException err) {
            Log.e("tag", err.getMessage());
        }
        return result.toString();
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

    public void getCurrentLocation() {
        map.clear();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        if (locationManager != null) {
            location = locationManager.getLastKnownLocation(bestProvider);

            if (location != null) {
                setAddress(location.getLatitude(), location.getLongitude());
            } else {
                Toast.makeText(this, "Waiting for location..", Toast.LENGTH_LONG).show();
                locationManager.requestLocationUpdates(bestProvider, 2000, 0, this);
            }
        }

    }

    private void setAddress(double lat, double lng) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            utilizeLocation(lat, lng, geocoder);
        } catch (NullPointerException err) {
            //gives null value when app is run for the first time
        }
    }

    private void utilizeLocation(double lat, double lng, Geocoder geocoder) {
        StringBuilder result = new StringBuilder();
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);

        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        } catch (NullPointerException err) {
            Log.e("tag", err.getMessage());
        }

        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            if (address.getSubLocality() != null) {
                result.append(address.getSubLocality()).append(", ");//sanepa
            }
            result.append(address.getLocality()).append(", ");//lalitpur
            result.append(address.getCountryName());//nepal
        }
        String address = result.toString();

        double distanceTo = calculateDistance(toLat, toLong, lat, lng);
        double distanceFrom = calculateDistance(fromLat, fromLong, lat, lng);


        LatLng latLng = new LatLng(lat, lng);
        MarkerOptions marker = new MarkerOptions();
        marker.position(latLng);
        marker.title(address);
        map.addMarker(marker);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Distance from current position");
        alertDialog.setMessage("From: " + new DecimalFormat("##.##").format(distanceFrom) + "m, To: " + new DecimalFormat("##.##").format(distanceTo) + "m");
        alertDialog.show();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {


    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        if (location != null) {
            setAddress(latitude, longitude);
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }


    // Haversine formula
    public double calculateDistance(double Lat1, double Lng1,
                                    double Lat2, double Lng2) {

        double latDistance = Math.toRadians(Lat1 - Lat2);
        double lngDistance = Math.toRadians(Lng1 - Lng2);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(Lat1)) * Math.cos(Math.toRadians(Lat2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double d = AVERAGE_RADIUS_OF_EARTH * c * 1000; //returns in meter

        return d;
    }

    class GetRoute extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            String response = "";
            document = gMapV2GetRouteDirection.getDocument(fromPosition, toPosition, GMapV2GetRouteDirection.MODE_DRIVING);
            response = "Success";

            final String finalResponse = response;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (finalResponse.equalsIgnoreCase("Success")) {
                        ArrayList<LatLng> directionPoint = gMapV2GetRouteDirection.getDirection(document);
                        PolylineOptions rectLine = new PolylineOptions().width(3).color(Color.BLUE);
                        String toAddress = getAddress(toLat, toLong);
                        String fromAddress = getAddress(fromLat, fromLong);

                        for (int i = 0; i < directionPoint.size(); i++) {
                            rectLine.add(directionPoint.get(i));
                        }
                        // Adding route and marker on the map
                        map.addPolyline(rectLine);
                        fromMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        fromMarker.position(fromPosition);
                        fromMarker.draggable(false);
                        fromMarker.title(fromAddress);
                        map.addMarker(fromMarker);
                        toMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        toMarker.position(toPosition);
                        toMarker.draggable(false);
                        toMarker.title(toAddress);
                        map.addMarker(toMarker);

                    }
                }
            });
            return null;
        }
    }
}
