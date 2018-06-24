package com.zoho.android.db.dao;

import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListProvider;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.zoho.android.vo.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAll(List<User> users);


    @Query("SELECT * FROM User")
    public abstract DataSource.Factory<Integer,User> usersByFirstName();

}
