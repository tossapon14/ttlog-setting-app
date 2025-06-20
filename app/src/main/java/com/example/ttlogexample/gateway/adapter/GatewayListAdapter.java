package com.example.ttlogexample.gateway.adapter;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ttlogexample.R;
import com.example.ttlogexample.databinding.GatewayScanListItemBinding;
import com.example.ttlogexample.gateway.InitGatewayActivity;
import com.ttlock.bl.sdk.api.ExtendedBluetoothDevice;
import com.ttlock.bl.sdk.gateway.api.GatewayClient;
import com.ttlock.bl.sdk.gateway.callback.ConnectCallback;

import java.util.LinkedList;


/**
 * Created on  2019/4/12 0012 14:19
 *
 * @author theodre
 */
// list of Gatewat app take it
public class GatewayListAdapter extends  RecyclerView.Adapter<GatewayListAdapter.DeviceViewHolder>{

    public LinkedList<ExtendedBluetoothDevice> mDataList = new LinkedList<>();

    private Activity mContext;
    private static final int TIMEOUT = 5000;
    private LinkedList<ExtendedBluetoothDevice> mAddStatusList = new LinkedList<>();
    private LinkedList<ExtendedBluetoothDevice> mNormalStatusList = new LinkedList<>();
    private long lastSyncTimeStamp = 0;

    public GatewayListAdapter(Activity context){
        mContext = context;
    }

    public synchronized void updateData(ExtendedBluetoothDevice device){
        if(device != null) {
            if(device.isSettingMode()){
                addOrSortLock(device,mAddStatusList);
                removeOtherStatusLock(device,mNormalStatusList);
            }else {
                addOrSortLock(device,mNormalStatusList);
                removeOtherStatusLock(device,mAddStatusList);
            }

            long currentTime = System.currentTimeMillis();
            if((currentTime - lastSyncTimeStamp) >= 800 ){
                if(!mDataList.isEmpty()){
                    mDataList.clear();
                }

                mDataList.addAll(0,mAddStatusList);
                mDataList.addAll(mNormalStatusList);
                notifyDataSetChanged();
                lastSyncTimeStamp = currentTime;
            }
        }
    }


    /**
     * you can sort the lock that be discovered by signal value.
     */
    private void addOrSortLock(ExtendedBluetoothDevice scanDevice,LinkedList<ExtendedBluetoothDevice> lockList){
        boolean isContained = false;
        int length = lockList.size();
        ExtendedBluetoothDevice mTopOneDevice;
        scanDevice.setDate(System.currentTimeMillis());
        if(length > 0){

            mTopOneDevice = lockList.get(0);

            for(int i = 0;i < length;i++) {
                if(i >= length){
                    break;
                }

                ExtendedBluetoothDevice currentDevice = lockList.get(i);

                if(scanDevice.getAddress().equals(currentDevice.getAddress()) ){
                    isContained = true;
                    if(i != 0 && scanDevice.getRssi() > mTopOneDevice.getRssi()){
                        lockList.remove(i);
                        lockList.add(0,scanDevice);
                    }else {
                        currentDevice.setDate(System.currentTimeMillis());
                        lockList.set(i,currentDevice);
                    }
                }else {
                    if(System.currentTimeMillis() - currentDevice.getDate() >= TIMEOUT) {
                        lockList.remove(i);
                        length = lockList.size();
                    }
                }
            }

            if(!isContained){
                if(scanDevice.getRssi() > mTopOneDevice.getRssi()){
                    lockList.add(0,scanDevice);
                }else {
                    lockList.add(scanDevice);
                }
            }

        }else {
            lockList.add(scanDevice);
        }

    }

    /**
     * the lock mode will be changed,so should update the list when lock mode changed.
     * @param scanDevice the lock that be discovered.
     */
    private void removeOtherStatusLock(ExtendedBluetoothDevice scanDevice,LinkedList<ExtendedBluetoothDevice> lockList){
        if(!lockList.isEmpty()){
            int length = lockList.size();
            for(int i = 0; i < length ; i++){
                ExtendedBluetoothDevice device = lockList.get(i);
                if(device.getAddress().equals(scanDevice.getAddress())){
                    lockList.remove(i);
                    length --;
                }else {
                    if(System.currentTimeMillis() - device.getDate() >= TIMEOUT) {
                        lockList.remove(i);
                        length --;
                    }
                }
            }
        }
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.gateway_scan_list_item, parent, false);
        return new DeviceViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder _holder, int position) {
        final ExtendedBluetoothDevice item = mDataList.get(position);
        _holder.Bind(item);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class DeviceViewHolder extends RecyclerView.ViewHolder {

        GatewayScanListItemBinding itemBinding;


        public DeviceViewHolder(View itemView){
            super(itemView);
            itemBinding = GatewayScanListItemBinding.bind(itemView);
        }


        public void Bind(ExtendedBluetoothDevice item){
            itemBinding.tvGatewayName.setText(item.getName());

            itemBinding.ivSettingMode.setOnClickListener(view -> {
                Log.d("sys","gateway click --  "+ExtendedBluetoothDevice.class.getName());
                Toast.makeText(mContext,"--connect gateway--",Toast.LENGTH_LONG).show();
                GatewayClient.getDefault().connectGateway(item, new ConnectCallback() {
                    @Override
                    public void onConnectSuccess(ExtendedBluetoothDevice device) {
                        Log.d("sys","connect success");
                       // InitGatewayActivity.launch(mContext, item); // call wifi init
                        Intent intent = new Intent(mContext, InitGatewayActivity.class);
                        intent.putExtra(ExtendedBluetoothDevice.class.getName(), device);
                        mContext.startActivity(intent);
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("gateway", true); // Passing the HashMap
                        mContext.setResult(RESULT_OK, resultIntent);
                        mContext.finish();
                    }

                    @Override
                    public void onDisconnected() {
                        Toast.makeText(mContext, "Timeout. The gateway is out of setting mode", Toast.LENGTH_LONG).show();
                        Log.e("sys","Timeout. The gateway is out of setting mode");
                    }

                });
            });
        }

    }

}
