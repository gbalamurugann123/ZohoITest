package com.zoho.android.repository.inMemory.byItem;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import java.util.concurrent.Executor;

/**
 * Created by Balamurugan on 24/6/18.
 */

public class ZohoUserDataSourceFactory implements DataSource.Factory {

    MutableLiveData<ZohoUserDataSource> mutableLiveData;
    ZohoUserDataSource itemKeyedUserDataSource;
    Executor executor;

    public ZohoUserDataSourceFactory(Executor executor) {
        this.mutableLiveData = new MutableLiveData<ZohoUserDataSource>();
        this.executor = executor;
    }


    @Override
    public DataSource create() {
        itemKeyedUserDataSource = new ZohoUserDataSource(executor);
        mutableLiveData.postValue(itemKeyedUserDataSource);
        return itemKeyedUserDataSource;
    }

    public MutableLiveData<ZohoUserDataSource> getMutableLiveData() {
        return mutableLiveData;
    }

}
