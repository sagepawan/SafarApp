package com.trekcoders.safar.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.trekcoders.safar.R;
import com.trekcoders.safar.adapter.MyListAdapter;




public class HomeFragment extends Fragment {

    ListView trailListView;

    String[] trailNames = {
            "Annapurna Base Camp Trail",
            "Langtang Hike Trail",
            "Everest Base Camp Trail",
            "Rocky Mountain Trail",
            "The Alps Trail"
    };

    Integer[] trailImages = {
            R.drawable.sliderimage_abc,
            R.drawable.sliderimage_langtang,
            R.drawable.sliderimage_ebc,
            R.drawable.sliderimage_rocky,
            R.drawable.sliderimage_alps
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);


        MyListAdapter adapter = new MyListAdapter(getActivity(),trailNames,trailImages);
        trailListView = (ListView)rootView.findViewById(R.id.trail_list);
        trailListView.setAdapter(adapter);

        trailListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                AddFriendFragment addFriend = new AddFriendFragment();
                getActivity().getFragmentManager().beginTransaction().replace(R.id.container_body, addFriend).commit();
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    /*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }*/
}
