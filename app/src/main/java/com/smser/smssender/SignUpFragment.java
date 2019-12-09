package com.smser.smssender;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.smser.smssender.apicaller.ApiSignature;
import com.smser.smssender.apicaller.RetroCreator;
import com.smser.smssender.comman.Constants;
import com.smser.smssender.comman.Utilities;
import com.smser.smssender.definer.MainApp;
import com.smser.smssender.model.RegisterReq;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.smser.smssender.comman.Utilities.emptyValidator;

public class SignUpFragment extends Fragment implements Constants {

    private EditText uName, emailId, mobile, bName, cityState, adminCode;
    private TextInputLayout adminLay;
    private AppCompatCheckBox isAdmin;

    public SignUpFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View signUpView = inflater.inflate(R.layout.fragment_signup, container, false);

        init(signUpView);

        return signUpView;
    }

    private void init(View view) {
        uName = view.findViewById(R.id.edt_uname);
        emailId = view.findViewById(R.id.edt_email);
        mobile = view.findViewById(R.id.edt_mobile);
        bName = view.findViewById(R.id.edt_bname);
        cityState = view.findViewById(R.id.edt_city);
        adminLay = view.findViewById(R.id.tt_edt_admin_code);
        adminCode = view.findViewById(R.id.edt_admin_code);
        isAdmin = view.findViewById(R.id.admin_check);
        Button btnSave = view.findViewById(R.id.btn_save);

        isAdmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adminCode.setText("");
                if (isChecked) {
                    adminLay.setVisibility(View.VISIBLE);
                } else {
                    adminLay.setVisibility(View.GONE);
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateRequest()) {
                    if (Utilities.isInternetAvailable(getActivity())) {
                        callRegisterApi();
                    } else {
                        Utilities.showToast(getActivity(), getString(R.string.error_internet));
                    }
                }
            }
        });

        if (MainApp.getValue(REGISTER).equals(ENABLE)) {
            uName.setText(MainApp.getValue(USERNAME));
            uName.setEnabled(false);
            emailId.setText(MainApp.getValue(EMAILID));
            emailId.setEnabled(false);
            mobile.setText(MainApp.getValue(MOBILE));
            mobile.setEnabled(false);
            bName.setText(MainApp.getValue(COMPANY));
            bName.setEnabled(false);
            cityState.setText(MainApp.getValue(CITY));
            cityState.setEnabled(false);
            adminCode.setEnabled(false);
            isAdmin.setEnabled(false);
            btnSave.setEnabled(false);
        }

    }

    private RegisterReq getRequest() {

        RegisterReq req = new RegisterReq();
        req.setUsername(uName.getText().toString());
        req.setPass("12345");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 3);
        Date date = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        req.setExpiryDate(dateFormat.format(date));
        req.setAgentCode(null);
        req.setTokenNo("0");
        req.setIsActive(TRUE);
        req.setEmailId(emailId.getText().toString());
        req.setMobileNo(mobile.getText().toString());
        req.setCompanyName(bName.getText().toString());
        req.setCity(cityState.getText().toString());
        req.setIMEI(Utilities.getIMEI(getActivity()));
        req.setIMEI(Utilities.getIMEI(getActivity()));
        req.setModel(Utilities.getDeviceName());
        req.setIsAdmin(isAdmin.isChecked());

        if (isAdmin.isChecked()){
            req.setAdminCode(adminCode.getText().toString());
        }

        return req;

