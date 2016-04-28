package com.execube.genesis.utils;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Prateek Phoenix on 4/24/2016.
 */
public class OkHttpHandler {

    private String queryUrl;
    private static final String TAG = "CustomTAG1";
    private Callback mCallback;

    public OkHttpHandler(String Url, Callback callback) {
        this.mCallback = callback;
        this.queryUrl = Url;
    }

    public void fetchData() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(queryUrl)
                .build();

        Call call = client.newCall(request);
        call.enqueue(mCallback);


    }

}