package com.smser.smssender.comman;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.smser.smssender.definer.MainApp;
import com.smser.smssender.service.CallerService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Utilities implements Constants {

    private static String[] permissions = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_CONTACTS};

    private static final int MY_PERMISSIONS_REQUEST = 321;

    public static void checkPermissions(Context context) {
        try {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
                for (String permission : permissions) {
                    if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]
                                    {Manifest.permission.READ_PHONE_STATE,
                                            Manifest.permission.READ_CALL_LOG,
                                            Manifest.permission.SEND_SMS,
                                            Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST);
                        } else {
                            ActivityCompat.requestPermissions((Activity) context, new String[]
                                    {Manifest.permission.READ_PHONE_STATE,
                                            Manifest.permission.READ_CALL_LOG,
                                            Manifest.permission.SEND_SMS,
                                            Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST);
                        }
//                        return false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return true;
    }

    public static boolean isInternetAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager != null && connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static boolean isBackServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (CallerService.class.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String numberGenerator(String number) {

        String num = number.replace(ELEMENATOR1, ELEMENATOR3);
        num = num.replace(ELEMENATOR2, ELEMENATOR3);

        if (num.length() > 10) {
            num = num.substring(num.length() - 10);
        }

        return "+91" + num;
    }

    public static int numberConverter(String value) {

        if (value.equals("")) {
            return 0;
        } else {
            return Integer.parseInt(value);
        }
    }

    public static boolean emailValidator(String email) {
        return email.length() == 0 || android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches();
    }

    public static boolean mobileValidator(String mob) {
        return (mob.trim().length() == 10 && mob.trim().charAt(0) != '0');
    }

    public static void showToast(Context context, String msg) {

        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static boolean dateChecker(String dateStr) {

        boolean flag = false;
        Calendar cal = Calendar.getInstance();
        Calendar dob = Calendar.getInstance();

        if (!dateStr.equals("")) {

            String[] str = dateStr.split("/");
            cal.set(Calendar.MONTH, (Integer.parseInt(str[0]) - 1));
            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(str[1]));
            cal.set(Calendar.YEAR, Integer.parseInt(str[2]));

            Date expiryDate = cal.getTime();
            Date currentDate = dob.getTime();

            long days = TimeUnit.DAYS.convert(expiryDate.getTime() - currentDate.getTime(), TimeUnit.MILLISECONDS);

//            flag = dob.get(Calendar.YEAR) <= cal.get(Calendar.YEAR) && dob.get(Calendar.MONTH) <=
//                    cal.get(Calendar.MONTH) && dob.get(Calendar.DAY_OF_MONTH) <= cal.get(Calendar.DAY_OF_MONTH);

            if (days >= 0) {
                flag = true;
            }

        }

        return flag;
    }

    public static void saveExpiryDate(String receivedDate) {

        String[] str = receivedDate.split(" ");
        MainApp.storeValue(EXPIRY, str[0]);
        Log.e(EXPIRY, str[0]);
    }

    public static void resetCount() {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date date = cal.getTime();
        String strDate = sdf.format(date);
        if (!MainApp.getValue(DAILY).equals(strDate)) {
            MainApp.storeValue(DAILY, strDate);
            MainApp.removeValue(SMSDAILYTOTAL);
            MainApp.removeValue(WHATSDAILYTOTAL);
            Log.e("resetCount", "daily data cleared");
        }
    }

    public static String remainingDays() {

        String reamingDays;
        String dateStr = MainApp.getValue(EXPIRY);

        Calendar calendar = Calendar.getInstance();
        String[] str = dateStr.split("/");
        calendar.set(Calendar.MONTH, (Integer.parseInt(str[0]) - 1));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(str[1]));
        calendar.set(Calendar.YEAR, Integer.parseInt(str[2]));
        Date expiryDate = calendar.getTime();

        Calendar cal = Calendar.getInstance();
        Date currentDate = cal.getTime();

        long days = TimeUnit.DAYS.convert(expiryDate.getTime() - currentDate.getTime(), TimeUnit.MILLISECONDS);

        reamingDays = String.valueOf(days);

        return reamingDays;
    }

    public static String getIMEI(Context context) {
        String IMEI = "";

        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            if (tm != null) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return "000000000000000";
                }
                IMEI = tm.getDeviceId();
                Log.e("IMEI", IMEI);
            }

            if (IMEI == null || IMEI.length() == 0)
                IMEI = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            IMEI = "000000000000000";
            Log.e("IMEI", IMEI);
        }

        return IMEI;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

    public static boolean emptyValidator(String data) {

        return data != null && !data.isEmpty();
    }
}
