package com.smser.smssender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.smser.smssender.comman.Constants;
import com.smser.smssender.comman.Utilities;
import com.smser.smssender.definer.MainApp;
import com.smser.smssender.service.CallerService;

import static android.os.Build.VERSION.SDK_INT;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity implements Constants {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        if (MainApp.getValue(APPPOWER).equals("")) {
            MainApp.storeValue(APPPOWER, ENABLE);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, DashBoardActivity.class));
                finish();
            }
        }, 3000);

        if (!Utilities.isBackServiceRunning(this)) {
            Intent service = new Intent(this, CallerService.class);
            service.setAction(Constants.STARTFOREGROUND_ACTION);
            if (SDK_INT >= 26) {
                startForegroundService(service);
            } else {
                startService(service);
            }
        }
    }

}
