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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
 * Created by javadbadirkhanly on 8/10/17.
 */

public class ContractConfirmationActivity extends ParentActivity {

    private static final String TAG = ContractConfirmationActivity.class.getSimpleName();

    @BindView(R.id.toolbarContractConfirmationActivity)
    Toolbar toolbar;

    @BindView(R.id.imgCancelContractConfirmationActivity)
    ImageView imgCancel;

    @BindView(R.id.tvCalculatedPriceContractConfirmationActivity)
    TextView tvCalculatedPrice;

    @BindView(R.id.tvBmCoefficientContractConfirmationActivity)
    TextView tvBmCoefficient;

    @BindView(R.id.tvInsurancePriceContractConfirmationActivity)
    TextView tvInsurancePrice;

    @BindView(R.id.lnCertificateNumberContractConfirmationActivity)
    LinearLayout lnCertificateNumber;

    @BindView(R.id.tvCertificateNumberContractConfirmationActivity)
    TextView tvCertificateNumber;

    @BindView(R.id.lnCarNumberContractConfirmationActivity)
    LinearLayout lnCarNumber;

    @BindView(R.id.tvCarNumberContractConfirmationActivity)
    TextView tvCarNumber;

    @BindView(R.id.lnVehicleNameContractConfirmationActivity)
    LinearLayout lnVehicleName;

    @BindView(R.id.tvVehicleNameContractConfirmationActivity)
    TextView tvVehicleName;

    @BindView(R.id.lnInsuredPersonContractConfirmationActivity)
    LinearLayout lnInsuredPerson;

    @BindView(R.id.tvInsuredPersonContractConfirmationActivity)
    TextView tvInsuredPerson;

    @BindView(R.id.btnConfirmContractConfirmationActivity)
    Button btnConfirm;

    private ApiInterface apiInterface;

    private String operationId;
    private String token;
    private String appCode;

    private MaterialDialog dialog = null;

    private SharedPreference sharedPreference;

    private boolean isPriceFromLastContract;

