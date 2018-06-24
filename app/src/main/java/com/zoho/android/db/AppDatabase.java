package com.zoho.android.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.zoho.android.db.dao.UserDao;
import com.zoho.android.vo.User;


@Database(entities = {User.class}, version = 1)
abstract public class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "UserDb";

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                            .build();
        }
        return INSTANCE;
    }

    public abstract UserDao userDao();
}