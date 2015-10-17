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

        /*ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this line.
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);*/

        //For Push

        /*ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {

                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });*/


        //Enable to receive push
        //PushService.setDefaultPushCallback(this, NavigationDrawerActivity.class);
        //PushService.setDefaultPushCallback(this, MainActivity.class,R.drawable.favicon);
        //ParseInstallation.getCurrentInstallation().saveInBackground();
        //PushService.subscribe(this,"",NavigationDrawerActivity.class);
    }
}
