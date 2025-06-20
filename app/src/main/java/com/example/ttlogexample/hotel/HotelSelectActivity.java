package com.example.ttlogexample.hotel;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.ttlogexample.BaseActivity;
import com.example.ttlogexample.MyApplication;
import com.example.ttlogexample.R;
import com.example.ttlogexample.databinding.ActivityHotelSelectBinding;
import com.example.ttlogexample.gateway.dialog.ChooseNetDialog;
import com.example.ttlogexample.model.GatewayObj;
import com.example.ttlogexample.modelNestjs.AuthModel;
import com.example.ttlogexample.modelNestjs.ServerErrorIO;
import com.example.ttlogexample.retrofit.ApiService;
import com.example.ttlogexample.retrofit.RetrofitAPIManager;
import com.google.gson.reflect.TypeToken;
import com.ttlock.bl.sdk.util.GsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class HotelSelectActivity extends BaseActivity {
    ActivityHotelSelectBinding binding;
    private CardViewAdapter cardHotelAdapter;
    public String email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHotelSelectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        this.email = intent.getStringExtra("email");
        this.password = intent.getStringExtra("password");
        getDataHotel();

    }
    public void navigationBack(){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("authAll", true); // Passing the HashMap
        setResult(RESULT_OK, resultIntent);
        finish();
    }
   private void getDataHotel(){
       ApiService apiService = RetrofitAPIManager.provideClientApi();
       Call<String> call = apiService.getHotelNest(mToken.getAccess_token());
       call.enqueue(new Callback<String>() {
           @Override
           public void onResponse(Call<String> call, retrofit2.Response<String> response) {
               if (response.isSuccessful()) {
                   String json = response.body();
                   if (json.contains("list")) {
                       try {
                           JSONObject jsonObject = new JSONObject(json);
                           JSONArray array = jsonObject.getJSONArray("list");
                           ArrayList<String> data = GsonUtil.toObject(array.toString(), new TypeToken<ArrayList<String>>() {
                           });
                           binding.countHotel.setText(String.valueOf(data.size()));
                           updateAdapter(data);
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
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
               }
           }

           @Override
           public void onFailure(Call<String> call, Throwable t) {
               makeToast(t.getMessage());
           }
       });
    }
    private void hotellogIn(String hotel){
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        Call<String> call = apiService.authWithHotelNest(email,password, hotel);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if(response.isSuccessful()){
                    String json = response.body();
                    if(!TextUtils.isEmpty(json)){
                        AuthModel auth = GsonUtil.toObject(json, AuthModel.class);
                        auth.setAccess_token("Bearer "+auth.getAccess_token());
                        MyApplication.getmInstance().setAuthModel(auth);
                        MyApplication.getmInstance().setAuthHotelName(hotel);
                        makeToast("Login Success");
                        Log.d("hotel auth", json);
                        navigationBack();

                    } else {
                        makeToast(response.message());
                        Log.e("auth", response.message());
                    }
                }else{
                    try {
                        String errorBody = response.errorBody().string();
                        ServerErrorIO error = GsonUtil.toObject(errorBody, ServerErrorIO.class); // Convert JSON to ServerErrorIO object
                        Log.d("API", "Error: " + errorBody);
                        makeToast(response.message());
                    } catch (Exception e) {
                        Log.e("API", "Error parsing error response: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                makeToast(t.getMessage());
                Log.e("auth3", t.getMessage());
            }
        });
    }

    private void updateAdapter(ArrayList<String> data) {
        int spanCount = 3; // Number of columns
        int spacing = 16; // Spacing in pixels
        boolean includeEdge = true; // Include spacing at the edges
//        ArrayList<String> data =new ArrayList<String>();
//        data.add("tt hotel");
//        data.add("tor hotel");
//        data.add("kllk hotel");
        cardHotelAdapter = new CardViewAdapter(this,data);
        cardHotelAdapter.setSelectHotel(new CardViewAdapter.OnSelectHotelListener(){
            @Override
            public void click(String hotel) {
                hotellogIn(hotel);
            }
        });
        GridLayoutManager layoutManager =new GridLayoutManager(this,3);

        binding.gridArae.setLayoutManager(layoutManager);
        binding.gridArae.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        binding.gridArae.setAdapter(cardHotelAdapter);

    }
}