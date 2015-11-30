package com.trekcoders.safar.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.parse.ParseUser;
import com.trekcoders.safar.R;
import com.trekcoders.safar.SafarApplication;


public class SettingsFragment extends Fragment {

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        CheckBox fiveFriend = (CheckBox) rootView.findViewById(R.id.cbFive);
        CheckBox threeFriend = (CheckBox) rootView.findViewById(R.id.cbThree);

        if (SafarApplication.app.pref.getFriendsNumberFive())
            fiveFriend.setChecked(true);
        if (SafarApplication.app.pref.getFriendsNumberThree())
            threeFriend.setChecked(true);
        fiveFriend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println("change");
                if (isChecked) {
                    SafarApplication.app.pref.setFriendsNumberFive(false);
                }
                else {
                    SafarApplication.app.pref.setFriendsNumberFive(true);
                }

            }
        });
        threeFriend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println("change");
                if (isChecked) {
                    SafarApplication.app.pref.setFriendsNumberThree(false);
                }
                else {
                    SafarApplication.app.pref.setFriendsNumberThree(true);
                }

            }
        });
        rootView.findViewById(R.id.btnChngPass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("");

                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialogLayout = inflater.inflate(R.layout.layout_dialogue_forgetpass, null);
                alertDialog.setView(dialogLayout);

                final EditText pass = (EditText) dialogLayout.findViewById(R.id.edNewPass);



                //METHOD TO CHANGE CURRENT USER PASSWORD - - DONE BY UDAY
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Set New Password",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                final String userPass = pass.getText().toString();  //gets new password user enters
                                ParseUser user = ParseUser.getCurrentUser();        //gets current user from parse
                                user.setPassword(userPass);                         //sets the new password into users account in parse
                                user.saveInBackground();                            //saves the change in parse

                            }
                        });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                alertDialog.dismiss();
                            }
                        });

                alertDialog.show();
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }

}
