package org.simberg.cib.policywriting.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

import org.simberg.cib.policywriting.R;
import org.simberg.cib.policywriting.java.Constants;
import org.simberg.cib.policywriting.java.DateUtil;
import org.simberg.cib.policywriting.java.SharedPreference;
import org.simberg.cib.policywriting.models.ErrorWrapper;
import org.simberg.cib.policywriting.models.request.Blank;
import org.simberg.cib.policywriting.models.response.Result;
import org.simberg.cib.policywriting.rest.ApiClient;
import org.simberg.cib.policywriting.rest.ApiInterface;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by javadbadirkhanly on 10/10/17.
 */

public class BlankOperationActivity extends ParentActivity {

    private final static String TAG = BlankOperationActivity.class.getSimpleName();

    @BindView(R.id.toolbarBlankOperationActivity)
    Toolbar toolbar;

    @BindView(R.id.imgCancelBlankOperationActivity)
    ImageView imgCancel;

    @BindView(R.id.tvBlankSeriesBlankOperationActivity)
    TextView tvBlankSeries;

    @BindView(R.id.spBlankSeriesBlankOperationActivity)
    Spinner spBlankSeries;

    @BindView(R.id.tilBlankNumberBlankOperationActivity)
    TextInputLayout tilBlankNumber;

    @BindView(R.id.etBlankNumberBlankOperationActivity)
    EditText etBlankNumber;

    @BindView(R.id.tvEffectiveDateBlankOperationActivity)
    TextView tvEffectiveDate;

    @BindView(R.id.btnSearchBlankOperationActivity)
    Button btnEnter;

    private ApiInterface apiInterface;
    private SharedPreference sharedPreference;
    private MaterialDialog dialog;

    private String operationId;
    private String appCode;
    private String token;
    private String operationCode;

    private boolean isSessionFinished;

