package org.simberg.cib.policywriting.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

import org.simberg.cib.policywriting.R;
import org.simberg.cib.policywriting.java.Constants;
import org.simberg.cib.policywriting.java.SharedPreference;
import org.simberg.cib.policywriting.models.Auth;
import org.simberg.cib.policywriting.models.AuthResult;
import org.simberg.cib.policywriting.models.Authorization;
import org.simberg.cib.policywriting.models.ErrorWrapper;
import org.simberg.cib.policywriting.models.Transaction;
import org.simberg.cib.policywriting.rest.ApiClient;
import org.simberg.cib.policywriting.rest.ApiInterface;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by javadbadirkhanly on 9/13/17.
 */

public class AuthenticationActivity extends ParentActivity {

    private final static String TAG = AuthenticationActivity.class.getSimpleName();

    @BindView(R.id.textView)
    TextView tvHeader;

    @BindView(R.id.tilPhoneNumberLoginActivity)
    TextInputLayout tilPhoneNumber;

    @BindView(R.id.tilUserIdLoginActivity)
    TextInputLayout tilUserId;

    @BindView(R.id.etPhoneNumberLoginActivity)
    EditText etPhoneNumber;
    @BindView(R.id.etUserIdLoginActivity)
    EditText etUserId;

    @BindView(R.id.btnEnterLoginActivity)
    Button btnEnter;

    private SharedPreference sharedPreference;

    private ApiInterface apiInterface;

    private MaterialDialog dialogProgress;
    private MaterialDialog dialogVerification;

    private String operationId;
    private String token;
    private String appCode;

    private String phoneNumber;
    private String userId;

