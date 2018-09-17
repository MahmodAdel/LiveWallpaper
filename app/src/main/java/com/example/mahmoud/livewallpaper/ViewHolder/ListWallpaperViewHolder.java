package com.example.mahmoud.livewallpaper.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.mahmoud.livewallpaper.Interface.ItemClickListener;
import com.example.mahmoud.livewallpaper.R;

/**
 * Created by mahmoud on 7/04/18.
 */

public class ListWallpaperViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    ItemClickListener itemclickListener;
    public ImageView wallpaper;


    public ItemClickListener getItemclickListener() {
        return itemclickListener;
    }

    public void setItemclickListener(ItemClickListener itemclickListener) {
        this.itemclickListener = itemclickListener;
    }


    public ListWallpaperViewHolder(View itemView) {
        super(itemView);
        wallpaper=(ImageView) itemView.findViewById(R.id.imageView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemclickListener.onClick(view,getAdapterPosition());

    }
}
