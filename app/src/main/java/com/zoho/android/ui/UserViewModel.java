package com.zoho.android.ui;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.zoho.android.MainActivity;
import com.zoho.android.db.AppDatabase;
import com.zoho.android.db.dao.UserDao;
import com.zoho.android.repository.NetworkState;
import com.zoho.android.repository.inMemory.byItem.ZohoUserDataSourceFactory;
import com.zoho.android.vo.User;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Balamurugan on 24/6/18.
 */

public class UserViewModel extends ViewModel {

    public LiveData<PagedList<User>> userList;
    public LiveData<NetworkState> networkState;
    ZohoUserDataSourceFactory zohoUserDataSourceFactory;
    Executor executor;

    public UserViewModel(){
        executor = Executors.newFixedThreadPool(5);
        zohoUserDataSourceFactory = new ZohoUserDataSourceFactory(executor);

        networkState = Transformations.switchMap(zohoUserDataSourceFactory.getMutableLiveData(), dataSource -> {
            return dataSource.getNetworkState();
        });
    }


    public void online() {

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder()).setEnablePlaceholders(false)
                        .setInitialLoadSizeHint(3)
                        .setPageSize(3).build();
        userList = (new LivePagedListBuilder(zohoUserDataSourceFactory, pagedListConfig))
                .setBackgroundThreadExecutor(executor)
                .build();
    }


    public void offline(UserDao userDao) {
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder()).setEnablePlaceholders(true)
                        .setPrefetchDistance(10)
                        .setPageSize(20).build();

        userList = (new LivePagedListBuilder(userDao.usersByFirstName(), pagedListConfig))
                .build();

    }


}
