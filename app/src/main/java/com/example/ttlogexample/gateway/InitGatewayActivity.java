package com.example.ttlogexample.gateway;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.ttlogexample.BaseActivity;
import com.example.ttlogexample.MyApplication;
import com.example.ttlogexample.databinding.ActivityInitGatewayBinding;
import com.example.ttlogexample.dialog.LoadingDialog;
import com.example.ttlogexample.gateway.dialog.ChooseNetDialog;
import com.example.ttlogexample.lock.model.GatewayObj;
import com.example.ttlogexample.model.AccountInfo;
import com.example.ttlogexample.model.ServerError;
import com.example.ttlogexample.modelNestjs.ServerErrorIO;
import com.example.ttlogexample.retrofit.RetrofitAPIManager;
import com.ttlock.bl.sdk.api.ExtendedBluetoothDevice;
import com.ttlock.bl.sdk.gateway.api.GatewayClient;
import com.ttlock.bl.sdk.gateway.callback.InitGatewayCallback;
import com.ttlock.bl.sdk.gateway.callback.ScanWiFiByGatewayCallback;
import com.ttlock.bl.sdk.gateway.model.ConfigureGatewayInfo;
import com.ttlock.bl.sdk.gateway.model.DeviceInfo;
import com.ttlock.bl.sdk.gateway.model.GatewayError;
import com.ttlock.bl.sdk.gateway.model.WiFi;
import com.ttlock.bl.sdk.util.DigitUtil;
import com.ttlock.bl.sdk.util.GsonUtil;
import com.ttlock.bl.sdk.util.LogUtil;
import com.ttlock.bl.sdk.util.NetworkUtil;
import com.example.ttlogexample.retrofit.ApiService;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


// set wifi to gateway device
public class InitGatewayActivity extends BaseActivity {


    private ActivityInitGatewayBinding binding;
    private ConfigureGatewayInfo configureGatewayInfo;
    private ExtendedBluetoothDevice device;
    private ChooseNetDialog dialog;
    ApiService apiService = RetrofitAPIManager.provideClientApi();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInitGatewayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        device = getIntent().getParcelableExtra(ExtendedBluetoothDevice.class.getName());
        configureGatewayInfo = new ConfigureGatewayInfo();


        initView();
        uploadData();
        initListener();
        chooseWifiDialog();
    }

    private void uploadData() {
        Call<String> call = apiService.userInfoNest(mToken.getAccess_token());
        Log.d("initgate", "call server isSuccess api");
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful()) {
                    // Handle success
                    try {
                        String json = response.body();
                        if (!TextUtils.isEmpty(json)) {
                            AccountInfo acc = GsonUtil.toObject(json, AccountInfo.class);
                            MyApplication.getmInstance().setAccountInfo(acc);

                        } else {
                            makeToast("uid null");
                            finish();
                        }
                    } catch (Exception e) {
                        Log.e("API", "Error reading response body: " + e.getMessage());
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
                Log.d("error", t.getMessage());
            }
        });
    }

    LoadingDialog load = new LoadingDialog(this);

    void navigateBack() {
        load.dismissLoad();
        finish();
    }

    void showLoad() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                load.showLoad();
            }
        }, 50);
    }

    private void initListener() {
        binding.btnInitGateway.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.wifiPwd.getWindowToken(), 0);
            showLoad();
            configureGatewayInfo.uid = MyApplication.getmInstance().getAccountInfo().getUid();
            configureGatewayInfo.userPwd = MyApplication.getmInstance().getAccountInfo().getMd5Pwd();

            configureGatewayInfo.ssid = binding.wifiName.getText().toString().trim();
//            configureGatewayInfo.plugName = binding.gatewayName.getText().toString().trim();
            configureGatewayInfo.wifiPwd = binding.wifiPwd.getText().toString().trim();

            //TODO:   // device is gateway info
            configureGatewayInfo.plugName = device.getAddress();

            configureGatewayInfo.plugVersion = device.getGatewayType();

