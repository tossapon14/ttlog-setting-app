package com.example.ttlogexample.lock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ttlogexample.BaseActivity;
import com.example.ttlogexample.MyApplication;
import com.example.ttlogexample.R;
import com.example.ttlogexample.databinding.UserLockListItemBinding;
import com.example.ttlogexample.lock.LockApiActivity;
import com.example.ttlogexample.modelNestjs.LockModel;

import java.util.ArrayList;

public class UserLockListAdapter extends  RecyclerView.Adapter<UserLockListAdapter.DeviceViewHolder>{

    public ArrayList<LockModel> mDataList = new ArrayList<>();

    private Context mContext;
    public UserLockListAdapter(Context context){
        mContext = context;
    }

    public void updateData(ArrayList<LockModel> lockList) {
        if (lockList != null) {
            mDataList.clear();
            mDataList.addAll(lockList);
            notifyDataSetChanged();
        }
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.user_lock_list_item, parent, false);
        return new DeviceViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder _holder, int position) {
        final LockModel item = mDataList.get(position);
        _holder.Bind(item,position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class DeviceViewHolder extends RecyclerView.ViewHolder {

        UserLockListItemBinding itemBinding;

        public DeviceViewHolder(View itemView){
            super(itemView);
            itemBinding = UserLockListItemBinding.bind(itemView);
        }

        public void Bind(LockModel item,int index){
            itemBinding.tvIndex.setText(index+1+"  ");
            itemBinding.tvLockName.setText( item.getHotel()+" room  "+item.getRoomNumber());
            itemBinding.getRoot().setOnClickListener(view -> {
                MyApplication.getmInstance().saveChoosedLock(item);
                ((BaseActivity) mContext).startTargetActivity(LockApiActivity.class);
            });
        }
    }
}

