package com.example.second;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Util {

    public void saveLocalData(Context activity, String key, String value) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("my_preferences", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply(); // apply changes
    }

    public String getLocalData(Context activity, String key) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("my_preferences", Context.MODE_PRIVATE);

        return sharedPreferences.getString(key, "");
    }


    public void sendMessage(String userid, String message, String sender, String time, String type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {


                    Message message1 = new Message(sender,message,time,type,userid);

                    ApiService apiService = ApiClient.getClient().create(ApiService.class);
                    Call<Void> call = apiService.sendmessage(message1);

                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Log.d("asdf123","yes");
                            } else {
                                Log.d("asdf123","unsucess");
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.d("asdf123",t.toString());
                        }
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
