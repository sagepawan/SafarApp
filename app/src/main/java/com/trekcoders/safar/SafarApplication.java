package com.trekcoders.safar;

import android.app.Application;

import com.google.android.gms.maps.model.LatLng;
import com.parse.Parse;

import java.util.ArrayList;

/**
 * Created by Sagepawan on 10/4/2015.
 */
public class SafarApplication extends Application {

    public static SafarApplication app;

    public ArrayList<LatLng> trailPath;

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;

        //27.677223, 85.342937

        //TO ADD CHECKPOINT COORDINATES IN BETWEEN STARTING AND END CHECKPOINTS - - DONE BY KALYAN
        trailPath = new ArrayList<>();
        trailPath.add(new LatLng(27.677121, 85.342741));
        trailPath.add(new LatLng(27.676615, 85.342661));
        trailPath.add(new LatLng(27.675855, 85.342484));
        trailPath.add(new LatLng(27.675292, 85.342374));
        /*trailPath.add(new LatLng(30.086528, -95.991661));
        trailPath.add(new LatLng(30.086788, -95.991596));
        trailPath.add(new LatLng(30.087048, -95.991510));
        trailPath.add(new LatLng(30.087299, -95.991446));
        trailPath.add(new LatLng(30.087327, -95.991221));*/

        // Initialize Crash Reporting.
        //ParseCrashReporting.enable(this);
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(this, "lm6f7z8HJdtKEWCEmp1Zfy4azSXIeDi2NoTt9A7S", "3bfZ0eWRVknPmBpqmSjtczydosj7z13dcFP6CpqA");

    }
}
