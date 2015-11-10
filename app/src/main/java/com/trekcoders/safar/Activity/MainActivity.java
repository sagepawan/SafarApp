package com.trekcoders.safar.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.trekcoders.safar.Fragments.FragmentDrawer;
import com.trekcoders.safar.Fragments.HomeFragment;
import com.trekcoders.safar.Fragments.LogoutFragment;
import com.trekcoders.safar.Fragments.NotificationFragment;
import com.trekcoders.safar.Fragments.SafarFriendsFragment;
import com.trekcoders.safar.Fragments.SettingsFragment;
import com.trekcoders.safar.R;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    TextView name;

    ParseUser parseUser;
    ParseInstallation installation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = (TextView) findViewById(R.id.tvName);


        mToolbar = (Toolbar) findViewById(R.id.toolbar_main);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbar.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_logo);

        parseUser = ParseUser.getCurrentUser();
        if (parseUser != null && parseUser.getSessionToken() != null) {
            installation = ParseInstallation.getCurrentInstallation();
            installation.put("user_objectId", parseUser.getObjectId());
            installation.saveInBackground();
            getUserDetailsFromParse();
        }else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch
        displayView(0);




    }

    private void getUserDetailsFromParse() {
        name.setText(parseUser.getUsername());
    }


    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = "";
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = "";
                break;
            case 1:
                fragment = new NotificationFragment();
                title = getString(R.string.title_notification);
                break;

            case 2:
                fragment = new SafarFriendsFragment();
                title = getString(R.string.title_friends);
                break;


            case 3:
                fragment = new SettingsFragment();
                title = getString(R.string.title_settings);
                break;

            case 4:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                // set title
                alertDialogBuilder.setTitle("LOG OUT");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Are you sure you want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                parseUser.logOut();
                                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                            }


                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }
}
