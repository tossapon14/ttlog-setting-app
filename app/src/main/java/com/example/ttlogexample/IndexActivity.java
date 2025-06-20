package com.example.ttlogexample;

import android.os.Bundle;


import com.example.ttlogexample.databinding.ActivityIndexBinding;
import com.example.ttlogexample.gateway.UserGatewayActivity;
import com.example.ttlogexample.lock.UserLockActivity;
import com.example.ttlogexample.lock.UserLockActivity;

public class IndexActivity extends BaseActivity{
    ActivityIndexBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIndexBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initListener();
    }
    private void initListener(){
        binding.btnLock.setOnClickListener(v -> {startTargetActivity(UserLockActivity.class);});
        binding.btnGateway.setOnClickListener(v -> {startTargetActivity(UserGatewayActivity.class);});
    }
}