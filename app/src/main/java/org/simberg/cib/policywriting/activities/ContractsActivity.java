package org.simberg.cib.policywriting.activities;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.simberg.cib.policywriting.R;
import org.simberg.cib.policywriting.adapters.ContractsAdapter;
import org.simberg.cib.policywriting.java.Constants;
import org.simberg.cib.policywriting.java.SharedPreference;
import org.simberg.cib.policywriting.models.Contracts;
import org.simberg.cib.policywriting.models.SearchContract;
import org.simberg.cib.policywriting.rest.ApiClient;
import org.simberg.cib.policywriting.rest.ApiInterface;
import org.simberg.cib.policywriting.rest.CustomTrust;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by javadbadirkhanly on 8/31/17.
 */

public class ContractsActivity extends ParentActivity {

    private final static String TAG = ContractsActivity.class.getSimpleName();

    private final static int PERMISSION_REQUEST_CODE = 1;

    @BindView(R.id.toolbarContractsActivity)
    Toolbar toolbar;

    @BindView(R.id.ivSearchContractContractsActivity)
    ImageView ivSearchContract;

    @BindView(R.id.rvContractsActivity)
    RecyclerView rvContracts;

    private AppCompatRadioButton rbContractNumber;
    private AppCompatRadioButton rbCarNumber;
    private TextInputLayout tilSearch;
    private EditText etSearch;

    private ApiInterface apiInterface;

    private SharedPreference sharedPreference;

    private ContractsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private Contracts contracts;

    private String operationId;
    private String token;
    private String appCode;
    private String operationType;

