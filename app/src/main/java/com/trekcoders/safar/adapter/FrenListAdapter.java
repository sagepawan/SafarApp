package com.trekcoders.safar.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.trekcoders.safar.R;
import com.trekcoders.safar.model.Trails;
import com.trekcoders.safar.model.Users;

import java.util.ArrayList;


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
            convertView = layoutInflater.inflate(R.layout.row_fren_list, null);
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

                ParseQuery pushQuery = ParseInstallation.getQuery();
                pushQuery.whereEqualTo("user_objectId", user.objectId);
                ParseUser parseUser = ParseUser.getCurrentUser();

                ParsePush push = new ParsePush();
                push.setQuery(pushQuery); // Set our Installation query
                push.setMessage("You have been added as a friend by "+ parseUser.getUsername());
                push.sendInBackground();

                //insert
                ParseObject parseObject = new ParseObject("Friends");
                parseObject.put("userObjId", ParseObject.createWithoutData("_User", parseUser.getObjectId()));
                parseObject.put("frenObjId", ParseObject.createWithoutData("_User", user.objectId));
                parseObject.saveInBackground();
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView email;
        Button addFren;

    }
}
