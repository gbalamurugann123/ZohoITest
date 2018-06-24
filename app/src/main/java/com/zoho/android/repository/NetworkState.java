package com.zoho.android.repository;

/**
 * Created by Balamurugan on 24/6/18.
 */


public class NetworkState {
    private final Status status;
    private final String msg;

    public static final NetworkState LOADED;
    public static final NetworkState LOADING;

    public NetworkState(Status status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    static {
        LOADED=new NetworkState(Status.SUCCESS,"Success");
        LOADING=new NetworkState(Status.RUNNING,"Running");
    }

    public Status getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }



}
