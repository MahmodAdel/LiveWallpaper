package com.example.mahmoud.livewallpaper.Fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mahmoud.livewallpaper.Adapter.MyRecyclerAdapter;
import com.example.mahmoud.livewallpaper.Database.DataSource.RecentRepository;
import com.example.mahmoud.livewallpaper.Database.LocalDatabase.LocalDatabase;
import com.example.mahmoud.livewallpaper.Database.LocalDatabase.RecentsDataSource;
import com.example.mahmoud.livewallpaper.Database.Recents;
import com.example.mahmoud.livewallpaper.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class RecentsFragment extends Fragment {

    private static RecentsFragment INSTANCE=null;
    RecyclerView recyclerView;
    List<Recents> recentsList;
    MyRecyclerAdapter adapter;

    Context context;

    CompositeDisposable compositeDisposable;
    RecentRepository recentRepository;



    @SuppressLint("ValidFragment")
    public RecentsFragment(Context context) {
        // Required empty public constructor
        this.context=context;
        //init room
        compositeDisposable=new CompositeDisposable();
        LocalDatabase database=LocalDatabase.getInstance(context);
        recentRepository=RecentRepository.getInstance(RecentsDataSource.getInstance(database.recentsDAO()));

    }


    public static RecentsFragment getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE=new RecentsFragment(context);
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
        View view= inflater.inflate(R.layout.fragment_recents, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerview_recent);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),2);
        recentsList=new ArrayList<>();

        recyclerView.setLayoutManager(gridLayoutManager);
        adapter=new MyRecyclerAdapter(context,recentsList);
        recyclerView.setAdapter(adapter);

        loadRecents();

        return view;
    }

    private void loadRecents() {
        Disposable disposable=recentRepository.getAllRecents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Recents>>() {
                    @Override
                    public void accept(List<Recents> recents) throws Exception {
                        onGetAllRecentsSucess(recents);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("Error",throwable.getMessage());
                    }
                });

        compositeDisposable.add(disposable);

    }

    private void onGetAllRecentsSucess(List<Recents> recents) {
        recentsList.clear();
        recentsList.addAll(recents);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
