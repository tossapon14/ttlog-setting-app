package com.example.ttlogexample.lock;

import android.Manifest;
import android.annotation.TargetApi;

import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;


import com.example.ttlogexample.BaseActivity;
import com.example.ttlogexample.MyApplication;
import com.example.ttlogexample.databinding.ActivityScanLockBinding;
import com.example.ttlogexample.lock.adapter.LockListAdapter;
import com.example.ttlogexample.dialog.LoadingDialog;

import com.example.ttlogexample.modelNestjs.ServerErrorIO;
import com.example.ttlogexample.retrofit.ApiService;
import com.example.ttlogexample.retrofit.RetrofitAPIManager;

import com.ttlock.bl.sdk.api.ExtendedBluetoothDevice;
import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.callback.InitLockCallback;
import com.ttlock.bl.sdk.callback.ResetLockCallback;
import com.ttlock.bl.sdk.callback.ScanLockCallback;
import com.ttlock.bl.sdk.callback.SetNBServerCallback;
import com.ttlock.bl.sdk.constant.FeatureValue;
import com.ttlock.bl.sdk.entity.LockError;
import com.ttlock.bl.sdk.util.FeatureValueUtil;
import com.ttlock.bl.sdk.util.GsonUtil;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// lockdata encode from InitLockCallback()
public class ScanLockActivity extends BaseActivity implements LockListAdapter.onLockItemClick {

    private ActivityScanLockBinding binding;
    private static final int REQUEST_PERMISSION_REQ_CODE = 11;
    private LockListAdapter lockListAdapter;
    private ExtendedBluetoothDevice device;
    LoadingDialog load = new LoadingDialog(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScanLockBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupRecyclerView();
        initializeBluetoothService();
        setupListeners();
    }

