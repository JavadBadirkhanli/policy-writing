package org.simberg.cib.policywriting.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

import org.simberg.cib.policywriting.R;
import org.simberg.cib.policywriting.java.Constants;
import org.simberg.cib.policywriting.java.DateUtil;
import org.simberg.cib.policywriting.java.SharedPreference;
import org.simberg.cib.policywriting.models.Authorization;
import org.simberg.cib.policywriting.models.ErrorWrapper;
import org.simberg.cib.policywriting.models.Insurer;
import org.simberg.cib.policywriting.models.Operation;
import org.simberg.cib.policywriting.models.Participant;
import org.simberg.cib.policywriting.models.TerminateContract;
import org.simberg.cib.policywriting.models.TerminationResult;
import org.simberg.cib.policywriting.models.response.ContractTermination;
import org.simberg.cib.policywriting.rest.ApiClient;
import org.simberg.cib.policywriting.rest.ApiInterface;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by javadbadirkhanly on 11/14/17.
 */

public class TerminateContractActivity extends ParentActivity {

    private static final String TAG = TerminateContractActivity.class.getSimpleName();

    /* ----- Views ----- */

    @BindView(R.id.toolbarTerminateContractActivity)
    Toolbar toolbar;

    @BindView(R.id.imgCancelTerminateContractActivity)
    ImageView imgCancel;

    @BindView(R.id.tvFullNameTerminateContractActivity)
    TextView tvFullName;

    @BindView(R.id.tvCarNumberTerminateContractActivity)
    TextView tvCarNumber;

    @BindView(R.id.tvTerminationDateTerminateContractActivity)
    TextView tvTerminationDate;

    @BindView(R.id.tilTerminationReasonTerminateContractActivity)
    TextInputLayout tilTerminationReason;

    @BindView(R.id.etTerminationReasonTerminateContractActivity)
    EditText etTerminationReason;

    @BindView(R.id.tvTerminationReturningAmountTerminateContractActivity)
    TextView tvTerminationReturningAmount;

    @BindView(R.id.lnParticipantNameTerminateContractActivity)
    LinearLayout lnParticipantName;

    @BindView(R.id.tvParticipantNameTerminateContractActivity)
    TextView tvParticipantName;

    @BindView(R.id.spParticipantNameTerminateContractActivity)
    Spinner spParticipantName;

    @BindView(R.id.btnTerminateTerminateContractActivityActivity)
    Button btnTerminate;

    /* ------------ */

    private ApiInterface apiInterface;
    private SharedPreference sharedPreference;
    private MaterialDialog dialog;

    private String operationId;
    private String operationCode;
    private String token;
    private String appCode;

    private boolean isSessionFinished;

    private TerminationResult terminationResult;

    private List<String> participantNameList;
    private List<String> participantTinList;
    private String participantTIN;

    private String insuranceCompanyTIN;

