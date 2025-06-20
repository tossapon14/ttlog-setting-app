package com.example.ttlogexample.hotel;
import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ttlogexample.MyApplication;
import com.example.ttlogexample.R;
import com.example.ttlogexample.modelNestjs.AuthModel;
import com.example.ttlogexample.modelNestjs.ServerErrorIO;
import com.example.ttlogexample.retrofit.ApiService;
import com.example.ttlogexample.retrofit.RetrofitAPIManager;
import com.ttlock.bl.sdk.util.GsonUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.CardHotelViewHolder> {

    private ArrayList<String> dataArrayList;
    private Context mcontext;

    private OnSelectHotelListener selectHotel;


    public CardViewAdapter(Context context, ArrayList<String> hotel) {
        this.dataArrayList = hotel;
        this.mcontext = context;

    }
    public interface OnSelectHotelListener{
       void click(String hotel);
    }
    public void setSelectHotel(OnSelectHotelListener s){
        selectHotel = s;
    }
    @NonNull
    @Override
    public CardHotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(mcontext).inflate(R.layout.hotel_card, parent, false);
        return new CardHotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardHotelViewHolder holder, int position) {
        // Set the data to textview and imageview.
        String hotelStr = dataArrayList.get(position);
        holder.hotelText.setText(hotelStr);
        holder.hotelSelect.setOnClickListener(v->{
            selectHotel.click(hotelStr);
        });
    }



    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return dataArrayList.size();
    }

    // View Holder Class to handle Recycler View.
    public class CardHotelViewHolder extends RecyclerView.ViewHolder {

        private TextView hotelText;
        private CardView hotelSelect;
        public CardHotelViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelText = itemView.findViewById(R.id.hotel_text);
            hotelSelect = itemView.findViewById(R.id.hotel_select);
        }
    }
}
