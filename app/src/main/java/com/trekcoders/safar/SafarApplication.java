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
        trailPath = new ArrayList<>();
        trailPath.add(new LatLng(27.68973779, 85.30766174));
        trailPath.add(new LatLng(27.690103, 85.307084));
        trailPath.add(new LatLng(27.690412, 85.305979));
        trailPath.add(new LatLng(27.6906498, 85.30543014));

        // Initialize Crash Reporting.
        //ParseCrashReporting.enable(this);
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(this, "lm6f7z8HJdtKEWCEmp1Zfy4azSXIeDi2NoTt9A7S", "3bfZ0eWRVknPmBpqmSjtczydosj7z13dcFP6CpqA");

    }
}
