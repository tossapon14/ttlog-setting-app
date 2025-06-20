package com.example.ttlogexample.retrofit;

import android.util.Log;

import com.example.ttlogexample.modelNestjs.ServerErrorIO;
import com.google.gson.reflect.TypeToken;
import com.ttlock.bl.sdk.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Retrofit;


public class RetrofitAPIManager {

    //    public static final String SERVER_URL = "https://euapi.sciener.com";
//    public static final String SERVER_URL = "http://192.168.156.177:3000";
    public static final String SERVER_URL = "http://192.168.137.1:3000";
//    public static final String SERVER_URL = "https://tossapon-tunnel.onrender.com";


    public static ApiService provideClientApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(genericClient())
                .baseUrl(SERVER_URL)
                .addConverterFactory(new Converter.Factory() {
                    @Override
                    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
                        return new Converter<ResponseBody, String>() {
                            @Override
                            public String convert(ResponseBody value) throws IOException {
                                String json = value.string();
                                try {
                                    JSONObject jsonObject = new JSONObject(json);
                                    if (jsonObject.getInt("errcode")!=0) {
                                        throw new IOException();
                                    }
                                } catch (JSONException e) {
                                    Log.e("Converter", "Error parsing JSON: " + e.getMessage());
                                }

                                return json; // Return the raw JSON string for success responses
                            }
                        };
                    }
                })
                .build();
        return retrofit.create(ApiService.class);
    }

    public static OkHttpClient genericClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(35, TimeUnit.SECONDS)
                .readTimeout(35, TimeUnit.SECONDS)
                .writeTimeout(35, TimeUnit.SECONDS)
                .build();

        return httpClient;
    }

    public static <T> ApiRequtest enqueue(Call<ResponseBody> call, TypeToken<T> resultType, ApiResponse.Listener<ApiResult<T>> listener, ApiResponse.ErrorListener errorListener) {
        ApiRequtest<T> request = new ApiRequtest<>(call, resultType, listener, errorListener);
        return request;
    }
}