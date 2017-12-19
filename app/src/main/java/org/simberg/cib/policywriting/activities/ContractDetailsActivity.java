package org.simberg.cib.policywriting.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.simberg.cib.policywriting.BuildConfig;
import org.simberg.cib.policywriting.R;
import org.simberg.cib.policywriting.java.Constants;
import org.simberg.cib.policywriting.java.DateUtil;
import org.simberg.cib.policywriting.models.ContractDetails;
import org.simberg.cib.policywriting.rest.ApiClient;
import org.simberg.cib.policywriting.rest.ApiInterface;
import org.simberg.cib.policywriting.rest.CustomTrust;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by javadbadirkhanly on 9/5/17.
 */

public class ContractDetailsActivity extends ParentActivity {

    private final static String TAG = ContractDetailsActivity.class.getSimpleName();

    @BindView(R.id.toolbarContractDetailsActivity)
    Toolbar toolbar;

    @BindView(R.id.cardViewContractDetailsActivity)
    CardView cardView;

    @BindView(R.id.tvContractNumberContractDetailsActivity)
    TextView tvContractNumber;

    @BindView(R.id.tvContractEffectiveDateContractDetailsActivity)
    TextView tvContractEffectiveDate;

    @BindView(R.id.tvContractExpiryDateContractDetailsActivity)
    TextView tvContractExpiryDate;

    @BindView(R.id.tvContractStatusContractDetailsActivity)
    TextView tvStatus;

    @BindView(R.id.imgDownloadPdfContractDetailsActivity)
    ImageView imgDownloadPdf;

    @BindView(R.id.tvInsurerNameContractDetailsActivity)
    TextView tvInsuranceCompanyName;

    @BindView(R.id.tvInsuredNameContractDetailsActivity)
    TextView tvInsuredName;

    @BindView(R.id.tvCarModelContractDetailsActivity)
    TextView tvCarModel;

    @BindView(R.id.tvInsurePriceContractDetailsActivity)
    TextView tvInsurancePrice;

    private ApiInterface apiInterface;

    private SharedPreferences sharedPreferences;

    private MaterialDialog dialog;

    private String token;
    private String appCode;

    private String contractNumber;

    private String encodedDocument;

    private byte[] decodedDocument;

