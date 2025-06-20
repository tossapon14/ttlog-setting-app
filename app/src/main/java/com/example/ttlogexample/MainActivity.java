package com.example.ttlogexample;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.ttlogexample.databinding.ActivityMainBinding;
import com.example.ttlogexample.iccard.ICCardActivity;
import com.example.ttlogexample.lock.FormInitLockActivity;
import com.example.ttlogexample.lock.LockApiActivity;
import com.example.ttlogexample.model.Building;
import com.example.ttlogexample.modelNestjs.ServerErrorIO;
import com.example.ttlogexample.retrofit.ApiService;
import com.example.ttlogexample.retrofit.RetrofitAPIManager;
import com.example.ttlogexample.user.UserActivity;
import com.example.ttlogexample.utils.AppUtil;
import com.google.gson.reflect.TypeToken;
import com.ttlock.bl.sdk.util.GsonUtil;

import androidx.core.view.WindowCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Tossapon on 2024/11/14.
 */
public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initListener();
        if (AppUtil.isAndroid12OrOver()) {
            AppUtil.checkPermission(this, Manifest.permission.BLUETOOTH_CONNECT);
        }
//        startTargetActivity(AuthActivity.class);
    }

    private void initListener() {
        binding.btnRegister.setOnClickListener(v -> startTargetActivity(UserActivity.class));
        binding.btnAuth.setOnClickListener(v -> startTargetActivity(AuthActivity.class));
        binding.btnLock.setOnClickListener(v -> {
            if (mToken == null) {
                startTargetActivity(AuthActivity.class);
            } else {
                startTargetActivity(LockApiActivity.class);

            }
        });

//        binding.btnPasscode.setOnClickListener(v -> startTargetActivity(FormInitLockActivity.class));
//        binding.btnFirmware.setOnClickListener(v ->  startTargetActivity(FirmwareUpdateActivity.class));
//        binding.btnFingerprint.setOnClickListener(v -> startTargetActivity(FingerprintActivity.class));
//        binding.btnIc.setOnClickListener(v -> startTargetActivity(ICCardActivity.class));
//        binding.btnWirelessKeyboard.setOnClickListener(v -> startTargetActivity(WirelessKeyboardActivity.class));
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}