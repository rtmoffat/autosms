package com.rtmoffat.autosms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * BroadcastReceiver that listens for incoming SMS messages
 * Works regardless of device lock state
 */
public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsReceiver";
    private static final String PREFS_NAME = "AutoSMSPrefs";
    
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == null || 
            !intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            return;
        }

        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return;
        }

        try {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus == null || pdus.length == 0) {
                return;
            }

            String format = bundle.getString("format");
            
            for (Object pdu : pdus) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu, format);
                
                String sender = smsMessage.getDisplayOriginatingAddress();
                String messageBody = smsMessage.getMessageBody();
                
                Log.d(TAG, "SMS received from: " + sender + ", Message: " + messageBody);
                
                // Check if auto-response is enabled
                SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                boolean isEnabled = prefs.getBoolean("auto_response_enabled", false);
                
                if (!isEnabled) {
                    Log.d(TAG, "Auto-response is disabled");
                    continue;
                }
                
                // Get trigger keyword and response message
                String triggerKeyword = prefs.getString("trigger_keyword", "");
                String responseMessage = prefs.getString("response_message", "");
                
                if (triggerKeyword.isEmpty() || responseMessage.isEmpty()) {
                    Log.d(TAG, "Trigger keyword or response message is not set");
                    continue;
                }
                
                // Check if message contains the trigger keyword (case-insensitive)
                if (messageBody != null && 
                    messageBody.toLowerCase().contains(triggerKeyword.toLowerCase())) {
                    Log.d(TAG, "Trigger keyword found! Sending auto-response...");
                    sendSms(sender, responseMessage);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error processing SMS: " + e.getMessage(), e);
        }
    }
    
    /**
     * Send SMS message
     */
    private void sendSms(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Log.d(TAG, "Auto-response sent to: " + phoneNumber);
        } catch (Exception e) {
            Log.e(TAG, "Failed to send SMS: " + e.getMessage(), e);
        }
    }
}