    private long transactionId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_login);
        ButterKnife.bind(this);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sharedPreference = new SharedPreference(this);

        if (isFirstRun()) {
            // App registration for first time

            etPhoneNumber.setText("+994 ");
            etPhoneNumber.setSelection(etPhoneNumber.length());

            etPhoneNumber.addTextChangedListener(new TextWatcher() {

                private boolean isDeleting = false;

                @Override
                public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                    Log.d(TAG, "beforeTextChanged: " + charSequence);

                    Log.d(TAG, "beforeTextChanged: \nstart: " + start + "\ncount: " + count + "\nafter: " + after);

                    isDeleting = after < count;
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    Log.d(TAG, "onTextChanged: " + charSequence);

                    Log.d(TAG, "onTextChanged: \nstart: " + start + "\nbefore: " + before + "\ncount: " + count);

                    if (charSequence.length() < 5){
                        etPhoneNumber.setText("+994 ");
                        etPhoneNumber.setSelection(etPhoneNumber.length());
                    }

                    if (charSequence.length() == 6){
                        if (charSequence.charAt(5) == '0'){
                            tilPhoneNumber.setErrorEnabled(true);
                            tilPhoneNumber.setError("Nömrə formatı düzgün deyil. Nümunə: +994 50 500 00 00");
                        } else if (charSequence.charAt(5) != '0'){
                            tilPhoneNumber.setErrorEnabled(false);
                        }
                    }

                    if (!isDeleting){
                        if (charSequence.length() == 7 || charSequence.length() == 11 || charSequence.length() == 14){
                            etPhoneNumber.append(" ");
                        }}

                    if (charSequence.length() > 17){
                        tilPhoneNumber.setErrorEnabled(true);
                        tilPhoneNumber.setError("Nömrə formatı düzgün deyil. Nümunə: +994 50 500 00 00");
                    } else if (charSequence.length() == 17){
                        tilPhoneNumber.setErrorEnabled(false);
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    Log.d(TAG, "afterTextChanged: " + editable);
                }
            });

            Log.d(TAG, "onCreate: App launched for first time");
            showProgressDialog();
            startRegistration();

            // authParams
            // authResult
            // registerApp
            // authentication
            // authorizations

            btnEnter.setText(getString(R.string.register));
        } else {
            // App already registered

            Log.d(TAG, "onCreate: App launched not first time");
            etPhoneNumber.setText(sharedPreference.getData(Constants.PHONE_NUMBER));
            etPhoneNumber.setSelection(etPhoneNumber.getEditableText().length());
            etUserId.setText(sharedPreference.getData(Constants.ASAN_ID));
            etUserId.setSelection(etUserId.getEditableText().length());
            showProgressDialog();
            startAuthentication();

            // authParams
            // authResult  (without showing info dialog)
            // authorizations

            tvHeader.setText("");
            btnEnter.setText(getString(R.string.enter));
        }

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressDialog();
                authParams();
            }
        });
    }

    private void startRegistration() {
        Call<String> startRegistrationCall = apiInterface.startRegistration();

        startRegistrationCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (dialogProgress.isShowing())
                    dialogProgress.dismiss();

                int statusCode = response.code();
                Log.d(TAG, "startRegistration: onResponse: statusCode: " + statusCode +
                        "\nmessage: " + response.message());

                if (statusCode == 200) {
                    // get Operation ID
                    operationId = response.body();
                    Log.d(TAG, "startRegistration: onResponse: operationId: " + operationId);
                } else {
                    // unSuccessful
                    Log.w(TAG, "startRegistration: onResponse");
                    errorBodyConverter(AuthenticationActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                if (dialogProgress.isShowing())
                    dialogProgress.dismiss();
                Log.e(TAG, "onFailure: ", t);
            }
        });

    }

    private void startAuthentication() {
        appCode = sharedPreference.getData(Constants.APP_CODE);
        Call<String> startAuthenticationCall = apiInterface.startAuthentication(appCode);

        startAuthenticationCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (dialogProgress.isShowing())
                    dialogProgress.dismiss();

                int statusCode = response.code();
                Log.d(TAG, "startAuthentication: onResponse: statusCode: " + statusCode +
                        "\nmessage: " + response.message());

                if (statusCode == 200) {
                    operationId = response.body();
                    sharedPreference.saveData(Constants.AUTHENTICATE_OPERATION_ID, operationId);
                    Log.d(TAG, "startAuthentication: onResponse: operationId: " + operationId);

                    if (isFirstRun()) {
                        showProgressDialog();
                        getAuthorizations();
                    }
                } else {
                    // unSuccessful
                    Converter<ResponseBody, ErrorWrapper> converter = ApiClient.getClient().responseBodyConverter(ErrorWrapper.class, new Annotation[0]);
                    try {
                        ErrorWrapper errorWrapper = converter.convert(response.errorBody());
                        Log.i(TAG, "onResponse: Error Message:" + errorWrapper.getError());
                        showErrorDialog(errorWrapper.getError());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                if (dialogProgress.isShowing())
                    dialogProgress.dismiss();
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void authParams() {

        phoneNumber = etPhoneNumber.getEditableText().toString();

        Log.d(TAG, "onCreate: phoneNumber before regex: " + phoneNumber);

        phoneNumber = phoneNumber.replaceAll(" ", "");

        Log.d(TAG, "onCreate: phoneNumber after regex: " + phoneNumber);

        Auth auth = new Auth();

        //phoneNumber = etPhoneNumber.getEditableText().toString();
        userId = etUserId.getEditableText().toString();

        sharedPreference.saveData(Constants.PHONE_NUMBER, phoneNumber);
        sharedPreference.saveData(Constants.ASAN_ID, userId);

        auth.setPhoneNumber(phoneNumber);
        auth.setAsanID(userId);

        Call<Transaction> authParamsCall = apiInterface.authParams(operationId, auth);

        authParamsCall.enqueue(new Callback<Transaction>() {
            @Override
            public void onResponse(@NonNull Call<Transaction> call, @NonNull Response<Transaction> response) {
                if (dialogProgress.isShowing())
                    dialogProgress.dismiss();

                int statusCode = response.code();
                Log.d(TAG, "authParams: onResponse: statusCode: " + statusCode +
                        "\nmessage: " + response.message());

                if (statusCode == 200 && response.body() != null) {
                    // get Transaction ID and show dialog
                    Transaction transaction = response.body();
                    transactionId = transaction.getTransactionId();
                    showVerificationDialog(transaction.getVerificationCode());
                } else {
                    // unSuccessful
                    Log.w(TAG, "authParams: onResponse");
                    errorBodyConverter(AuthenticationActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Transaction> call, @NonNull Throwable t) {
                if (dialogProgress.isShowing())
                    dialogProgress.dismiss();
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void getAuthResultRegistration() {
        Call<AuthResult> authResultCall = apiInterface.authResultRegistration(operationId, transactionId);

        authResultCall.enqueue(new Callback<AuthResult>() {
            @Override
            public void onResponse(@NonNull Call<AuthResult> call, @NonNull Response<AuthResult> response) {
                if (dialogVerification.isShowing())
                    dialogVerification.dismiss();
                dialogVerification = null;

                int statusCode = response.code();

                Log.d(TAG, "AuthResultRegistration: onResponse: statusCode" + statusCode +
                        "\nmessage: " + response.message());

                if (statusCode == 200 && response.body() != null) {
                    AuthResult authResult = response.body();
                    Log.d(TAG, "onResponse: authResult: " + authResult.toString());

                    Gson gson = new Gson();
                    String authResultObject = gson.toJson(authResult);
                    sharedPreference.saveData(Constants.AUTH_RESULT_OBJECT, authResultObject);
                    showUserDetailsDialog(authResult);
                } else {
                    // unSuccessful
                    Log.w(TAG, "getAuthResultRegistration: onResponse");
                    errorBodyConverter(AuthenticationActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResult> call, @NonNull Throwable t) {
                if (dialogProgress.isShowing())
                    dialogProgress.dismiss();
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void getAuthResultAuthentication() {
        Call<AuthResult> authResultCall = apiInterface.authResultAuthentication(operationId, appCode, transactionId);

        authResultCall.enqueue(new Callback<AuthResult>() {
            @Override
            public void onResponse(@NonNull Call<AuthResult> call, @NonNull Response<AuthResult> response) {
                if (dialogVerification.isShowing())
                    dialogVerification.dismiss();
                dialogVerification = null;

                if (response.headers().get(Constants.TOKEN) != null) {
                    token = response.headers().get(Constants.TOKEN);
                    sharedPreference.saveData(Constants.TOKEN, token);
                    Log.d(TAG, "authResult: onResponse: token: " + token);
                }

                int statusCode = response.code();
                Log.d(TAG, "AuthResultAuthentication: onResponse: statusCode: " + statusCode +
                        "\nmessage: " + response.message());

                if (statusCode == 200 && response.body() != null) {
                    AuthResult authResult = response.body();
                    Log.d(TAG, "onResponse: authResult: " + authResult.toString());

                    showProgressDialog();
                    getAuthorizations();
                } else {
                    // unSuccessful
                    Log.w(TAG, "getAuthResultAuthentication: onResponse");
                    errorBodyConverter(AuthenticationActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResult> call, @NonNull Throwable t) {
                if (dialogProgress.isShowing())
                    dialogProgress.dismiss();
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void misMatchPersonInfo() {
        Call<Object> misMatchPersonInfoCall = apiInterface.mismatchPersonInfo(operationId, token);

        misMatchPersonInfoCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                if (dialogProgress.isShowing())
                    dialogProgress.dismiss();

                int statusCode = response.code();

                Log.d(TAG, "misMatchPersonInfo: onResponse: statusCode: " + statusCode +
                        "\nmessage: " + response.message());

                if (statusCode == 200) {
                    // show dialog for mismatch
                    showErrorDialog("Xahiş olunur operatorla əlaqə saxlayın");
                } else {
                    Log.w(TAG, "mismatchPersonInfo: onResponse");
                    errorBodyConverter(AuthenticationActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                if (dialogProgress.isShowing())
                    dialogProgress.dismiss();
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void registerApp() {
        Call<Object> registerAppCall = apiInterface.registerApp(operationId, appCode);

        registerAppCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                if (dialogProgress.isShowing())
                    dialogProgress.dismiss();

                if (response.headers().get(Constants.TOKEN) != null) {
                    token = response.headers().get(Constants.TOKEN);
                    sharedPreference.saveData(Constants.TOKEN, token);
                }

                int statusCode = response.code();

                Log.d(TAG, "registerApp: onResponse: statusCode: " + statusCode +
                        "\nmessage: " + response.message());

                if (statusCode == 200) {
                    showProgressDialog();
                    startAuthentication();
                } else {
                    // register problem
                    // unSuccessful
                    Log.w(TAG, "registerApp: onResponse");
                    errorBodyConverter(AuthenticationActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                if (dialogProgress.isShowing())
                    dialogProgress.dismiss();
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void getAuthorizations() {
        Call<Authorization> authorizationsCall = apiInterface.authorizations(operationId, token, appCode, phoneNumber, userId);

        authorizationsCall.enqueue(new Callback<Authorization>() {
            @Override
            public void onResponse(@NonNull Call<Authorization> call, @NonNull Response<Authorization> response) {
                if (dialogProgress.isShowing())
                    dialogProgress.dismiss();

                int statusCode = response.code();

                Log.d(TAG, "Authorizations: onResponse: statusCode: " + statusCode +
                        "\nmessage: " + response.message());

                if (statusCode == 200 && response.body() != null) {
                    // successful
                    sharedPreference.saveData("firstRun", false);
                    Authorization authorization = response.body();

                    Log.d(TAG, "onResponse: authorizations: " + authorization.toString());

                    Gson gson = new Gson();

                    String authorizationObject = gson.toJson(authorization);

                    sharedPreference.saveData("authorizationObject", authorizationObject);

                    Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    // unSuccessful
                    Log.w(TAG, "getAuthorizations : onResponse");
                    errorBodyConverter(AuthenticationActivity.this, response);
                }
            }

            @Override
            public void onFailure(Call<Authorization> call, Throwable t) {
                if (dialogProgress.isShowing())
                    dialogProgress.dismiss();
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private boolean isFirstRun() {
        Bundle bundle = getIntent().getExtras();
        return bundle != null && bundle.getBoolean("firstRun", true);
    }

    private void showUserDetailsDialog(AuthResult authResult) {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .customView(R.layout.dialog_auth_results, false)
                .positiveText("TƏSDİQLƏ")
                .negativeText("İMTİNA")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        uuidGenerator();
                        showProgressDialog();
                        registerApp();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        misMatchPersonInfo();
                    }
                }).build();


        View view = dialog.getCustomView();

        if (view != null) {
            TextView tvName = view.findViewById(R.id.tvNameAuthResultDialog);
            TextView tvSurname = view.findViewById(R.id.tvSurnameAuthResultDialog);
            TextView tvPatronymic = view.findViewById(R.id.tvFatherNameAuthResultDialog);
            TextView tvLivingPlace = view.findViewById(R.id.tvLivingPlaceAuthResultDialog);
            TextView tvSerialNumber = view.findViewById(R.id.tvIdDocumentAuthResultDialog);
            TextView tvPinCode = view.findViewById(R.id.tvPinCodeAuthResultDialog);

            tvName.setText(authResult.getFirstName());
            tvSurname.setText(authResult.getLastName());
            tvPatronymic.setText(authResult.getPatronymic());
            tvLivingPlace.setText(authResult.getAddress());
            tvSerialNumber.setText(authResult.getIdDocument());
            tvPinCode.setText(authResult.getPinCode());
        }

        dialog.show();
    }

    private void showVerificationDialog(String verificationCode) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .cancelable(false)
                .title("Yoxlama Kodu")
                .content(verificationCode);

        if (dialogVerification == null)
            dialogVerification = builder.build();

        dialogVerification.show();

        if (isFirstRun())
            getAuthResultRegistration();
        else
            getAuthResultAuthentication();
    }

    private void showProgressDialog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .cancelable(false)
                .content(getString(R.string.wait))
                .progress(true, 0);

        if (dialogProgress == null)
            dialogProgress = builder.build();
        dialogProgress.show();
    }

    private void showErrorDialog(String errorText) {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Diqqət")
                .content(errorText)
                .positiveText("Ok")
                .build();

        dialog.show();
    }

    private void uuidGenerator() {
        appCode = UUID.randomUUID().toString().replace("-", "");
        sharedPreference.saveData(Constants.APP_CODE, appCode);
        Log.d(TAG, "uuidGenerator: appCode: " + appCode);
    }
}
