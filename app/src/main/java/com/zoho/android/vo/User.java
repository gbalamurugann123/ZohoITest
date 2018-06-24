package com.zoho.android.vo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;

import com.google.gson.annotations.SerializedName;

@Entity
public class User {
    public static DiffCallback<User> DIFF_CALLBACK = new DiffCallback<User>() {
        @Override
        public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.userId == newItem.userId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.equals(newItem);
        }
    };

    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long userId;

    @SerializedName("first_name")
    @ColumnInfo(name = "first_name")
    public String firstName;

    @SerializedName("last_name")
    @ColumnInfo(name = "last_name")
    public String lastName;

    @SerializedName("avatar")
    @ColumnInfo(name = "avatar")
    public String avatar;

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        User user = (User) obj;

        return user.userId == this.userId && user.firstName == this.firstName && user.lastName == this.lastName && user.avatar == this.avatar;
    }
}
