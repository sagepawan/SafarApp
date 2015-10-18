package com.trekcoders.safar;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Sagepawan on 10/4/2015.
 */
public class SafarApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        // Initialize Crash Reporting.
        //ParseCrashReporting.enable(this);
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(this, "lm6f7z8HJdtKEWCEmp1Zfy4azSXIeDi2NoTt9A7S", "3bfZ0eWRVknPmBpqmSjtczydosj7z13dcFP6CpqA");

    }
}
