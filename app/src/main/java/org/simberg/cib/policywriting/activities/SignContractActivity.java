package org.simberg.cib.policywriting.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.simberg.cib.policywriting.BuildConfig;
import org.simberg.cib.policywriting.R;
import org.simberg.cib.policywriting.java.Constants;
import org.simberg.cib.policywriting.java.SharedPreference;
import org.simberg.cib.policywriting.models.ErrorWrapper;
import org.simberg.cib.policywriting.models.SignContract;
import org.simberg.cib.policywriting.models.Transaction;
import org.simberg.cib.policywriting.models.response.SigningResult;
import org.simberg.cib.policywriting.rest.ApiClient;
import org.simberg.cib.policywriting.rest.ApiInterface;
import org.simberg.cib.policywriting.rest.CustomTrust;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by javadbadirkhanly on 8/29/17.
 */

public class SignContractActivity extends ParentActivity {

    private final static String TAG = SignContractActivity.class.getSimpleName();

    @BindView(R.id.clParentViewSignContractActivity)
    CoordinatorLayout clParentView;

    @BindView(R.id.toolbarSignContractActivity)
    Toolbar toolbar;

    @BindView(R.id.imgCancelSignContractActivity)
    ImageView imgCancel;

    @BindView(R.id.tilInsuredPhoneNumberSignContractActivity)
    TextInputLayout tilInsuredPhoneNumber;

    @BindView(R.id.etInsuredPhoneNumberSignContractActivity)
    EditText etInsuredPhoneNumber;

    @BindView(R.id.tilInsuredEmailSignContractActivity)
    TextInputLayout tilInsuredEmail;

    @BindView(R.id.etInsuredEmailSignContractActivity)
    EditText etInsuredEmail;

    @BindView(R.id.btnSignSignContractActivity)
    Button btnSign;

    private ApiInterface apiInterface;

    private SharedPreference sharedPreference;

    private String operationId;
    private String token;
    private String appCode;
    private String operationCode;

    private String encodedDocument;

    private byte[] decodedDocument;

    private MaterialDialog dialog;
    private MaterialDialog dialogVerification;

    private boolean isSessionFinished;

