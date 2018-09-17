package com.example.mahmoud.livewallpaper.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mahmoud.livewallpaper.Interface.ItemClickListener;
import com.example.mahmoud.livewallpaper.R;

/**
 * Created by mahmoud on 7/04/18.
 */

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView category_name;
    public ImageView background_image;

    ItemClickListener itemclickListener;


    public ItemClickListener getItemclickListener() {
        return itemclickListener;
    }

    public void setItemclickListener(ItemClickListener itemclickListener) {
        this.itemclickListener = itemclickListener;
    }



    public CategoryViewHolder(View itemView) {
        super(itemView);
        category_name=(TextView)itemView.findViewById(R.id.name);
        background_image=(ImageView)itemView.findViewById(R.id.image);
        background_image.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        itemclickListener.onClick(view,getAdapterPosition());

    }
}
