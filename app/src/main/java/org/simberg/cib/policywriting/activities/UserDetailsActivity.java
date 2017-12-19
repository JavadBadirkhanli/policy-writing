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
import org.simberg.cib.policywriting.java.DateUtil;
import org.simberg.cib.policywriting.java.SharedPreference;
import org.simberg.cib.policywriting.models.ContractPrice;
import org.simberg.cib.policywriting.models.Person;
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

public class UserDetailsActivity extends ParentActivity {

    private static final String TAG = UserDetailsActivity.class.getSimpleName();

    /* ----- UI ----- */
    @BindView(R.id.toolbarUserDetailsActivity)
    Toolbar toolbar;

    @BindView(R.id.imgCancelUserDetailsActivity)
    ImageView imgCancel;

    @BindView(R.id.lnSurnameUserDetailsActivity)
    LinearLayout lnSurname;

    @BindView(R.id.lnNameUserDetailsActivity)
    LinearLayout lnName;

    @BindView(R.id.lnPatronymicUserDetailsActivity)
    LinearLayout lnPatronymic;

    @BindView(R.id.lnBirthPlaceUserDetailsActivity)
    LinearLayout lnBirthPlace;

    @BindView(R.id.lnBirthDateUserDetailsActivity)
    LinearLayout lnBirthDate;

    @BindView(R.id.lnFinUserDetailsActivity)
    LinearLayout lnFin;

    @BindView(R.id.lnLivingPlaceUserDetailsActivity)
    LinearLayout lnLivingPlace;

    @BindView(R.id.tvSurnameUserDetailsActivity)
    TextView tvSurname;

    @BindView(R.id.tvNameUserDetailsActivity)
    TextView tvName;

    @BindView(R.id.tvFatherNameUserDetailsActivity)
    TextView tvFatherName;

    @BindView(R.id.tvBirthPlaceUserDetailsActivity)
    TextView tvBirthPlace;

    @BindView(R.id.tvBirthDateUserDetailsActivity)
    TextView tvBirthDate;

    @BindView(R.id.tvFinUserDetailsActivity)
    TextView tvFin;

    @BindView(R.id.tvLivingPlaceUserDetailsActivity)
    TextView tvLivingPlace;

    @BindView(R.id.btnConfirmUserDetailsActivity)
    Button btnConfirm;

    @BindView(R.id.btnMisMatchUserDetailsActivity)
    Button btnMisMatch;

    /* ---------- */

    private ApiInterface apiInterface;

    private String operationId;
    private String token;
    private String appCode;
    private String operationCode;

    private MaterialDialog dialog;

    private SharedPreference sharedPreference;

    private ContractPrice contractPrice;

    private String personInfo;
    private int personType;

