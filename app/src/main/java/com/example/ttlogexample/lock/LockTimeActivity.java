package com.example.ttlogexample.lock;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ttlogexample.BaseActivity;
import com.example.ttlogexample.R;
import com.example.ttlogexample.databinding.ActivityLockApiBinding;
import com.example.ttlogexample.databinding.ActivityLockTimeBinding;
import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.callback.GetLockTimeCallback;
import com.ttlock.bl.sdk.callback.SetLockTimeCallback;
import com.ttlock.bl.sdk.entity.LockError;

public class LockTimeActivity extends BaseActivity {
ActivityLockTimeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLockTimeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if(mCurrentLock == null){
            makeToast("please choose at least one initialized lock first");
        }
        initListener();
    }

    private void initListener(){
        binding.btnGetTime.setOnClickListener(v -> getLockTime());
        binding.btnSetLockTime.setOnClickListener(v -> setLockTime());
    }

    private void getLockTime(){
        if(!TTLockClient.getDefault().isBLEEnabled(this)){
            /**
             * this operation is asynchronous,so make sure Bluetooth is enabled before call TTLock api.
             */
            TTLockClient.getDefault().requestBleEnable(this);
        }
        makeToast("is query time of lock..");
        TTLockClient.getDefault().getLockTime(mCurrentLock.getLockData(),mCurrentLock.getLockMac(), new GetLockTimeCallback() {
            @Override
            public void onGetLockTimeSuccess(long lockTimestamp) {
                binding.tvLockTime.setText(getResources().getString(R.string.lock_time_is) + getDateFormat(lockTimestamp));
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });

    }

    private void setLockTime(){
        if(!TTLockClient.getDefault().isBLEEnabled(this)){
            /**
             * this operation is asynchronous,so make sure Bluetooth is enabled before call TTLock api.
             */
            TTLockClient.getDefault().requestBleEnable(this);
        }

        makeToast("is correcting lock time..");

        /**
         *  the time you want to correct lock time ,should be get from your server.
         */
        long currentTimeStamp = System.currentTimeMillis();
        TTLockClient.getDefault().setLockTime(currentTimeStamp, mCurrentLock.getLockData(),mCurrentLock.getLockMac(), new SetLockTimeCallback() {
            @Override
            public void onSetTimeSuccess() {
                makeToast("lock time is corrected");
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }



    @Override
    public void onDestroy(){
        super.onDestroy();
        /**
         * BT service should be released before Activity finished.
         */
        TTLockClient.getDefault().stopBTService();
    }
}