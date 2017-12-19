package org.simberg.cib.policywriting.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

import org.simberg.cib.policywriting.R;
import org.simberg.cib.policywriting.java.Constants;
import org.simberg.cib.policywriting.java.SharedPreference;
import org.simberg.cib.policywriting.models.ContractPrice;
import org.simberg.cib.policywriting.rest.ApiClient;
import org.simberg.cib.policywriting.rest.ApiInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by javadbadirkhanly on 11/1/17.
 */

public class PeriodActivity extends ParentActivity {

    private static final String TAG = PeriodActivity.class.getSimpleName();

    @BindView(R.id.toolbarPeriodActivity)
    Toolbar toolbar;

    @BindView(R.id.imgCancelPeriodActivity)
    ImageView imgCancel;

    @BindView(R.id.cvPeriodPeriodActivity)
    CardView cvPeriod;

    @BindView(R.id.spPeriodPeriodActivity)
    Spinner spPeriod;

    @BindView(R.id.btnPeriodEnterPeriodActivity)
    Button btnEnterPeriod;

    private ApiInterface apiInterface;
    private SharedPreference sharedPreference;
    private ContractPrice contractPrice;
    private MaterialDialog dialog;

    private String operationId;
    private String appCode;
    private String token;

    private int period;

    private boolean isSessionFinished;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period);

        ButterKnife.bind(this);

        if (getSupportActionBar() != null){
            setSupportActionBar(toolbar);
        }

        toolbar.setTitle("Sığorta müddəti");

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sharedPreference = new SharedPreference(this);

        operationId = sharedPreference.getData(Constants.CONTRACT_OPERATION_ID);
        appCode = sharedPreference.getData(Constants.APP_CODE);
        token = sharedPreference.getData(Constants.TOKEN);

        btnEnterPeriod.setEnabled(false);

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showCancelOperationDialog();
            }
        });

        spPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    btnEnterPeriod.setEnabled(false);
                } else {
                    btnEnterPeriod.setEnabled(true);
                    String selectedItem = adapterView.getSelectedItem().toString();
                    Log.d(TAG, "onItemSelected: selectedItem: " + selectedItem);
                    period = Integer.parseInt(selectedItem.split(" ")[0]);
                    Log.d(TAG, "onItemSelected: period: " + period);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnEnterPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress();
                getContractDetails(period);
            }
        });
    }

    /* ----- REST ----- */
    private void getContractDetails(int period) {
        Call<ContractPrice> callBorderContractPrice = apiInterface.borderContractPrice(operationId, token, appCode, period + "");

        callBorderContractPrice.enqueue(new Callback<ContractPrice>() {
            @Override
            public void onResponse(@NonNull Call<ContractPrice> call, @NonNull Response<ContractPrice> response) {
                if (dialog.isShowing())
                    dialog.dismiss();

                int statusCode = response.code();

                Log.d(TAG, "getBorderContractDetails: onResponse: statusCode: " + statusCode + "\nMessage: " + response.message());

                if (statusCode == 200 && response.body() != null) {
                    isSessionFinished = true;
                    sharedPreference.saveData(Constants.IS_UNFINISHED, false);

                    contractPrice = response.body();
                    Log.d(TAG, "onResponse: Contract Price: " + contractPrice.toString());

                    Gson gson = new Gson();
                    String contractPriceObject = gson.toJson(contractPrice);

                    Intent intent = new Intent(PeriodActivity.this, ContractConfirmationActivity.class);
                    intent.putExtra(Constants.CONTRACT_PRICE_OBJECT, contractPriceObject);
                    startActivity(intent);
                } else if (statusCode == 401) {
                    showTokenExpiredDialog(PeriodActivity.this);
                } else {
                    Log.w(TAG, "getContractDetails: onResponse");
                    errorBodyConverter(PeriodActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ContractPrice> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                showFailureDialog(PeriodActivity.this);
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void cancelContractOperation() {
        Call<Object> cancelContractOperationCall = apiInterface.cancelContractOperation(operationId, token, appCode);

        cancelContractOperationCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call,@NonNull Response<Object> response) {
                if (dialog.isShowing())
                    dialog.dismiss();

                int statusCode = response.code();

                Log.i(TAG, "onResponse: statusCode: " + statusCode + "\nMessage: " + response.message());

                if (statusCode == 200) {
                    isSessionFinished = true;
                    sharedPreference.saveData(Constants.IS_UNFINISHED, false);

                    Intent intent = new Intent(PeriodActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else if (statusCode == 401) {
                    showTokenExpiredDialog(PeriodActivity.this);
                } else {
                    Log.w(TAG, "cancelContractOperation: onResponse");
                    errorBodyConverter(PeriodActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                showFailureDialog(PeriodActivity.this);
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    /* ----- Dialogs ----- */
    private void showCancelOperationDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(PeriodActivity.this)
                .title("Diqqət")
                .content("Əməliyyatı ləğv etmək istədiyinizə əminsiniz?")
                .positiveText("Bəli")
                .negativeText("Xeyr")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        showProgress();
                        cancelContractOperation();
                    }
                }).build();

        dialog.show();
    }

    private void showProgress(){
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .content("Gözləyin...")
                .progress(true, 0)
                .cancelable(false);

        dialog = builder.build();
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

    private void showBackPressDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Diqqət")
                .content("Sizin yarımçıq əməliyyatınız qalıb.")
                .positiveText("Davam et")
                .negativeText("Ləğv et")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        showProgress();
                        cancelContractOperation();
                    }
                }).build();

        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreference.saveData(Constants.IS_UNFINISHED, true);
        Log.d(TAG, "onResume: IS_UNFINISHED: " + sharedPreference.getData(Constants.IS_UNFINISHED, false));
        sharedPreference.saveData(Constants.UNFINISHED_SESSION_ACTIVITY, PeriodActivity.class.getName());
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
            spPeriod.setSelection(sharedPreference.getData(Constants.SESSION_PERIOD_POSITION, 0));
        }
    }

    private void saveSession() {
        if (!isSessionFinished) {
            sharedPreference.saveData(Constants.SESSION_PERIOD_POSITION, spPeriod.getSelectedItemPosition());
            sharedPreference.saveData(Constants.UNFINISHED_SESSION_ACTIVITY, PeriodActivity.class.getName());
            sharedPreference.saveData(Constants.IS_UNFINISHED, true);
        }
    }
}
