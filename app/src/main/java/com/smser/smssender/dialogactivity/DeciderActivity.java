package com.smser.smssender.dialogactivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.smser.smssender.R;
import com.smser.smssender.comman.Constants;
import com.smser.smssender.service.CallerService;

public class DeciderActivity extends AppCompatActivity implements Constants {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent data = getIntent();

        if (data != null) {

            String number = data.getStringExtra("number");
            String callType = data.getStringExtra("callType");

            askPermission(this, number, callType);
        }
    }

    private void askPermission(final Context context, final String number, final String callType) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(context.getString(R.string.permission_dialog));

        builder.setPositiveButton(context.getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                smsMethodChecker(number, context);

                Intent checker = new Intent(DeciderActivity.this, CallerService.PermissionChecker.class);
                checker.setAction(PERMISSION_STATUS);
                checker.putExtra("status", "grant");
                checker.putExtra("number", number);
                checker.putExtra("callType", callType);
                LocalBroadcastManager.getInstance(context).sendBroadcast(checker);
                finish();

            }
        });

        builder.setNegativeButton(context.getString(R.string.btn_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent checker = new Intent(DeciderActivity.this, CallerService.PermissionChecker.class);
                checker.setAction(PERMISSION_STATUS);
                checker.putExtra("status", "reject");
                checker.putExtra("number", number);
                checker.putExtra("callType", callType);
                LocalBroadcastManager.getInstance(context).sendBroadcast(checker);
                finish();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
