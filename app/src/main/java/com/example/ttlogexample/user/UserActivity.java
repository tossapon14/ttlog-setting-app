package com.example.ttlogexample.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ttlogexample.BaseActivity;
import com.example.ttlogexample.IndexActivity;
import com.example.ttlogexample.MainActivity;
import com.example.ttlogexample.MyApplication;

import com.example.ttlogexample.databinding.ActivityUserBinding;
import com.example.ttlogexample.model.AccountInfo;
import com.example.ttlogexample.modelNestjs.ServerErrorIO;
import com.example.ttlogexample.modelNestjs.UserModel;
import com.example.ttlogexample.retrofit.ApiService;
import com.example.ttlogexample.retrofit.RetrofitAPIManager;
import com.ttlock.bl.sdk.util.GsonUtil;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends BaseActivity {
    ActivityUserBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initview();
    }
    void initview(){
        binding.btnSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }
    void registerUser(){
        String email = Objects.requireNonNull(binding.filledTextField1.getEditText()).getText().toString();
        String password = Objects.requireNonNull(binding.filledTextField2.getEditText()).getText().toString();
        String name = Objects.requireNonNull(binding.filledTextField3.getEditText()).getText().toString();
        String hotel = Objects.requireNonNull(binding.filledTextField4.getEditText()).getText().toString();
        String tel = Objects.requireNonNull(binding.filledTextField5.getEditText()).getText().toString();
//        String data = email+password+name+hotel+tel;
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        Call<String> call = apiService.registerNest(email,password,name,hotel,tel);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    String json = response.body();
                    if (!TextUtils.isEmpty(json)) {
                        UserModel user = GsonUtil.toObject(json, UserModel.class);
                        if (user.getKey() != null) {
                            makeToast(user.ttlockData.getTtlockuser());
                        Intent intent = new Intent(UserActivity.this, UserViewActivity.class);
                        intent.putExtra("userData", user.toString()); // Replace "key" and "value" with your actual key-value pair
                        startActivity(intent);
                        }
                    } else {
                        makeToast(response.message());
                        Log.e("user2", response.message());
                    }
                }else{
                    try {
                        String errorBody = response.errorBody().string();
                        ServerErrorIO error = GsonUtil.toObject(errorBody, ServerErrorIO.class); // Convert JSON to ServerErrorIO object
                        Log.e("API", "Error: " + errorBody);
                        makeToast("Error: " + error.getErrmsg());
                    } catch (IOException e) {
                        Log.e("API", "Error parsing error response: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                makeToast(t.getMessage());
                Log.e("user3", t.getMessage());
            }
        });
    }
}