    private MaterialDialog dialog;
    private MaterialDialog dialogSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_contracts);

        ButterKnife.bind(ContractsActivity.this);

        setSupportActionBar(toolbar);
        toolbar.setTitle("Müqavilələr");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        new CustomTrust(getApplicationContext());
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        sharedPreference = new SharedPreference(this);

        operationId = sharedPreference.getData(Constants.CONTRACT_OPERATION_ID);
        token = sharedPreference.getData(Constants.TOKEN);
        appCode = sharedPreference.getData(Constants.APP_CODE);

        getBundle();

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                if (operationType != null) {
                    searchAuthorizedContracts(operationType, "", 0);
                } else {
                    searchMyContracts("", 0);
                }
            } else {
                requestPermission();
            }
        } else {
            if (operationType != null) {
                searchAuthorizedContracts(operationType, "", 0);
            } else {
                searchMyContracts("", 0);
            }
        }

        ivSearchContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearchContractDialog();
            }
        });

        layoutManager = new LinearLayoutManager(ContractsActivity.this);
        rvContracts.setLayoutManager(layoutManager);
    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.getString(Constants.OPERATION_TYPE) != null) {
                Log.d(TAG, "getBundle: operationType is not NULL");
                operationType = bundle.getString(Constants.OPERATION_TYPE);
            } else {
                Log.d(TAG, "getBundle: operationType is NULL");
            }
        } else {
            Log.d(TAG, "getBundle: bundle is NULL");
        }
    }

    private void searchMyContracts(String number, int numberType) {
        showProgress();
        Log.d(TAG, "searchMyContracts: called");
        Log.d(TAG, "searchMyContracts: number: " + number + "\nnumberType: " + numberType);
        SearchContract searchContract = new SearchContract();

        if (numberType == 1) {
            if (!number.equals("")){
                searchContract.setContractNumber(number);
            }
        } else if (numberType == 2){
            if (!number.equals("")){
                searchContract.setCarNumber(number);
            }
        }

        Log.d(TAG, "searchMyContracts: searchContract: " + searchContract.toString());

        Call<Contracts> searchContractsCall = apiInterface.searchMyContracts(token, appCode, searchContract);

        searchContractsCall.enqueue(new Callback<Contracts>() {
            @Override
            public void onResponse(@NonNull Call<Contracts> call, @NonNull Response<Contracts> response) {
                if (dialog.isShowing())
                    dialog.dismiss();

                int statusCode = response.code();

                Log.i(TAG, "onResponse: statusCode: " + statusCode + "\nMessage: " + response.message());

                if (statusCode == 200 && response.body() != null) {
                    contracts = response.body();
                    Log.i(TAG, "searchContracts: onResponse: " + contracts.toString());

                    adapter = new ContractsAdapter(ContractsActivity.this, contracts.getData(), 0);
                    rvContracts.setAdapter(adapter);

                } else if (statusCode == 401) {
                    showTokenExpiredDialog(ContractsActivity.this);
                } else {
                    Log.w(TAG, "searchMyContracts: onResponse");
                    errorBodyConverter(ContractsActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Contracts> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                showFailureDialog(ContractsActivity.this);
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void searchAuthorizedContracts(String operationType, String number, int numberType) {
        showProgress();
        Log.d(TAG, "searchAuthorizedContracts: called");
        Log.d(TAG, "searchAuthorizedContracts: number: " + number + "\nnumberType: " + numberType);

        SearchContract searchContract = new SearchContract();
        searchContract.setParticipantTIN(sharedPreference.getData(Constants.PARTICIPANT_TIN));
        searchContract.setInsurerTIN(sharedPreference.getData(Constants.INSURER_TIN));
        searchContract.setOperationType(operationType);

        if (numberType == 1) {
            if (!number.equals("")){
                searchContract.setContractNumber(number);
            }
        } else if (numberType == 2){
            if (!number.equals("")){
                searchContract.setCarNumber(number);
            }
        }

        Log.d(TAG, "searchAuthorizedContracts: searchContract: " + searchContract.toString());

        Call<Contracts> searchAuthorizedContractsCall = apiInterface.searchAuthorizedContracts(operationId, token, appCode, searchContract);

        searchAuthorizedContractsCall.enqueue(new Callback<Contracts>() {
            @Override
            public void onResponse(@NonNull Call<Contracts> call, @NonNull Response<Contracts> response) {
                if (dialog.isShowing())
                    dialog.dismiss();

                int statusCode = response.code();

                Log.i(TAG, "onResponse: statusCode: " + statusCode + "\nMessage: " + response.message());

                if (statusCode == 200 && response.body() != null) {
                    contracts = response.body();
                    Log.i(TAG, "searchContracts: onResponse: " + contracts.toString());

                    adapter = new ContractsAdapter(ContractsActivity.this, contracts.getData(), 1);
                    rvContracts.setAdapter(adapter);

                } else if (statusCode == 401) {
                    showTokenExpiredDialog(ContractsActivity.this);
                } else {
                    Log.w(TAG, "searchAuthorizedContracts: onResponse");
                    errorBodyConverter(ContractsActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Contracts> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                showFailureDialog(ContractsActivity.this);
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void showSearchContractDialog() {
        dialogSearch = new MaterialDialog.Builder(this)
                .customView(R.layout.dialog_contract_search, false)
                .title("Axtarış")
                .positiveText("Axtar")
                .negativeText("Ləğv et")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (rbContractNumber.isChecked()) {
                            if (operationType != null) {
                                if (etSearch.length() != 0) {
                                    searchAuthorizedContracts(operationType, etSearch.getEditableText().toString(), 1);
                                } else {
                                    searchAuthorizedContracts(operationType, "",0);
                                }
                            } else {
                                if (etSearch.length() != 0) {
                                    searchMyContracts(etSearch.getEditableText().toString(), 1);
                                } else {
                                    searchMyContracts("",0);
                                }
                            }
                        } else {
                            if (operationType != null) {
                                if (etSearch.length() != 0) {
                                    searchAuthorizedContracts(operationType, etSearch.getEditableText().toString(), 2);
                                } else {
                                    searchAuthorizedContracts(operationType, "",0);
                                }
                            } else {
                                if (etSearch.length() != 0) {
                                    searchMyContracts(etSearch.getEditableText().toString(), 2);
                                } else {
                                    searchMyContracts("",0);
                                }
                            }
                        }
                    }
                })
                .build();

        View view = dialogSearch.getCustomView();

        if (view != null) {
            rbContractNumber = view.findViewById(R.id.rbContractNumberContractSearchDialog);
            rbCarNumber = view.findViewById(R.id.rbCarNumberContractSearchDialog);
            tilSearch = view.findViewById(R.id.tilSearchContractSearchDialog);
            etSearch = view.findViewById(R.id.etSearchContractSearchDialog);
        }

        rbContractNumber.setChecked(true);
        tilSearch.setHint("Müqavilə nömrəsi");

        rbContractNumber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    tilSearch.setHint("Müqavilə nömrəsi");
                } else {
                    tilSearch.setHint("Dövlət qeydiyyat nişanı");
                }
            }
        });

        dialogSearch.show();
    }

    private void showProgress() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(ContractsActivity.this)
                .content("Gözləyin...")
                .progress(true, 0)
                .cancelable(false);

        dialog = builder.build();
        dialog.show();
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (operationType != null) {
                        searchAuthorizedContracts(operationType, "", 0);
                    } else {
                        searchMyContracts("", 0);
                    }
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
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
}
