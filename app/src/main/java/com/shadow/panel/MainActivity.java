package com.shadow.panel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    static {
        System.loadLibrary("shadowpanel");
    }

    private Switch switchHead, switchESP, switchLine, switchAntiBan;
    private TextView txtStatus;
    private Button btnUpdate;

    // Native methods
    public native void toggleHead(boolean on);
    public native void toggleESP(boolean on);
    public native void toggleLine(boolean on);
    public native void toggleAntiBan(boolean on);
    public native void updateOffsets();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Request overlay permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }

        // Request storage permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        // Initialize views
        switchHead = findViewById(R.id.switch_head);
        switchESP = findViewById(R.id.switch_esp);
        switchLine = findViewById(R.id.switch_line);
        switchAntiBan = findViewById(R.id.switch_antiban);
        txtStatus = findViewById(R.id.txt_status);
        btnUpdate = findViewById(R.id.btn_update);

        // Set listeners
        switchHead.setOnCheckedChangeListener((buttonView, isChecked) -> {
            toggleHead(isChecked);
            updateStatus("Auto Head: " + (isChecked ? "ON" : "OFF"));
        });

        switchESP.setOnCheckedChangeListener((buttonView, isChecked) -> {
            toggleESP(isChecked);
            updateStatus("ESP: " + (isChecked ? "ON" : "OFF"));
        });

        switchLine.setOnCheckedChangeListener((buttonView, isChecked) -> {
            toggleLine(isChecked);
            updateStatus("Line ESP: " + (isChecked ? "ON" : "OFF"));
        });

        switchAntiBan.setOnCheckedChangeListener((buttonView, isChecked) -> {
            toggleAntiBan(isChecked);
            updateStatus("AntiBan: " + (isChecked ? "ON" : "OFF"));
        });

        btnUpdate.setOnClickListener(v -> {
            updateOffsets();
            updateStatus("Offsets Updated!");
        });

        // Start overlay service
        startService(new Intent(this, OverlayService.class));
    }

    private void updateStatus(String msg) {
        runOnUiThread(() -> txtStatus.setText("Status: " + msg));
    }
                                           }
