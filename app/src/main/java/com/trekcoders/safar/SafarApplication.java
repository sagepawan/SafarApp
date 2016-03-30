package com.trekcoders.safar;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.trekcoders.safar.utils.CheckNetwork;
import com.trekcoders.safar.utils.Pref;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sagepawan on 10/4/2015.
 */
public class SafarApplication extends Application {

    public static SafarApplication app;

    public ArrayList<LatLng> trailPath;

    ArrayList<String> latArray;
    ArrayList<String> longArray;

    public Pref pref;

    public CheckNetwork checkNetwork;

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;

        pref = new Pref(this);

        checkNetwork = new CheckNetwork();

        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(this, "lm6f7z8HJdtKEWCEmp1Zfy4azSXIeDi2NoTt9A7S", "3bfZ0eWRVknPmBpqmSjtczydosj7z13dcFP6CpqA");

        //For Facebook Login
        ParseFacebookUtils.initialize(this);

        latArray = new ArrayList<>();
        longArray = new ArrayList<>();
        trailPath = new ArrayList<>();

        ParseQuery<ParseObject> checkQuery = ParseQuery.getQuery("Checkpoints");
        checkQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                //Log.d("ListSizeCheckpoints", ": " + list.size());
                Double lat = null;
                Double lng = null;
                for (ParseObject Obj : list) {
                    //latArray.add(Obj.getString("latitude"));
                    //longArray.add(Obj.getString("longitude"));
                    lat = Double.parseDouble(Obj.getString("latitude"));
                    lng = Double.parseDouble(Obj.getString("longitude"));
                    trailPath.add(new LatLng(lat, lng));
                }

                for (int i = 0; i < trailPath.size(); i++) {
                    Log.d("latArraySize",": "+trailPath.get(i));

                }
            }
        });


        //27.677223, 85.342937

        //TO ADD CHECKPOINT COORDINATES IN BETWEEN STARTING AND END CHECKPOINTS - - DONE BY KALYAN


       /* trailPath.add(new LatLng(27.677121, 85.342741));
        trailPath.add(new LatLng(27.676615, 85.342661));
        trailPath.add(new LatLng(27.675855, 85.342484));
        trailPath.add(new LatLng(27.675292, 85.342374));*/
        /*trailPath.add(new LatLng(30.086528, -95.991661));
        trailPath.add(new LatLng(30.086788, -95.991596));
        trailPath.add(new LatLng(30.087048, -95.991510));
        trailPath.add(new LatLng(30.087299, -95.991446));
        trailPath.add(new LatLng(30.087327, -95.991221));*/

        // Initialize Crash Reporting.
        //ParseCrashReporting.enable(this);

    }
}
