package com.example.ttlogexample.lock;

import android.os.Bundle;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.example.ttlogexample.BaseActivity;
import com.example.ttlogexample.databinding.ActivityLockApi2Binding;
import com.example.ttlogexample.iccard.ICCardActivity;
import com.example.ttlogexample.modelNestjs.ServerErrorIO;
import com.example.ttlogexample.retrofit.ApiService;
import com.example.ttlogexample.retrofit.RetrofitAPIManager;

import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.callback.GetBatteryLevelCallback;
import com.ttlock.bl.sdk.callback.GetLockStatusCallback;
import com.ttlock.bl.sdk.callback.GetLockSystemInfoCallback;
import com.ttlock.bl.sdk.callback.GetOperationLogCallback;
import com.ttlock.bl.sdk.callback.ResetKeyCallback;
import com.ttlock.bl.sdk.callback.ResetLockCallback;
import com.ttlock.bl.sdk.callback.SetAutoLockingPeriodCallback;
import com.ttlock.bl.sdk.constant.FeatureValue;
import com.ttlock.bl.sdk.constant.LogType;
import com.ttlock.bl.sdk.entity.DeviceInfo;
import com.ttlock.bl.sdk.entity.LockError;
import com.ttlock.bl.sdk.util.FeatureValueUtil;
import com.ttlock.bl.sdk.util.GsonUtil;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LockApiActivity extends BaseActivity {
    ActivityLockApi2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLockApi2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if(mCurrentLock == null){
            makeToast("please choose at least one initialized lock first");
        }
        else{makeToast("lock infomation is ready");}
        TTLockClient.getDefault().prepareBTService(getApplicationContext());
        initListener();
    }

    private void initListener(){
        binding.btnUnlock.setOnClickListener(v -> startTargetActivity(UnlockActivity.class));
//        binding.btnResetKey.setOnClickListener(v -> resetEKey());
        binding.btnResetLock.setOnClickListener(v -> resetLock());
        binding.btnAddCard.setOnClickListener(v->startTargetActivity(ICCardActivity.class));
//        binding.btnConfigFeature.setOnClickListener(v -> startTargetActivity(EnableDisableSomeLockFunctionActivity.class));
//        binding.btnPassageMode.setOnClickListener(v -> startTargetActivity(PassageModeActivity.class));
//        binding.btnTime.setOnClickListener(v -> startTargetActivity(LockTimeActivity.class));
//        binding.btnLog.setOnClickListener(v -> getOperationLog());
//        binding.btnBattery.setOnClickListener(v -> getLockBatteryLevel());
//        binding.btnLockInfo.setOnClickListener(v -> getLockSystemInfo());
//        binding.btnLockStatus.setOnClickListener(v -> getLockStatus());
//        binding.btnSetAutoLockTime.setOnClickListener(v -> setAutoLockingPeriod());
    }
    private void resetEKey(){
        ensureBluetoothIsEnabled();
        showConnectLockToast();
        TTLockClient.getDefault().resetEkey(mCurrentLock.getLockData(),mCurrentLock.getLockMac(), new ResetKeyCallback() {
            @Override
            public void onResetKeySuccess(String lockData) {
//                updateLockData(lockData);
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }
//    private void updateLockData(String lockData){
//        ApiService apiService = RetrofitAPIManager.provideClientApi();
//        HashMap<String,String> params = new HashMap<>(8);
//        params.put("clientId",ApiService.CLIENT_ID);
//        params.put("accessToken", MyApplication.getmInstance().getAccountInfo().getAccess_token());
//        params.put("lockId",String.valueOf(mCurrentLock.getLockId()));
//        params.put("lockData", lockData);
//        params.put("date",String.valueOf(System.currentTimeMillis()));
//
//        Call<ResponseBody> call = apiService.updateLockData(params);
//        call.enqueue(new Callback<>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if(response.isSuccessful()){
//                    String json = response.body();
//                    if (!TextUtils.isEmpty(json)) {
//                        mCurrentLock.setLockData(lockData);
//                        makeToast("--update the lock data to server success--");
//                    } else {
//                        makeToast(response.message());
//                        Log.e("updateLock1", response.message());
//                    }
//                }else{
//                    try {
//                        String errorBody = response.errorBody().string();
//                        ServerErrorIO error = GsonUtil.toObject(errorBody, ServerErrorIO.class); // Convert JSON to ServerErrorIO object
//                        Log.e("updateLock2", "Error: " + errorBody);
//                        makeToast("Error: " + error.getErrmsg());
//                    } catch (IOException e) {
//                        Log.e("updateLock3", "Error parsing error response: " + e.getMessage());
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                makeToast(t.getMessage());
//                Log.e("updateLock4", t.getMessage());
//            }
//        });
//    }
    /**
     * resetLock
     * means the lock will be reset to factory mode and if you want to use it,you should do initLock.
     */
    private void resetLock(){
        ensureBluetoothIsEnabled();
        showConnectLockToast();
        TTLockClient.getDefault().resetLock(mCurrentLock.getLockData(), mCurrentLock.getLockMac(),new ResetLockCallback() {
            @Override
            public void onResetLockSuccess() {
                makeToast("-lock is reset and now upload to  server -");
                uploadResetLock2Server();
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }

    /**
     * this must be done after resetLock is called success.
     */
    private void uploadResetLock2Server(){
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        Call<String> call = apiService.deleteLockNest(mToken.getAccess_token(),mCurrentLock.getLockId(), System.currentTimeMillis());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    String json = response.body();
                    if (!TextUtils.isEmpty(json)) {
                        makeToast("--reset lock and notify server success--");
                    } else {
                        makeToast(response.message());
                        Log.e("uploadResetLock2Server1", response.message());
                    }
                }else{
                    try {
                        String errorBody = response.errorBody().string();
                        ServerErrorIO error = GsonUtil.toObject(errorBody, ServerErrorIO.class); // Convert JSON to ServerErrorIO object
                        Log.e("uploadResetLock2Server2", "Error: " + errorBody);
                        makeToast("Error: " + error.getErrmsg());
                    } catch (IOException e) {
                        Log.e("uploadResetLock2Server3", "Error parsing error response: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                makeToast(t.getMessage());
                Log.e("uploadResetLock2Server4", t.getMessage());
            }
        });
    }

    /**
     *  logType  ALL - all the operation record from lock is initialized.
     *          NEW - only the new added operation record from last time you get log.
     */
    private void getOperationLog(){
        showConnectLockToast();
        TTLockClient.getDefault().getOperationLog(LogType.NEW, mCurrentLock.getLockData(),mCurrentLock.getLockMac(), new GetOperationLogCallback() {
            @Override
            public void onGetLogSuccess(String log) {
                makeToast("Get log success!");
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }

    private void getLockBatteryLevel(){
        showConnectLockToast();
        TTLockClient.getDefault().getBatteryLevel(mCurrentLock.getLockData(),mCurrentLock.getLockMac(), new GetBatteryLevelCallback() {
            @Override
            public void onGetBatteryLevelSuccess(int electricQuantity) {
                makeToast("lock battery is " + electricQuantity + "%");
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }

    private void getLockSystemInfo(){
        showConnectLockToast();
        TTLockClient.getDefault().getLockSystemInfo(mCurrentLock.getLockData(), mCurrentLock.getLockMac(),new GetLockSystemInfoCallback() {
            @Override
            public void onGetLockSystemInfoSuccess(DeviceInfo info) {
                makeToast(info.toString());
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }

    /**
     * query lock status,such as unlock,lock.
     *
     * status :0-lock 1-unlock  2-unknown status 3-unlocked,has car top(this is only for car parking lock)
     */
    private void getLockStatus(){
        showConnectLockToast();
        TTLockClient.getDefault().getLockStatus(mCurrentLock.getLockData(),mCurrentLock.getLockMac(), new GetLockStatusCallback() {
            @Override
            public void onGetLockStatusSuccess(int status) {
                makeToast("lock status is now " + status);
            }

            @Override
            public void onGetDoorSensorStatusSuccess(int status) {
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }

    /**
     * set Automatic locking period.
     *
     */
    private void setAutoLockingPeriod(){
        if(!FeatureValueUtil.isSupportFeature(mCurrentLock.getLockData(), FeatureValue.AUTO_LOCK)){
            makeToast("this lock dose not support automatic locking");
        }
        showConnectLockToast();
        TTLockClient.getDefault().setAutomaticLockingPeriod(5, mCurrentLock.getLockData(), mCurrentLock.getLockMac(),new SetAutoLockingPeriodCallback() {
            @Override
            public void onSetAutoLockingPeriodSuccess() {
                makeToast("set automatic locking period success");
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }

}