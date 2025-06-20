package com.example.ttlogexample;

import android.annotation.SuppressLint;
import android.app.Application;

import com.example.ttlogexample.model.AccountInfo;
import com.example.ttlogexample.model.Building;
import com.example.ttlogexample.model.LockObj;
import com.example.ttlogexample.modelNestjs.AuthModel;
import com.example.ttlogexample.modelNestjs.LockModel;
import com.example.ttlogexample.modelNestjs.UserModel;
import com.ttlock.bl.sdk.util.GsonUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by Tossapon on 2024/11/14.
 */
public class MyApplication extends Application {
    private static MyApplication mInstance;
    private AccountInfo accountInfo;

    private AuthModel authModel;
    private LockModel mLockObj;
    private ArrayList<Building> building;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static MyApplication getmInstance() {
        return mInstance;
    }

    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }
    public void setAuthHotelName(String hotel) {
        this.authModel.setHotel(hotel);
    }

    public void setAuthModel(AuthModel auth) {
        this.authModel = auth;
    }

    public AuthModel getAuthToken() {
        return this.authModel;
    }

    public void saveChoosedLock(LockModel lockObj) {
        this.mLockObj = lockObj;
    }

    public LockModel getChoosedLock() {
        return this.mLockObj;
    }
    public ArrayList<Building> getBuilding() {
        return this.building;
    }
    public void setBuilding(ArrayList<Building> b) {
        this.building = b;
    }
    @SuppressLint("NewApi")
    public LockObj getDemoLockData() {
        LockObj lock = null;
        try {
            String reader = "{ \"date\": 1732012290000, \"specialValue\": 1699279346, " +
                    "\"lockAlias\": \"MyTestLock_2024/11/19 17:31:27\", \"noKeyPwd\": \"5041756\", " +
                    "\"electricQuantityUpdateDate\": 1732012290000, \"lockMac\": \"33:E5:6E:B4:8D:70\"," +
                    " \"passageMode\": 2, \"timezoneRawOffset\": 25200000, \"lockId\": 18230133, " +
                    "\"featureValue\": \"8C27446548F1F2\", \"electricQuantity\": 5, " +
                    "\"bindDate\": 1732012290000, " +
                    "\"lockData\": \"857EpiK74c0Cu7Xw1vuYzVXqhv/AiIaoBV4egbhN26CYYNhd5ugo4dtrIXxJXeETQmydSqj2wBwiVXt776VwIDbDhD8dmVMG4Rni+1XZhN97dVSIz8Ej1EBd4qoXsjpeX3O2y9QIndnkoR+ipxvwfpVXRORgKUpAAdwgKugADie0HcbIi8Q04ikD5oYHptqoaAzo4qxbF1fISbgMv06oty7cxoh1uz9xa8KDz2ZwfcS/TwVv9MEVllEZJzQLKJjs2i2gSwB9o+m6ru/syhJ7oioFKbJIwnGpij5MIdsTLOw3IZ0TU/6MEE4Vct80qSp9pP+gr/5l1fO+NNriVVsSQhp8blgIJRCrwrcaOv1vkzJdS+pbK2UGeYlBHA4vwnkkNQ/FHRZ4g8Kmk7zkZqIaCRvxjYDAiSQZyEVhpvYotBS5rzYsSIBcpq8+PksfGmrDSBsO6sFa7RZelhhsmORkwuqs6iovo7NHlYEcBFxpFyratIbEgzzNq1HGivJInjC0LUaYsjX4Sz+mqEtwTeRUfDBIGPHuXfOmJWCJcYN9tq3f8FqAhZ4fAa5vx/tpUoKIAtb3MFYNUnrMsDIp97rnu9ST58mKlYMjWKpbgUHUjqzF6gVQ+OIhcKZ7zLmsrEQKRzLDOBo6LuAL4lELzfj2aZZ+TLK+KoiFRD5R71+eXPdRDwbmsm83i9lkg1AhJN50ypeVN5vbHsSEJcMdAbBmct7xCmEOc92hvfN1WSziR9kz5W60jXA=\", \"hasGateway\": 0, \"keyboardPwdVersion\": 4, \"wirelessKeypadFeatureValue\": \"0\", \"lockVersion\": { \"showAdminKbpwdFlag\": true, \"groupId\": 12, \"protocolVersion\": 3, \"protocolType\": 5, \"orgId\": 1, \"logoUrl\": \"\", \"scene\": 2 }, \"lockName\": \"H901_708db4\" }";
            lock = GsonUtil.toObject(reader, LockObj.class);
//            System.out.println("JSON data read from file:");
//            System.out.println(lock.getLockData()); // Pretty-print the JSON
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lock;
    }
}
