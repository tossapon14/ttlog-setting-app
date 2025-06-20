package com.example.ttlogexample.iccard;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ttlogexample.BaseActivity;
import com.example.ttlogexample.MyApplication;
import com.example.ttlogexample.databinding.ActivityMyIccardListBinding;
import com.example.ttlogexample.iccard.model.ICCardListObj;
import com.example.ttlogexample.iccard.model.ICCardObj;
import com.example.ttlogexample.retrofit.ApiService;
import com.example.ttlogexample.retrofit.RetrofitAPIManager;
import com.google.gson.reflect.TypeToken;

import okhttp3.ResponseBody;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.HashMap;

public class MyICCardListActivity extends BaseActivity implements ICCardListAdapter.onListenerClick {
    ActivityMyIccardListBinding binding;
    ICCardListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMyIccardListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }
    void initView(){
        adapter = new ICCardListAdapter(this);
        RecyclerView rv_card = binding.rvCardList;
        rv_card.setAdapter(adapter);
        rv_card.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnListener(this);

    }

    @Override
    public void onClick(ICCardObj cardObj) {
        getICCardList();
    }
    void getICCardList(){
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        HashMap<String,String> param = new HashMap<>(6);
        param.put("clientId",ApiService.CLIENT_ID);
        param.put("accessToken", MyApplication.getmInstance().getAccountInfo().getAccess_token());
        param.put("lockId",String.valueOf(mCurrentLock.getLockId()));
        param.put("pageNo","1");
        param.put("pageSize","1000");
        param.put("date",String.valueOf(System.currentTimeMillis()));
        // TODO @GET("/ v3/ identityCard/ list")
        Call<ResponseBody> call = apiService.getUserICCardList(param);
        RetrofitAPIManager.enqueue(call, new TypeToken<ICCardListObj>(){}, result -> {
            if(!result.success){
                makeToast("--get my fingerprint list fail-" + result.getMsg());
                return;
            }
            Log.d("Card list","===result===" + result.getResult() + "===" + result);
            ICCardListObj icCardListObj = result.getResult();
            ArrayList<ICCardObj> myCardList = icCardListObj.getList();
            if(myCardList.isEmpty()){
                makeToast("- please add IC Card first --");
            }
            adapter.updateData(myCardList);

        }, requestError -> {
            makeToast("--get my fingerprint list fail-" + requestError.getMessage());
        });
    }


}