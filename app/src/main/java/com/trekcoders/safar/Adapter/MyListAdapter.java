package com.trekcoders.safar.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.trekcoders.safar.R;
import com.trekcoders.safar.model.Trails;

import java.util.ArrayList;


public class MyListAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<Trails> trails;
    private LayoutInflater layoutInflater;

    public MyListAdapter(Context context, ArrayList<Trails> trails){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.trails = trails;

    }

    @Override
    public int getCount() {
        return trails.size();
    }

    @Override
    public Object getItem(int position) {
        return trails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.custom_list_layout, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.trail_title);
            holder.imgPic = (ImageView) convertView.findViewById(R.id.trail_image);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Trails t = (Trails) trails.get(position);

        holder.name.setText(t.trailName);
        byte[] data = new byte[0];
        try {
            data = t.trailPic.getData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        holder.imgPic.setImageBitmap(bitmap);


        return convertView;
    }

    static class ViewHolder {
        TextView name;
        ImageView imgPic;

    }
}
