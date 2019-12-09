package com.smser.smssender.service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.smser.smssender.apicaller.ApiSignature;
import com.smser.smssender.apicaller.RetroCreator;
import com.smser.smssender.comman.Constants;
import com.smser.smssender.definer.MainApp;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class WebSmsCaller implements Constants {

    static void callWebSms(final String mobile, final String msg) {


        ApiSignature caller = new RetroCreator().getRetroClient(BASE_URL).create(ApiSignature.class);

        Call<ResponseBody> request = caller.sendWebSms(MainApp.getValue(USERNAME), MainApp.getValue(PASSWORD),
                mobile, msg, MainApp.getValue(SENDERID));

        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                try {

                    if (response.body() != null) {
                        if (!response.body().string().equals("0")) {
                            Log.e("web sms", "sent");
                        } else {
                            callWebSms(mobile, msg);
                        }
                    } else {
                        callWebSms(mobile, msg);
                    }

                } catch (Exception e) {
                    Log.e("callWebSms exception", e.toString());
                    callWebSms(mobile, msg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e("callWebSms fail", t.toString());
                callWebSms(mobile, msg);
            }
        });

    }
}
