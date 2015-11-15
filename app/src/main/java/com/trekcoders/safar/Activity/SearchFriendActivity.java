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

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.trekcoders.safar.Adapter.UserFriendAdapter;
import com.trekcoders.safar.R;
import com.trekcoders.safar.Adapter.FrenListAdapter;
import com.trekcoders.safar.model.Friends;
import com.trekcoders.safar.model.Users;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akrmhrjn on 10/24/15.
 */
public class SearchFriendActivity extends AppCompatActivity {

    Toolbar mToolbar;
    ImageView searchBtn;
    ParseUser pUser;
    List<ParseUser> ob;
    ParseQuery<ParseUser> query;
    EditText search;

    ArrayList<Users> usersArrayList;
    ListView frenlist;
    ArrayList<Friends> friendsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);

        initToolbar();

        pUser = ParseUser.getCurrentUser();
        search = (EditText) findViewById(R.id.etEmail);
        frenlist = (ListView) findViewById(R.id.frenList);


        searchBtn = (ImageView) findViewById(R.id.btn_search_friend);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friendsArrayList = new ArrayList<>();
                usersArrayList = new ArrayList<>();
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

                            if (pUser.getObjectId().equals(user_F.getObjectId())) {
                                Friends friends = new Friends();
                                friends.emailF = fren_F.getString("email");
                                friends.mobilenumberF = String.valueOf(fren_F.getInt("mobilenumber"));
                                friends.objectIdF = Obj.getObjectId();

                                Log.d("CurrentUserNameFriends", ": " + fren_F.getString("email"));
                                friendsArrayList.add(friends);

                            } else if (pUser.getObjectId().equals(fren_F.getObjectId())) {
                                Friends friends = new Friends();
                                friends.emailF = user_F.getString("email");
                                friends.mobilenumberF = String.valueOf(user_F.getInt("mobilenumber"));
                                friends.objectIdF = Obj.getObjectId();
                                friendsArrayList.add(friends);
                            }
                        }

                        query = ParseUser.getQuery().whereMatches("email", search.getText().toString());

                        try {
                            ob = query.find();
                            Log.d("ObSize", ": " + ob.size());

                            for (int i = 0; i < ob.size(); i++) {

                                if (!ob.get(i).getObjectId().equals(pUser.getObjectId())) {
                                    for(Friends fren : friendsArrayList){
                                        if(!fren.emailF.equalsIgnoreCase(ob.get(i).getUsername())){
                                            Users user = new Users();
                                            user.objectId = ob.get(i).getObjectId();
                                            user.username = ob.get(i).getUsername();
                                            user.email = ob.get(i).getEmail();
                                            user.mobilenumber = String.valueOf(ob.get(i).getInt("mobilenumber"));
                                            usersArrayList.add(user);
                                        } else{
                                            break;
                                        }
                                    }


                                    //Log.d("usernames", ": " + i + " - " + ob.get(i).getUsername());
                                }
                            }

                            FrenListAdapter adapter = new FrenListAdapter(SearchFriendActivity.this, usersArrayList);
                            frenlist.setAdapter(adapter);


                        } catch (com.parse.ParseException err) {
                            err.printStackTrace();
                            Log.e("errormsg", ": " + err.getMessage());
                        }

                    }
                });

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
