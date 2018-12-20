package com.liul.trc_study_task.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getAction();
        switch (action){
            case Intent.ACTION_PACKAGE_REPLACED:
            case Intent.ACTION_BOOT_COMPLETED:
                setBluetoothDisable();
                setWifiDisable(context);
                break;
            case BluetoothAdapter.ACTION_STATE_CHANGED:
                setBluetoothDisable();
                break;
            case WifiManager.WIFI_STATE_CHANGED_ACTION:
                setWifiDisable(context);
                break;
            default:
                break;
        }
    }

    private void setBluetoothDisable(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter.getState()==BluetoothAdapter.STATE_ON)
            mBluetoothAdapter.disable();
    }

    private void setWifiDisable(Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if(wifiManager.isWifiEnabled())
            wifiManager.setWifiEnabled(false);
    }
}
