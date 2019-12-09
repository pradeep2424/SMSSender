package com.smser.smssender.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.smser.smssender.comman.Constants;
import com.smser.smssender.comman.Utilities;
import com.smser.smssender.definer.MainApp;
import com.smser.smssender.service.CallerService;

import static android.os.Build.VERSION.SDK_INT;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            if (!Utilities.isBackServiceRunning(context)) {

                MainApp.initializeApp(context.getApplicationContext());

                Intent service = new Intent(context, CallerService.class);
                service.setAction(Constants.STARTFOREGROUND_ACTION);
                if (SDK_INT >= 26) {
                    context.startForegroundService(service);
                } else {
                    context.startService(service);
                }
            }

        }
    }
}
