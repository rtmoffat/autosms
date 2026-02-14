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
        // Verify intent action to prevent improper intent handling
        if (intent == null || intent.getAction() == null) {
            Log.w(TAG, "Received null intent or action");
            return;
        }
        
        if (!Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.w(TAG, "Received unexpected action: " + intent.getAction());
            return;
        }
        
        Log.d(TAG, "Device booted - Auto SMS settings will be loaded when SMS is received");
        // Settings are stored in SharedPreferences and will be automatically
        // loaded when SmsReceiver processes incoming messages
    }
}
