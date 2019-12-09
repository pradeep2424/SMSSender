package com.smser.smssender;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smser.smssender.comman.Constants;
import com.smser.smssender.comman.Utilities;
import com.smser.smssender.definer.MainApp;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;

public class DashBoardFragment extends Fragment implements Constants {

    public DashBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View dashBoard = inflater.inflate(R.layout.fragment_dash_board, container, false);

        init(dashBoard);

        return dashBoard;
    }

    private void init(View view) {

        TextView welcomeTxt = view.findViewById(R.id.welcome_text);
        TextView validity = view.findViewById(R.id.validity);
        TextView support = view.findViewById(R.id.customer_care);

        CircularProgressIndicator birthdayCount = view.findViewById(R.id.birthday_count);
        CircularProgressIndicator taskCount = view.findViewById(R.id.task_count);
        CircularProgressIndicator smsCount = view.findViewById(R.id.sms_count);
        CircularProgressIndicator whatsUpCount = view.findViewById(R.id.whats_up_count);

        String welcomeStr = getString(R.string.str_hi) + " " + MainApp.getValue(USERNAME) + getString(R.string.welcome);
        welcomeTxt.setText(welcomeStr);
        String remainingDays = getString(R.string.validity) + " " + Utilities.remainingDays();
        validity.setText(remainingDays);

        String tollFree;
        if (MainApp.getValue(TOLLFREE) != null && !MainApp.getValue(TOLLFREE).equals("")) {
            tollFree = getString(R.string.customer_care) + " " + MainApp.getValue(TOLLFREE);
        } else {
            tollFree = getString(R.string.customer_care) + " 8000000000" + MainApp.getValue(TOLLFREE);
        }
        support.setText(tollFree);

        birthdayCount.setMaxProgress(200);
        birthdayCount.setCurrentProgress(0);
        taskCount.setMaxProgress(200);
        taskCount.setCurrentProgress(0);
        smsCount.setMaxProgress(200);
        smsCount.setCurrentProgress(Utilities.numberConverter(MainApp.getValue(SMSDAILYTOTAL)));
        whatsUpCount.setMaxProgress(200);
        whatsUpCount.setCurrentProgress(Utilities.numberConverter(MainApp.getValue(WHATSDAILYTOTAL)));
    }

}
