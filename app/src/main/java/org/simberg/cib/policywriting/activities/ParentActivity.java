package org.simberg.cib.policywriting.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.simberg.cib.policywriting.java.Constants;
import org.simberg.cib.policywriting.java.SharedPreference;
import org.simberg.cib.policywriting.models.ErrorWrapper;
import org.simberg.cib.policywriting.rest.ApiClient;
import org.simberg.cib.policywriting.rest.ApiInterface;
import org.simberg.cib.policywriting.rest.CustomTrust;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by javadbadirkhanly on 9/13/17.
 */

public abstract class ParentActivity extends AppCompatActivity {

    private String TAG;

    private boolean isSessionFinished;

    private MaterialDialog dialog;

    private ApiInterface apiInterface;

    private SharedPreference sharedPreference;

    private String operationId;
    private String token;
    private String appCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomTrust.getInstance(getApplicationContext());

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sharedPreference = new SharedPreference(this);

        operationId = sharedPreference.getData(Constants.CONTRACT_OPERATION_ID);
        token = sharedPreference.getData(Constants.TOKEN);
        appCode = sharedPreference.getData(Constants.APP_CODE);
    }

    public void errorBodyConverter(Context context, Response response) {
        String TAG = context.getClass().getSimpleName();
        Converter<ResponseBody, ErrorWrapper> converter = ApiClient.getClient().responseBodyConverter(ErrorWrapper.class, new Annotation[0]);
        try {
            ErrorWrapper errorWrapper = converter.convert(response.errorBody());
            Log.w(TAG, "onResponse: Error Message:" + errorWrapper.getError());
            showErrorDialog(context, errorWrapper.getError());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showTokenExpiredDialog(final Context context) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title("Diqqət")
                .content("Əməliyyata başlamaq üşün daxil olun.")
                .positiveText("DAXİL OL")
                .negativeText("LƏĞV ET")
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        tokenExpired(context);
                    }
                }).build();
        dialog.show();
    }

    public void showFailureDialog(Context context) {
        new MaterialDialog.Builder(context)
                .title("Diqqət")
                .content("Sorğu üçün ayrılmış müddət bitdi. Zəhmət olmasa bir daha yoxlayın.")
                .positiveText("Oldu")
                .build()
                .show();
    }

    public void showNetworkStateDialog(Context context) {
        new MaterialDialog.Builder(context)
                .title("Diqqət")
                .content("İnternet bağlantınızı yoxlayın və yenidən cəhd edin.")
                .positiveText("Oldu")
                .build()
                .show();
    }

    public void tokenExpired(Context context) {
        Intent intent = new Intent(context, AuthenticationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("firstRun", false);
        startActivity(intent);
        finish();
    }

    public void showErrorDialog(Context context, String errorContent) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title("Diqqət")
                .content(errorContent)
                .positiveText("Ok")
                .cancelable(false)
                .build();
        dialog.show();
    }

    public void showProgress(Context context) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
                .content("Gözləyin...")
                .progress(true, 0)
                .cancelable(false);

        dialog = builder.build();
        dialog.show();
    }

    public boolean isNetworkOnline() {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;
    }
}
