package com.trekcoders.safar.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
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
import com.parse.ParseUser;
import com.trekcoders.safar.Activity.AddFriendActivity;
import com.trekcoders.safar.Activity.TrailMapActivity;
import com.trekcoders.safar.R;
import com.trekcoders.safar.Adapter.MyListAdapter;
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

    ParseUser parseUser;

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
        parseUser = ParseUser.getCurrentUser();
        if (parseUser != null && parseUser.getSessionToken() != null)
            getTrailsDetailsFromParse();


        // Inflate the layout for this fragment
        return rootView;
    }


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


                    MyListAdapter adapter = new MyListAdapter(getActivity(), trails);
                    trailListView = (ListView) getView().findViewById(R.id.trail_list);
                    trailListView.setAdapter(adapter);
                    progressDialog.dismiss();

                    trailListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            ParseQuery query = ParseQuery.getQuery("Friends");
                            query.include("userObjId");
                            query.include("frenObjId");
                            query.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> list, ParseException e) {
                                    if (e == null) {
                                        boolean isFrenEmpty = true;
                                        for (ParseObject obj : list) {
                                            ParseObject usr = obj.getParseObject("userObjId");

                                            if (usr.getObjectId().equalsIgnoreCase(parseUser.getObjectId())) {
                                                isFrenEmpty = false;
                                                Intent next = new Intent(getActivity(), TrailMapActivity.class);
                                                startActivity(next);
                                            }
                                        }
                                        if (isFrenEmpty) {
                                            Intent next = new Intent(getActivity(), AddFriendActivity.class);
                                            startActivity(next);
                                        }

                                    }

                                }

                            });

                        }
                    });
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }
}
