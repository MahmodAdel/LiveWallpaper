package com.example.mahmoud.livewallpaper.Database.DataSource;

import com.example.mahmoud.livewallpaper.Database.Recents;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by mahmoud on 20/04/18.
 */

public interface IReacentsDataSource {

    Flowable<List<Recents>> getAllRecents();
    void insertRecents(Recents... recents);
    void updateRecents(Recents... recents);
    void deleteRecents(Recents... recents);
    void deleteAllRecents();

}
