package com.trekcoders.safar.Fragments;


import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.andexert.expandablelayout.library.ExpandableLayoutListView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.trekcoders.safar.Activity.SearchFriendActivity;
import com.trekcoders.safar.R;
import com.trekcoders.safar.Adapter.UserFriendAdapter;
import com.trekcoders.safar.model.Friends;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SafarFriendsFragment extends Fragment {

    ArrayList<Friends> friendsArrayList;

    String emails[] = {"pankaj@gmail.com", "sam@yahoo.com", "bilbo@hotmail.com"};
    String mobile[] = {"99898888", "99892781", "89899111"};
    ExpandableLayoutListView listView;

    Button addFriends;

    ParseUser user = ParseUser.getCurrentUser();
    ParseQuery<ParseObject> frenTag = ParseQuery
            .getQuery("Friends");


    public SafarFriendsFragment() {
        // Required empty public constructor
    }

    String trails = "";
    UserFriendAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_safar_friends, container, false);
        listView = (ExpandableLayoutListView) rootView.findViewById(R.id.listUserFriends);
        addFriends = (Button) rootView.findViewById(R.id.addSafarFriends);

        friendsArrayList = new ArrayList<>();
        friendsArrayList.clear();

        addFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), SearchFriendActivity.class));
            }
        });

        String user_id = user.getObjectId();
        frenTag.whereEqualTo("userObjectId", user_id);

        frenTag.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                if (e == null) {

                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {

                            Log.d("curretnUserfrnlist", ": " + list.get(i));
                        }
                    }
                } else {
                    Log.e("frenlisterror", ":" + e.getMessage());
                }
            }
        });

        //send the entire friendArrayList to Adapter
        adapter = new UserFriendAdapter(getActivity(), friendsArrayList, SafarFriendsFragment.this);
        listView.setAdapter(adapter);

        callFriendList();
        return rootView;
    }

