package com.smser.smssender;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.smser.smssender.comman.Constants;
import com.smser.smssender.comman.Utilities;
import com.smser.smssender.definer.MainApp;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

public class DashBoardActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, Constants {

    private DrawerLayout drawer;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        View headerView = navigationView.getHeaderView(0);

        TextView navigationText = headerView.findViewById(R.id.txt_user_info);
        String strText = MainApp.getValue(USERNAME) + "\n" + MainApp.getValue(EMAILID);
        navigationText.setText(strText);

        progressDialog = new ProgressDialog(DashBoardActivity.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.setCancelable(false);

        navigationView.setNavigationItemSelectedListener(this);

        Utilities.checkPermissions(this);

        defaultSetting();
    }

    @Override
    public void onBackPressed() {
        drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dash) {
            callDashBoard();
        } else if (id == R.id.nav_setting) {
            callSmsTemplate();
        } else if (id == R.id.nav_block_contact) {
            callBlockContact();
        } else if (id == R.id.nav_ad_setting) {
            callAdSetting();
        } else if (id == R.id.nav_u_info) {
            callUserInfo();
        } else if (id == R.id.nav_sms) {
            callWebSms();
        } else if (id == R.id.nav_report) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void defaultSetting() {

        if (MainApp.getValue(LASTOPTION).equals("")){
            MainApp.storeValue(LASTOPTION, FIRSTSWITCH);
        }

        if (MainApp.getValue(FIRSTSWITCH).equals("")) {
            MainApp.storeValue(FIRSTSWITCH, getString(R.string.default_msg));
        }

        if (MainApp.getValue(SMSMETHOD).equals("")) {
            MainApp.storeValue(SMSMETHOD, ONLYSMS);
        }

        if (MainApp.getValue(PERMISSION).equals("")) {
            MainApp.storeValue(PERMISSION, ENABLE);
        }

        if (MainApp.getValue(WEBSMS).equals("")) {
            MainApp.storeValue(WEBSMS, DISABLE);
        }

        if (MainApp.getValue(MSGLIMITER).equals("")) {
            MainApp.storeValue(MSGLIMITER, ENABLE);
        }

        if (MainApp.getValue(REGISTER).equals(ENABLE)) {
            callDashBoard();
        } else {
            callSignUp();
        }

    }

    private void callSignUp() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.base_layout, new SignUpFragment());
        ft.commit();

    }

    private void callBlockContact() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.base_layout, new BlockContactFragment());
        ft.commit();

    }

    private void callAdSetting() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.base_layout, new AdvanceSettingFragment());
        ft.commit();

    }

    private void callWebSms() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.base_layout, new WebSmsFragment());
        ft.commit();

    }

    public void callDashBoard() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.base_layout, new DashBoardFragment());
        ft.commit();

    }

    private void callSmsTemplate() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.base_layout, new SmsTemplateFragment());
        ft.commit();

    }

    private void callUserInfo() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.base_layout, new UserInfoFragment());
        ft.commit();

    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void hideProgress() {
        hideKeyboard(this.getCurrentFocus());
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void showProgress() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }
}
