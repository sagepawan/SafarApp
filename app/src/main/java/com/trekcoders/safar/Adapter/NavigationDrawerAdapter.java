package com.trekcoders.safar.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.trekcoders.safar.R;
import com.trekcoders.safar.model.NavDrawerItem;

import java.util.Collections;
import java.util.List;



public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    List<NavDrawerItem> data = Collections.emptyList();
    List<NavDrawerItem> image = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data, List<NavDrawerItem> image) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.image = image;
    }

    public void delete(int position) {
        data.remove(position);
        image.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavDrawerItem current = data.get(position);
        NavDrawerItem current1 = image.get(position);
        holder.title.setText(current.getTitle());
        holder.title_image.setImageResource(Integer.parseInt(current1.getImage()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView title_image;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            title_image = (ImageView) itemView.findViewById(R.id.logo);
        }
    }
}