//        String json = "{\n" +
//                "    \"AdminCode\": null,\n" +
//                "    \"City\": \"Kolhapur\",\n" +
//                "    \"CompanyName\": \"Miracle\",\n" +
//                "    \"EmailId\": \"dhairya246@gmail.com\",\n" +
//                "    \"ExpiryDate\": \"1/1/2020 12:00:00 AM\",\n" +
//                "    \"IMEI\": \"356928092421868\",\n" +
//                "    \"LoginID\": 1,\n" +
//                "    \"MobileNo\": \"9665175415\",\n" +
//                "    \"Model\": null,\n" +
//                "    \"Pass\": \"12345\",\n" +
//                "    \"TokenNo\": \"54150101201\",\n" +
//                "    \"TollFree\": \"9021167662\",\n" +
//                "    \"UserID\": null,\n" +
//                "    \"Username\": \"Dhairyashil\",\n" +
//                "    \"isActive\": \"True\",\n" +
//                "    \"isadmin\": false\n" +
//                "}";
//
//        Gson gson = new Gson();
//        req = gson.fromJson(json, RegisterReq.class);
//
//        return req;
    }

    private boolean validateRequest() {

        if (uName.getText().toString().trim().length() < 2) {
            uName.setError(getString(R.string.error_uname));
            return false;
        } else {
            uName.setError(null);
        }

        if (!Utilities.emailValidator(emailId.getText().toString().trim())) {
            emailId.setError(getString(R.string.error_email));
            return false;
        } else {
            emailId.setError(null);
        }

        if (!Utilities.mobileValidator(mobile.getText().toString().trim())) {
            mobile.setError(getString(R.string.error_mobile));
            return false;
        } else {
            mobile.setError(null);
        }

        if (bName.getText().toString().trim().length() < 2) {
            bName.setError(getString(R.string.error_cname));
            return false;
        } else {
            bName.setError(null);
        }

        if (cityState.getText().toString().trim().length() < 2) {
            cityState.setError(getString(R.string.error_city));
            return false;
        } else {
            cityState.setError(null);
        }

        if (isAdmin.isChecked()) {
            if (adminCode.getText().toString().trim().length() < 2) {
                adminCode.setError(getString(R.string.error_admin_code));
                return false;
            } else {
                adminCode.setError(null);
            }
        }

        return true;
    }

    private void callRegisterApi() {

        ((DashBoardActivity) getActivity()).showProgress();

        ApiSignature caller = new RetroCreator().getRetroClient(BASE_URL).create(ApiSignature.class);

        final RegisterReq req = getRequest();

        Call<ResponseBody> request = caller.registerUser(req);

        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                try {

                    if (response.body() != null) {
                        if (!response.body().string().equals("0")) {
                            MainApp.storeValue(REGISTER, ENABLE);
                            MainApp.storeValue(UID, req.getUserID());
                            MainApp.storeValue(USERNAME, req.getUsername());
                            MainApp.storeValue(PASSWORD, req.getPass());
                            MainApp.storeValue(EMAILID, req.getEmailId());
                            MainApp.storeValue(MOBILE, req.getMobileNo());
                            MainApp.storeValue(COMPANY, req.getCompanyName());
                            MainApp.storeValue(CITY, req.getCity());
//                            MainApp.storeValue(EXPIRY, req.getExpiryDate());
                            MainApp.storeValue(AGENTCODE, "" + req.getAgentCode());
                            MainApp.storeValue(ISACTIVE, req.getIsActive());

                            callUserDetailsApi(req);
                        } else {
                            ((DashBoardActivity) getActivity()).hideProgress();
                            Utilities.showToast(getActivity(), getString(R.string.error_already));
                        }
                    } else {
                        ((DashBoardActivity) getActivity()).hideProgress();
                        Utilities.showToast(getActivity(), getString(R.string.error_register));
                    }

                } catch (Exception e) {
                    Log.e("callRegisterApi", e.toString());
                    ((DashBoardActivity) getActivity()).hideProgress();
                    Utilities.showToast(getActivity(), getString(R.string.error_register));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e("callRegisterApi fail", t.toString());
                Utilities.showToast(getActivity(), getString(R.string.error_register));
                ((DashBoardActivity) getActivity()).hideProgress();
            }
        });

    }

    private void callUserDetailsApi(RegisterReq req) {

        ((DashBoardActivity) getActivity()).showProgress();

        ApiSignature caller = new RetroCreator().getRetroClient(BASE_URL).create(ApiSignature.class);

        Call<RegisterReq> request = caller.getUserDetails(req.getMobileNo());

        request.enqueue(new Callback<RegisterReq>() {
            @Override
            public void onResponse(@NonNull Call<RegisterReq> call, @NonNull Response<RegisterReq> response) {

                try {

                    if (response.body() != null) {
                        if (response.body().getMobileNo() != null) {
                            MainApp.storeValue(REGISTER, ENABLE);
                            if (emptyValidator(response.body().getExpiryDate()))
                                Utilities.saveExpiryDate(response.body().getExpiryDate());

                            if (emptyValidator(response.body().getUserID()))
                                MainApp.storeValue(UID, response.body().getUserID());

                            if (emptyValidator(response.body().getUsername()))
                                MainApp.storeValue(USERNAME, response.body().getUsername());

                            if (emptyValidator(response.body().getPass()))
                                MainApp.storeValue(PASSWORD, response.body().getPass());

                            if (emptyValidator(response.body().getEmailId()))
                                MainApp.storeValue(EMAILID, response.body().getEmailId());

                            if (emptyValidator(response.body().getMobileNo()))
                                MainApp.storeValue(MOBILE, response.body().getMobileNo());

                            if (emptyValidator(response.body().getCompanyName()))
                                MainApp.storeValue(COMPANY, response.body().getCompanyName());

                            if (emptyValidator(response.body().getCity()))
                                MainApp.storeValue(CITY, response.body().getCity());

                            if (emptyValidator(response.body().getTokenNo()))
                                MainApp.storeValue(TOKEN, response.body().getTokenNo());

                            if (emptyValidator(response.body().getTollFree()))
                                MainApp.storeValue(TOLLFREE, response.body().getTollFree());

                            if (response.body().getAgentCode() != null)
                                MainApp.storeValue(AGENTCODE, "" + response.body().getAgentCode());

                            if (response.body().getIsAdmin() != null)
                                MainApp.storeValue(ISADMIN, response.body().getIsAdmin().toString());
                            else
                                MainApp.storeValue(ISADMIN, "false");

                            if (response.body().getAdminCode() != null)
                                MainApp.storeValue(ADMINCODE, response.body().getAdminCode());

                            if (MainApp.getValue(FIRSTSWITCH).equals(getString(R.string.default_msg))) {
                                String defaultMsg = getString(R.string.default_msg) + "\n," + response.body().getUsername() +
                                        "\n," + response.body().getCompanyName() + "\n," + response.body().getCity() + "\n," +
                                        response.body().getEmailId() + "\n," + response.body().getMobileNo();
                                MainApp.storeValue(FIRSTSWITCH, defaultMsg);
                            }

                            Utilities.showToast(getActivity(), getString(R.string.register_success));
                            ((DashBoardActivity) getActivity()).callDashBoard();
                        }
                    } else {
                        Utilities.showToast(getActivity(), getString(R.string.error_register));
                    }

                } catch (Exception e) {
                    Log.e("callRegisterApi", e.toString());
                    Utilities.showToast(getActivity(), getString(R.string.error_register));
                }

                ((DashBoardActivity) getActivity()).hideProgress();
            }

            @Override
            public void onFailure(@NonNull Call<RegisterReq> call, @NonNull Throwable t) {
                Log.e("callRegisterApi fail", t.toString());
                Utilities.showToast(getActivity(), getString(R.string.error_register));
                ((DashBoardActivity) getActivity()).hideProgress();
            }
        });

    }
}
