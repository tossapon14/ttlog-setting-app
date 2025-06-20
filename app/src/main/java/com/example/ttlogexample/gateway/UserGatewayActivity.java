package com.example.ttlogexample.gateway;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ttlogexample.BaseActivity;
import com.example.ttlogexample.MyApplication;
import com.example.ttlogexample.databinding.ActivityUserGatewayBinding;
import com.example.ttlogexample.gateway.adapter.UserGatewayListAdapter;
import com.example.ttlogexample.lock.ScanLockActivity;
import com.example.ttlogexample.model.GatewayObj;
import com.example.ttlogexample.modelNestjs.ServerErrorIO;
import com.example.ttlogexample.retrofit.ApiService;
import com.example.ttlogexample.retrofit.RetrofitAPIManager;
import com.google.gson.reflect.TypeToken;
import com.ttlock.bl.sdk.util.GsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;


public class UserGatewayActivity extends BaseActivity {

    private int pageNo = 1;
    private int pageSize = 20;
    private UserGatewayListAdapter mListApapter;
    private ActivityUserGatewayBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserGatewayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnInitGateway.setOnClickListener(v -> {
            Intent intent = new Intent(this, GatewayActivity.class);
            activityResultLauncher.launch(intent);
        });
        initList();
        gatewayList();
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            if (data.getBooleanExtra("gateway", false)) {
                                gatewayList();
                            }
                        }
                    }
                }
            });

    private void initList() {
        mListApapter = new UserGatewayListAdapter(this);
        binding.rvGatewayList.setAdapter(mListApapter);
        binding.rvGatewayList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void gatewayList() {
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        Log.d("gateway", "Bearer " + mToken.getAccess_token());
        Call<String> call = apiService.getGatewayListNest(mToken.getAccess_token());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful()) {
                    String json = response.body();
                    if (json.contains("list")) {
                        try {
                            Log.d("gateway", json);
                            JSONObject jsonObject = new JSONObject(json);
                            JSONArray array = jsonObject.getJSONArray("list");
                            ArrayList<GatewayObj> gatewayObjs = GsonUtil.toObject(array.toString(), new TypeToken<ArrayList<GatewayObj>>() {
                            });
                            mListApapter.updateData(gatewayObjs);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    // Handle errors
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
            }
        });
    }


}
