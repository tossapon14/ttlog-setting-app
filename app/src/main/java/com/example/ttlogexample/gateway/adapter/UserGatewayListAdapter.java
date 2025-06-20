package com.example.ttlogexample.gateway.adapter;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ttlogexample.R;
import com.example.ttlogexample.databinding.UserGatewayListItemBinding;
import com.example.ttlogexample.gateway.GatewayDfuActivity;
import com.example.ttlogexample.model.GatewayObj;

import java.util.ArrayList;


;

/**
 * Created on  2019/4/12 0012 14:19
 *
 * @author theodre
 */
public class UserGatewayListAdapter extends  RecyclerView.Adapter<UserGatewayListAdapter.DeviceViewHolder>{

    public ArrayList<GatewayObj> mDataList = new ArrayList<>();

    private Context mContext;
    public UserGatewayListAdapter(Context context){
        mContext = context;
    }

    public void updateData(ArrayList<GatewayObj> gatewayList) {
        if (gatewayList != null) {
            mDataList.clear();
            mDataList.addAll(gatewayList);
            notifyDataSetChanged();
        }
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.user_gateway_list_item, parent, false);
        return new DeviceViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder _holder, int position) {
        final GatewayObj item = mDataList.get(position);
        _holder.Bind(item,position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class DeviceViewHolder extends RecyclerView.ViewHolder {

        UserGatewayListItemBinding itemBinding;

        public DeviceViewHolder(View itemView){
            super(itemView);
            itemBinding = UserGatewayListItemBinding.bind(itemView);
        }

        public void Bind(GatewayObj item,int index){
            itemBinding.tvGatewayName.setText((index+1) +" "+ item.getGatewayMac());
                itemBinding.tvGatewayName.setOnClickListener(view -> {
                    //G2 gateway has dfu function
                    if (item.getGatewayVersion() == 2) {
                        Intent intent = new Intent((Activity) mContext, GatewayDfuActivity.class);
                        intent.putExtra(GatewayObj.class.getName(), item);
                        mContext.startActivity(intent);
                    }
                });
            }
        }
}
