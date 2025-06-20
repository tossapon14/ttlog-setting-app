package com.example.ttlogexample.lock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.example.ttlogexample.BaseActivity;
import com.example.ttlogexample.MyApplication;
import com.example.ttlogexample.R;
import com.example.ttlogexample.databinding.ActivityFormInitLockBinding;
import com.example.ttlogexample.lock.adapter.SpinnerAdapter;
import com.example.ttlogexample.model.Building;
import com.example.ttlogexample.modelNestjs.ServerErrorIO;
import com.example.ttlogexample.retrofit.ApiService;
import com.example.ttlogexample.retrofit.RetrofitAPIManager;
import com.google.gson.reflect.TypeToken;
import com.ttlock.bl.sdk.util.GsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.view.inputmethod.InputMethodManager;

import retrofit2.Call;
import retrofit2.Callback;

public class FormInitLockActivity extends BaseActivity {
    private ActivityFormInitLockBinding binding;
    final String[] buildingData = new String[3];
    final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFormInitLockBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getdataHotel();
    }

    void getdataHotel() {
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        Call<String> call = apiService.getUserBuildingNest(mToken.getAccess_token());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful()) {
                    String json = response.body();
                    if (json.contains("list")) {
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            JSONArray array = jsonObject.getJSONArray("list");
                            ArrayList<Building> building = GsonUtil.toObject(array.toString(), new TypeToken<ArrayList<Building>>() {
                            });
                            MyApplication.getmInstance().setBuilding(building);
                            Log.d("ddddd", building.toString());
                            initListener();
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

    void setSpinner(@NonNull Spinner binding, List<String> data, int icon) {
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this, R.layout.custom_spinner_adapter, data, icon);
        binding.setAdapter(spinnerAdapter);
    }

    void initListener() {

        Intent intent = getIntent();
        String hotel = intent.getStringExtra("hotel");
        binding.hotelName.setText(hotel);


        List<String> bdata = new ArrayList<>();
        bdata.add("select building");
        for (Building b : MyApplication.getmInstance().getBuilding()) {
            bdata.add(b.getBuildingName());
        }
        setSpinner(binding.spinnerBuilding, bdata, R.drawable.baseline_home_work_24);
        binding.spinnerBuilding.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item
                if (position == 0) {
                    buildingData[0] = "";
                    buildingData[1] = "";
                    buildingData[2] = "";
                    binding.spinnerFloor.setAdapter(null);
                    return;
                }

                String selected = (String) parent.getItemAtPosition(position);
                buildingData[0] = selected;
                binding.error1.setVisibility(View.GONE);
                // Perform action based on the selected item
                Toast.makeText(getApplicationContext(),
                        "Selected: " + selected,
                        Toast.LENGTH_SHORT).show();
                //======================================================================
                List<String> fdata = new ArrayList<>();
                fdata.add("select floor");
                for (Building b : MyApplication.getmInstance().getBuilding()) {
                    if (b.getBuildingName() == selected) {
                        fdata.addAll(b.getFloor());
                        break;
                    }
                }
                setSpinner(binding.spinnerFloor, fdata, R.drawable.baseline_layers_24);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection
                Toast.makeText(getApplicationContext(), "Nothing Selected", Toast.LENGTH_SHORT).show();
            }
        });
        binding.spinnerFloor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item
                if (position == 0) {
                    buildingData[1] = "";
                    buildingData[2] = "";
                    binding.spinnerRoom.setAdapter(null);
                    return;
                }
                String selected = (String) parent.getItemAtPosition(position);
                buildingData[1] = selected;
                binding.error2.setVisibility(View.GONE);


                Toast.makeText(getApplicationContext(),
                        "floor: " + selected,
                        Toast.LENGTH_SHORT).show();
                //======================================================================
                List<String> rdata = new ArrayList<>();
                rdata.add("select room");
                for (Building b : MyApplication.getmInstance().getBuilding()) {
                    if (b.getBuildingName() == buildingData[0]) {
                        int index = b.getFloor().indexOf(selected);
                        rdata.addAll(b.getRoom().get(index));
                        break;
                    }
                }
                setSpinner(binding.spinnerRoom, rdata, R.drawable.round_bedroom_parent_24);
                //======================================================================
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection
                Toast.makeText(getApplicationContext(), "Nothing Selected", Toast.LENGTH_SHORT).show();
            }
        });
        binding.spinnerRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item
                if (position == 0) {
                    return;
                }
                String selected = (String) parent.getItemAtPosition(position);
                buildingData[2] = selected;
                binding.error3.setVisibility(View.GONE);


                Toast.makeText(getApplicationContext(),
                        "room: " + selected,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection
                Toast.makeText(getApplicationContext(), "Nothing Selected", Toast.LENGTH_SHORT).show();
            }
        });


        binding.lockSubmit.setOnClickListener(v -> {
            for (int i = 0; i < buildingData.length; i++) {
                if (buildingData[i].isEmpty()) {
                    if (i == 0) {
                        binding.error1.setVisibility(View.VISIBLE);
                        continue;
                    } else if (i == 1) {
                        binding.error2.setVisibility(View.VISIBLE);
                        continue;
                    } else if (i == 2) {
                        binding.error3.setVisibility(View.VISIBLE);
                    }
                    return;
                }
            }
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }


            String alias = "";
            if (Objects.requireNonNull(binding.tfLockAlias.getText()).toString().isBlank()) {
                Date date = new Date();
                String formattedDate = formatter.format(date);
                alias = "initLock: " + formattedDate;
            }else {
                alias = Objects.requireNonNull(binding.tfLockAlias.getText()).toString();
            }

            HashMap<String, Object> data = new HashMap<>();
            data.put("building", buildingData[0]);
            data.put("floor", buildingData[1]);
            data.put("room", buildingData[2]);
            data.put("alias", alias);
            Log.d("form",data.toString());
            Intent resultIntent = new Intent();
            resultIntent.putExtra("formSubmitted", data); // Passing the HashMap
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}