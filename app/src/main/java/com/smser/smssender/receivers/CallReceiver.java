package com.smser.smssender.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.smser.smssender.comman.Constants;
import com.smser.smssender.comman.Utilities;
import com.smser.smssender.dbmanager.dbidentifier.MasterCaller;
import com.smser.smssender.definer.MainApp;
import com.smser.smssender.dialogactivity.DeciderActivity;
import com.smser.smssender.model.BlockData;

public class CallReceiver extends BroadcastReceiver implements Constants {

    private Context context;
//    private boolean startedCall;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
//        startedCall = false; // New added boolean
        TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

//        try {
//
//            if (intent.getAction() != null && intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
//                String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
//                Log.e("PHONE RECEIVER", "Outgoing call " + number);
//
//                conditionChecker(context, number);
//
//            } else {
//                String newPhoneState = intent.hasExtra(TelephonyManager.EXTRA_STATE) ?
//                        intent.getStringExtra(TelephonyManager.EXTRA_STATE) : null;
//                Bundle bundle = intent.getExtras();
//
//                if (newPhoneState != null && newPhoneState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
//
//                    String incomingNumber = bundle != null ? bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER) : "999999999999";
//                    Log.e("incomingNumber", incomingNumber);
//
//                    conditionChecker(context, incomingNumber);
//
//                } else if (newPhoneState != null && newPhoneState.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
//                    String incomingNumber = bundle != null ? bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER) : "999999999999";
//                    Log.e("incomingNumber", incomingNumber);
//                }
//            }
//
//        } catch (Exception e) {
//            Log.e("Telephony receiver", e.toString());
//        }
    }

    private final PhoneStateListener phoneListener = new PhoneStateListener() {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            super.onCallStateChanged(state, incomingNumber);
            try {
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING: {
                        if (incomingNumber != null && !incomingNumber.isEmpty()) {
                            //incoming call
                            Log.e("incomingNumber", incomingNumber);
                            conditionChecker(context, incomingNumber);
                        }
                        break;
                    }
                    case TelephonyManager.CALL_STATE_OFFHOOK: {
//                        startedCall = true; // Newly added code
                        if (incomingNumber != null && !incomingNumber.isEmpty()) {
                            //outgoing call
                            Log.e("Outgoing call", incomingNumber);
                            conditionChecker(context, incomingNumber);
                        }
                        break;
                    }
//                    case TelephonyManager.CALL_STATE_IDLE: {
//                        if (startedCall) {
//                        }
//                        break;
//                    }
                    default: {
                    }
                }
            } catch (Exception ex) {
                Log.e("PhoneStateListener", ex.toString());
            }
        }
    };

    private void conditionChecker(Context context, String number) {

        if (MainApp.getValue(REGISTER).equals(ENABLE) && !Utilities.dateChecker(MainApp.getValue(EXPIRY))) {

            BlockData data = MasterCaller.getBlockData(number);

            if (data == null) {
                callSmsSender(context, number);
            } else {
                if (!MainApp.getValue(MSGLIMITER).equals(DISABLE)) {

                    int count = MasterCaller.getSentData(number);

                    int limitCount = Utilities.numberConverter(MainApp.getValue(CALLLIMIT));

                    if (limitCount == 0) {
                        limitCount = 100;
                    }

                    if (count == 0 || count > limitCount) {
                        callSmsSender(context, number);
                        MasterCaller.updateSentData(number);
                    }
                }
            }

            MasterCaller.insertUpdateSentData(number);
        }
    }

    private void callSmsSender(Context context, String number) {

        Intent intent = new Intent(context, DeciderActivity.class);
        intent.putExtra("number", number);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

}