    private boolean isSessionFinished;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_user_details);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle("Sığortalının məlumatları");

        getBundle();

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        sharedPreference = new SharedPreference(this);

        operationId = sharedPreference.getData(Constants.CONTRACT_OPERATION_ID);
        token = sharedPreference.getData(Constants.TOKEN);
        appCode = sharedPreference.getData(Constants.APP_CODE);
        operationCode = sharedPreference.getData(Constants.OPERATION_CODE);

        toolbar.setTitle("Müştəri məlumatları");

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCancelOperationDialog();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSessionFinished = true;
                if (operationCode.equals("GREENCARD_CMTPL_CONTRACT_OPERATION") || operationCode.equals("BORDER_CMTPL_CONTRACT_OPERATION")) {
                    Intent intent = new Intent(UserDetailsActivity.this, PeriodActivity.class);
                    startActivity(intent);
                } else {
                    showProgress();
                    getContractDetails();
                }
            }
        });

        btnMisMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress();
                mismatchPersonInfo();
            }
        });
    }

    /* ----- REST ----- */
    private void getContractDetails() {
        Call<ContractPrice> callContractPrice = apiInterface.contractPrice(operationId, token, appCode);

        callContractPrice.enqueue(new Callback<ContractPrice>() {
            @Override
            public void onResponse(@NonNull Call<ContractPrice> call, @NonNull Response<ContractPrice> response) {
                if (dialog.isShowing())
                    dialog.dismiss();

                int statusCode = response.code();

                Log.i(TAG, "getContractDetails: onResponse: statusCode: " + statusCode + "\nMessage: " + response.message());

                if (statusCode == 200 && response.body() != null) {
                    isSessionFinished = true;
                    sharedPreference.saveData(Constants.IS_UNFINISHED, false);

                    contractPrice = response.body();
                    Log.d(TAG, "onResponse: Contract Price: " + contractPrice.toString());

                    Gson gson = new Gson();
                    String contractPriceObject = gson.toJson(contractPrice);

                    Intent intent = new Intent(UserDetailsActivity.this, ContractConfirmationActivity.class);
                    intent.putExtra(Constants.CONTRACT_PRICE_OBJECT, contractPriceObject);
                    startActivity(intent);

                } else if (statusCode == 401) {
                    showTokenExpiredDialog(UserDetailsActivity.this);
                } else {
                    Log.w(TAG, "getContractDetails: onResponse");
                    errorBodyConverter(UserDetailsActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ContractPrice> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                showFailureDialog(UserDetailsActivity.this);
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void mismatchPersonInfo() {
        Call<Object> mismatchPersonInfoCall = apiInterface.mismatchPersonInfo(operationId, token);

        mismatchPersonInfoCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                if (dialog.isShowing())
                    dialog.dismiss();
                int statusCode = response.code();

                if (statusCode == 200) {
                    isSessionFinished = true;
                    sharedPreference.saveData(Constants.IS_UNFINISHED, false);
                    Intent intent = new Intent(UserDetailsActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else if (statusCode == 401) {
                    showTokenExpiredDialog(UserDetailsActivity.this);
                } else {
                    Log.w(TAG, "mismatchPersonInfo: onResponse");
                    errorBodyConverter(UserDetailsActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                showFailureDialog(UserDetailsActivity.this);
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

                Log.i(TAG, "onResponse: statusCode: " + statusCode + "\nMessage: " + response.message());

                if (statusCode == 200) {
                    isSessionFinished = true;
                    sharedPreference.saveData(Constants.IS_UNFINISHED, false);

                    Intent intent = new Intent(UserDetailsActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else if (statusCode == 401) {
                    showTokenExpiredDialog(UserDetailsActivity.this);
                } else {
                    Log.w(TAG, "cancelContractOperation: onResponse");
                    errorBodyConverter(UserDetailsActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                showFailureDialog(UserDetailsActivity.this);
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    /* ----- UI ----- */
    private void getBundle() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.getString(Constants.PERSON_INFO) != null) {
                if (bundle.getString(Constants.PERSON_TYPE, "Not Found!").equals(Constants.NATURAL_PERSON)) {
                    personType = 1;
                } else if (bundle.getString(Constants.PERSON_TYPE, "Not Found!").equals(Constants.JURIDICAL_PERSON)) {
                    personType = 2;
                } else if (bundle.getString(Constants.PERSON_TYPE, "Not Found!").equals(Constants.NON_RESIDENT_PERSON)) {
                    personType = 3;
                } else {
                    Log.i(TAG, "getBundle: personType is null");
                }

                personInfo = bundle.getString(Constants.PERSON_INFO);
                jsonParser(personType, personInfo);
            }
        } else {
            Log.d(TAG, "getBundle: person not found");
        }
    }

    private void jsonParser(int personType, String json) {
        Gson gson = new Gson();
        if (personType == 1) {
            Person naturalPerson = gson.fromJson(json, Person.class);
            fillNaturalPersonInfoLayouts(naturalPerson);
        } else if (personType == 2) {
            Person juridicalPerson = gson.fromJson(json, Person.class);
            fillJuridicalPersonInfoLayouts(juridicalPerson);
        } else {
            Person nonResidentPerson = gson.fromJson(json, Person.class);
            fillNonResidentPersonInfoLayouts(nonResidentPerson);
        }
    }

    private void fillNaturalPersonInfoLayouts(Person naturalPerson) {
        if (naturalPerson.getFirstName() != null) {
            lnName.setVisibility(View.VISIBLE);
            tvName.setText(naturalPerson.getFirstName());
        }

        if (naturalPerson.getLastName() != null) {
            lnSurname.setVisibility(View.VISIBLE);
            tvSurname.setText(naturalPerson.getLastName());
        }

        if (naturalPerson.getPatronymic() != null) {
            lnPatronymic.setVisibility(View.VISIBLE);
            tvFatherName.setText(naturalPerson.getPatronymic());
        }

        if (naturalPerson.getBirthPlace() != null) {
            lnBirthPlace.setVisibility(View.VISIBLE);
            tvBirthPlace.setText(naturalPerson.getBirthPlace());
        }

        if (naturalPerson.getBirthPlace() != null) {
            lnBirthDate.setVisibility(View.VISIBLE);
            tvBirthDate.setText(DateUtil.DateFormatter(naturalPerson.getBirthDate()));
        }

        if (naturalPerson.getPin() != null) {
            lnFin.setVisibility(View.VISIBLE);
            tvFin.setText(naturalPerson.getPin());
        }

        if (naturalPerson.getAddress() != null) {
            lnLivingPlace.setVisibility(View.VISIBLE);
            tvLivingPlace.setText(naturalPerson.getAddress());
        }
    }

    private void fillJuridicalPersonInfoLayouts(Person juridicalPerson) {
        if (juridicalPerson.getFullName() != null) {
            lnName.setVisibility(View.VISIBLE);
            tvName.setText(juridicalPerson.getFullName());
        }

        if (juridicalPerson.getBirthPlace() != null) {
            lnBirthPlace.setVisibility(View.VISIBLE);
            tvBirthPlace.setText(juridicalPerson.getBirthPlace());
        }

        if (juridicalPerson.getBirthDate() != null) {
            lnBirthDate.setVisibility(View.VISIBLE);
            tvBirthDate.setText(DateUtil.DateFormatter(juridicalPerson.getBirthDate()));
        }

        if (juridicalPerson.getPin() != null) {
            lnFin.setVisibility(View.VISIBLE);
            tvFin.setText(juridicalPerson.getTin());
        }

        if (juridicalPerson.getAddress() != null) {
            lnLivingPlace.setVisibility(View.VISIBLE);
            tvLivingPlace.setText(juridicalPerson.getAddress());
        }
    }

    private void fillNonResidentPersonInfoLayouts(Person nonResidentPerson) {
        if (nonResidentPerson.getFirstName() != null) {
            lnName.setVisibility(View.VISIBLE);
            tvName.setText(nonResidentPerson.getFirstName());
        }

        if (nonResidentPerson.getLastName() != null) {
            lnSurname.setVisibility(View.VISIBLE);
            tvSurname.setText(nonResidentPerson.getLastName());
        }

        if (nonResidentPerson.getPatronymic() != null) {
            lnPatronymic.setVisibility(View.VISIBLE);
            tvFatherName.setText(nonResidentPerson.getPatronymic());
        }

        if (nonResidentPerson.getBirthPlace() != null) {
            lnBirthPlace.setVisibility(View.VISIBLE);
            tvBirthPlace.setText(nonResidentPerson.getBirthPlace());
        }

        if (nonResidentPerson.getBirthPlace() != null) {
            lnBirthDate.setVisibility(View.VISIBLE);
            tvBirthDate.setText(DateUtil.DateFormatter(nonResidentPerson.getBirthDate()));
        }

        if (nonResidentPerson.getPin() != null) {
            lnFin.setVisibility(View.VISIBLE);
            tvFin.setText(nonResidentPerson.getPin());
        }

        if (nonResidentPerson.getAddress() != null) {
            lnLivingPlace.setVisibility(View.VISIBLE);
            tvLivingPlace.setText(nonResidentPerson.getAddress());
        }
    }

    /* ----- Dialogs ----- */
    private void showCancelOperationDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(UserDetailsActivity.this)
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
                .content("Sizin bitməmiş əməliyyatınız qalıb.")
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
        sharedPreference.saveData(Constants.UNFINISHED_SESSION_ACTIVITY, UserDetailsActivity.class.getName());
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
            personInfo = sharedPreference.getData(Constants.SESSION_PERSON_DETAILS_OBJECT);
            personType = sharedPreference.getData(Constants.SESSION_PERSON_TYPE, 0);
            Log.d(TAG, "restoreSession: personDetailsObject: " + personInfo + "\npersonType: " + personType);
            jsonParser(personType, personInfo);
        }
    }

    private void saveSession() {
        if (!isSessionFinished) {
            Log.d(TAG, "saveSession: personDetailsObject: " + personInfo + "\npersonType: " + personType);
            sharedPreference.saveData(Constants.SESSION_PERSON_DETAILS_OBJECT, personInfo);
            sharedPreference.saveData(Constants.SESSION_PERSON_TYPE, personType);
        }
    }
}
