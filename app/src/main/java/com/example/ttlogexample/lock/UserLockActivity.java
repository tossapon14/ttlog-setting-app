package com.example.ttlogexample.lock;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ttlogexample.BaseActivity;
import com.example.ttlogexample.MyApplication;
import com.example.ttlogexample.R;
import com.example.ttlogexample.databinding.ActivityMainBinding;
import com.example.ttlogexample.databinding.ActivityUserLockBinding;
import com.example.ttlogexample.lock.adapter.UserLockListAdapter;
import com.example.ttlogexample.model.LockObj;
import com.example.ttlogexample.modelNestjs.LockModel;
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

public class UserLockActivity extends BaseActivity {
//    private int pageNo = 1;
//    private int pageSize = 20;
    private UserLockListAdapter mListApapter;
    ActivityUserLockBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserLockBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initList();
        lockList();
        binding.btnInitLock.setOnClickListener(v -> {
            Intent intent = new Intent(this,ScanLockActivity.class);
            activityResultLauncher.launch(intent);
        });
    }
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if(data!=null){
                            if(data.getBooleanExtra("scanLock",false)){
                                lockList();
                            }
                        }
                    }
                }
            });

    private void initList(){
        mListApapter = new UserLockListAdapter(this);
        binding.rvLockList.setAdapter(mListApapter);
        binding.rvLockList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void lockList() {
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        Call<String> call = apiService.getLockListNest(mToken.getAccess_token());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful()) {
                    try {  // Handle success
                        String json = response.body();
                        if (!json.isEmpty()) {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray array = jsonObject.getJSONArray("list");
                        Log.d("lock",array.toString());
                        ArrayList<LockModel> lockList = GsonUtil.toObject(array.toString(), new TypeToken<ArrayList<LockModel>>(){});
                        mListApapter.updateData(lockList);
                        }
                    }catch (Exception e) {
                        Log.e("API", "Error parsing error response: " + e.getMessage());
                    }
                } else {
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