    private final ActivityResultLauncher<Intent> formResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData(); // Retrieve the Intent
                    if (data != null) {
                        HashMap<String, Object> formData = (HashMap<String, Object>) data.getSerializableExtra("formSubmitted"); // Retrieve the HashMap                        if (isFormSubmitted) {
                        if (formData != null) {
                            showLoad();
                            initializeLock(device,formData.get("building").toString(), formData.get("floor").toString(), formData.get("room").toString(),formData.get("alias").toString());
                        }

                    }
                }
            });

    /**
     * Initializes the RecyclerView and its adapter.
     */

    private void setupRecyclerView() {
        lockListAdapter = new LockListAdapter(this);
        binding.rvLockList.setAdapter(lockListAdapter);
        binding.rvLockList.setLayoutManager(new LinearLayoutManager(this));
        lockListAdapter.setOnLockItemClick(this);
    }

    /**
     * Prepares the TTLock Bluetooth service.
     */
    private void initializeBluetoothService() {
        TTLockClient.getDefault().prepareBTService(this);
    }

    /**
     * Sets up button click listeners for Bluetooth enable, scan start, and stop.
     */
    private void setupListeners() {
//        binding.btnEnableBle.setOnClickListener(v -> enableBluetooth());
        binding.btnEnableBle.setOnClickListener(v -> {
            Intent intent = new Intent(this, FormInitLockActivity.class);
            intent.putExtra("hotel", MyApplication.getmInstance().getAuthToken().getHotel());
            formResultLauncher.launch(intent);
        });

        binding.btnStartScan.setOnClickListener(v -> startScanning());
        binding.btnStopScan.setOnClickListener(v -> TTLockClient.getDefault().stopScanLock());
    }

    /**
     * Checks and requests Bluetooth to be enabled.
     *
     * @return true if Bluetooth is enabled, false otherwise.
     */
    private boolean enableBluetooth() {
        if (!TTLockClient.getDefault().isBLEEnabled(this)) {
            TTLockClient.getDefault().requestBleEnable(this);
            return false;
        }
        return true;
    }

    /**
     * Starts scanning for locks after checking permissions.
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void startScanning() {
        if (!enableBluetooth()) {
            Log.e("Bluetooth", "Bluetooth is not enabled");
            makeToast("Please enable Bluetooth");
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH_SCAN}, REQUEST_PERMISSION_REQ_CODE);
                return;
            }
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_REQ_CODE);
            return;
        }

        startLockScan();
    }

    /**
     * Initiates the Bluetooth lock scanning process.
     */
    private void startLockScan() {
        TTLockClient.getDefault().startScanLock(new ScanLockCallback() {
            @Override
            public void onScanLockSuccess(ExtendedBluetoothDevice device) {
                Log.d("Scan", "Lock found: " + device.toString());
                if (lockListAdapter != null) {
                    lockListAdapter.updateData(device);
                }
            }

            @Override
            public void onFail(LockError error) {
                Log.e("Scan", "Failed to scan locks: " + error.getErrorMsg());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_REQ_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("Permissions", "1");
                startScanning();
            } else {
                Log.e("Permissions", "Required permissions not granted");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TTLockClient.getDefault().stopBTService();
        Log.d("onDestroy", "");
    }

    @Override
    public void onClick(ExtendedBluetoothDevice device) {
        Intent intent = new Intent(this, FormInitLockActivity.class);
        intent.putExtra("hotel", MyApplication.getmInstance().getAuthToken().getHotel());
        formResultLauncher.launch(intent);
        this.device = device;
    }

    /**
     * Initializes the discovered lock.
     */

    private void initializeLock(ExtendedBluetoothDevice device, String building, String floor, String room, String lockAlias) {   // lockdata encode from InitLockCallback()
        TTLockClient.getDefault().initLock(device, new InitLockCallback() {
            @Override
            public void onInitLockSuccess(String lockData) {
                if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.NB_LOCK)) {
//                    setNBServerForNBLock(lockData, device.getAddress());  // GET MAC
                } else {
                    makeToast("Lock initialized successfully!");
                    uploadLockData(lockData, device.getAddress(), building, floor, room, lockAlias);
                }
            }
            @Override
            public void onFail(LockError error) {
                if(load.isShow()){
                    load.dismissLoad();
                }
                System.out.println("3  error *** " + error.getErrorMsg());
                makeErrorToast(error);
            }
        });
    }


    void navigateBack() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                load.dismissLoad();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("scanLock", true); // Passing the HashMap
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        }, 1000);
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

    /**
     * Configures the NB-IoT server for the lock if required.
     */
//    private void setNBServerForNBLock(String lockData, String lockMac) {
//        short nbServerPort = 8011;
//        String nbServerAddress = "192.127.123.11";
//
//        TTLockClient.getDefault().setNBServerInfo(nbServerPort, nbServerAddress, lockData, new SetNBServerCallback() {
//            @Override
//            public void onSetNBServerSuccess(int battery) {
//                makeToast("NB server configured successfully!");
//                uploadLockData(lockData);
//            }
//
//            @Override
//            public void onFail(LockError error) {
//                makeErrorToast(error);
//                uploadLockData(lockData);
//            }
//        });
//    }

    /**
     * Uploads lock data to the server.
     */

    private void uploadLockData(String lockData, String mac, String building, String floor, String room, String lockAlias) {
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        Call<ResponseBody> call = apiService.lockInitNest(
                mToken.getAccess_token(),
                lockData,
                mac,
                building,
                floor,
                room,
                lockAlias,
                System.currentTimeMillis()
        );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Handle success
                    try {
                        // Parse the successful response
                        String json = response.body().string();
                        if (!json.isEmpty()) {
//                            Log.d("API", "Success: " + lock);
                            makeToast("Lock initialization successful!");
                            navigateBack();
                        }
                    } catch (IOException e) {
                        Log.e("API", "Error reading response body: " + e.getMessage());
                    }
                } else {
                    // Handle errors
                    try {
                        load.dismissLoad();
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("API", "Network Error: " + t.getMessage());
            }
        });
    }
}
