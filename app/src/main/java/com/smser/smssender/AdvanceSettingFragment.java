package com.smser.smssender;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.smser.smssender.comman.Constants;
import com.smser.smssender.definer.MainApp;

public class AdvanceSettingFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, Constants {

    private Switch settingEnabler, smsSender, whatsSmsSender, whatsSender, askSender, webSender, appPower/*, whatsSms,
            whatsImg, whatsVideo*/;
    private SeekBar smsRanger;
    private TextView totalSms, balanceSms;

    public AdvanceSettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View advanceView = inflater.inflate(R.layout.fragment_advance_setting, container, false);

        init(advanceView);
        listener();

        return advanceView;
    }

    private void init(View view) {

        settingEnabler = view.findViewById(R.id.switch_setting);
        smsSender = view.findViewById(R.id.switch_sms_sending);
        whatsSmsSender = view.findViewById(R.id.switch_sms_whats);
        whatsSender = view.findViewById(R.id.switch_whats);
        askSender = view.findViewById(R.id.switch_ask);
        webSender = view.findViewById(R.id.switch_web_sms);
        appPower= view.findViewById(R.id.switch_app_power);
//        whatsSms = view.findViewById(R.id.switch_whats_sms);
//        whatsImg = view.findViewById(R.id.switch_whats_img);
//        whatsVideo = view.findViewById(R.id.switch_whats_video);

        smsRanger = view.findViewById(R.id.sms_ranger);

        totalSms = view.findViewById(R.id.total_sms);
        balanceSms = view.findViewById(R.id.balance_sms);
    }

    private void listener() {

        settingEnabler.setOnCheckedChangeListener(this);
        askSender.setOnCheckedChangeListener(this);
        webSender.setOnCheckedChangeListener(this);
        appPower.setOnCheckedChangeListener(this);

//        whatsSms.setOnClickListener(this);
//        whatsImg.setOnClickListener(this);
//        whatsVideo.setOnClickListener(this);

        smsSender.setOnClickListener(this);
        whatsSmsSender.setOnClickListener(this);
        whatsSender.setOnClickListener(this);

        smsRanger.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.e("progress", "" + progress);

                MainApp.storeValue(CALLLIMIT, "" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        settingEnabler.setChecked(!MainApp.getValue(MSGLIMITER).equals(DISABLE));
        askSender.setChecked(!MainApp.getValue(PERMISSION).equals(DISABLE));
        webSender.setChecked(!MainApp.getValue(WEBSMS).equals(DISABLE));
        appPower.setChecked(!MainApp.getValue(APPPOWER).equals(DISABLE));

        switch (MainApp.getValue(SMSMETHOD)) {

//            case ONLYSMS:
//                smsSender.performClick();
//                break;
            case SMSWHATSAPP:
                whatsSmsSender.performClick();
                break;
            case WHATSAPP:
                whatsSender.performClick();
                break;
            default:
                smsSender.performClick();
                break;
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.switch_setting:
                if (isChecked) {
                    MainApp.storeValue(MSGLIMITER, ENABLE);
                    smsRanger.setEnabled(true);
                    MainApp.storeValue(CALLLIMIT, "" + smsRanger.getProgress());
                } else {
                    MainApp.storeValue(MSGLIMITER, DISABLE);
                    smsRanger.setEnabled(false);
                }
                break;
            case R.id.switch_ask:
                if (isChecked) {
                    MainApp.storeValue(PERMISSION, ENABLE);
                } else {
                    MainApp.storeValue(PERMISSION, DISABLE);
                }
                break;
            case R.id.switch_web_sms:
                if (isChecked) {
                    MainApp.storeValue(WEBSMS, ENABLE);
                } else {
                    MainApp.storeValue(WEBSMS, DISABLE);
                }
                break;
            case R.id.switch_app_power:
                if (isChecked) {
                    MainApp.storeValue(APPPOWER, ENABLE);
                } else {
                    MainApp.storeValue(APPPOWER, DISABLE);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.switch_sms_sending:
                if (smsSender.isChecked()) {
                    whatsSmsSender.setChecked(false);
                    whatsSender.setChecked(false);
                    MainApp.storeValue(SMSMETHOD, ONLYSMS);
                } else {
                    smsSender.setChecked(true);
                }
                break;
            case R.id.switch_sms_whats:
                if (whatsSmsSender.isChecked()) {
                    smsSender.setChecked(false);
                    whatsSender.setChecked(false);
                    MainApp.storeValue(SMSMETHOD, SMSWHATSAPP);
                } else {
                    whatsSmsSender.setChecked(true);
                }
                break;
            case R.id.switch_whats:
                if (whatsSender.isChecked()) {
                    smsSender.setChecked(false);
                    whatsSmsSender.setChecked(false);
                    MainApp.storeValue(SMSMETHOD, WHATSAPP);
                } else {
                    whatsSender.setChecked(true);
                }
                break;
            /*case R.id.switch_whats_sms:
                if (whatsSms.isChecked()){
                    whatsImg.setChecked(false);
                    whatsVideo.setChecked(false);
                    MainApp.storeValue(WHATSAPPMETHOD, WHATSAPPSMS);
                } else {
                    whatsSms.setChecked(true);
                }
                break;
            case R.id.switch_whats_img:
                if (whatsImg.isChecked()){
                    whatsSms.setChecked(false);
                    whatsVideo.setChecked(false);
                    MainApp.storeValue(WHATSAPPMETHOD, WHATSAPPIMG);
                } else {
                    whatsImg.setChecked(true);
                }
                break;
            case R.id.switch_whats_video:
                if (whatsVideo.isChecked()){
                    whatsImg.setChecked(false);
                    whatsSms.setChecked(false);
                    MainApp.storeValue(WHATSAPPMETHOD, WHATSAPPVIDEO);
                } else {
                    whatsVideo.setChecked(true);
                }
                break;*/
        }
    }
}
