package com.trekcoders.safar.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.trekcoders.safar.Activity.TrailMapActivity;
import com.trekcoders.safar.R;
import com.trekcoders.safar.model.Friends;
import com.trekcoders.safar.model.Notification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sagepawan on 11/14/2015.
 */
public class NotificationAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<Notification> notificationArrayList;
    private LayoutInflater layoutInflater;


    public NotificationAdapter(Context context,ArrayList<Notification> notificationArrayList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.notificationArrayList = notificationArrayList;
    }

    @Override
    public int getCount() {
        return notificationArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return notificationArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_user_notifications, null);
            holder = new ViewHolder();
            holder.userNotification = (TextView) convertView.findViewById(R.id.tvNotification);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Notification notification = notificationArrayList.get(i);
        Log.d("noficationMessage",": "+notificationArrayList.size());
        holder.userNotification.setText(notification.nMessage);

        holder.userNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, TrailMapActivity.class);
                intent.putExtra("from","notification");
                intent.putExtra("trailObjId",notification.nTrailobjId);
                intent.putExtra("frenObjId",notification.nFriendObjId);
                
                context.startActivity(intent);
                //context.startActivity(new Intent(context, TrailMapActivity.class));
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView userNotification;

    }
}
