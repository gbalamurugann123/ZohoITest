package com.zoho.android;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.zoho.android.db.AppDatabase;
import com.zoho.android.db.dao.UserDao;
import com.zoho.android.ui.UserAdapter;
import com.zoho.android.ui.UserViewModel;
import com.zoho.android.util.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    private UserViewModel viewModel;
    private String TAG = "MainActivity";

    public static AppDatabase appDatabase;
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appDatabase = AppDatabase.getDatabase(MainActivity.this);
        userDao = appDatabase.userDao();


        RecyclerView recyclerView = findViewById(R.id.userList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);


        NetworkUtils networkUtils = new NetworkUtils(MainActivity.this);
        if(networkUtils.isOnline()) {
            viewModel = ViewModelProviders.of(this).get(UserViewModel.class);
            viewModel.online();
        }
        else {
            viewModel = ViewModelProviders.of(this).get(UserViewModel.class);
            viewModel.offline(userDao);
        }


        final UserAdapter userAdapter = new UserAdapter(MainActivity.this);
        viewModel.userList.observe(this, pagedList -> {
            userAdapter.setList(pagedList);
        });
        viewModel.networkState.observe(this, networkState -> {
            userAdapter.setNetworkState(networkState);
            Log.d(TAG, "Network State Change");
        });
        recyclerView.setAdapter(userAdapter);


    }


}

