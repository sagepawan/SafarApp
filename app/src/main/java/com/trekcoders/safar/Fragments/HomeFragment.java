package com.trekcoders.safar.Fragments;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.trekcoders.safar.R;
import com.trekcoders.safar.adapter.MyListAdapter;
import com.trekcoders.safar.model.Trails;

import java.util.ArrayList;
import java.util.List;


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

    ArrayList<Trails> trails;

    ParseQuery<ParseObject> query;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        trails = new ArrayList<Trails>();
        getTrailsDetailsFromParse();


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

    private void getTrailsDetailsFromParse() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Getting trails lists..");
        progressDialog.show();

        query = ParseQuery.getQuery("Trails");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> listObj, ParseException e) {
                if (e == null) {

                    for (ParseObject object : listObj) {
                        Trails trail = new Trails();
                        String name = object.getString("trailName");
                        trail.trailName = name;
                        ParseFile parseFile = (ParseFile) object.get("trailPic");
                        trail.trailPic = parseFile;
                        trails.add(trail);
                    }
                    progressDialog.dismiss();

                    MyListAdapter adapter = new MyListAdapter(getActivity(), trails);
                    trailListView = (ListView) getView().findViewById(R.id.trail_list);
                    trailListView.setAdapter(adapter);

                    trailListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            AddFriendFragment addFriend = new AddFriendFragment();
                            getActivity().getFragmentManager().beginTransaction().replace(R.id.container_body, addFriend).commit();
                        }
                    });
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
        //Fetch profile photo

    }
}