    private boolean isPdfOpened;

    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_sign_contract);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCancelOperationDialog();
            }
        });

        new CustomTrust(getApplicationContext());

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        sharedPreference = new SharedPreference(this);

        operationId = sharedPreference.getData(Constants.CONTRACT_OPERATION_ID);
        token = sharedPreference.getData(Constants.TOKEN);
        appCode = sharedPreference.getData(Constants.APP_CODE);
        operationCode = sharedPreference.getData(Constants.OPERATION_CODE);

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(sharedPreference.getData(Constants.PARTICIPANT_NAME));
            getSupportActionBar().setSubtitle(sharedPreference.getData(Constants.INSURER_NAME));
        }

        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= 23){
                    if (checkPermission()){
                        if (isValidate()) {
                            showProgress();
                            if (operationCode.equals("GREENCARD_CMTPL_CONTRACT_OPERATION")) {
                                createContract();
                            } else {
                                signContract();
                            }
                        } else {
                            Snackbar.make(clParentView, "Zəhmət olmasa məlumatları tam daxil edin.", Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    if (isValidate()) {
                        showProgress();
                        if (operationCode.equals("GREENCARD_CMTPL_CONTRACT_OPERATION")) {
                            createContract();
                        } else {
                            signContract();
                        }
                    } else {
                        Snackbar.make(clParentView, "Zəhmət olmasa məlumatları tam daxil edin.", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean isValidate() {
        if (etInsuredPhoneNumber.getEditableText().length() == 0) {
            tilInsuredPhoneNumber.setErrorEnabled(true);
            tilInsuredPhoneNumber.setError("Məlumatlar boş ola bilməz");
            return false;
        } else if (etInsuredEmail.getEditableText().length() == 0) {
            tilInsuredPhoneNumber.setErrorEnabled(false);
            tilInsuredEmail.setErrorEnabled(true);
            tilInsuredEmail.setError("Məlumatlar boş ola bilməz");
            return false;
        } else {
            tilInsuredEmail.setErrorEnabled(false);
            return true;
        }
    }

    private void signContract() {
        SignContract signContract = new SignContract();
        signContract.setOperatorPhoneNumber(sharedPreference.getData(Constants.PHONE_NUMBER));
        signContract.setOperatorAsanID(sharedPreference.getData(Constants.ASAN_ID));
        signContract.setInsuredPhoneNumber(etInsuredPhoneNumber.getEditableText().toString());
        signContract.setInsuredEmail(etInsuredEmail.getEditableText().toString());

        Call<Transaction> signContractCall = apiInterface.signContract(operationId, token, appCode, signContract);

        signContractCall.enqueue(new Callback<Transaction>() {
            @Override
            public void onResponse(@NonNull Call<Transaction> call, @NonNull Response<Transaction> response) {
                if (dialog.isShowing())
                    dialog.dismiss();

                int statusCode = response.code();

                Log.d(TAG, "signContract: onResponse: statusCode: " + statusCode +
                        "\nmessage: " + response.message());

                if (statusCode == 200) {
                    Log.d(TAG, "signContract: onResponse: responseBody: "+ response.body());

                    Transaction transaction = response.body();

                    if (transaction.getVerificationCode() == null){
                        showProgress();
                        getSignResult();
                    } else {
                        String transactionCode = transaction.getVerificationCode();
                        Log.d(TAG, "onResponse: transactionCode: " + transactionCode);
                        showVerificationDialog(transactionCode);
                    }
                } else if (statusCode == 401) {
                    showTokenExpiredDialog(SignContractActivity.this);
                } else {
                    Log.w(TAG, "signContract: onResponse");
                    errorBodyConverter(SignContractActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Transaction> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                showFailureDialog(SignContractActivity.this);
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void createContract() {
        final SignContract signContract = new SignContract();
        signContract.setInsuredPhoneNumber(etInsuredPhoneNumber.getEditableText().toString());
        signContract.setInsuredEmail(etInsuredEmail.getEditableText().toString());

        Call<SigningResult> createContractCall = apiInterface.createContract(operationId, appCode, token, signContract);

        createContractCall.enqueue(new Callback<SigningResult>() {
            @Override
            public void onResponse(@NonNull Call<SigningResult> call, @NonNull Response<SigningResult> response) {
                if (dialog.isShowing())
                    dialog.dismiss();

                int statusCode = response.code();

                Log.d(TAG, "createContract: onResponse: statusCode: " + statusCode +
                        "\nmessage: " + response.message());

                if (statusCode == 200) {
                    SigningResult signingResult = response.body();
                    getContractDocument(signingResult.getData());
                } else if (statusCode == 401) {
                    showTokenExpiredDialog(SignContractActivity.this);
                } else {
                    Log.w(TAG, "createContract: onResponse");
                    errorBodyConverter(SignContractActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SigningResult> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                showFailureDialog(SignContractActivity.this);
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

                Log.i(TAG, "getContractDocument: onResponse: statusCode: " + statusCode + "\nmessage: " + response.message());

                if (statusCode == 200 && response.body() != null) {
                    encodedDocument = response.body();

                    documentDecoder(encodedDocument);

                    Log.i(TAG, "onResponse: encodedDocument: " + encodedDocument);
                } else if (statusCode == 401) {
                    showTokenExpiredDialog(SignContractActivity.this);
                } else {
                    Log.w(TAG, "getContractDocument: onResponse");
                    errorBodyConverter(SignContractActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call,@NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                showFailureDialog(SignContractActivity.this);
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
        Uri pdfUri = FileProvider.getUriForFile(SignContractActivity.this,
                BuildConfig.APPLICATION_ID + ".provider",
                file);

        try {
            isPdfOpened = true;
            Log.d(TAG, "readPdfFile: isPdfOpened: " + isPdfOpened);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(pdfUri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(SignContractActivity.this, "No PDF reader found to open this file.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getSignResult() {
        Call<SigningResult> signingResultCall = apiInterface.signingResult(operationId, token, appCode);

        signingResultCall.enqueue(new Callback<SigningResult>() {
            @Override
            public void onResponse(@NonNull Call<SigningResult> call, @NonNull Response<SigningResult> response) {
                if (dialog.isShowing())
                    dialog.dismiss();

                if (dialogVerification != null && dialogVerification.isShowing()) {
                    dialogVerification.dismiss();
                    dialogVerification = null;
                }

                int statusCode = response.code();
                Log.i(TAG, "signResult: onResponse: statusCode: " + statusCode + "\nmessage: " + response.message());

                if (statusCode == 200) {
                    SigningResult signingResult = response.body();
                    isSessionFinished = true;
                    sharedPreference.saveData(Constants.IS_UNFINISHED, false);
                    showResultDialog("Əməliyyat uğurla tamamlandı!", R.drawable.right_icon, signingResult.getData());
                } else if (statusCode == 401) {
                    showTokenExpiredDialog(SignContractActivity.this);
                } else {
                    Converter<ResponseBody, ErrorWrapper> converter = ApiClient.getClient().responseBodyConverter(ErrorWrapper.class, new Annotation[0]);
                    try {
                        ErrorWrapper errorWrapper = converter.convert(response.errorBody());
                        Log.w(TAG, "SigningResult: onResponse: Error Message:" + errorWrapper.getError());
                        showResultDialog(errorWrapper.getError(), R.drawable.wrong_icon, null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SigningResult> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();

                if (dialogVerification != null && dialogVerification.isShowing())
                    dialogVerification.dismiss();

                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void cancelContractOperation() {
        Call<Object> cancelContractOperationCall = apiInterface.cancelContractOperation(operationId, token, appCode);

        cancelContractOperationCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (dialog.isShowing())
                    dialog.dismiss();

                int statusCode = response.code();

                Log.i(TAG, "cancelContractOperation: onResponse: statusCode: " + statusCode + "\nMessage: " + response.message());

                if (statusCode == 200) {
                    isSessionFinished = true;
                    sharedFlusher();

                    Intent intent = new Intent(SignContractActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else if (statusCode == 401) {
                    showTokenExpiredDialog(SignContractActivity.this);
                } else {
                    Log.w(TAG, "cancelContractOperation: onResponse");
                    errorBodyConverter(SignContractActivity.this, response);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    /* ----- Dialogs ----- */
    private void showVerificationDialog(String verificationCode) {
        Log.d(TAG, "showVerificationDialog: verificationCode: " + verificationCode);
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .cancelable(false)
                .title("Yoxlama Kodu")
                .content(verificationCode);


            dialogVerification = builder.build();

        dialogVerification.show();

        getSignResult();
    }

    private void showResultDialog(String text, int resId, final String contractNumber) {
        String positiveText;
        String negativeText;
        if (contractNumber == null){
            positiveText = "DÜZƏLİŞ ET";
            negativeText = "";
        } else {
            positiveText = "MÜQAVİLƏYƏ BAX";
            negativeText = "ANA SƏHİFƏYƏ QAYIT";
        }

        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Diqqət")
                .content(text)
                .iconRes(resId)
                .limitIconToDefaultSize()
                .positiveText(positiveText)
                .negativeText(negativeText)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (contractNumber == null) {
                            Log.d(TAG, "onClick: contractNumber is NULL!");
                            dialog.dismiss();
                            return;
                        }
                        dialog.dismiss();
                        showProgress();
                        getContractDocument(contractNumber);

                        Log.d(TAG, "onClick: contractNumber: " + contractNumber);
                    }
                }).onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        sharedFlusher();
                        Intent intent = new Intent(SignContractActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                })
                .cancelable(false)
                .build();

        dialog.show();
    }

    private void showCancelOperationDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(SignContractActivity.this)
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

    private void sharedFlusher() {
        sharedPreference.saveData(Constants.IS_UNFINISHED, false);
        sharedPreference.saveData(Constants.UNFINISHED_SESSION_ACTIVITY);
        sharedPreference.saveData(Constants.CERTIFICATE_NUMBER);
        sharedPreference.saveData(Constants.CAR_NUMBER);
        sharedPreference.saveData(Constants.INSURED_VALUE);
        sharedPreference.saveData(Constants.INSURANCE_CALCULATED_PRICE);
        sharedPreference.saveData(Constants.INSURANCE_BM_COEFFICIENT);
        sharedPreference.saveData(Constants.INSURANCE_PRICE);
        sharedPreference.saveData(Constants.INSURED_PHONE_NUMBER);
        sharedPreference.saveData(Constants.INSURED_EMAIL);

        sharedPreference.saveData(Constants.SESSION_USER_RESIDENCY);
        sharedPreference.saveData(Constants.SESSION_USER_TIN);
        sharedPreference.saveData(Constants.SESSION_USER_ID);
        sharedPreference.saveData(Constants.SESSION_USER_OBJECT);
        sharedPreference.saveData(Constants.SESSION_USER_OBJECT);
        sharedPreference.saveData(Constants.INSURED_VEHICLE_NAME);
        sharedPreference.saveData(Constants.INSURED_FULL_NAME);
        sharedPreference.saveData(Constants.INSURED_CAR_NUMBER);
        sharedPreference.saveData(Constants.INSURED_CERTIFICATE_NUMBER);
        ;
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
                    if (isValidate()) {
                        showProgress();
                        if (operationCode.equals("GREENCARD_CMTPL_CONTRACT_OPERATION")) {
                            createContract();
                        } else {
                            signContract();
                        }
                    } else {
                        Snackbar.make(clParentView, "Zəhmət olmasa məlumatları tam daxil edin.", Snackbar.LENGTH_SHORT).show();
                    }
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
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
            etInsuredPhoneNumber.setText(sharedPreference.getData(Constants.INSURED_PHONE_NUMBER));
            etInsuredPhoneNumber.setSelection(etInsuredPhoneNumber.getEditableText().length());
            etInsuredEmail.setText(sharedPreference.getData(Constants.INSURED_EMAIL));
            etInsuredEmail.setSelection(etInsuredEmail.getEditableText().length());
        }
    }

    private void saveSession() {
        if (!isSessionFinished) {
            sharedPreference.saveData(Constants.INSURED_PHONE_NUMBER, etInsuredPhoneNumber.getEditableText().toString());
            sharedPreference.saveData(Constants.INSURED_EMAIL, etInsuredEmail.getEditableText().toString());

            sharedPreference.saveData(Constants.IS_UNFINISHED, true);
            sharedPreference.saveData(Constants.UNFINISHED_SESSION_ACTIVITY, SignContractActivity.class.getName());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: restarted");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: paused!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreference.saveData(Constants.IS_UNFINISHED, true);
        Log.d(TAG, "onResume: IS_UNFINISHED: " + sharedPreference.getData(Constants.IS_UNFINISHED, false));
        sharedPreference.saveData(Constants.UNFINISHED_SESSION_ACTIVITY, SignContractActivity.class.getName());
        Log.d(TAG, "onResume: resumed!");
        if (isPdfOpened) {
            sharedFlusher();
            isSessionFinished = true;
            Intent intent = new Intent(SignContractActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
