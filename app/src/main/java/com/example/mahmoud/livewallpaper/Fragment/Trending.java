package com.example.mahmoud.livewallpaper.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mahmoud.livewallpaper.Common.Common;
import com.example.mahmoud.livewallpaper.Interface.ItemClickListener;
import com.example.mahmoud.livewallpaper.ListWallpaper;
import com.example.mahmoud.livewallpaper.Model.WallpaperItem;
import com.example.mahmoud.livewallpaper.R;
import com.example.mahmoud.livewallpaper.ViewHolder.ListWallpaperViewHolder;
import com.example.mahmoud.livewallpaper.ViewWallpaper;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


public class Trending extends Fragment {
    private static Trending INSTANCE=null;

    RecyclerView recyclerView;
    FirebaseDatabase database;
    DatabaseReference categoryBackground;

    FirebaseRecyclerOptions<WallpaperItem> options;
    FirebaseRecyclerAdapter<WallpaperItem,ListWallpaperViewHolder> adapter;



    public Trending() {
        // Required empty public constructor
        //init firebase
        database=FirebaseDatabase.getInstance();
        categoryBackground=database.getReference(Common.STR_WALLPAPER);

        Query query=categoryBackground.orderByChild("viewCount")
                .limitToLast(10); // get 10 item bigest

        options=new FirebaseRecyclerOptions.Builder<WallpaperItem>().setQuery(query,WallpaperItem.class)
                .build();

        adapter=new FirebaseRecyclerAdapter<WallpaperItem, ListWallpaperViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ListWallpaperViewHolder holder, int position, @NonNull final WallpaperItem model) {

                Picasso.with(getActivity())
                        .load(model.getImageLink())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(holder.wallpaper, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {

                                Picasso.with(getActivity())
                                        .load(model.getImageLink())
                                        .error(R.drawable.ic_terrain_black_24dp)
                                        .into(holder.wallpaper);

                            }
                        });
                holder.setItemclickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent intent=new Intent(getActivity(),ViewWallpaper.class);
                        Common.selected_background=model;
                        Common.selected_background_key=adapter.getRef(position).getKey();
                        startActivity(intent);

                    }
                });

            }

            @Override
            public ListWallpaperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemview= LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_wallpaper_item,parent,false);

                int height=parent.getMeasuredHeight()/2;
                itemview.setMinimumHeight(height);
                return  new ListWallpaperViewHolder(itemview);
            }
        };
    }


    public static Trending getInstance(){
        if(INSTANCE == null){
            INSTANCE=new Trending();
        }
        return INSTANCE;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_daily_popular, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerview_terend);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());

        //reverse recycler
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        loadTerendingList();

        return view;
    }

    private void loadTerendingList() {
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        if(adapter != null)
            adapter.startListening();
    }

    @Override
    public void onStop() {
        if (adapter != null)
            adapter.stopListening();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter != null)
            adapter.startListening();
    }
}