//    private String callTrailsList(String friendObjId) {
//
////        final ParseQuery parseQueryUserMeta = ParseQuery.getQuery("UserMeta");
////
////        for (int i = 0; i < friendsArrayList.size(); i++) {
////            final String user_id = friendsArrayList.get(i).objectIdF;
////            parseQueryUserMeta.include("trailObjId");
////
//////            ParseObject obj = ParseObject.createWithoutData("User", user_id);
//////
//////            parseQueryUserMeta.whereEqualTo("usrObjId", obj);
////            parseQueryUserMeta.whereContains("usrObjId", user_id);
////
////            parseQueryUserMeta.findInBackground(new FindCallback<ParseObject>() {
////                @Override
////                public void done(List<ParseObject> list_, ParseException e) {
////                    String trials = null;
////                    if (e != null) {
////                        e.printStackTrace();
////                    } else {
////                        for (ParseObject Obj : list_) {
////                            ParseObject trail_F = Obj.getParseObject("trailObjId");
////                            trials += trail_F.getString("trailName") + ", ";
////                            Log.d("trails", ": " + trials);
////                        }
////                    }
////                }
////            });
////        }
//
////        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserMeta");
////        query.include("usrObjId");
////        query.include("trailObjId");
////
////        final String[] value = {null};
////
////        query.findInBackground(new FindCallback<ParseObject>() {
////            public void done(List<ParseObject> list_, ParseException e) {
////                if (e == null) {
////                    String trails = "";
////
////                    for (ParseObject Obj : list_) {
////                        ParseObject trail_F = Obj.getParseObject("trailObjId");
////                        ParseObject user__F = Obj.getParseObject("usrObjId");    //pKOERJheaB -- Sujit
////
////                        if (friendObjId.equals(user__F.getObjectId())) {
////                            trails += trail_F.getString("trailName") + ", ";
////                            value[0] = trails;
////                            Log.d("traversed", ": " + friendObjId + " -- " + user__F.getObjectId() + value[0]);
////                        }
////
////                        Log.d("NoTtraversed", ": " + friendObjId + " -- " + user__F.getObjectId() + value[0]);
////
////
////                    }
////
////                    Log.d("trailsOut", ": " + value[0]);
////
////
////                } else {
////                    Log.d("score", "Error: " + e.getMessage());
////                }
////
////                Log.d("trailsOut_", ": " + value[0]);
////            }
////        });
//        return "";
//    }

    //CALLING CURRENT USER'S FRIEND LIST INTO UI FROM PARSE - - DONE BY KALYAN

    public void callFriendList() {

        //Start a query on parse "Friends" table where all friend relation between users are stored
        final ParseQuery parseQuery = ParseQuery.getQuery("Friends");
        parseQuery.include("userObjId");    //access userObjId column from parse table "friends"
        parseQuery.include("frenObjId");    //access frenObjId column from parse table "friends"

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> list, ParseException e) {

                for (ParseObject Obj : list) {
                    final ParseObject user_F = Obj.getParseObject("userObjId");  //get all userObjId values from userObjId Column
                    final ParseObject fren_F = Obj.getParseObject("frenObjId");  //get all frenObjId values from frenObjId Column

                    Log.d("user_fObjID", ": " + user_F.getObjectId());
                    Log.d("fren_fObjID", ": " + fren_F.getObjectId());

                    final Handler handler = new Handler();

                    if (user.getObjectId().equals(user_F.getObjectId())) {  //check if current user's id matches any userObjIds from friends table
                        final Friends friends = new Friends();
                        friends.emailF = fren_F.getString("email");
                        friends.mobilenumberF = String.valueOf(fren_F.getInt("mobilenumber"));
                        friends.objectIdF = Obj.getObjectId();

                        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserMeta");
                        query.whereEqualTo("usrObjId", fren_F);
                        query.include("trailObjId");
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(final List<ParseObject> objects, com.parse.ParseException e) {
                                if (e == null) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            for (ParseObject parseObject : objects) {
                                                final ParseObject trail_F = parseObject.getParseObject("trailObjId");

                                                String trail;
                                                trail = trail_F.getString("trailName");
                                                trails += trail + ", ";
                                            }
                                            friends.trials = removeLastCharacter(trails);
                                            friendsArrayList.add(friends);  //add all the friend's credentials into friendsArrayList

                                            Log.d("userKoEmailList", ": " + friendsArrayList.size());

                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                                } else {
                                    e.printStackTrace();
                                    Log.d("Upload to user meta", "Failed!!");
                                }
                            }
                        });
                    }

                    //To check for users who has current user in their friend list
                    else if (user.getObjectId().equals(fren_F.getObjectId())) {
                        final Friends friends = new Friends();
                        friends.emailF = user_F.getString("email");
                        friends.mobilenumberF = user_F.getString("mobilenumber");
                        friends.objectIdF = Obj.getObjectId();

                        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserMeta");
                        query.whereEqualTo("usrObjId", user_F);
                        query.include("trailObjId");
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(final List<ParseObject> objects, com.parse.ParseException e) {
                                if (e == null) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            for (ParseObject parseObject : objects) {
                                                final ParseObject trail_F = parseObject.getParseObject("trailObjId");

                                                String trail;
                                                trail = trail_F.getString("trailName");
                                                trails += trail + ", ";
                                            }
                                            friends.trials = removeLastCharacter(trails);
                                            friendsArrayList.add(friends);  //add all the friend's credentials into friendsArrayList

                                            Log.d("userKoEmailList", ": " + friendsArrayList.size());

                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                                } else {
                                    e.printStackTrace();
                                    Log.d("Upload to user meta", "Failed!!");
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    //to refresh friend array list after deleting a friend
    public void refresh(int position) {

        friendsArrayList.remove(position);
        UserFriendAdapter adapter = new UserFriendAdapter(getActivity(), friendsArrayList, SafarFriendsFragment.this);
        listView.setAdapter(adapter);
    }

    public String removeLastCharacter(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 2);
        }
        return str;
    }
}
