package com.trekcoders.safar.Fragments;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trekcoders.safar.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SafarFriendsFragment extends Fragment {


    public SafarFriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_safar_friends, container, false);
    }


}
