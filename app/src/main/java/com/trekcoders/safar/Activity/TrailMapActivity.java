package com.trekcoders.safar.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.trekcoders.safar.Location.LocationService;
import com.trekcoders.safar.R;
import com.trekcoders.safar.SafarApplication;
import com.trekcoders.safar.utils.GMapV2GetRouteDirection;

import org.w3c.dom.Document;

import java.io.IOException;
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

    int value;

    GMapV2GetRouteDirection gMapV2GetRouteDirection;

    public final static double AVERAGE_RADIUS_OF_EARTH = 6371;

    String frenObjId, trailObjId, from;

    Button startTrace, stopTrace;
    LinearLayout bottomLinear;
    Intent serviceIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trail_map);

        initToolbar();
        startTrace = (Button) findViewById(R.id.btnTracingStart);
        stopTrace = (Button) findViewById(R.id.btnTracingEnd);
        bottomLinear = (LinearLayout) findViewById(R.id.bottomLinear);
        serviceIntent = new Intent(TrailMapActivity.this, LocationService.class);


        //To check whether the user is accessing map view from Notifications or from Homepage - - DONE BY KALYAN
        if (getIntent() != null) {
            frenObjId = getIntent().getStringExtra("frenObjId");
            trailObjId = getIntent().getStringExtra("trailObjId");
            from = getIntent().getStringExtra("from");
        }

        System.out.println("fromValue" + from);

        /*frenObjId = "TEklhW9wpH";
        trailObjId = "FkdyO1nXFz";*/

        //To check whether Play Services are available or not
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }

        value = SafarApplication.app.trailPath.size();
        //Define start and end coordinates here
        //27.677121, 85.342741
        for(int i=0; i<value;i++) {
            fromLat = SafarApplication.app.trailPath.get(0).latitude;
            fromLong = SafarApplication.app.trailPath.get(0).longitude;
            toLat = SafarApplication.app.trailPath.get(value-1).latitude;
            toLong = SafarApplication.app.trailPath.get(value-1).longitude;
            Log.d("fromAalo",": "+fromLat+" ToAalo: "+toLong);
        }
        /*fromLat = 27.68973779;
        fromLong = 85.30766174;*/
        /*toLat = 27.6906498;
        toLong = 85.30543014;*/

        //27.676448, 85.342686
        /*fromLat = 27.677178;
        fromLong = 85.342872;
        toLat = 27.676448;
        toLong = 85.342686;*/
        /*fromLat = 27.68973779;
        fromLong = 85.30766174;
        toLat = 27.6906498;
        toLong = 85.30543014;*/



        fromPosition = new LatLng(fromLat, fromLong);   //start checkpoint position
        toPosition = new LatLng(toLat, toLong);         //end checkpoint position

        //Create marker to show up on map (for starting and ending checkpoint)
        toMarker = new MarkerOptions();
        fromMarker = new MarkerOptions();
        marker = new MarkerOptions();
        mapFragment = (MapFragment) this.getFragmentManager().findFragmentById(R.id.map);   //where map is shown in xml
        gMapV2GetRouteDirection = new GMapV2GetRouteDirection();                            //to get directions

        map = mapFragment.getMap();

        //Set entire map view for starting checkpoint
        LatLng start = new LatLng(fromLat, fromLong);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(start, 15));
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(true);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(true);

        if (from.equalsIgnoreCase("trace")) {
            bottomLinear.setVisibility(View.VISIBLE);
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            //Checks whether User GPS is on or not, asks them to turn it on if not
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(this, "GPS is Enabled in your device", Toast.LENGTH_SHORT).show();
                getCurrentLocation();
            } else {
                //Alert msg shown when gps is not enabled
                showGPSDisabledAlertToUser();
            }
        } else{
            bottomLinear.setVisibility(View.GONE);
        }



        //When start tracing button is pressed in Map View - - DONE BY PANKAJ
        startTrace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTrace.setBackgroundColor(getResources().getColor(R.color.login_btn_bg));  //sets background color of tracing button to green
                TrailMapActivity.this.startService(serviceIntent);                             //calls the service intent so app can run in background
            }
        });

        //When end tracing button is pressed in Map View
        stopTrace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTrace.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));  //changes truace start button color back to blue
                stopTrace.setBackgroundColor(getResources().getColor(R.color.login_btn_bg));   //changes trace end button color to green
                TrailMapActivity.this.stopService(serviceIntent);
            }
        });


        //Calling Parse Table "Traces" from parse to load user coordinates into parse
        ParseQuery parseQuery = ParseQuery.getQuery("Traces");
        parseQuery.include("usrObjId");
        parseQuery.include("trailObjId");
        parseQuery.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> list, ParseException e) {

                ArrayList<LatLng> points = new ArrayList<LatLng>();
                for (ParseObject Obj : list) {
                    ParseObject user = Obj.getParseObject("usrObjId");
                    ParseObject trail = Obj.getParseObject("trailObjId");

                    if (user.getObjectId().equals(frenObjId) && trail.getObjectId().equals("FkdyO1nXFz")) {
                        LatLng latLng = new LatLng(Double.valueOf(Obj.getString("Latitude")), Double.valueOf(Obj.getString("Longitude")));
                        points.add(latLng);  //loads user friend's updates GPS coordinates from traces table into points ArrayList
                    }
                }


                //Set entire map view for ending checkpoint - - Kalyan
                String toAddress = getAddress(toLat, toLong);
                String fromAddress = getAddress(fromLat, fromLong);
                fromMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));  //gives blue color to marker
                fromMarker.position(fromPosition);
                fromMarker.draggable(false);    //cannot drag marker
                fromMarker.title(fromAddress);
                map.addMarker(fromMarker);
                toMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                toMarker.position(toPosition);
                toMarker.draggable(false);
                toMarker.title(toAddress);
                map.addMarker(toMarker);

                LatLng coordinate = null;

                //gets user friends updates GPS coordinates from parse table "traces" into map markers
                for (int i = 0; i < points.size(); ++i) {
                    coordinate = points.get(i);
                    MarkerOptions marker = new MarkerOptions();
                    marker.position(coordinate);
                    map.addMarker(marker);
                }

                ArrayList<LatLng> path = new ArrayList<LatLng>();
                LatLng from = new LatLng(Double.valueOf(fromLat), Double.valueOf(fromLong));
                path.add(from);
                for (LatLng ll : points) {
                    path.add(ll);
                }
                LatLng to = new LatLng(Double.valueOf(toLat), Double.valueOf(toLong));
                path.add(to);

                //PolyLineOptions envelopes the given set of coordinates and gives a uniform path that connects them all together
                PolylineOptions polyLineOptions = new PolylineOptions();
                polyLineOptions.addAll(path);
                polyLineOptions.width(2);
                polyLineOptions.color(Color.GREEN); //connecting path gets green color
                map.addPolyline(polyLineOptions);   //path is added to the map view
            }
        });
    }

    //Dialog box that appears when GPS is disabled
    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                //Intent that shows interface to start GPS in user's device
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
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

    //get Address from longitude and latitude - - get the name of coordinate using geoCoder
    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            // get the marker coordinates for addressList
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {  //check if addressList size is greater than zero
                Address address = addresses.get(0);
                if (address.getSubLocality() != null)
                    result.append(address.getSubLocality()).append(", "); //Gets Sublocality
                result.append(address.getLocality()).append(", "); //gets Locality
                result.append(address.getCountryName()); //Gets country name

                //since , is appended - the address is accessed as Sublocality, Locality, CountryName on touching the marker
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

    //Method to get current Location of user
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
                locationManager.requestLocationUpdates(bestProvider, 3000, 0, this);   //new location is updated after every 3000 milliseconds
            }
        }
    }

    //MEthod used to set address once coordinates accessed
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
                result.append(address.getSubLocality()).append(", ");
            }
            result.append(address.getLocality()).append(", ");
            result.append(address.getCountryName());
        }
        String address = result.toString();

        //double distanceTo = calculateDistance(toLat, toLong, lat, lng);
        double distanceFrom = calculateDistance(fromLat, fromLong, lat, lng);


        LatLng latLng = new LatLng(lat, lng);
        MarkerOptions marker = new MarkerOptions();
        marker.position(latLng);
        marker.title(address);
        map.addMarker(marker);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));


        System.out.println("distance from:" + distanceFrom);

        //Method to check if user is within a certain proximity of starting checkpoint to start tracing
        if (from.equalsIgnoreCase("trace")) {
            if (distanceFrom > 1000) {   //minimum tracing distance set at 1000 metres
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Message");
                alertDialog.setMessage("You are very far from tracking distance.");
                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alertDialog.show();
            } else {
            }
        } else if (from.equalsIgnoreCase("notification")) {

        }
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


    // Haversine formula used to calculate distance between two google coordinates
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

    /*class GetRoute extends AsyncTask<Void, Void, Void> {
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
    }*/

}
