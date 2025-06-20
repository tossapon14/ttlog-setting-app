package com.example.ttlogexample.lock;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ttlogexample.BaseActivity;
import com.example.ttlogexample.R;
import com.example.ttlogexample.databinding.ActivityPassageModeBinding;
import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.callback.ClearPassageModeCallback;
import com.ttlock.bl.sdk.callback.DeletePassageModeCallback;
import com.ttlock.bl.sdk.callback.GetPassageModeCallback;
import com.ttlock.bl.sdk.callback.SetPassageModeCallback;
import com.ttlock.bl.sdk.entity.LockError;
import com.ttlock.bl.sdk.entity.PassageModeConfig;
import com.ttlock.bl.sdk.entity.PassageModeType;
import com.ttlock.bl.sdk.util.GsonUtil;

public class PassageModeActivity extends BaseActivity {
   ActivityPassageModeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPassageModeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ensureBluetoothIsEnabled();
        TTLockClient.getDefault().prepareBTService(getApplicationContext());
        initListener();
    }

    private void initListener() {
        binding.btnGet.setOnClickListener(v -> getPassageModeSettingInLock());
        binding.btnSet.setOnClickListener(v -> setPassageMode2Lock());
        binding.btnDelete.setOnClickListener(v -> deleteOnePassageModeSetting());
        binding.btnClear.setOnClickListener(v -> clearAllPassageModeInLock());
    }
    private void getPassageModeSettingInLock(){
        showConnectLockToast();
        TTLockClient.getDefault().getPassageMode(mCurrentLock.getLockData(), mCurrentLock.getLockMac(), new GetPassageModeCallback() {
            @Override
            public void onGetPassageModeSuccess(String passageModeData) {
                makeToast("--get--success--" + passageModeData);
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }

    /**
     * PassageModeConfig params:
     * type    PassageModeType
     * weekly  effective value from 1 to 7,1 means Monday 2 means Tuesday ,...,7 means Sunday
     *  monthly   effective value  ║from 1 to 12
     */
    private void setPassageMode2Lock(){
        PassageModeConfig modeData = new PassageModeConfig();
        modeData.setStartDate(8 * 60);//am: 8:00
        modeData.setEndDate(18 * 60);//pm:  6:00
        modeData.setModeType(PassageModeType.Weekly);
        //if mode is Weekly,then the WeekDays is
        int[] mCircleWeeksArray = {1,2,3,4};
        modeData.setRepeatWeekOrDays(GsonUtil.toJson(mCircleWeeksArray));
        showConnectLockToast();
        TTLockClient.getDefault().setPassageMode(modeData, mCurrentLock.getLockData(), mCurrentLock.getLockMac(), new SetPassageModeCallback() {
            @Override
            public void onSetPassageModeSuccess() {
                makeToast("-set passage mode success-");
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }

    private void deleteOnePassageModeSetting(){
        PassageModeConfig modeData = new PassageModeConfig();
        modeData.setStartDate(8 * 60);//am: 8:00
        modeData.setEndDate(18 * 60);//pm:  6:00
        modeData.setModeType(PassageModeType.Weekly);
        //if mode is Weekly,then the WeekDays is
        int[] mCircleWeeksArray = {2,3,4,5,6};
        modeData.setRepeatWeekOrDays(GsonUtil.toJson(mCircleWeeksArray));
        showConnectLockToast();
        TTLockClient.getDefault().deletePassageMode(modeData, mCurrentLock.getLockData(), mCurrentLock.getLockMac(), new DeletePassageModeCallback() {
            @Override
            public void onDeletePassageModeSuccess() {
                makeToast("-delete passage mode success-");
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }

    private void clearAllPassageModeInLock(){
        showConnectLockToast();
        TTLockClient.getDefault().clearPassageMode(mCurrentLock.getLockData(), mCurrentLock.getLockMac(), new ClearPassageModeCallback() {
            @Override
            public void onClearPassageModeSuccess() {
                makeToast("--all passage mode are cleared success-");
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }
}