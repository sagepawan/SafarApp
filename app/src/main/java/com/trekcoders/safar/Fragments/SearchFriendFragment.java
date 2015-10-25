package com.trekcoders.safar.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.trekcoders.safar.R;

import java.text.ParseException;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFriendFragment extends Fragment {

    ImageView searchBtn;
    ParseUser user;
    List<ParseUser> ob;
    ParseQuery<ParseUser> query;

    public SearchFriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search_friend, container, false);

        //user = ParseUser.getCurrentUser();


        searchBtn = (ImageView)rootView.findViewById(R.id.btn_search_friend);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Log.d("Username",": "+user.getUsername());
                query = ParseUser.getQuery();
                //query.whereEqualTo("username",user);

                try {
                    ob = query.find();
                    Log.d("ObSize",": "+ob.size());

                    for (int i = 0; i < ob.size(); i++) {

                        Log.d("usernames",": "+i+" - "+ob.get(i).getUsername());
                    }

                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                            getActivity());
                    //builderSingle.setIcon(R.drawable.ic_launcher);
                    builderSingle.setTitle("Select Gender");
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                            getActivity(),
                            android.R.layout.select_dialog_item);

                    for (int i = 0; i < ob.size(); i++) {

                        arrayAdapter.add(ob.get(i).getUsername());
                    }

                    builderSingle.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //String strName = arrayAdapter.getItem(which);
                                    dialog.dismiss();
                                }
                            });

                    builderSingle.setAdapter(arrayAdapter,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    /*strGender = arrayAdapter.getItem(which);
                                    genderValue.setText(strGender);*/
                                }
                            });
                    builderSingle.show();


                } catch (com.parse.ParseException e) {
                    e.printStackTrace();
                    Log.e("errormsg",": "+e.getMessage());
                }

            }
        });
        return rootView;
    }

}