    private String mDate;
    private String mPostDate;
    private String mTime;
    private String mDateTime;
    private String mPostDateTime;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank_operation);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Blankın daxil edilməsi");
        }

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sharedPreference = new SharedPreference(this);

        operationId = sharedPreference.getData(Constants.CONTRACT_OPERATION_ID);
        appCode = sharedPreference.getData(Constants.APP_CODE);
        token = sharedPreference.getData(Constants.TOKEN);
        operationCode = sharedPreference.getData(Constants.OPERATION_CODE);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // TODO: 11/17/17 time

        mPostDateTime = mYear + "-" + (mMonth + 1) + "-" + mDay + " " + mHour + ":" + mMinute + ":00";
        Log.d(TAG, "onCreate: mPostDateTime: " + mPostDateTime);

        mDateTime = mDay + "." + mMonth + "." + mYear + " " + mHour + ":" + mMinute;
        Log.d(TAG, "onCreate: DateTime: " + mDateTime);
        //tvEffectiveDate.setText(mDateTime);

        tvEffectiveDate.setText(DateUtil.CurrentDateTime());

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidate()) {
                    getDataFromFields();
                }
            }
        });

        tvEffectiveDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCancelOperationDialog();
            }
        });
    }

    private boolean isValidate() {
        if (spBlankSeries.getSelectedItemPosition() == 0) {
            // TODO: 11/17/17 show choose dialog
            return false;
        } else if (etBlankNumber.getEditableText().length() == 0) {
            tilBlankNumber.setErrorEnabled(true);
            tilBlankNumber.setError("Blank nömrəsini daxil edin");
            return false;
        }
        return true;
    }

    private void sendBlankInformation(Blank blank) {
        Log.d(TAG, "sendBlankInformation: blank: " + blank.toString());
        Call<Result> blankOperationCall = apiInterface.blankOperation(operationId, appCode, token, blank);

        blankOperationCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                if (dialog.isShowing())
                    dialog.dismiss();

                int statusCode = response.code();
                Log.d(TAG, "sendBlankInformation: onResponse: statusCode: " + statusCode +
                        "\nmessage: " + response.message());

                if (statusCode == 200) {
                    isSessionFinished = true;
                    sharedPreference.saveData(Constants.IS_UNFINISHED, false);

                    Result result = response.body();
                    Log.d(TAG, "onRejsponse: result: " + result.toString());

                    Intent intent = new Intent(BlankOperationActivity.this, VehicleSearchActivity.class);
                    startActivity(intent);
                } else {
                    Converter<ResponseBody, ErrorWrapper> converter = ApiClient.getClient().responseBodyConverter(ErrorWrapper.class, new Annotation[0]);
                    try {
                        ErrorWrapper errorWrapper = converter.convert(response.errorBody());
                        Log.w(TAG, "onResponse: Error Message:" + errorWrapper.getError());
                        showErrorDialog(BlankOperationActivity.this, errorWrapper.getError());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void cancelContractOperation() {
        Call<Object> cancelContractOperationCall = apiInterface.cancelContractOperation(operationId, token, appCode);

        cancelContractOperationCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                if (dialog.isShowing())
                    dialog.dismiss();

                int statusCode = response.code();

                Log.d(TAG, "onResponse: statusCode: " + statusCode + "\nmessage: " + response.message());

                if (statusCode == 200 && response.body() != null) {
                    isSessionFinished = true;
                    sharedPreference.saveData(Constants.IS_UNFINISHED, false);
                    finish();
                } else if (statusCode == 401) {
                    showTokenExpiredDialog(BlankOperationActivity.this);
                } else {
                    Log.w(TAG, "cancelContractOperation: onResponse");
                    errorBodyConverter(BlankOperationActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                showFailureDialog(BlankOperationActivity.this);
                ;
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void showCancelOperationDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Diqqət")
                .content("Əməliyyatı ləğv etmək istədiyinizə əminsiniz?")
                .positiveText("BƏLİ")
                .negativeText("XEYR")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        showProgress();
                        cancelContractOperation();
                    }
                }).build();

        dialog.show();
    }

    private void getDataFromFields() {
        Blank blank = new Blank();
        blank.setBlankNumber(etBlankNumber.getEditableText().toString());
        blank.setBlankSeries(spBlankSeries.getSelectedItem().toString());
        blank.setEffectiveDate(mPostDateTime);

        Log.d(TAG, "getDataFromFields: blank: " + blank.toString());

        showProgress();
        sendBlankInformation(blank);
    }

    private void datePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mDate = dayOfMonth + "." + (monthOfYear + 1) + "." + year;
                        mPostDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        timePicker();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void timePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mHour = hourOfDay;
                        mMinute = minute;

                        mTime = mHour + ":" + mMinute;
                        mDateTime = mDate + " " + mTime;
                        mPostDateTime = mPostDate + " " + mTime + ":00";
                        tvEffectiveDate.setText(mDateTime);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void showBackPressDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Diqqət")
                .content("Sizin yarımçıq əməliyyatınız qalıb.")
                .negativeText("Davam et")
                .positiveText("Ləğv et")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        showProgress();
                        cancelContractOperation();
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

    private void showProgress() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .content("Gözləyin...")
                .progress(true, 0);

        dialog = builder.build();
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreference.saveData(Constants.IS_UNFINISHED, true);
        Log.d(TAG, "onResume: IS_UNFINISHED: " + sharedPreference.getData(Constants.IS_UNFINISHED, false));
        sharedPreference.saveData(Constants.UNFINISHED_SESSION_ACTIVITY, BlankOperationActivity.class.getName());
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

    private void saveSession() {
        if (!isSessionFinished) {
            Blank blank = new Blank();
            blank.setBlankSeriesId(spBlankSeries.getSelectedItemPosition());
            blank.setBlankNumber(etBlankNumber.getEditableText().toString());
            blank.setEffectiveDate(mPostDateTime);

            Gson gson = new Gson();
            String blankObject = gson.toJson(blank);

            Log.d(TAG, "saveSession: blankObject: " + blankObject);

            sharedPreference.saveData(Constants.SESSION_BLANK_OBJECT, blankObject);
            sharedPreference.saveData(Constants.IS_UNFINISHED, true);
            sharedPreference.saveData(Constants.UNFINISHED_SESSION_ACTIVITY, BlankOperationActivity.class.getName());
        }
    }

    private void restoreSession() {
        if (sharedPreference.getData(Constants.IS_UNFINISHED, false)) {
            Gson gson = new Gson();
            String blankObject = sharedPreference.getData(Constants.SESSION_BLANK_OBJECT);
            Log.d(TAG, "restoreSession: blankObject: " + blankObject);
            Blank blank = gson.fromJson(blankObject, Blank.class);

            spBlankSeries.setSelection(blank.getBlankSeriesId());
            etBlankNumber.setText(blank.getBlankNumber());
            tvEffectiveDate.setText(blank.getEffectiveDate());
        }
    }
}