    private ContractDetails contractDetails;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_contract_details);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Müqavilə Detalları");

        getBundle();

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new CustomTrust(getApplicationContext());

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        sharedPreferences = getSharedPreferences(Constants.PROJECT_NAME, MODE_PRIVATE);

        token = sharedPreferences.getString(Constants.TOKEN, "Not Found!");
        appCode = sharedPreferences.getString(Constants.APP_CODE, "Not Found!");

        showProgress();
        getContractDetails();

        imgDownloadPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress();
                getContractDocument(contractNumber);
            }
        });
    }

    private void getContractDetails() {
        Call<ContractDetails> getContractDetailsCall = apiInterface.getContract(token, appCode, contractNumber);

        getContractDetailsCall.enqueue(new Callback<ContractDetails>() {
            @Override
            public void onResponse(@NonNull Call<ContractDetails> call, @NonNull Response<ContractDetails> response) {
                if (dialog.isShowing())
                    dialog.dismiss();
                int statusCode = response.code();

                Log.i(TAG, "onResponse: statusCode: " + statusCode + "\nMessage: " + response.message());

                if (statusCode == 200 && response.body() != null) {
                    contractDetails = response.body();

                    Log.i(TAG, "getContractDetails: onResponse: " + contractDetails.toString());

                    initFields(contractDetails);
                } else if (statusCode == 401) {
                    showTokenExpiredDialog(ContractDetailsActivity.this);
                } else {
                    Log.w(TAG, "getContractDetails: onResponse");
                    errorBodyConverter(ContractDetailsActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ContractDetails> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                showFailureDialog(ContractDetailsActivity.this);
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void getContractDocument(String contractNumber) {
        Call<String> contractDocumentCall = apiInterface.getContractDocument(token, appCode, contractNumber);

        contractDocumentCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call,@NonNull Response<String> response) {
                if (dialog.isShowing())
                    dialog.dismiss();

                int statusCode = response.code();

                Log.i(TAG, "getContractDocument: onResponse: statusCode: " + statusCode + "\nMessage: " + response.message());

                if (statusCode == 200 && response.body() != null) {
                    encodedDocument = response.body();

                    documentDecoder(encodedDocument);

                    Log.i(TAG, "onResponse: encodedDocument: " + encodedDocument);
                } else if (statusCode == 401) {
                    showTokenExpiredDialog(ContractDetailsActivity.this);
                } else {
                    Log.w(TAG, "getContractDocument: onResponse");
                    errorBodyConverter(ContractDetailsActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void documentDecoder(String encodedDocument) {
        decodedDocument = android.util.Base64.decode(encodedDocument, android.util.Base64.DEFAULT);

        File createFile = new File(Environment.getExternalStorageDirectory().getPath() + "/PolicyWriting/");
        String folderPath = createFile.getPath();
        Log.d(TAG, "documentDecoder: fileAbsolutePath: " + createFile.getAbsolutePath() +
                "\nfilePath: " + createFile.getPath());

        createFile.mkdirs();

        File outputFile = new File(createFile, "Sample.pdf");
        try {
            FileOutputStream fos = new FileOutputStream(outputFile);
            String filePath = folderPath + "/Sample.pdf";

            OutputStream pdfFos = new FileOutputStream(filePath);
            pdfFos.write(decodedDocument);
            pdfFos.flush();
            pdfFos.close();

            readPdfFile(outputFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readPdfFile(File file) {
        Uri pdfUri = FileProvider.getUriForFile(ContractDetailsActivity.this,
                BuildConfig.APPLICATION_ID + ".provider",
                file);

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(pdfUri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(ContractDetailsActivity.this, "No PDF reader found to open this file.", Toast.LENGTH_SHORT).show();
        }
    }

    private void initFields(ContractDetails contractDetails) {
        tvContractNumber.setText(contractDetails.getContractNumber());
        tvContractEffectiveDate.setText(DateUtil.DateFormatter(contractDetails.getEffectiveDate()));
        tvContractExpiryDate.setText(DateUtil.DateFormatter(contractDetails.getExpiryDate()));

        tvStatus.setText(contractDetails.getStatus().getName());

        tvInsuranceCompanyName.setText(contractDetails.getInsuranceCompanyName());
        tvInsuredName.setText(contractDetails.getInsuredPerson().getFullName());
        tvCarModel.setText(contractDetails.getVehicle().getMake() + " " + contractDetails.getVehicle().getModel()); // TODO: 11/17/17 update string
        tvInsurancePrice.setText(getString(R.string.price, contractDetails.getContractPrice().getPrice()));

        cardView.setCardBackgroundColor(getColor(contractDetails.getStatus().getCode()));
    }

    private int getColor(String statusCode) {
        switch (statusCode) {
            case "TERMINATED_CONTRACT_STATUS":
                return Color.parseColor("#01BECF");
            case "CANCELLED_CONTRACT_STATUS":
                return Color.parseColor("#E74C3C");
            case "EXPIRED_CONTRACT_STATUS":
                return Color.parseColor("#9B59B6");
            case "ONGOING_CONTRACT_STATUS":
                return Color.parseColor("#1ABC9C");
            case "PENDING_CONTRACT_STATUS":
                return Color.parseColor("#F39C12");
            case "DRAFTED_CONTRACT_STATUS":
                return Color.parseColor("#3498Db");
            default:
                return Color.parseColor("#000000");
        }
    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            contractNumber = bundle.getString(Constants.CONTRACT_NUMBER, "Not Found!");
            Log.i(TAG, "getBundle: contractNumber: " + contractNumber);
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }
}
