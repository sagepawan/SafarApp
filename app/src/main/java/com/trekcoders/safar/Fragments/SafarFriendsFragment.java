package com.trekcoders.safar.Fragments;


import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.trekcoders.safar.Activity.SearchFriendActivity;
import com.trekcoders.safar.R;
import com.trekcoders.safar.adapter.UserFriendAdapter;
import com.trekcoders.safar.model.Friends;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SafarFriendsFragment extends Fragment {

    ArrayList<Friends> friendsArrayList;

    String emails[] = {"pankaj@gmail.com","sam@yahoo.com","bilbo@hotmail.com"};
    String mobile[] = {"99898888","99892781","89899111"};
    ListView listView;

    Button addFriends;

    public SafarFriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_safar_friends, container, false);
        listView = (ListView)rootView.findViewById(R.id.listUserFriends);
        addFriends = (Button)rootView.findViewById(R.id.addSafarFriends);

        friendsArrayList = new ArrayList<>();

        addFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), SearchFriendActivity.class));
            }
        });

        for (int i = 0; i < emails.length; i++) {
            Friends friends = new Friends();
            friends.emailF = emails[i];
            friends.mobilenumberF = mobile[i];

            friendsArrayList.add(friends);
        }

        UserFriendAdapter adapter = new UserFriendAdapter(getActivity(),friendsArrayList);
        listView.setAdapter(adapter);



        return rootView;
    }


}
