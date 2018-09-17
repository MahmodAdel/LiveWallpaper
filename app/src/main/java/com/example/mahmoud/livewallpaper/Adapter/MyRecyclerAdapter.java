package com.example.mahmoud.livewallpaper.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mahmoud.livewallpaper.Common.Common;
import com.example.mahmoud.livewallpaper.Database.Recents;
import com.example.mahmoud.livewallpaper.Interface.ItemClickListener;
import com.example.mahmoud.livewallpaper.ListWallpaper;
import com.example.mahmoud.livewallpaper.Model.WallpaperItem;
import com.example.mahmoud.livewallpaper.R;
import com.example.mahmoud.livewallpaper.ViewHolder.ListWallpaperViewHolder;
import com.example.mahmoud.livewallpaper.ViewWallpaper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mahmoud on 20/04/18.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<ListWallpaperViewHolder> {

    private Context context;
    private List<Recents> recents;


    public MyRecyclerAdapter(Context context, List<Recents> recents) {
        this.context = context;
        this.recents = recents;
    }

    @Override
    public ListWallpaperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_wallpaper_item,parent,false);

        int height=parent.getMeasuredHeight()/2;
        itemview.setMinimumHeight(height);
        return  new ListWallpaperViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(final ListWallpaperViewHolder holder, final int position) {
        Picasso.with(context)
                .load(recents.get(position).getImageLink())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.wallpaper, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                        Picasso.with(context)
                                .load(recents.get(position).getImageLink())
                                .error(R.drawable.ic_terrain_black_24dp)
                                .into(holder.wallpaper);

                    }
                });
        holder.setItemclickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent=new Intent(context,ViewWallpaper.class);
                WallpaperItem wallpaperItem=new WallpaperItem();
                wallpaperItem.setCategoryId(recents.get(position).getCategoryId());
                wallpaperItem.setImageLink(recents.get(position).getImageLink());
                Common.selected_background=wallpaperItem;
                Common.selected_background_key=recents.get(position).getKey();
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return recents.size();
    }
}
