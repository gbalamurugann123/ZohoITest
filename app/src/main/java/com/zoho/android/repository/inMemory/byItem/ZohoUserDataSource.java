package com.zoho.android.repository.inMemory.byItem;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.ItemKeyedDataSource;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.zoho.android.MainActivity;
import com.zoho.android.api.ZohoUserApi;
import com.zoho.android.api.ZohoUserService;
import com.zoho.android.repository.NetworkState;
import com.zoho.android.repository.Status;
import com.zoho.android.vo.Page;
import com.zoho.android.vo.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Balamurugan on 24/6/18.
 */

public class ZohoUserDataSource extends ItemKeyedDataSource<Long, User> {
    public static final String TAG = "ItemKeyedUserDataSource";
    ZohoUserService zohoUserService;
    LoadInitialParams<Long> initialParams;
    LoadParams<Long> afterParams;
    private MutableLiveData networkState;
    private MutableLiveData initialLoading;
    private Executor retryExecutor;

    public ZohoUserDataSource(Executor retryExecutor) {
        zohoUserService = ZohoUserApi.createGitHubService();
        networkState = new MutableLiveData();
        initialLoading = new MutableLiveData();
        this.retryExecutor = retryExecutor;
    }


    public MutableLiveData getNetworkState() {
        return networkState;
    }

    public MutableLiveData getInitialLoading() {
        return initialLoading;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<User> callback) {
        Log.i(TAG, "Loading Rang " + 1 + " Count " + params.requestedLoadSize);
        List<User> zohoUser = new ArrayList();
        initialParams = params;
        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);
        zohoUserService.getUser(1, params.requestedLoadSize).enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    zohoUser.addAll(response.body().getUser());
                    callback.onResult(zohoUser);

                    insertIntoRoom(zohoUser);

                    initialLoading.postValue(NetworkState.LOADED);
                    networkState.postValue(NetworkState.LOADED);
                    initialParams = null;
                } else {
                    Log.e("API CALL", response.message());
                    initialLoading.postValue(new NetworkState(Status.FAILED, response.message()));
                    networkState.postValue(new NetworkState(Status.FAILED, response.message()));
                }
            }

            @Override
            public void onFailure(Call<Page> call, Throwable t) {
                String errorMessage;
                errorMessage = t.getMessage();
                if (t == null) {
                    errorMessage = "unknown error";
                }
                networkState.postValue(new NetworkState(Status.FAILED, errorMessage));
            }
        });

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<User> callback) {
        Log.i(TAG, "Loading Rang " + params.key + " Count " + params.requestedLoadSize);
        List<User> zohoUser = new ArrayList();
        afterParams = params;

        int page = (int) (params.key / 3 + 1);

        networkState.postValue(NetworkState.LOADING);
        zohoUserService.getUser(page, params.requestedLoadSize).enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {
                if (response.isSuccessful()) {
                    zohoUser.addAll(response.body().getUser());
                    callback.onResult(zohoUser);

                    insertIntoRoom(zohoUser);


                    networkState.postValue(NetworkState.LOADED);
                    afterParams = null;
                } else {
                    networkState.postValue(new NetworkState(Status.FAILED, response.message()));
                    Log.e("API CALL", response.message());
                }
            }

            @Override
            public void onFailure(Call<Page> call, Throwable t) {
                String errorMessage;
                errorMessage = t.getMessage();
                if (t == null) {
                    errorMessage = "unknown error";
                }
                networkState.postValue(new NetworkState(Status.FAILED, errorMessage));
            }
        });

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<User> callback) {

    }

    @NonNull
    @Override
    public Long getKey(@NonNull User item) {
        return item.userId;
    }


    private void insertIntoRoom(List<User> zohoUser)
    {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                MainActivity.appDatabase.userDao().insertAll(zohoUser);
                return null;
            }
        }.execute();
    }

}
