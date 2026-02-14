package com.rtmoffat.autosms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * BroadcastReceiver that listens for device boot completion
 * Ensures auto-response settings persist after reboot
 */
public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootReceiver";
    
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && 
            intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d(TAG, "Device booted - Auto SMS settings will be loaded when SMS is received");
            // Settings are stored in SharedPreferences and will be automatically
            // loaded when SmsReceiver processes incoming messages
        }
    }
}
