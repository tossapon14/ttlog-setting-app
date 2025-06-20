package com.example.ttlogexample.lock;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.example.ttlogexample.BaseActivity;
import com.example.ttlogexample.MyApplication;
import com.example.ttlogexample.databinding.ActivityUnlockBinding;
import com.example.ttlogexample.retrofit.ApiService;
import com.example.ttlogexample.retrofit.RetrofitAPIManager;
import com.google.gson.reflect.TypeToken;
import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.callback.ControlLockCallback;
import com.ttlock.bl.sdk.constant.ControlAction;
import com.ttlock.bl.sdk.entity.ControlLockResult;
import com.ttlock.bl.sdk.entity.LockError;
import com.example.ttlogexample.lock.model.*;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class UnlockActivity extends BaseActivity {
    ActivityUnlockBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUnlockBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());;
        TTLockClient.getDefault().prepareBTService(getApplicationContext());
        Log.d("lock44", mCurrentLock.getLockData());
        Log.d("lock44",  mCurrentLock.getLockMac());
        initListener();
    }

    private void initListener() {
        binding.btnUnlock.setOnClickListener(v -> doUnlock());
        binding.btnLock.setOnClickListener(v -> doLockLock());
    }


    private void doUnlock(){

        ensureBluetoothIsEnabled();
        showConnectLockToast();
        TTLockClient.getDefault().controlLock(ControlAction.UNLOCK, mCurrentLock.getLockData(), mCurrentLock.getLockMac(),new ControlLockCallback() {
            @Override
            public void onControlLockSuccess(ControlLockResult controlLockResult) {
                Toast.makeText(UnlockActivity.this,"lock is unlock  success!",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFail(LockError error) {
                Toast.makeText(UnlockActivity.this,"unLock fail!--" + error.getDescription(),Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * use eKey for controlLock interface
     */
    private void doLockLock(){
//        if(mMyTestLockEKey == null){
//            makeToast(" you should get your key list first ");
//            return;
//        }
        ensureBluetoothIsEnabled();
        showConnectLockToast();
        TTLockClient.getDefault().controlLock(ControlAction.LOCK, mCurrentLock.getLockData(), mCurrentLock.getLockMac(),new ControlLockCallback() {
            @Override
            public void onControlLockSuccess(ControlLockResult controlLockResult) {
                Toast.makeText(UnlockActivity.this,"lock is locked!",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFail(LockError error) {
                Toast.makeText(UnlockActivity.this,"lock lock fail!--" + error.getDescription(),Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * stopBTService should be called when Activity is finishing to release Bluetooth resource.
     */
    @Override
    public void onDestroy(){
        super.onDestroy();
        TTLockClient.getDefault().stopBTService();
    }
}