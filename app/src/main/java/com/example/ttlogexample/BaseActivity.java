package com.example.ttlogexample;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.ttlogexample.model.LockObj;
import com.example.ttlogexample.modelNestjs.AuthModel;
import com.example.ttlogexample.modelNestjs.LockModel;
import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.entity.LockError;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    public final static String LOCK_OBJ = "lock_obj_data";
    public LockModel mCurrentLock;
    public AuthModel  mToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        mCurrentLock  = MyApplication.getmInstance().getChoosedLock();
        mToken = MyApplication.getmInstance().getAuthToken();
    }



    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    public void startTargetActivity(Class targetClass) {
        Intent mIntent = new Intent(getApplicationContext(), targetClass);
        startActivity(mIntent);
    }

    public void makeToast(String content){
        Toast.makeText(this,content, Toast.LENGTH_LONG).show();
    }

    public void makeErrorToast(LockError error){
        Toast.makeText(this,error.getDescription(),Toast.LENGTH_LONG).show();
    }

//    public void makeErrorToast(KeypadError error){
//        Toast.makeText(this,error.getDescription(),Toast.LENGTH_LONG).show();
//    }


    /**
     * make sure Bluetooth is enabled.
     */
    public void ensureBluetoothIsEnabled(){
        if(!TTLockClient.getDefault().isBLEEnabled(this)){
            TTLockClient.getDefault().requestBleEnable(this);
        }
    }

    public String getDateFormat(long timestamp) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
        return sDateFormat.format(new Date(timestamp));
    }

    public void showConnectLockToast(){
        makeToast("start connect lock...");
    }
}

