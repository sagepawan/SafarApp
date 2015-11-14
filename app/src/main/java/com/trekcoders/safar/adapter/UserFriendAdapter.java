package com.trekcoders.safar.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.trekcoders.safar.Fragments.SafarFriendsFragment;
import com.trekcoders.safar.R;
import com.trekcoders.safar.model.Friends;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sagepawan on 10/27/2015.
 */
public class UserFriendAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<Friends> userFriends;
    private LayoutInflater layoutInflater;
    SafarFriendsFragment fragment;

    public UserFriendAdapter(Context context,ArrayList<Friends> userFriends, SafarFriendsFragment fragment) {

        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.userFriends = userFriends;
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return userFriends.size();
    }

    @Override
    public Object getItem(int i) {
        return userFriends.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_user_friend_list, null);
            holder = new ViewHolder();
            holder.friendEmail = (TextView) convertView.findViewById(R.id.tvEmailF);
            holder.friendMobile = (TextView) convertView.findViewById(R.id.tvmobileF);
            holder.friendDelete = (TextView) convertView.findViewById(R.id.tvRemove);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Friends friends = userFriends.get(i);

        holder.friendEmail.setText(friends.emailF);
        holder.friendMobile.setText(friends.mobilenumberF);

        holder.friendDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(context,"Delete Selected",Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                // set title
                alertDialogBuilder.setTitle("DELETE FRIEND");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Are you sure you want to delete this friend?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                fragment.refresh(i);

                                ParseQuery parseQuery = ParseQuery.getQuery("Friends");
                                parseQuery.whereEqualTo("objectId", friends.objectIdF);
                                //parseQuery.include("userObjId");
                                //parseQuery.include("frenObjId");

                                parseQuery.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> list, ParseException e) {

                                        for (ParseObject Obj : list) {

                                            Obj.deleteInBackground();
                                        }
                                    }
                                });

                            }


                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView friendEmail;
        TextView friendMobile;
        TextView friendDelete;
    }
}
