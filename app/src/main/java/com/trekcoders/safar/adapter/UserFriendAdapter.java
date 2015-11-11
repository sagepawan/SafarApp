package com.trekcoders.safar.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.trekcoders.safar.R;
import com.trekcoders.safar.model.Friends;

import java.util.ArrayList;

/**
 * Created by Sagepawan on 10/27/2015.
 */
public class UserFriendAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<Friends> userFriends;
    private LayoutInflater layoutInflater;

    public UserFriendAdapter(Context context,ArrayList<Friends> userFriends) {

        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.userFriends = userFriends;
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
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
                                dialog.cancel();
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
