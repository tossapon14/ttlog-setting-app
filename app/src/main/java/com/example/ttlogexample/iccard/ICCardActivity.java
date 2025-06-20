package com.example.ttlogexample.iccard;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import com.example.ttlogexample.BaseActivity;
import com.example.ttlogexample.MyApplication;
import com.example.ttlogexample.databinding.ActivityIccardBinding;
import com.example.ttlogexample.modelNestjs.LockModel;
import com.example.ttlogexample.modelNestjs.ServerErrorIO;
import com.example.ttlogexample.retrofit.ApiService;
import com.example.ttlogexample.retrofit.RetrofitAPIManager;
import com.google.gson.reflect.TypeToken;
import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.callback.AddICCardCallback;
import com.ttlock.bl.sdk.callback.GetAllValidICCardCallback;
import com.ttlock.bl.sdk.callback.ClearAllICCardCallback;
import com.ttlock.bl.sdk.entity.LockError;
import com.ttlock.bl.sdk.util.GsonUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ICCardActivity extends BaseActivity {
    ActivityIccardBinding binding;
    final int ADD_PERMANENT = 1;
    final int ADD_TIMED = 2;
    long addStartDate = 0;
    long addEndDate = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityIccardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initListener();
    }
    private void initListener(){
        binding.btnMyList.setOnClickListener(v -> startTargetActivity(MyICCardListActivity.class));
        binding.btnAddPermanent.setOnClickListener(v -> addICCard(ADD_PERMANENT));
        binding.btnAddTimed.setOnClickListener(v -> addICCard(ADD_TIMED)); // add by limit timer
        binding.btnGetAllCards.setOnClickListener(v -> getAllCards());
        binding.btnClearCard.setOnClickListener(v -> clearAllCards());

    }
    private void addICCard(int type){

        ensureBluetoothIsEnabled();
        showConnectLockToast();
        switch (type){
            case ADD_PERMANENT:
                addStartDate = 0;
                addEndDate = 0;
                break;
            case ADD_TIMED:
                addStartDate = System.currentTimeMillis();
                //means 2 minutes later this card will expired.
                addEndDate = addStartDate + 2 * 60 * 1000;
                break;
            default:
                break;
        }


        TTLockClient.getDefault().addICCard(addStartDate, addEndDate, mCurrentLock.getLockData(), mCurrentLock.getLockMac(), new AddICCardCallback() {
            @Override
            public void onEnterAddMode() {
                makeToast("-you can put ic card on lock now-");
                Log.d("add card","-you can put ic card on lock now-");
            }
            @Override
            public void onAddICCardSuccess(long cardNum) {
                makeToast("card is added to lock  " + cardNum);
                Log.d("add card","card is added to lock " + cardNum);
                uploadICCard2Server(addStartDate,addEndDate,cardNum);
            }

            @Override
            public void onFail(LockError error) {
                Log.e("add card",error.getErrorMsg());
                makeErrorToast(error);
            }
        });
    }

    private void uploadICCard2Server(long startDate,long endDate,long cardNumber){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm");
        java.util.Date date = new java.util.Date(System.currentTimeMillis());
        String readableDate = sdf.format(date);
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        HashMap<String,String> params = new HashMap<>(8);
        params.put("lockId",String.valueOf(mCurrentLock.getLockId()));
        params.put("cardNumber",String.valueOf(cardNumber));
        params.put("cardName","mobile add " + readableDate);
        if(startDate > 0 && endDate > 0){
            params.put("startDate",String.valueOf(startDate));
            params.put("endDate",String.valueOf(endDate));
        }
        params.put("date",String.valueOf(System.currentTimeMillis()));

        Call<String> call = apiService.addICCardNest(mToken.getAccess_token(),params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful()) {
                    try {  // Handle success
                        String json = response.body();
                        if (!json.isEmpty()) {
                            makeToast("added Success to server" );
                        }
                    }catch (Exception e) {
                        Log.e("API", "Error parsing: " + e.getMessage());
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        ServerErrorIO error = GsonUtil.toObject(errorBody, ServerErrorIO.class); // Convert JSON to ServerErrorIO object
                        Log.d("API", "Error: " + errorBody);
                        makeToast("Error: " + error.getErrmsg());
                    } catch (Exception e) {
                        Log.e("API", "Error parsing: " + e.getMessage());
                    }
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                makeToast(t.getMessage());
            }
        });
    }



    private void getAllCards(){
        showConnectLockToast();
        TTLockClient.getDefault().getAllValidICCards(mCurrentLock.getLockData(), mCurrentLock.getLockMac(), new GetAllValidICCardCallback() {
            @Override
            public void onGetAllValidICCardSuccess(String cardDataStr) {
                makeToast("-all ic cards info " + cardDataStr);
                Log.d("GetCard","-all ic cards info " + cardDataStr);
            }
            @Override
            public void onFail(LockError error) {
                Log.e("GetAllCard",error.getErrorMsg());
                makeErrorToast(error);
            }
        });
    }

    private void clearAllCards(){
        showConnectLockToast();
        TTLockClient.getDefault().clearAllICCard(mCurrentLock.getLockData(), mCurrentLock.getLockMac(), new ClearAllICCardCallback() {
            @Override
            public void onClearAllICCardSuccess() {
                uploadClear2Server();
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }

    private void uploadClear2Server(){
        ApiService apiService = RetrofitAPIManager.provideClientApi();
         // TODO @POST("/ v3/ identityCard/ clear")
        Call<String> call = apiService.clearICCardsNest(mToken.getAccess_token(), mCurrentLock.getLockId(),System.currentTimeMillis());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful()) {
                    try {  // Handle success
                        String json = response.body();
                        if (!json.isEmpty()) {
                            makeToast("-clear all card success" );
                        }
                    }catch (Exception e) {
                        Log.e("API", "Error parsing: " + e.getMessage());
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