package com.rtmoffat.autosms;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Main activity for configuring auto-response settings
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String PREFS_NAME = "AutoSMSPrefs";
    private static final int PERMISSION_REQUEST_CODE = 123;
    
    private Switch switchEnabled;
    private EditText editTriggerKeyword;
    private EditText editResponseMessage;
    private Button btnSave;
    
    private SharedPreferences prefs;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        
        // Initialize UI components
        switchEnabled = findViewById(R.id.switch_enabled);
        editTriggerKeyword = findViewById(R.id.edit_trigger_keyword);
        editResponseMessage = findViewById(R.id.edit_response_message);
        btnSave = findViewById(R.id.btn_save);
        
        // Load saved settings
        loadSettings();
        
        // Set up save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });
        
        // Request permissions
        requestSmsPermissions();
    }
    
    /**
     * Load settings from SharedPreferences
     */
    private void loadSettings() {
        boolean isEnabled = prefs.getBoolean("auto_response_enabled", false);
        String triggerKeyword = prefs.getString("trigger_keyword", "");
        String responseMessage = prefs.getString("response_message", "");
        
        switchEnabled.setChecked(isEnabled);
        editTriggerKeyword.setText(triggerKeyword);
        editResponseMessage.setText(responseMessage);
    }
    
    /**
     * Save settings to SharedPreferences
     */
    private void saveSettings() {
        String triggerKeyword = editTriggerKeyword.getText().toString().trim();
        String responseMessage = editResponseMessage.getText().toString().trim();
        
        if (switchEnabled.isChecked() && (triggerKeyword.isEmpty() || responseMessage.isEmpty())) {
            Toast.makeText(this, "Please enter both trigger keyword and response message", 
                          Toast.LENGTH_SHORT).show();
            return;
        }
        
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("auto_response_enabled", switchEnabled.isChecked());
        editor.putString("trigger_keyword", triggerKeyword);
        editor.putString("response_message", responseMessage);
        editor.apply();
        
        Toast.makeText(this, "Settings saved successfully!", Toast.LENGTH_SHORT).show();
    }
    
    /**
     * Request SMS permissions
     */
    private void requestSmsPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean hasReceiveSms = ContextCompat.checkSelfPermission(this, 
                Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
            boolean hasReadSms = ContextCompat.checkSelfPermission(this, 
                Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
            boolean hasSendSms = ContextCompat.checkSelfPermission(this, 
                Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
            
            if (!hasReceiveSms || !hasReadSms || !hasSendSms) {
                ActivityCompat.requestPermissions(this,
                    new String[]{
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.SEND_SMS
                    },
                    PERMISSION_REQUEST_CODE);
            }
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, 
                                          @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            
            if (allGranted) {
                Toast.makeText(this, "Permissions granted! You can now use auto-response.", 
                              Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permissions are required for auto-response to work.", 
                              Toast.LENGTH_LONG).show();
            }
        }
    }
}