    private String contractNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminate_contract);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Müqavilənin xitamı");
        }


        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sharedPreference = new SharedPreference(this);

        operationId = sharedPreference.getData(Constants.CONTRACT_OPERATION_ID);
        operationCode = sharedPreference.getData(Constants.OPERATION_CODE);
        token = sharedPreference.getData(Constants.TOKEN);
        appCode = sharedPreference.getData(Constants.APP_CODE);

        getBundle();
        getAuthorizationObject();

        btnTerminate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidate(view)) {
                    showProgress();
                    postTerminateBody();
                }
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCancelOperationDialog();
            }
        });

        tvTerminationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });
    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            String contractTerminationObject = bundle.getString(Constants.EXTRA_CONTRACT_TERMINATION_OBJECT);
            Log.d(TAG, "getBundle: contractTerminationObject: " + contractTerminationObject);

            Gson gson = new Gson();

            ContractTermination contractTermination = gson.fromJson(contractTerminationObject, ContractTermination.class);

            insuranceCompanyTIN = contractTermination.getInsuranceCompanyTIN();
            contractNumber = contractTermination.getContractNumber();

            Log.d(TAG, "getBundle: insuranceCompanyTIN: " + insuranceCompanyTIN +
                    "\ncontractNumber: " + contractNumber);

            fillLayouts(contractTermination);
        }
    }

    private void getAuthorizationObject() {
        Gson gson = new Gson();

        String authorizationObject = sharedPreference.getData("authorizationObject");

        if (authorizationObject == null)
            return;

        Authorization authorization = gson.fromJson(authorizationObject, Authorization.class);

        for (int i = 0; i < authorization.getOperations().size(); i++) {
            Operation operation = authorization.getOperations().get(i);

            if (operation.getCode().equals(operationCode)) {
                getRelevantParticipants(operation.getParticipants());
                return;
            }
        }
    }

    private void getRelevantParticipants(List<Participant> participants) {

        participantNameList = new ArrayList<>();
        participantTinList = new ArrayList<>();

        for (int i = 0; i < participants.size(); i++) {
            Participant participant = participants.get(i);

            Log.d(TAG, "getRelevantParticipants: participant: " + participant.toString());

            List<Insurer> insurers = participant.getInsurers();

            for (int j = 0; j < insurers.size(); j++) {
                Insurer insurer = insurers.get(j);

                Log.d(TAG, "getRelevantParticipants: insurer: " + insurer.toString());

                if (insurer.getTin().equals(insuranceCompanyTIN)) {
                    Log.d(TAG, "getRelevantParticipants: participantName: " + participant.getName());
                    participantNameList.add(participant.getName());
                    participantTinList.add(participant.getTin());
                }
            }
        }

        Log.d(TAG, "getRelevantParticipants: participants size: " + participantNameList.size() +
                "\nparticipantNameList: " + participantNameList.toString());

        viewReDesigner();
    }

    /* ----- UI ----- */

    private void fillLayouts(ContractTermination contractTermination) {
        tvFullName.setText(contractTermination.getInsuredPerson().getFullName());
        tvCarNumber.setText(contractTermination.getVehicle().getCarNumber());
        tvTerminationReturningAmount.setText(contractTermination.getTerminationReturningAmount().toString());
        tvTerminationDate.setText(DateUtil.CurrentDate());
    }

    private void viewReDesigner() {
        if (participantNameList.size() == 1) {
            lnParticipantName.setVisibility(View.VISIBLE);
            tvParticipantName.setText(participantNameList.get(0));
            participantTIN = participantTinList.get(0);
        } else {
            participantNameList.add(0, "İştirakçını seçin");
            Log.d(TAG, "viewReDesigner: participantNameList: " + participantNameList);

            spParticipantName.setVisibility(View.VISIBLE);

            ArrayAdapter<String> adapter;

            adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item
                    , participantNameList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spParticipantName.setAdapter(adapter);

            spParticipantName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i > 0) {
                        participantTIN = participantTinList.get(i - 1);
                        Log.d(TAG, "onItemSelected: participantTIN: " + participantTIN);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }

    private boolean isValidate(View view) {

        if (etTerminationReason.getEditableText().length() == 0) {
            tilTerminationReason.setErrorEnabled(true);
            tilTerminationReason.setError("Xitam səbəbini daxil edin");
            return false;
        } else if (participantNameList.size() > 1) {
            tilTerminationReason.setErrorEnabled(false);
            if (spParticipantName.getSelectedItemPosition() == 0) {
                Snackbar.make(view, "Zəhmət olmasa İştirakçını seçin", Snackbar.LENGTH_SHORT).show();
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    private void datePicker() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String mDate = dayOfMonth + "." + (monthOfYear + 1) + "." + year;
                        tvTerminationDate.setText(mDate);
                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis() - 1000);
        datePickerDialog.show();
    }

    /* ----- REST ----- */

    private void postTerminateBody() {
        TerminateContract terminateContract = new TerminateContract();
        terminateContract.setParticipantTIN(participantTIN);
        terminateContract.setContractNumber(contractNumber);
        terminateContract.setTerminationDate(DateUtil.ApiDateFormatter(tvTerminationDate.getText().toString()));
        terminateContract.setTerminationReason(etTerminationReason.getEditableText().toString());
        terminateContract.setReturnedAmount(new BigDecimal(tvTerminationReturningAmount.getText().toString()));

        Log.d(TAG, "postTerminateBody: terminateContract: " + terminateContract.toString());

        terminateContract(terminateContract);
    }

    private void terminateContract(TerminateContract terminateContract) {
        Log.i(TAG, "terminateContract: TerminateContract: " + terminateContract.toString());
        Call<TerminationResult> terminateContractCall = apiInterface.terminateContract(operationId, token, appCode, terminateContract);

        terminateContractCall.enqueue(new Callback<TerminationResult>() {
            @Override
            public void onResponse(@NonNull Call<TerminationResult> call, @NonNull Response<TerminationResult> response) {
                if (dialog.isShowing())
                    dialog.dismiss();

                int statusCode = response.code();

                Log.i(TAG, "terminateContract: onResponse: statusCode: " + statusCode +
                        "\nMessage: " + response.message());

                if (statusCode == 200 && response.body() != null) {
                    isSessionFinished = true;
                    sharedPreference.saveData(Constants.IS_UNFINISHED, false);

                    terminationResult = response.body();
                    Log.i(TAG, "onResponse: TerminationResult: " + terminationResult.toString());
                    showDialog("Müqaviləyə xitam verildi", R.drawable.right_icon);
                } else if (statusCode == 401) {
                    showTokenExpiredDialog(TerminateContractActivity.this);
                } else {
                    Converter<ResponseBody, ErrorWrapper> converter = ApiClient.getClient().responseBodyConverter(ErrorWrapper.class, new Annotation[0]);
                    try {
                        ErrorWrapper errorWrapper = converter.convert(response.errorBody());
                        Log.w(TAG, "terminateContract: onResponse: Error Message:" + errorWrapper.getError());
                        //showErrorDialog(TerminateContractActivity.this, errorWrapper.getError());
                        showDialog(errorWrapper.getError(), R.drawable.wrong_icon);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    isSessionFinished = true;
                    sharedPreference.saveData(Constants.IS_UNFINISHED, false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<TerminationResult> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                showFailureDialog(TerminateContractActivity.this);
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    /* ----- Dialogs ----- */
    private void showDialog(String text, int resId) {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Diqqət")
                .content(text)
                .iconRes(resId)
                .limitIconToDefaultSize()
                .positiveText("Ok")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(TerminateContractActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        sharedFlusher();
                        finish();
                    }
                }).build();

        dialog.show();
    }

    private void cancelOperation() {
        isSessionFinished = true;
        sharedPreference.saveData(Constants.IS_UNFINISHED, false);
        Intent intent = new Intent(TerminateContractActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void showCancelOperationDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(TerminateContractActivity.this)
                .title("Diqqət")
                .content("Əməliyyatı ləğv etmək istədiyinizə əminsiniz?")
                .positiveText("BƏLİ")
                .negativeText("XEYR")
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
        MaterialDialog dialog = new MaterialDialog.Builder(TerminateContractActivity.this)
                .title("Diqqət")
                .content("Sizin yarımçıq əməliyyatınız qalıb.")
                .negativeText("Davam et")
                .positiveText("Ləğv et")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        sharedPreference.saveData(Constants.IS_UNFINISHED, false);
                        Intent intent = new Intent(TerminateContractActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }).build();

        dialog.show();
    }

    /* ------------ */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showBackPressDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        saveSession();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        restoreSession();
    }

    private void restoreSession() {
        if (sharedPreference.getData(Constants.IS_UNFINISHED, false)){
            Log.d(TAG, "restoreSession: session has restored!");
        }
    }

    private void saveSession() {
        if (!isSessionFinished) {
            Log.d(TAG, "saveSession: session has finished!");
        }
    }

    private void sharedFlusher() {

    }
}
