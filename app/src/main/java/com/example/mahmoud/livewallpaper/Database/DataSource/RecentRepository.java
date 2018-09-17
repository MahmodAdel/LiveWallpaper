package com.example.mahmoud.livewallpaper.Database.DataSource;

import com.example.mahmoud.livewallpaper.Database.Recents;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by mahmoud on 20/04/18.
 */

public class RecentRepository implements IReacentsDataSource {

    private IReacentsDataSource mLocalDataSource;
    private static RecentRepository instance;


    public RecentRepository(IReacentsDataSource mLocalDataSource) {
        this.mLocalDataSource = mLocalDataSource;
    }

    public static RecentRepository getInstance(IReacentsDataSource mLocalDataSource) {
        if(instance == null){
            instance=new RecentRepository(mLocalDataSource);
        }
        return instance;
    }


    @Override
    public Flowable<List<Recents>> getAllRecents() {
        return mLocalDataSource.getAllRecents();
    }

    @Override
    public void insertRecents(Recents... recents) {
        mLocalDataSource.insertRecents(recents);

    }

    @Override
    public void updateRecents(Recents... recents) {
        mLocalDataSource.updateRecents(recents);

    }

    @Override
    public void deleteRecents(Recents... recents) {
        mLocalDataSource.deleteRecents(recents);
    }

    @Override
    public void deleteAllRecents() {
        mLocalDataSource.deleteAllRecents();
    }
}
