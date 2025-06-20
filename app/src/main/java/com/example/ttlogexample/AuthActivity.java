package com.example.ttlogexample;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ttlogexample.databinding.ActivityAuthBinding;
import com.example.ttlogexample.hotel.HotelSelectActivity;
import com.example.ttlogexample.lock.FormInitLockActivity;
import com.example.ttlogexample.model.AccountInfo;
import com.example.ttlogexample.modelNestjs.AuthModel;
import com.example.ttlogexample.modelNestjs.ServerErrorIO;
import com.example.ttlogexample.retrofit.ApiService;
import com.example.ttlogexample.retrofit.RetrofitAPIManager;
import com.ttlock.bl.sdk.util.DigitUtil;
import com.ttlock.bl.sdk.util.GsonUtil;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class AuthActivity extends BaseActivity {
    ActivityAuthBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initListener();
    }
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            if (data.getBooleanExtra("authAll", false)) {
                                startTargetActivity(IndexActivity.class);
                            }
                        }
                    }
                }
            });

    private void initListener() {
        binding.textEmail.setText("tor.36105@gmail.com");
        binding.textPassword.setText("1234");
        binding.btnAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authNestServer();
            }
        });
    }
    private void startTarget(){
        Intent intent = new Intent(this, HotelSelectActivity.class);
        intent.putExtra("email", binding.textEmail.getText().toString().trim()); // For String
        intent.putExtra("password", binding.textPassword.getText().toString().trim()); // For int
        activityResultLauncher.launch(intent);
    }

    //    private void auth() {
//        ApiService apiService = RetrofitAPIManager.provideClientApi();
//        String account = binding.etAccount.getText().toString().trim();
//        password = binding.etPassword.getText().toString().trim();
//        password = DigitUtil.getMD5(password);
//        Call<String> call = apiService.auth(ApiService.CLIENT_ID, ApiService.CLIENT_SECRET, account, password);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//                String json = response.body();
//                accountInfo = GsonUtil.toObject(json, AccountInfo.class);
//                if (accountInfo != null) {
//                    if (accountInfo.errcode == 0) {
//                        accountInfo.setMd5Pwd(password);
//                        MyApplication.getmInstance().setAccountInfo(accountInfo);
//                        makeToast(accountInfo.toString());
//                        Log.e("response", json);
//                        startTargetActivity(IndexActivity.class);
//                    } else {
//                        makeToast(accountInfo.errmsg);
//                        Log.e("auth1", accountInfo.errmsg);
//                    }
//                } else {
//                    makeToast(response.message());
//                    Log.e("auth2", response.message());
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                makeToast(t.getMessage());
//                Log.e("auth3", t.getMessage());
//            }
//        });
//    }
    private void authNestServer() {
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        String account = binding.textEmail.getText().toString().trim();
        String password = binding.textPassword.getText().toString().trim();
        Call<String> call = apiService.authNest(account, password);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if(response.isSuccessful()){
                    String json = response.body();
                    if(!TextUtils.isEmpty(json)){
                        AuthModel auth = GsonUtil.toObject(json, AuthModel.class);
                        auth.setAccess_token("Bearer "+auth.getAccess_token());
                        MyApplication.getmInstance().setAuthModel(auth);
                        makeToast("Select hotel");
                        Log.d("Login auth", json);
                        startTarget();
                    } else {
                        makeToast(response.message());
                        Log.e("auth", response.message());
                    }
                }else{
                    try {
                        String errorBody = response.errorBody().string();
                        ServerErrorIO error = GsonUtil.toObject(errorBody, ServerErrorIO.class); // Convert JSON to ServerErrorIO object
                        Log.d("API", "Error: " + errorBody);
                        makeToast("Error: " + error.getErrmsg());
                    } catch (Exception e) {
                        Log.e("API", "Error parsing error response: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                makeToast(t.getMessage());
                Log.e("auth3", t.getMessage());
            }
        });
    }
}