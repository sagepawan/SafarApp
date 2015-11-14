package com.trekcoders.safar.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.trekcoders.safar.Adapter.NotificationAdapter;
import com.trekcoders.safar.R;
import com.trekcoders.safar.model.Notification;

import java.util.ArrayList;
import java.util.List;


public class NotificationFragment extends Fragment {

    ListView listView;
    ArrayList<Notification> notificationList;
    ParseUser user;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);

        notificationList = new ArrayList<>();
        user = ParseUser.getCurrentUser();
        listView = (ListView)rootView.findViewById(R.id.listUserNotification);

        ParseQuery<ParseObject> notificationQuery = ParseQuery
                .getQuery("Notifications");

        notificationQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                for (ParseObject Obj : list) {


                    if(user.getObjectId().equals(Obj.get("userObjId"))){

                        Notification notification = new Notification();
                        notification.nObjectId = Obj.getObjectId();
                        notification.nUserObjId = Obj.getString("userObjId");
                        notification.nFriendObjId = Obj.getString("friendObjId");
                        notification.nMessage = Obj.getString("message");
                        notification.nTrailobjId = Obj.getString("trailObjId");

                        notificationList.add(notification);
                    }

                }

                Log.d("notificationArraySize",": "+notificationList.size());

                // UserFriendAdapter adapter = new UserFriendAdapter(getActivity(), friendsArrayList, SafarFriendsFragment.this);
                NotificationAdapter nAdapter = new NotificationAdapter(getActivity(), notificationList);
                listView.setAdapter(nAdapter);
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

}
