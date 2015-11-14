package com.trekcoders.safar.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.trekcoders.safar.R;
import com.trekcoders.safar.Adapter.FrenListAdapter;
import com.trekcoders.safar.model.Users;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akrmhrjn on 10/24/15.
 */
public class SearchFriendActivity extends AppCompatActivity {

    Toolbar mToolbar;
    ImageView searchBtn;
    ParseUser user;
    List<ParseUser> ob;
    ParseQuery<ParseUser> query;
    EditText search;

    ArrayList<Users> usersArrayList;
    ListView frenlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);

        initToolbar();
        search = (EditText) findViewById(R.id.etEmail);
        frenlist = (ListView) findViewById(R.id.frenList);
        usersArrayList = new ArrayList<>();

        searchBtn = (ImageView) findViewById(R.id.btn_search_friend);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                query = ParseUser.getQuery().whereMatches("email", search.getText().toString());

                try {
                    ob = query.find();
                    Log.d("ObSize", ": " + ob.size());

                    for (int i = 0; i < ob.size(); i++) {
                        Users user = new Users();
                        user.objectId = ob.get(i).getObjectId();
                        user.username = ob.get(i).getUsername();
                        user.email = ob.get(i).getEmail();
                        user.mobilenumber = String.valueOf(ob.get(i).getInt("mobilenumber"));
                        usersArrayList.add(user);

                        //Log.d("usernames", ": " + i + " - " + ob.get(i).getUsername());
                    }

                    System.out.println("asdfg"+usersArrayList.size());
                    FrenListAdapter adapter = new FrenListAdapter(SearchFriendActivity.this, usersArrayList);
                    frenlist.setAdapter(adapter);


                } catch (com.parse.ParseException e) {
                    e.printStackTrace();
                    Log.e("errormsg", ": " + e.getMessage());
                }

            }
        });
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

    void initToolbar(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar_main);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbar.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_logo);
    }


}
