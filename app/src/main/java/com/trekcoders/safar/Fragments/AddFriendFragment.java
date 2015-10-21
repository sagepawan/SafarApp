package com.trekcoders.safar.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.trekcoders.safar.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFriendFragment extends Fragment {


    Button addFriendButton;

    public AddFriendFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_friend, container, false);

        addFriendButton = (Button)rootView.findViewById(R.id.btn_add_friend);

        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchFriendFragment frag = new SearchFriendFragment();
                getActivity().getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.container_body, frag).commit();
            }
        });

        return  rootView;
    }

}
