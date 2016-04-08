package com.trekcoders.safar.Location;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.trekcoders.safar.SafarApplication;
import com.trekcoders.safar.model.Friends;

import java.util.ArrayList;
import java.util.List;


public class LocationService extends Service implements IGpsHelper {

    ParseUser parseUser;

    private Thread traceThread;
    private long lastTraceTime = 0;


    double lastLat = 0.0;
    double lastLng = 0.0;

    double sentLat = 0.0;
    double sentLng = 0.0;

    GPS gps;

    ArrayList<Friends> friendsArrayList;

    public final static double AVERAGE_RADIUS_OF_EARTH = 6371;

    Boolean trigger = true;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    //Method to start Location service - - DONE BY PANKAJ
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("Service Started");
        friendsArrayList = new ArrayList<>();   //creates new friendListArray
        parseUser = ParseUser.getCurrentUser();  //gets cuurent logged in user
        callFriendList();   // Method to call the current user's friend list
        startLocationService();   // making the application work in background

        if (trigger) {
            //trigger used so that this condition only runs once
            uploadTrailInfoToUserMeta();
            trigger = false;
        }

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        gps = new GPS(LocationService.this);   //start GPS Services to continously access user location coordinates
        //prefs = new Prefs(getBaseContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Service Stopped");
        gps.stopGPS();      // Stop GPS

    }

    private void startLocationService() {
        final LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locManager == null) {
            Toast.makeText(this, "No location service found!",
                    Toast.LENGTH_LONG).show();
            return;
        }
    }

    @Override
    //Method to compare the distance between new GPS coordinate and nearest checkpoint with each new change in GPS coordinate
    public void locationChanged(double longitude, double latitude) {
        Log.d("Service location", "lat: " + latitude);

        //if (prefs == null) return;

        double lat = latitude;
        double lng = longitude;

        double distance = calculateDistance(lastLat, lastLng, lat, lng);
        System.out.println("Service Distance: " + distance);

        for (LatLng ll : SafarApplication.app.trailPath) {   //get user gps coordinate updates from parse traces table
            double cpDistance = calculateDistance(ll.latitude, ll.longitude, lat, lng);   //measure distance between GPS Coordinates and checkpoints
            if (cpDistance < 10) {    //if distance is less then 3 meters
                if (sentLat != ll.latitude || sentLng != ll.longitude) {
                    System.out.println("Near Checkpoint New New");
                    if (friendsArrayList != null) {
                        for (Friends friends : friendsArrayList) {   //get user's friend list
                            ParseQuery pushQuery = ParseInstallation.getQuery();  //start Push Query
                            pushQuery.whereEqualTo("user_objectId", friends.objectIdF);  //target query to user's friends
                            ParseUser parseUser = ParseUser.getCurrentUser();

                            String msg = "You friend " + parseUser.getUsername() + " reached to the checkpoint.";  //set push message

                            ParsePush push = new ParsePush();
                            push.setQuery(pushQuery); // Set our Installation query
                            push.setMessage(msg);
                            push.sendInBackground();    //send push notification of location to user's friends

                            //to update notification info into parse table "Notifications"
                            ParseObject parseObject = new ParseObject("Notifications");
                            parseObject.put("userObjId", friends.objectIdF);
                            parseObject.put("friendObjId", parseUser.getObjectId());
                            parseObject.put("trailObjId", "FkdyO1nXFz");
                            parseObject.put("message", msg);
                            parseObject.saveInBackground();
                        }
                    }

                }
                System.out.println("Near Checkpoint");
                sentLat = ll.latitude;
                sentLng = ll.longitude;
            }
        }

        //sends location if distance between previous and current location is greater than 10m
        if (distance > 10) {
            System.out.println("Service Location traced. Distance: " + distance);
            Location location = new Location("location");
            location.setLatitude(latitude);
            location.setLongitude(longitude);
            uploadTrace(lat, lng);     //uploads traced location coordinates to "traces" table in parse

            lastLat = lat;
            lastLng = lng;
        }
    }

    private void uploadTrailInfoToUserMeta() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserMeta");
        query.whereEqualTo("usrObjId", parseUser.getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                Boolean does_row_exist = false;
                if (e == null) {
                    for (ParseObject parseObject : objects) {
                        String trail;
                        trail = parseObject.get("trailObjId").toString();
                        if (trail.equals("FkdyO1nXFz")) {
                            does_row_exist = true;
                        }
                    }

                    if (!does_row_exist) {
                        ParseObject parseObject = new ParseObject("UserMeta");
                        parseObject.put("usrObjId", ParseObject.createWithoutData("_User", parseUser.getObjectId()));
                        parseObject.put("trailObjId", ParseObject.createWithoutData("Trails", "FkdyO1nXFz"));
                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Log.d("Upload to user meta", "Success!!");
                            }
                        });
                    }
                } else {
                    Log.d("Upload to user meta", "Failed!!");
                }
            }
        });
    }

    @Override
    public void displayGPSSettingsDialog() {

    }

    private void uploadTrace(final double lat, final double lng) {
        Log.d("Service Trace", "uploadTrace start...");

        if (traceThread != null) {
            Log.d("Trace", "Old traceThread running.");
            return;
        }
        // 10s, for each update.
        long des = System.currentTimeMillis() - lastTraceTime;
        if (des < 3000) {                   //sets timer of 3000 millisecond of each trace update
            Log.d("Trace", "Time < 3000");
            return;
        }
        lastTraceTime = System.currentTimeMillis();

        traceThread = new Thread() {
            public void run() {
                // Thread to upload user's latest gps coordinates into parse table "Traces" in every three seconds
                ParseObject parseObject = new ParseObject("Traces");
                parseObject.put("usrObjId", ParseObject.createWithoutData("_User", parseUser.getObjectId()));
                parseObject.put("trailObjId", ParseObject.createWithoutData("Trails", "FkdyO1nXFz"));
                parseObject.put("Latitude", String.valueOf(lat));       //user's latest location latitude
                parseObject.put("Longitude", String.valueOf(lng));      //user's latest location longitude
                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        //e.printStackTrace();
                        Log.d("Service upload trace", "Success!!");
                    }
                });
                traceThread = null;
            }
        };
        traceThread.start();    //start the tracing thread
    }


    // Haversine formula to calulate distance - - DONE BY KALYAN
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


    public void callFriendList() {

        ParseQuery parseQuery = ParseQuery.getQuery("Friends");
        parseQuery.include("userObjId");
        parseQuery.include("frenObjId");

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                for (ParseObject Obj : list) {
                    ParseObject user_F = Obj.getParseObject("userObjId");
                    ParseObject fren_F = Obj.getParseObject("frenObjId");

                    Log.d("user_fObjID", ": " + user_F.getObjectId());
                    Log.d("fren_fObjID", ": " + fren_F.getObjectId());

                    if (parseUser.getObjectId().equals(user_F.getObjectId())) {
                        Friends friends = new Friends();
                        friends.emailF = fren_F.getString("email");
                        friends.mobilenumberF = String.valueOf(fren_F.getInt("mobilenumber"));
                        friends.objectIdF = fren_F.getObjectId();

                        Log.d("CurrentUserNameFriends", ": " + fren_F.getString("email"));
                        friendsArrayList.add(friends);

                    } else if (parseUser.getObjectId().equals(fren_F.getObjectId())) {
                        Friends friends = new Friends();
                        friends.emailF = user_F.getString("email");
                        friends.mobilenumberF = String.valueOf(user_F.getInt("mobilenumber"));
                        friends.objectIdF = user_F.getObjectId();
                        friendsArrayList.add(friends);
                    }
                }
                Log.d("FrensSize", ": " + friendsArrayList.size());
            }
        });

    }
}