    private boolean isSessionFinished;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_contract_confirmation);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        toolbar.setTitle("Müqavilə");

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCancelOperationDialog();
            }
        });

        sharedPreference = new SharedPreference(this);

        operationId = sharedPreference.getData(Constants.CONTRACT_OPERATION_ID);
        token = sharedPreference.getData(Constants.TOKEN);
        appCode = sharedPreference.getData(Constants.APP_CODE);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        getBundle();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPriceFromLastContract) {
                    isSessionFinished = true;
                    Intent intent = new Intent(ContractConfirmationActivity.this, SignContractActivity.class);
                    startActivity(intent);
                } else {
                    showPriceWarningDialog();
                }
            }
        });

    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            Log.d(TAG, "getBundle: bundle is NOT null");
            if (bundle.getString(Constants.CONTRACT_PRICE_OBJECT) != null) {
                btnConfirm.setEnabled(true);
                jsonParser(bundle.getString(Constants.CONTRACT_PRICE_OBJECT));
                return;
            }
        }
        Log.d(TAG, "getBundle: bundle is null");
    }

    private void jsonParser(String json) {
        Gson gson = new Gson();
        ContractPrice contractPrice = gson.fromJson(json, ContractPrice.class);
        isPriceFromLastContract = contractPrice.isPriceFromLastContract();
        fillingLayouts(contractPrice);
    }

    private void cancelContractOperation() {
        Call<Object> cancelContractOperationCall = apiInterface.cancelContractOperation(operationId, token, appCode);

        cancelContractOperationCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                if (dialog.isShowing())
                    dialog.dismiss();

                int statusCode = response.code();

                Log.i(TAG, "cancelContractOperation: onResponse: statusCode: " + statusCode + "\nMessage: " + response.message());

                if (statusCode == 200) {
                    isSessionFinished = true;
                    sharedPreference.saveData(Constants.IS_UNFINISHED, false);
                    Intent intent = new Intent(ContractConfirmationActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else if (statusCode == 401) {
                    showTokenExpiredDialog(ContractConfirmationActivity.this);
                } else {
                    Log.w(TAG, "cancelContractOperation: onResponse");
                    errorBodyConverter(ContractConfirmationActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                showFailureDialog(ContractConfirmationActivity.this);
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void fillingLayouts(ContractPrice contractPrice) {
        tvCalculatedPrice.setText(contractPrice.getCalculatedPrice() + " AZN");
        tvBmCoefficient.setText(contractPrice.getBmCoefficient());
        tvInsurancePrice.setText(contractPrice.getPrice() + " AZN");

        if (!sharedPreference.getData(Constants.INSURED_CERTIFICATE_NUMBER).equals("")) {
            lnCertificateNumber.setVisibility(View.VISIBLE);
            tvCertificateNumber.setText(sharedPreference.getData(Constants.INSURED_CERTIFICATE_NUMBER));
        }

        if (!sharedPreference.getData(Constants.INSURED_CAR_NUMBER).equals("")) {
            lnCarNumber.setVisibility(View.VISIBLE);
            tvCarNumber.setText(sharedPreference.getData(Constants.INSURED_CAR_NUMBER));
        }

        if (!sharedPreference.getData(Constants.INSURED_VEHICLE_NAME).equals(" ")) {
            lnVehicleName.setVisibility(View.VISIBLE);
            tvVehicleName.setText(sharedPreference.getData(Constants.INSURED_VEHICLE_NAME));
        }

        if (!sharedPreference.getData(Constants.INSURED_FULL_NAME).equals("")) {
            lnInsuredPerson.setVisibility(View.VISIBLE);
            tvInsuredPerson.setText(sharedPreference.getData(Constants.INSURED_FULL_NAME));
        }
    }

    /* ----- Dialogs ------ */
    private void showPriceWarningDialog() {
        new MaterialDialog.Builder(this)
                .title("Diqqət")
                .content("NV-nin qeydiyyat şəhadətnaməsi məlumatları əvvəlki sığorta müqaviləsində " +
                        "daxil edilmiş məlumatlarla uyğun gəlmir. Zəhmət olmazsa, " +
                        "DİN-lə əlaqə saxlayıb məlumatları dəqiqləşdirin.")
                .positiveText("Yaxşı")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        isSessionFinished = true;
                        Intent intent = new Intent(ContractConfirmationActivity.this, SignContractActivity.class);
                        startActivity(intent);
                    }
                })
                .cancelable(false)
                .build()
                .show();
    }

    private void showCancelOperationDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(ContractConfirmationActivity.this)
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

    private void showProgress() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .content("Gözləyin...")
                .progress(true, 0)
                .cancelable(false);

        dialog = builder.build();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
        sharedPreference.saveData(Constants.UNFINISHED_SESSION_ACTIVITY, ContractConfirmationActivity.class.getName());
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
            tvCalculatedPrice.setText(sharedPreference.getData(Constants.INSURANCE_CALCULATED_PRICE));
            tvBmCoefficient.setText(sharedPreference.getData(Constants.INSURANCE_BM_COEFFICIENT));
            tvInsurancePrice.setText(sharedPreference.getData(Constants.INSURANCE_PRICE));

            lnCertificateNumber.setVisibility(View.VISIBLE);
            tvCertificateNumber.setText(sharedPreference.getData(Constants.INSURED_CERTIFICATE_NUMBER));

            lnCarNumber.setVisibility(View.VISIBLE);
            tvCarNumber.setText(sharedPreference.getData(Constants.INSURED_CAR_NUMBER));

            lnInsuredPerson.setVisibility(View.VISIBLE);
            tvInsuredPerson.setText(sharedPreference.getData(Constants.INSURED_FULL_NAME));

            lnVehicleName.setVisibility(View.VISIBLE);
            tvVehicleName.setText(sharedPreference.getData(Constants.INSURED_VEHICLE_NAME));
        }
    }

    private void saveSession() {
        if (!isSessionFinished) {
            sharedPreference.saveData(Constants.IS_UNFINISHED, true);
            sharedPreference.saveData(Constants.UNFINISHED_SESSION_ACTIVITY, ContractConfirmationActivity.class.getName());
            sharedPreference.saveData(Constants.INSURANCE_CALCULATED_PRICE, tvCalculatedPrice.getText().toString());
            sharedPreference.saveData(Constants.INSURANCE_BM_COEFFICIENT, tvBmCoefficient.getText().toString());
            sharedPreference.saveData(Constants.INSURANCE_PRICE, tvInsurancePrice.getText().toString());
        }
    }
}