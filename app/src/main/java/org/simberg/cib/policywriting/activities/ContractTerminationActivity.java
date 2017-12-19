package org.simberg.cib.policywriting.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

import org.simberg.cib.policywriting.R;
import org.simberg.cib.policywriting.java.Constants;
import org.simberg.cib.policywriting.java.SharedPreference;
import org.simberg.cib.policywriting.models.response.ContractTermination;
import org.simberg.cib.policywriting.rest.ApiClient;
import org.simberg.cib.policywriting.rest.ApiInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by javadbadirkhanly on 9/6/17.
 */

public class ContractTerminationActivity extends ParentActivity {

    private final static String TAG = ContractTerminationActivity.class.getSimpleName();

    @BindView(R.id.toolbarContractTerminationActivity)
    Toolbar toolbar;

    @BindView(R.id.imgCancelContractTerminationActivity)
    ImageView imgCancel;

    @BindView(R.id.etContractNumberContractTerminationActivity)
    EditText etContractNumber;

    @BindView(R.id.btnSearchContractNumberContractTerminationActivity)
    Button btnSearch;

    private ApiInterface apiInterface;

    private SharedPreference sharedPreference;

    private MaterialDialog dialog;

    private String token;
    private String appCode;
    private String operationCode;

    private boolean isSessionFinished;

    private ContractTermination contractTermination;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_contract_termination);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (getSupportActionBar()!= null)
            getSupportActionBar().setTitle("Müqavilənin xitamı");

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        sharedPreference = new SharedPreference(this);

        token = sharedPreference.getData(Constants.TOKEN);
        appCode = sharedPreference.getData(Constants.APP_CODE);
        operationCode = sharedPreference.getData(Constants.OPERATION_CODE);

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCancelOperationDialog();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etContractNumber.getText().toString().equals("")) {
                    showProgress();
                    getTerminationContract(etContractNumber.getEditableText().toString());
                }
            }
        });
    }

    private void getTerminationContract(String contractNumber) {
        Call<ContractTermination> getContractTerminationCall = apiInterface.getTerminationContract(token, appCode, contractNumber, operationCode);

        getContractTerminationCall.enqueue(new Callback<ContractTermination>() {
            @Override
            public void onResponse(@NonNull Call<ContractTermination> call, @NonNull Response<ContractTermination> response) {
                if (dialog.isShowing())
                    dialog.dismiss();
                int statusCode = response.code();

                Log.d(TAG, "getContractTermination: onResponse: statusCode: " + statusCode +
                        "\nmessage: " + response.message());

                if (statusCode == 200){
                    contractTermination = response.body();
                    Log.d(TAG, "onResponse: contractTermination: " + contractTermination.toString());

                    Gson gson = new Gson();

                    String contractTerminationObject = gson.toJson(contractTermination);

                    Intent intent = new Intent(ContractTerminationActivity.this, TerminateContractActivity.class);
                    intent.putExtra(Constants.EXTRA_CONTRACT_TERMINATION_OBJECT, contractTerminationObject);
                    startActivity(intent);
                } else if (statusCode == 401){
                    showTokenExpiredDialog(ContractTerminationActivity.this);
                } else {
                    Log.w(TAG, "getTerminationContract: onResponse");
                    errorBodyConverter(ContractTerminationActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ContractTermination> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                showFailureDialog(ContractTerminationActivity.this);
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void cancelOperation(){
        isSessionFinished = true;
        sharedPreference.saveData(Constants.IS_UNFINISHED, false);
        Intent intent = new Intent(ContractTerminationActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /* ----- Dialogs ----- */
    private void showCancelOperationDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(ContractTerminationActivity.this)
                .title("Diqqət")
                .content("Əməliyyatı ləğv etmək istədiyinizə əminsiniz?")
                .positiveText("Bəli")
                .negativeText("Xeyr")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        cancelOperation();
                    }
                }).build();

        dialog.show();
    }

    private void showProgress() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .content("Gözləyin...")
                .progress(true, 0)
                .cancelable(false);

        dialog = builder.build();
        dialog.show();
    }

    private void showBackPressDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(ContractTerminationActivity.this)
                .title("Diqqət")
                .content("Sizin yarımçıq əməliyyatınız qalıb.")
                .negativeText("Davam et")
                .positiveText("Ləğv et")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                    }
                }).build();

        dialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showBackPressDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreference.saveData(Constants.IS_UNFINISHED, true);
        Log.d(TAG, "onResume: IS_UNFINISHED: " + sharedPreference.getData(Constants.IS_UNFINISHED, false));
        sharedPreference.saveData(Constants.UNFINISHED_SESSION_ACTIVITY, ContractTerminationActivity.class.getName());
    }

    @Override
    protected void onStart() {
        super.onStart();
        restoreSession();
    }

    @Override
    protected void onStop() {
        saveSession();
        super.onStop();
    }

    private void restoreSession() {
        if (sharedPreference.getData(Constants.IS_UNFINISHED, false)) {
            etContractNumber.setText(sharedPreference.getData(Constants.CONTRACT_NUMBER));
            etContractNumber.setSelection(etContractNumber.getEditableText().length());
        }
    }

    private void saveSession() {
        if (!isSessionFinished) {
            sharedPreference.saveData(Constants.CONTRACT_NUMBER, etContractNumber.getEditableText().toString());

            sharedPreference.saveData(Constants.IS_UNFINISHED, true);
            sharedPreference.saveData(Constants.UNFINISHED_SESSION_ACTIVITY, ContractTerminationActivity.class.getName());
        }
    }
}
