package com.trekcoders.safar.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.trekcoders.safar.Fragments.FragmentDrawer;
import com.trekcoders.safar.Fragments.HomeFragment;
import com.trekcoders.safar.Fragments.LogoutFragment;
import com.trekcoders.safar.Fragments.NotificationFragment;
import com.trekcoders.safar.Fragments.SettingsFragment;
import com.trekcoders.safar.R;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mToolbar = (Toolbar) findViewById(R.id.toolbar_main);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbar.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_logo);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch
        displayView(0);

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
                fragment = new SettingsFragment();
                title = getString(R.string.title_settings);
                break;

            case 3:
                fragment = new LogoutFragment();
                title = getString(R.string.title_logout);
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
