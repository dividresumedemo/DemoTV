package com.example.resumedemo.netWorker;

import androidx.annotation.NonNull;

import com.example.resumedemo.callBack.ConnectListener;
import com.example.resumedemo.data.JsonData;
import com.example.resumedemo.data.ResultHttpData;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetWorkerClass extends Thread {
    private ConnectListener mListener;

    public static NetWorkerClass getInstance() {
        return new NetWorkerClass();
    }

    public void getWebData(String url, ConnectListener listener) {
        mListener = listener;
        runHttpMission(url);
    }

    private void runHttpMission(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                final ResultHttpData data = new ResultHttpData();
                data.setRtnCode(-1);
                data.setRtnDataString(e.getMessage());
                mListener.onResult(data);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    if (response.body() != null) {
                        String responseData = response.body().string();
                        JSONObject json = new JSONObject(responseData);
                        setSuccessData(json,response.code());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setSuccessData(JSONObject json,int resCode) {
        final ResultHttpData data = new ResultHttpData();
        Gson gson = new Gson();
        String jsonStr = json.toString();
        JsonData jsonData = gson.fromJson(jsonStr, JsonData.class);
        data.setRtnDataString(jsonStr);
        data.setRtnData(jsonData);
        data.setRtnCode(resCode);
        mListener.onResult(data);
    }
}