//            String data = "uid: "+MyApplication.getmInstance().getAccountInfo().getUid()+
//                          "  md5pwd: "+MyApplication.getmInstance().getAccountInfo().getMd5Pwd()+
//                          "  ssid: "+ configureGatewayInfo.getSsid()+
//                          "  wifipwd: "+configureGatewayInfo.getWifiPwd()+
//                          "  address: "+configureGatewayInfo.getPlugName()+
//                          "  gatewayType: "+configureGatewayInfo.getPlugVersion();
//            Log.d("GatewayInfo",data);

            GatewayClient.getDefault().initGateway(configureGatewayInfo, new InitGatewayCallback() {
                @Override
                public void onInitGatewaySuccess(DeviceInfo deviceInfo) {
                    Log.d("initgate1", "gateway init success");
                    isInitSuccess(deviceInfo);
                }

                @Override
                public void onFail(GatewayError error) {
                    makeToast(error.getDescription());
                    Log.e("initgate2", error.toString());
                    load.dismissLoad();
                }
            });
        });
//-----------------------------------------------------------------------------------//
        binding.rlWifiName.setOnClickListener(v -> {
            chooseWifiDialog();
        });
    }

    private void initView() {
        if (NetworkUtil.isWifiConnected(this)) {
            binding.wifiName.setText(NetworkUtil.getWifiSSid(this));
        }
        binding.gatewayName.setText(device.getName());
    }

    private void uploadGatewayDetail(DeviceInfo deviceInfo, int gatewayId) {
        Call<String> call = apiService.uploadGatewayDetailNest(mToken.getAccess_token(),
                gatewayId,
                deviceInfo.getModelNum(),
                deviceInfo.hardwareRevision,
                "1.0.0",
                binding.wifiName.getText().toString(),
                System.currentTimeMillis());
        Log.d("Gateway", "call server isSuccess api");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful()) {
                    // Handle success
                    try {
                        String json = response.body();
                        if (!TextUtils.isEmpty(json)) {
                            ServerErrorIO error = GsonUtil.toObject(json, ServerErrorIO.class);
                            if (error.errcode == 0)
                                navigateBack();
                        }
                    } catch (Exception e) {
                        Log.e("API", "Error reading response body: " + e.getMessage());
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
                    navigateBack();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                makeToast(t.getMessage());
                Log.e("initgate5", t.getMessage());
                navigateBack();
            }
        });
    }

    private void isInitSuccess(DeviceInfo deviceInfo) {
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        Call<String> call = apiService.gatewayIsInitSuccessNest(mToken.getAccess_token(), device.getAddress(), System.currentTimeMillis());
        Log.d("isInitSUccess", "call server isSuccess api");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful()) {
                    // Handle success
                    try {
                        String json = response.body();
                        if (!TextUtils.isEmpty(json)) {
                            GatewayObj gatewayObj = GsonUtil.toObject(json, GatewayObj.class);
                            if (gatewayObj.errcode == 0)
                                uploadGatewayDetail(deviceInfo, gatewayObj.getGatewayId());
                        }
                    } catch (Exception e) {
                        Log.e("API", "Error reading response body: " + e.getMessage());
                        load.dismissLoad();
                    }
                } else {
                    // Handle errors
                    try {
                        String errorBody = response.errorBody().string();
                        ServerErrorIO error = GsonUtil.toObject(errorBody, ServerErrorIO.class); // Convert JSON to ServerErrorIO object
                        Log.d("API", "Error: " + errorBody);
                        makeToast("Error: " + error.getErrmsg());
                        load.dismissLoad();

                    } catch (Exception e) {
                        Log.e("API", "Error parsing error response: " + e.getMessage());
                        load.dismissLoad();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                makeToast(t.getMessage());
                Log.e("initgate7", t.getMessage());
                load.dismissLoad();
            }
        });
    }

    private void chooseWifiDialog() {
        if (dialog == null) {
            dialog = new ChooseNetDialog(this);
            dialog.setOnSelectListener(new ChooseNetDialog.OnSelectListener() {
                @Override
                public void onSelect(WiFi wiFi) {
                    binding.wifiName.setText(wiFi.ssid);
                }
            });
        }
        dialog.show();
        GatewayClient.getDefault().scanWiFiByGateway(device.getAddress(), new ScanWiFiByGatewayCallback() {
            @Override
            public void onScanWiFiByGateway(List<WiFi> wiFis) {
                dialog.updateWiFi(wiFis);
            }

            @Override
            public void onScanWiFiByGatewaySuccess() {
                makeToast("scan completed");
            }

            @Override
            public void onFail(GatewayError error) {
                makeToast(error.getDescription());
            }
        });
    }
}
