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
import com.smser.smssender.apicaller.ApiSignature;
import com.smser.smssender.apicaller.RetroCreator;
import com.smser.smssender.comman.Constants;
import com.smser.smssender.comman.Utilities;
import com.smser.smssender.definer.MainApp;
import com.smser.smssender.model.RegisterReq;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.smser.smssender.comman.Utilities.emptyValidator;

public class UserInfoFragment extends Fragment implements Constants {

    private EditText uName, emailId, mobile, bName, cityState, agentCode, keyNo, adminCode;
    private TextInputLayout adminLay;
    private AppCompatCheckBox isAdmin;

    public UserInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View infoView = inflater.inflate(R.layout.fragment_user_info, container, false);

        init(infoView);

        return infoView;
    }

    private void init(View view) {
        uName = view.findViewById(R.id.edt_uname);
        emailId = view.findViewById(R.id.edt_email);
        mobile = view.findViewById(R.id.edt_mobile);
        bName = view.findViewById(R.id.edt_bname);
        cityState = view.findViewById(R.id.edt_city);
        agentCode = view.findViewById(R.id.edt_agent_code);
        keyNo = view.findViewById(R.id.edt_key);
        adminLay = view.findViewById(R.id.tt_edt_admin_code);
        adminCode = view.findViewById(R.id.edt_admin_code);
        isAdmin = view.findViewById(R.id.admin_check);
        Button btnEdit = view.findViewById(R.id.btn_edit);
        Button btnRegister = view.findViewById(R.id.btn_register);

        isAdmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    adminLay.setVisibility(View.VISIBLE);
                } else {
                    adminLay.setVisibility(View.GONE);
                }
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
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

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateToken()) {
                    if (Utilities.isInternetAvailable(getActivity())) {
                        callPurchaseApi(mobile.getText().toString(), keyNo.getText().toString());
                    } else {
                        Utilities.showToast(getActivity(), getString(R.string.error_internet));
                    }
                }
            }
        });

        if (MainApp.getValue(REGISTER).equals(ENABLE)) {
            uName.setText(MainApp.getValue(USERNAME));
            emailId.setText(MainApp.getValue(EMAILID));
            mobile.setText(MainApp.getValue(MOBILE));
            bName.setText(MainApp.getValue(COMPANY));
            cityState.setText(MainApp.getValue(CITY));
            if (MainApp.getValue(AGENTCODE) != null && !MainApp.getValue(AGENTCODE).equals("0")) {
                agentCode.setText(MainApp.getValue(AGENTCODE));
            }
            if (!MainApp.getValue(TOKEN).equals("0")) {
                keyNo.setText(MainApp.getValue(TOKEN));
            }
            if (!MainApp.getValue(ISADMIN).equals("false")) {
                isAdmin.setChecked(true);
            } else {
                isAdmin.setChecked(false);
            }

            if (!MainApp.getValue(ADMINCODE).equals("")) {
                adminCode.setText(MainApp.getValue(ADMINCODE));
            }

        }
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

        if (agentCode.getText().toString().trim().length() < 1) {
            agentCode.setError(getString(R.string.error_agent));
            return false;
        } else {
            agentCode.setError(null);
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

    private boolean validateToken() {

        if (!Utilities.mobileValidator(mobile.getText().toString().trim())) {
            mobile.setError(getString(R.string.error_mobile));
            return false;
        } else {
            mobile.setError(null);
        }

        if (keyNo.getText().toString().trim().length() < 2) {
            keyNo.setError(getString(R.string.error_key));
            return false;
        } else {
            keyNo.setError(null);
        }

        return true;
    }

    private RegisterReq getRequest() {

        RegisterReq req = new RegisterReq();
        req.setUserID(MainApp.getValue(UID));
        req.setUsername(uName.getText().toString());
        req.setPass(MainApp.getValue(PASSWORD));
        req.setExpiryDate(MainApp.getValue(EXPIRY));
        req.setTokenNo(MainApp.getValue(TOKEN));
        req.setIsActive(MainApp.getValue(ISACTIVE));
        req.setEmailId(emailId.getText().toString());
        req.setMobileNo(mobile.getText().toString());
        req.setCompanyName(bName.getText().toString());
        req.setCity(cityState.getText().toString());
        req.setAgentCode(Integer.valueOf(agentCode.getText().toString()));
        req.setIMEI(Utilities.getIMEI(getActivity()));
        req.setModel(Utilities.getDeviceName());
        req.setIsAdmin(isAdmin.isChecked());

        if (isAdmin.isChecked()) {
            req.setAdminCode(adminCode.getText().toString());
        }

        return req;
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
                            MainApp.storeValue(AGENTCODE, "" + req.getAgentCode());

                            Utilities.showToast(getActivity(), getString(R.string.modify_success));
                        } else {
                            Utilities.showToast(getActivity(), getString(R.string.error_register));
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
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e("callRegisterApi fail", t.toString());
                Utilities.showToast(getActivity(), getString(R.string.error_register));
                ((DashBoardActivity) getActivity()).hideProgress();
            }
        });

    }

    private void callPurchaseApi(String mobileNumber, String token) {

        ((DashBoardActivity) getActivity()).showProgress();
        ApiSignature caller = new RetroCreator().getRetroClient(BASE_URL).create(ApiSignature.class);

        final RegisterReq req = getRequest();

        Call<RegisterReq> request = caller.purchaseAccount(mobileNumber, token);

        request.enqueue(new Callback<RegisterReq>() {
            @Override
            public void onResponse(@NonNull Call<RegisterReq> call, @NonNull Response<RegisterReq> response) {

                try {
                    if (response.body() != null) {
                        if (response.body().getMobileNo() != null) {
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
                                MainApp.storeValue(AGENTCODE, "" + req.getAgentCode());

                            Utilities.showToast(getActivity(), getString(R.string.acc_activated));
                        } else {
                            Utilities.showToast(getActivity(), getString(R.string.error_invalid_register));
                        }
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
