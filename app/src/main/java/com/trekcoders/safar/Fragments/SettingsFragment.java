package com.trekcoders.safar.Fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.trekcoders.safar.R;

import java.util.List;


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

        rootView.findViewById(R.id.btnChngPass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("");

                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialogLayout = inflater.inflate(R.layout.layout_dialogue_forgetpass, null);
                alertDialog.setView(dialogLayout);

                final EditText email = (EditText)dialogLayout.findViewById(R.id.edEmailForgetPass);
                //successText.setText(finalResult);
                final EditText pass = (EditText)dialogLayout.findViewById(R.id.edNewPass);
                //alertDialog.setMessage("Registration information sent for approval");

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Set New Password",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                final String userPass = pass.getText().toString();
                                ParseUser user = ParseUser.getCurrentUser();
                                user.setPassword(userPass);
                                user.saveInBackground();

                            }
                        });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                alertDialog.show();
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }

}
