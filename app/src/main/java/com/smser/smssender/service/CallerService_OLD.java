//package com.smser.smssender.service;
//
//import android.app.AlarmManager;
//import android.app.IntentService;
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
//import android.content.ComponentName;
//import android.content.ContentResolver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.os.SystemClock;
//import android.provider.ContactsContract;
//import android.telephony.PhoneNumberUtils;
//import android.telephony.PhoneStateListener;
//import android.telephony.SmsManager;
//import android.telephony.TelephonyManager;
//import android.util.Log;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.annotation.RequiresApi;
//import androidx.core.app.NotificationCompat;
//import androidx.core.content.FileProvider;
//import androidx.localbroadcastmanager.content.LocalBroadcastManager;
//
//import com.smser.smssender.BuildConfig;
//import com.smser.smssender.R;
//import com.smser.smssender.comman.Constants;
//import com.smser.smssender.comman.Utilities;
//import com.smser.smssender.dbmanager.dbidentifier.MasterCaller;
//import com.smser.smssender.definer.MainApp;
//import com.smser.smssender.dialogactivity.DeciderActivity;
//import com.smser.smssender.model.BlockData;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class CallerService_OLD extends IntentService implements Constants {
//
//    private static final String LOG_TAG = "CallerService";
//    private static Timer timer = null;
//    private CallChecker receiver;
//    private PermissionChecker permissionReceiver;
//
//    public CallerService_OLD() {
//        super("CallerService");
//    }
//
//    public class PermissionChecker extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getExtras() != null) {
//                Bundle data = intent.getExtras();
//
//                if (data != null && data.getString("status") != null && data.getString("status").equals("grant")) {
//                    smsMethodChecker(data.getString("number"), context, data.getString("callType"));
//                }
//            }
//        }
//    }
//
//    public class CallChecker extends BroadcastReceiver {
//
//        private Context context;
//        private boolean callReceived = false;
//        private boolean ring = false;
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            this.context = context;
//            Utilities.resetCount();
//            if (MainApp.getValue(APPPOWER).equals(ENABLE)) {
//                TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//                telManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
//            }
//        }
//
//        private final PhoneStateListener phoneListener = new PhoneStateListener() {
//
//            @Override
//            public void onCallStateChanged(int state, String incomingNumber) {
//
//                super.onCallStateChanged(state, incomingNumber);
//                try {
//                    switch (state) {
//                        case TelephonyManager.CALL_STATE_RINGING: {
//
//                            ring = true;
//
//                            if (incomingNumber != null && !incomingNumber.isEmpty()) {
//                                //incoming call
//                                Log.e("incomingNumber", incomingNumber);
//                                conditionChecker(context, incomingNumber, INCOMMING);
//                            }
//                            break;
//                        }
//                        case TelephonyManager.CALL_STATE_OFFHOOK: {
//
//                            callReceived = true;
//
//                            if (incomingNumber != null && !incomingNumber.isEmpty()) {
//                                //outgoing call
//                                Log.e("Outgoing call", incomingNumber);
//                                conditionChecker(context, incomingNumber, OUTGOING);
//                            }
//                            break;
//                        }
//                        case TelephonyManager.CALL_STATE_IDLE: {
//
//                            if (ring && !callReceived) {
//                                //missed called
//                                conditionChecker(context, incomingNumber, MISSEDCALL);
//                            }
//                            break;
//                        }
//                        default: {
//                        }
//                    }
//                } catch (Exception ex) {
//                    Log.e("PhoneStateListener", ex.toString());
//                }
//            }
//        };
//
//        private void conditionChecker(Context context, String number, String callType) {
//
//            if (MainApp.getValue(REGISTER).equals(ENABLE) && Utilities.dateChecker(MainApp.getValue(EXPIRY))) {
//
//                BlockData data = MasterCaller.getBlockData(number);
//
//                if (data == null) {
//                    if (!MainApp.getValue(MSGLIMITER).equals(DISABLE)) {
//
//                        int count = MasterCaller.getSentData(number);
//
//                        int limitCount = Utilities.numberConverter(MainApp.getValue(CALLLIMIT));
//
//                        if (limitCount == 0) {
//                            limitCount = 100;
//                        }
//
//                        if (count == 0 || count > limitCount) {
//                            callSmsSender(context, number, callType);
//                            MasterCaller.updateSentData(number);
//                        }
//                    } else {
//                        callSmsSender(context, number, callType);
//                    }
//                }
//
//                MasterCaller.insertUpdateSentData(number);
//            }
//        }
//
//        private void callSmsSender(Context context, String number, String callType) {
//
//            if (MainApp.getValue(PERMISSION).equals(ENABLE)) {
//                Intent intent = new Intent(context, DeciderActivity.class);
//                intent.putExtra("number", number);
//                intent.putExtra("callType", callType);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//            } else {
//                smsMethodChecker(number, context, callType);
//            }
//
//        }
//    }
//
////    public void openWhatsApp(Context context, String toNumber) {
////        try {
////
////            String number;
////
////            if (toNumber.length() > 10) {
////                number = toNumber.substring(toNumber.length() - 10);
////            } else {
////                number = toNumber;
////            }
////
////            MainApp.storeValue(WHATSTOTAL, "" + (1 + Utilities.numberConverter(MainApp.getValue(WHATSTOTAL))));
////            MainApp.storeValue(WHATSDAILYTOTAL, "" + (1 + Utilities.numberConverter(MainApp.getValue(WHATSDAILYTOTAL))));
////
////            Intent sendIntent = new Intent("android.intent.action.SEND");
//////            File f=new File("path to the file");
//////            Uri uri = Uri.fromFile(f);
////
////            Uri uri =  Uri.parse( "https://homepages.cae.wisc.edu/~ece533/images/airplane.png" );
////            String whatsAppMsg = MainApp.getValue(WHATSAPPSWITCH);
////
////            sendIntent.setComponent(new ComponentName("com.whatsapp","com.whatsapp.ContactPicker"));
////            sendIntent.setType("image");
////            sendIntent.putExtra(Intent.EXTRA_STREAM,uri);
////            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(number)+"@s.whatsapp.net");
////            sendIntent.putExtra(Intent.EXTRA_TEXT,whatsAppMsg);
////            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////            startActivity(sendIntent);
////
//////            String whatsAppMsg = "http://api.whatsapp.com/send?phone=+91" + number + "&text=" + MainApp.getValue(WHATSAPPSWITCH);
//////
//////
//////            Intent intent = new Intent(Intent.ACTION_VIEW);
//////            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//////            intent.setData(Uri.parse(whatsAppMsg));
//////            context.startActivity(intent);
////        } catch (Exception e) {
////            Log.e("openWhatsApp", e.toString());
////        }
////
////        Log.e("openWhatsApp", "sent");
////    }
//
//
////    public void openWhatsApp(Context context, String toNumber) {
////
////        try {
////
////            String number;
////
////            if (toNumber.length() > 10) {
////                number = toNumber.substring(toNumber.length() - 10);
////            } else {
////                number = toNumber;
////            }
////
////            MainApp.storeValue(WHATSTOTAL, "" + (1 + Utilities.numberConverter(MainApp.getValue(WHATSTOTAL))));
////            MainApp.storeValue(WHATSDAILYTOTAL, "" + (1 + Utilities.numberConverter(MainApp.getValue(WHATSDAILYTOTAL))));
////
////            String whatsAppMsg = "http://api.whatsapp.com/send?phone=+91" + number + "&text=" + MainApp.getValue(WHATSAPPSWITCH);
//////            String whatsAppMsg = "https://wa.me/+91" + number + "&text=" + MainApp.getValue(WHATSAPPSWITCH);
////
////            Intent intent = new Intent(Intent.ACTION_VIEW);
////            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////            intent.setData(Uri.parse(whatsAppMsg));
////            context.startActivity(intent);
////        } catch (Exception e) {
////            Log.e("openWhatsApp", e.toString());
////        }
////
////        Log.e("openWhatsApp", "sent");
////    }
//
//    public void openWhatsApp(Context context, String toNumber) {
//        try {
//            toNumber = toNumber.replace("+", "").replace(" ", "");
//
//            boolean isContactExist = contactExists(context, toNumber);
//            if (isContactExist) {
//                sendMessageAndImageOnWhatsApp(context, toNumber);
//            } else {
//                sendMessageOnlyOnWhatsApp(context, toNumber);
//            }
//
////            toNumber = toNumber.replace("+", "").replace(" ", "");
////            String whatsAppMsg = MainApp.getValue(WHATSAPPSWITCH);
////
////            Uri outputFileUri = null;
////            String whatsAppTemplateImg = MainApp.getValue(WHATSAPPS_TEMPLATE_IMAGE);
////            if (whatsAppTemplateImg != null && whatsAppTemplateImg.trim().length() > 0) {
////                File file = new File(whatsAppTemplateImg);
////                outputFileUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
////            }
////
////            boolean installed = whatsAppInstalledOrNot("com.whatsapp");
////            if (installed) {
////                Intent sendIntent = new Intent("android.intent.action.SEND");
////                sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.ContactPicker"));
////                sendIntent.setType("image");
////                sendIntent.putExtra(Intent.EXTRA_STREAM, outputFileUri);
////                sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(toNumber) + "@s.whatsapp.net");
////                sendIntent.putExtra(Intent.EXTRA_TEXT, whatsAppMsg);
////                sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                startActivity(sendIntent);
////
//////                Intent sendIntent = new Intent("android.intent.action.MAIN");
//////                sendIntent.setAction(Intent.ACTION_SEND);
//////                sendIntent.putExtra(Intent.EXTRA_STREAM, outputFileUri);
//////                sendIntent.setPackage("com.whatsapp");
//////                sendIntent.setType("image/*");
//////                sendIntent.putExtra("jid", toNumber + "@s.whatsapp.net");// here 91 is country code
//////                sendIntent.putExtra(Intent.EXTRA_TEXT, whatsAppMsg);
//////                sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//////                startActivity(sendIntent);
////            } else {
////                Toast.makeText(context, "WhatsApp is not currently installed on your phone", Toast.LENGTH_SHORT).show();
////            }
//
//        } catch (Exception e) {
//            Log.e("openWhatsApp", e.toString());
//        }
//
//        Log.e("openWhatsApp", "sent");
//    }
//
//    private void sendMessageAndImageOnWhatsApp(Context context, String toNumber) {
//        String whatsAppMsg = MainApp.getValue(WHATSAPPSWITCH);
//
//        Uri outputFileUri = null;
//        String mimeType = "";
//        String whatsAppTemplateDocument = MainApp.getValue(WHATSAPPS_TEMPLATE_DOCUMENT);
//        if (whatsAppTemplateDocument != null && whatsAppTemplateDocument.trim().length() > 0) {
//            File file = new File(whatsAppTemplateDocument);
////            File file = new File("/sdcard/Download/Test.pdf");
//            outputFileUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
//
//            ContentResolver cr = context.getContentResolver();
//            mimeType = cr.getType(outputFileUri);
//        }
//
//        boolean installed = whatsAppInstalledOrNot("com.whatsapp");
//        if (installed) {
//            Intent sendIntent = new Intent("android.intent.action.SEND");
//            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.ContactPicker"));
////            sendIntent.setType("image/*");
//            sendIntent.setType(mimeType);
//            sendIntent.putExtra(Intent.EXTRA_STREAM, outputFileUri);
//            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(toNumber) + "@s.whatsapp.net");
//            sendIntent.putExtra(Intent.EXTRA_TEXT, whatsAppMsg);
//            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(sendIntent);
//
////                Intent sendIntent = new Intent("android.intent.action.MAIN");
////                sendIntent.setAction(Intent.ACTION_SEND);
////                sendIntent.putExtra(Intent.EXTRA_STREAM, outputFileUri);
////                sendIntent.setPackage("com.whatsapp");
////                sendIntent.setType("image/*");
////                sendIntent.putExtra("jid", toNumber + "@s.whatsapp.net");// here 91 is country code
////                sendIntent.putExtra(Intent.EXTRA_TEXT, whatsAppMsg);
////                sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                startActivity(sendIntent);
//        } else {
//            Toast.makeText(context, "WhatsApp is not currently installed on your phone", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void sendMessageOnlyOnWhatsApp(Context context, String toNumber) {
////        String whatsAppMsg = MainApp.getValue(WHATSAPPSWITCH);
//
//        boolean installed = whatsAppInstalledOrNot("com.whatsapp");
//        if (installed) {
////            String whatsAppMsg = "http://api.whatsapp.com/send?phone=+91" + number + "&text=" + MainApp.getValue(WHATSAPPSWITCH);
//
//            String whatsAppMsg = "http://api.whatsapp.com/send?phone=" + toNumber + "&text=" + MainApp.getValue(WHATSAPPSWITCH);
//
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setData(Uri.parse(whatsAppMsg));
//            context.startActivity(intent);
//
//        } else {
//            Toast.makeText(context, "WhatsApp is not currently installed on your phone", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//    private boolean whatsAppInstalledOrNot(String uri) {
//        PackageManager pm = getPackageManager();
//        boolean app_installed;
//        try {
//            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
//            app_installed = true;
//        } catch (PackageManager.NameNotFoundException e) {
//            app_installed = false;
//        }
//        return app_installed;
//    }
//
//    public boolean contactExists(Context context, String number) {
///// number is the phone number
//        Uri lookupUri = Uri.withAppendedPath(
//                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
//                Uri.encode(number));
//        String[] mPhoneNumberProjection = {ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME};
//        Cursor cur = context.getContentResolver().query(lookupUri, mPhoneNumberProjection, null, null, null);
//        try {
//            if (cur.moveToFirst()) {
//                return true;
//            }
//        } finally {
//            if (cur != null)
//                cur.close();
//        }
//        return false;
//    }
//
//    private void sendSMS(String phoneNo, String msg) {
//        try {
//            SmsManager smsManager = SmsManager.getDefault();
//            ArrayList<String> messages = smsManager.divideMessage(msg);
//
//            smsManager.sendMultipartTextMessage(phoneNo, null, messages, null, null);
//
////            for (String splitMsg : messages) {
////                smsManager.sendTextMessage(phoneNo, null, splitMsg, null, null);
////            }
//
//            MainApp.storeValue(SMSTOTAL, "" + (1 + Utilities.numberConverter(MainApp.getValue(SMSTOTAL))));
//            MainApp.storeValue(SMSDAILYTOTAL, "" + (1 + Utilities.numberConverter(MainApp.getValue(SMSDAILYTOTAL))));
//
//        } catch (Exception ex) {
//            Log.e("sendSMS", ex.toString());
//        }
//
//        Log.e("sendSMS", "sent");
//    }
//
//    private String getSmsToSend() {
//
//        switch (MainApp.getValue(LASTOPTION)) {
////            case FIRSTSWITCH:
////                return MainApp.getValue(FIRSTSWITCH);
//            case SECONDSWITCH:
//                return MainApp.getValue(SECONDSWITCH);
//            case THIRDSWITCH:
//                return MainApp.getValue(THIRDSWITCH);
//            default:
//                return MainApp.getValue(FIRSTSWITCH);
//        }
//    }
//
//    public void smsMethodChecker(String number, Context context, String callType) {
//
//        String smsMsg;
//        String sendingNumber = Utilities.numberGenerator(number);
//
//        if (callType.equals(MISSEDCALL)) {
//            smsMsg = MainApp.getValue(THIRDSWITCH);
//        } else {
//            smsMsg = getSmsToSend();
//        }
//
//        switch (MainApp.getValue(SMSMETHOD)) {
//            case ONLYSMS:
//                sendSMS(sendingNumber, smsMsg);
//                break;
//            case SMSWHATSAPP:
//                sendSMS(sendingNumber, smsMsg);
//                openWhatsApp(context, sendingNumber);
//                break;
//            case WHATSAPP:
//                openWhatsApp(context, sendingNumber);
//                break;
//        }
//
//        WebSmsCaller.callWebSms(number, smsMsg);
//    }
//
//    private void getInstanceTimer() {
//        if (timer == null) {
//            timer = new Timer();
//        }
//    }
//
//    private void stopTimer() {
//        if (timer != null) {
//            timer.cancel();
//            timer = null;
//        }
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        receiver = new CallChecker();
//        IntentFilter checkerFilter = new IntentFilter();
//        checkerFilter.addAction("android.intent.action.PHONE_STATE");
//        checkerFilter.addAction("android.intent.action.NEW_OUTGOING_CALL");
//        registerReceiver(receiver, checkerFilter);
//
////        smsMethodChecker("8655289417", getApplicationContext(), INCOMMING);
//
//        permissionReceiver = new PermissionChecker();
//        IntentFilter permissionFilter = new IntentFilter();
//        permissionFilter.addAction(PERMISSION_STATUS);
//        LocalBroadcastManager.getInstance(this).registerReceiver(permissionReceiver, permissionFilter);
//
//        Utilities.resetCount();
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d("########", "onStartCommand");
//
//        if (intent.getAction() != null) {
//            if (intent.getAction().equals(STARTFOREGROUND_ACTION)) {
//                if (timer == null) {
//                    getInstanceTimer();
//                }
//
//                Log.i(LOG_TAG, "Received Start Foreground Intent ");
//                showNotification();
//
//                timer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        Log.e(LOG_TAG, "Service Running");
//                    }
//                }, 1000, 60000);
//
//
//            } else if (intent.getAction().equals(PREV_ACTION)) {
//                Log.e(LOG_TAG, "Clicked Previous");
//
//            } else if (intent.getAction().equals(PLAY_ACTION)) {
//                Log.e(LOG_TAG, "Clicked Play");
//
//            } else if (intent.getAction().equals(NEXT_ACTION)) {
//                Log.e(LOG_TAG, "Clicked Next");
//
//            } else if (intent.getAction().equals(STOPFOREGROUND_ACTION)) {
//                Log.e(LOG_TAG, "Received Stop Foreground Intent");
//                stopTimer();
//                stopForeground(true);
//                stopSelf();
//            }
//        }
//        return START_STICKY;
//    }
//
//    private void showNotification() {
//        String channelId;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            channelId = createNotificationChannel();
//        } else {
//            channelId = "";
//        }
//
//        Intent notificationIntent = new Intent(this, CallerService.class);
//        notificationIntent.setAction(MAIN_ACTION);
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
//                notificationIntent, 0);
//
//        Notification notification = new NotificationCompat.Builder(this, channelId)
//                .setContentTitle("Checking calls")
//                .setTicker("")
//                .setContentText("")
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentIntent(pendingIntent)
//                .setOngoing(true)
//                .build();
//        startForeground(FOREGROUND_SERVICE, notification);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private String createNotificationChannel() {
//
//        NotificationChannel notificationChannel = new NotificationChannel("smsService"
//                , "My Background Service", NotificationManager.IMPORTANCE_NONE);
//        notificationChannel.setLightColor(getColor(R.color.colorAccent));
//        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.createNotificationChannel(notificationChannel);
//        return "smsService";
//    }
//
//
//    @Override
//    public void onDestroy() {
//        Log.d("########", "onDestroy");
//        unregisterReceiver(receiver);
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(permissionReceiver);
//        super.onDestroy();
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        Log.d("########", "onBind");
//        return null;
//    }
//
//    @Override
//    protected void onHandleIntent(@Nullable Intent intent) {
//        Log.d("########", "onHandleIntent");
//    }
//
//    @Override
//    public void onTaskRemoved(Intent rootIntent) {
//        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
//        restartServiceIntent.setPackage(getPackageName());
//
//        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
//        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, restartServicePendingIntent);
//
//        Log.d("########", "onTaskRemoved");
//        super.onTaskRemoved(rootIntent);
//    }
//}
