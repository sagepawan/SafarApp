package com.trekcoders.safar.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.trekcoders.safar.Activity.MainActivity;
import com.trekcoders.safar.R;
import com.trekcoders.safar.SafarApplication;
import com.trekcoders.safar.model.Users;

import java.util.ArrayList;
import java.util.List;


public class FrenListAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<Users> users;
    private LayoutInflater layoutInflater;

    public FrenListAdapter(Context context, ArrayList<Users> users){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.users = users;

    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_search_friend_list, null);
            holder = new ViewHolder();
            holder.email = (TextView) convertView.findViewById(R.id.tvEmail);
            holder.addFren = (Button) convertView.findViewById(R.id.addFren);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Users user = users.get(position);

        holder.email.setText(user.username);



        holder.addFren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int limit = 0;
                System.out.println("value"+
                        SafarApplication.app.pref.getFriendsNumberFive()+SafarApplication.app.pref.getFriendsNumberThree());
                if(SafarApplication.app.pref.getFriendsNumberFive() )
                    limit = 5;
                else if(SafarApplication.app.pref.getFriendsNumberThree())
                    limit = 3;
                else
                    limit = 0;
                ParseQuery userQuery = ParseQuery.getQuery("Friends");
                userQuery.whereEqualTo("userObjId", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));

                ParseQuery frenQuery = ParseQuery.getQuery("Friends");
                frenQuery.whereEqualTo("frenObjId", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));

                List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
                queries.add(userQuery);
                queries.add(frenQuery);

                ParseQuery query = ParseQuery.or(queries);
                final int finalLimit = limit;
                query.countInBackground(new CountCallback() {
                    public void done(int count, ParseException e) {
                        if (e == null) {
                            if(finalLimit == 0){
                                Toast.makeText(context, "Set friend number first", Toast.LENGTH_LONG).show();
                            } else {
                                if (count < finalLimit) {
                                    ParseQuery pushQuery = ParseInstallation.getQuery();
                                    pushQuery.whereEqualTo("user_objectId", user.objectId);
                                    ParseUser parseUser = ParseUser.getCurrentUser();

                                    ParsePush push = new ParsePush();
                                    push.setQuery(pushQuery); // Set our Installation query
                                    push.setMessage("You have been added as a friend by " + parseUser.getUsername());
                                    push.sendInBackground();

                                    //insert
                                    ParseObject parseObject = new ParseObject("Friends");
                                    parseObject.put("userObjId", ParseObject.createWithoutData("_User", parseUser.getObjectId()));
                                    parseObject.put("frenObjId", ParseObject.createWithoutData("_User", user.objectId));
                                    parseObject.saveInBackground();

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                                    // set title
                                    alertDialogBuilder.setTitle("Friend Added");

                                    // set dialog message
                                    alertDialogBuilder
                                            .setMessage("You successfully added a new friend!")
                                            .setCancelable(false)
                                            .setPositiveButton("Add More", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            })
                                            .setNegativeButton("Home", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    // if this button is clicked, just close
                                                    // the dialog box and do nothing
                                                    context.startActivity(new Intent(context, MainActivity.class));
                                                }
                                            });

                                    // create alert dialog
                                    AlertDialog alertDialog = alertDialogBuilder.create();

                                    // show it
                                    alertDialog.show();
                                } else {
                                    Toast.makeText(context, "Maximum number reached", Toast.LENGTH_LONG).show();
                                }
                            }
                        } else {
                            // The request failed
                        }
                    }
                });

            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView email;
        Button addFren;

    }
}
