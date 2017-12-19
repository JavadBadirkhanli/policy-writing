package org.simberg.cib.policywriting.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

import org.simberg.cib.policywriting.R;
import org.simberg.cib.policywriting.java.Constants;
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
 * Created by javadbadirkhanly on 9/21/17.
 */

public class UserSearchActivity extends ParentActivity {

    private static final String TAG = UserSearchActivity.class.getSimpleName();

    @BindView(R.id.toolbarUserDetailsSearchActivity)
    Toolbar toolbar;

    @BindView(R.id.imgCancelUserDetailsSearchActivity)
    ImageView imgCancel;

    @BindView(R.id.cvUserUserSearchActivity)
    CardView cardView;

    @BindView(R.id.rgPersonTypeUserSearchActivity)
    RadioGroup rgPersonType;

    @BindView(R.id.rbNaturalPersonUserSearchActivity)
    RadioButton rbNaturalPerson;

    @BindView(R.id.rbJuridicalPersonUserSearchActivity)
    RadioButton rbJuridicalPerson;

    @BindView(R.id.btnUserSearchEnterUserSearchActivity)
    Button btnUserSearchEnter;

    /* -------------- Standard & GreenCard operation views --------------- */

    @BindView(R.id.clStandardUserSearchActivity)
    ConstraintLayout clStandard;

    @BindView(R.id.cbResidentStandardUserSearchActivity)
    CheckBox cbResidentStandard;

    @BindView(R.id.tilIdDocumentStandardUserSearchActivity)
    TextInputLayout tilIdDocumentStandard;

    @BindView(R.id.etIdDocumentStandardUserSearchActivity)
    EditText etIdDocumentStandard;

    @BindView(R.id.tilTINStandardUserSearchActivity)
    TextInputLayout tilTINStandard;

    @BindView(R.id.etTINStandardUserSearchActivity)
    EditText etTINStandard;

    @BindView(R.id.clNonResidentStandardUserSearchActivity)
    ConstraintLayout clNonResidentStandard;

    /*@BindView(R.id.tilFirstNameNonResidentStandardUserSearchActivity)
    TextInputLayout tilFirstNameNonResidentStandard;

    @BindView(R.id.etFirstNameNonResidentStandardUserSearchActivity)
    EditText etFirstNameNonResidentStandard;

    @BindView(R.id.tilLastNameNonResidentStandardUserSearchActivity)
    TextInputLayout tilLastNameNonResidentStandard;

    @BindView(R.id.etLastNameNonResidentStandardUserSearchActivity)
    EditText etLastNameNonResidentStandard;

    @BindView(R.id.tilPatronymicNonResidentStandardUserSearchActivity)
    TextInputLayout tilPatronymicNonResidentStandard;

    @BindView(R.id.etPatronymicNonResidentStandardUserSearchActivity)
    EditText etPatronymicNonResidentStandard;

    @BindView(R.id.tilPinNonResidentStandardUserSearchActivity)
    TextInputLayout tilPinNonResidentStandard;

    @BindView(R.id.etPinNonResidentStandardUserSearchActivity)
    EditText etPinNonResidentStandard;*/

    @BindView(R.id.tilIdDocumentNonResidentStandardUserSearchActivity)
    TextInputLayout tilIdDocumentNonResidentStandard;

    @BindView(R.id.etIdDocumentNonResidentStandardUserSearchActivity)
    EditText etIdDocumentNonResidentStandard;

    @BindView(R.id.tvIdDocumentTypeNonResidentStandardUserSearchActivity)
    TextView tvIdDocumentTypeNonResidentStandard;

    @BindView(R.id.rgIdDocumentTypeNonResidentStandardUserSearchActivity)
    RadioGroup rgIdDocumentTypeNonResidentStandard;

    @BindView(R.id.rbPRCNonResidentStandardUserSearchActivity)
    RadioButton rbPRCNonResidentStandard;

    @BindView(R.id.rbTRCNonResidentStandardUserSearchActivity)
    RadioButton rbTRCNonResidentStandard;

    /* -------------- Border operation views --------------- */

    @BindView(R.id.clBorderUserSearchActivity)
    ConstraintLayout clBorder;

    @BindView(R.id.cbResidentBorderUserSearchActivity)
    CheckBox cbResidentBorder;

    @BindView(R.id.tilIdDocumentBorderUserSearchActivity)
    TextInputLayout tilIdDocumentBorder;

    @BindView(R.id.etIdDocumentBorderUserSearchActivity)
    EditText etIdDocumentBorder;

    @BindView(R.id.tilTINBorderUserSearchActivity)
    TextInputLayout tilTINBorder;

    @BindView(R.id.etTINBorderUserSearchActivity)
    EditText etTINBorder;

    @BindView(R.id.clNonResidentNaturalBorderUserSearchActivity)
    ConstraintLayout clNonResidentNaturalBorder;

    @BindView(R.id.clNonResidentJuridicalBorderUserSearchActivity)
    ConstraintLayout clNonResidentJuridicalBorder;

    @BindView(R.id.tilFirstNameNonResidentNaturalBorderUserSearchActivity)
    TextInputLayout tilFirstNameNonResidentNaturalBorder;

    @BindView(R.id.etFirstNameNonResidentNaturalBorderUserSearchActivity)
    EditText etFirstNameNonResidentNaturalBorder;

    @BindView(R.id.tilLastNameNonResidentNonResidentNaturalBorderUserSearchActivity)
    TextInputLayout tilLastNameNonResidentNonResidentNaturalBorder;

    @BindView(R.id.etLastNameNonResidentNonResidentNaturalBorderUserSearchActivity)
    EditText etLastNameNonResidentNaturalBorder;

    @BindView(R.id.tilPatronymicNonResidentNaturalBorderUserSearchActivity)
    TextInputLayout tilPatronymicNonResidentNaturalBorder;

    @BindView(R.id.etPatronymicNonResidentNaturalBorderUserSearchActivity)
    EditText etPatronymicNonResidentNaturalBorder;

    @BindView(R.id.tilIdDocumentNonResidentNaturalBorderUserSearchActivity)
    TextInputLayout tilIdDocumentNonResidentNaturalBorder;

    @BindView(R.id.etIdDocumentNonResidentNaturalBorderUserSearchActivity)
    EditText etIdDocumentNonResidentNaturalBorder;

    @BindView(R.id.tilFullNameNonResidentJuridicalBorderUserSearchActivity)
    TextInputLayout tilFullNameNonResidentJuridicalBorder;

    @BindView(R.id.etFullNameNonResidentJuridicalBorderUserSearchActivity)
    EditText etFullNameNonResidentJuridicalBorder;

    @BindView(R.id.tilIdDocumentNonResidentJuridicalBorderUserSearchActivity)
    TextInputLayout tilIdDocumentNonResidentJuridicalBorder;

    @BindView(R.id.etIdDocumentNonResidentJuridicalBorderUserSearchActivity)
    EditText etIdDocumentNonResidentJuridicalBorder;

    /* --------- */

    private ApiInterface apiInterface = null;

    private String operationId;
    private String token;
    private String appCode;
    private String operationCode;

    private SharedPreference sharedPreference;

    private MaterialDialog dialog;

    private Person naturalPerson;
    private Person juridicalPerson;
    private Person nonResidentPerson;

    private ContractPrice contractPrice;

    private boolean isSessionFinished;

    private static final String STANDARD_OPERATION_CODE = "STANDARD_CMTPL_CONTRACT_OPERATION";
    private static final String BORDER_OPERATION_CODE = "BORDER_CMTPL_CONTRACT_OPERATION";
    private static final String GREEN_CARD_OPERATION_CODE = "GREENCARD_CMTPL_CONTRACT_OPERATION";

    private static final int STANDARD_OPERATION = 1;
    private static final int BORDER_OPERATION = 2;
    private static final int GREEN_CARD_OPERATION = 3;

    private int mPersonType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_user_details_search);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle("Sığortalı");

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCancelOperationDialog();
            }
        });

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        sharedPreference = new SharedPreference(this);

        operationId = sharedPreference.getData(Constants.CONTRACT_OPERATION_ID);
        token = sharedPreference.getData(Constants.TOKEN);
        appCode = sharedPreference.getData(Constants.APP_CODE);
        operationCode = sharedPreference.getData(Constants.OPERATION_CODE);

        operationViewGroupsReDesigner();

        btnUserSearchEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbResidentBorder.isChecked()) {
                    // post info
                    if (isValidate()) {
                        showProgress();
                        postPersonInfo();
                    }
                } else {
                    // get info
                    if (isValidate()) {
                        showProgress();
                        getPersonInfo();
                    }
                }
            }
        });
    }

    private boolean isValidate() {
        String errorText = "Məlumatlar boş ola bilməz!";

        switch (operationCode) {
            case STANDARD_OPERATION_CODE:
            case GREEN_CARD_OPERATION_CODE:

                if (rbNaturalPerson.isChecked()) {
                    if (!cbResidentStandard.isChecked()) {
                        if (etIdDocumentStandard.getEditableText().length() == 0) {
                            tilIdDocumentStandard.setErrorEnabled(true);
                            tilIdDocumentStandard.setError(errorText);
                            return false;
                        } else {
                            return true;
                        }
                    } else {
                        /*if (etFirstNameNonResidentStandard.getEditableText().length() == 0) {
                            tilFirstNameNonResidentStandard.setErrorEnabled(true);
                            tilFirstNameNonResidentStandard.setError(errorText);
                            return false;
                        } else if (etLastNameNonResidentStandard.getEditableText().length() == 0) {
                            tilFirstNameNonResidentStandard.setErrorEnabled(false);
                            tilLastNameNonResidentStandard.setErrorEnabled(true);
                            tilLastNameNonResidentStandard.setError(errorText);
                            return false;
                        } else if (etPinNonResidentStandard.g=etEditableText().length() == 0) {
                            tilLastNameNonResidentStandard.setErrorEnabled(false);
                            tilPinNonResidentStandard.setErrorEnabled(true);
                            tilPinNonResidentStandard.setError(errorText);
                            return false;
                        } else*/ if (etIdDocumentNonResidentStandard.getEditableText().length() == 0) {
                            //tilPinNonResidentStandard.setErrorEnabled(false);
                            tilIdDocumentNonResidentStandard.setErrorEnabled(true);
                            tilIdDocumentNonResidentStandard.setError(errorText);
                            return false;
                        } else if (!rbTRCNonResidentStandard.isChecked() && !rbPRCNonResidentStandard.isChecked()) {
                            tilIdDocumentNonResidentStandard.setErrorEnabled(false);
                            showValidatingDialog();
                            return false;
                        } else {
                            return true;
                        }
                    }
                } else {
                    if (etTINStandard.getEditableText().length() == 0) {
                        tilTINStandard.setErrorEnabled(true);
                        tilTINStandard.setError(errorText);
                        return false;
                    } else {
                        tilTINStandard.setErrorEnabled(false);
                        return true;
                    }
                }

            case BORDER_OPERATION_CODE:
                if (rbNaturalPerson.isChecked()) {
                    if (!cbResidentBorder.isChecked()) {
                        if (etIdDocumentBorder.getEditableText().length() == 0) {
                            tilIdDocumentBorder.setErrorEnabled(true);
                            tilIdDocumentBorder.setError(errorText);
                            return false;
                        } else {
                            tilIdDocumentBorder.setErrorEnabled(false);
                            return true;
                        }
                    } else {
                        if (etFirstNameNonResidentNaturalBorder.getEditableText().length() == 0) {
                            tilFirstNameNonResidentNaturalBorder.setErrorEnabled(true);
                            tilFirstNameNonResidentNaturalBorder.setError(errorText);
                            return false;
                        } else if (etLastNameNonResidentNaturalBorder.getEditableText().length() == 0) {
                            tilFirstNameNonResidentNaturalBorder.setErrorEnabled(false);
                            tilLastNameNonResidentNonResidentNaturalBorder.setErrorEnabled(true);
                            tilLastNameNonResidentNonResidentNaturalBorder.setError(errorText);
                            return false;
                        } else if (etIdDocumentNonResidentNaturalBorder.getEditableText().length() == 0) {
                            tilLastNameNonResidentNonResidentNaturalBorder.setErrorEnabled(false);
                            tilIdDocumentNonResidentNaturalBorder.setErrorEnabled(true);
                            tilIdDocumentNonResidentNaturalBorder.setError(errorText);
                            return false;
                        } else {
                            tilIdDocumentNonResidentNaturalBorder.setErrorEnabled(false);
                            return true;
                        }
                    }
                } else {
                    if (!cbResidentBorder.isChecked()) {
                        if (etTINBorder.getEditableText().length() == 0) {
                            tilTINBorder.setErrorEnabled(true);
                            tilTINBorder.setError(errorText);
                            return false;
                        } else {
                            tilTINBorder.setErrorEnabled(false);
                            return true;
                        }
                    } else {
                        if (etFullNameNonResidentJuridicalBorder.getEditableText().length() == 0) {
                            tilFullNameNonResidentJuridicalBorder.setErrorEnabled(true);
                            tilFullNameNonResidentJuridicalBorder.setError(errorText);
                            return false;
                        } else if (etIdDocumentNonResidentJuridicalBorder.getEditableText().length() == 0) {
                            tilFullNameNonResidentJuridicalBorder.setErrorEnabled(false);
                            tilIdDocumentNonResidentJuridicalBorder.setErrorEnabled(true);
                            tilIdDocumentNonResidentJuridicalBorder.setError(errorText);
                            return false;
                        } else {
                            tilIdDocumentNonResidentJuridicalBorder.setErrorEnabled(false);
                            return true;
                        }
                    }
                }
        }
        return false;
    }

    private void getPersonInfo() {
        if (operationCode.equals(BORDER_OPERATION_CODE)) {
            if (rbNaturalPerson.isChecked()) {
                String idDocument = etIdDocumentBorder.getEditableText().toString();
                // getNaturalPersonInfo
                getNaturalPersonInfo(idDocument);
            } else {
                String tin = etTINBorder.getEditableText().toString();
                // getJuridicalPersonInfo
                getJuridicalPersonInfo(tin);
            }
        } else {
            if (rbNaturalPerson.isChecked()) {
                if (!cbResidentStandard.isChecked()) {
                    String idDocument = etIdDocumentStandard.getEditableText().toString();
                    // getNaturalPersonInfo
                    getNaturalPersonInfo(idDocument);
                } else {
                    String rcNumber = etIdDocumentNonResidentStandard.getEditableText().toString();
                    String docType = rbTRCNonResidentStandard.isChecked() ? "TRC" : "PRC";
                    // getNonResidentPersonInfo
                    getNonResidentPersonInfo(rcNumber, docType);
                }
            } else {
                String tin = etTINStandard.getEditableText().toString();
                // getJuridicalPersonInfo
                getJuridicalPersonInfo(tin);
            }
        }
    }

    private void postPersonInfo() {
        nonResidentPerson = new Person();
        if (operationCode.equals(BORDER_OPERATION_CODE)) {
            // Border operation
            if (rbNaturalPerson.isChecked()) {
                // personType = NATURAL_PERSON_TYPE
                String personType = "NATURAL_PERSON_TYPE";
                String lastName = etLastNameNonResidentNaturalBorder.getEditableText().toString();
                String firstName = etFirstNameNonResidentNaturalBorder.getEditableText().toString();
                String patronymic = etPatronymicNonResidentNaturalBorder.getEditableText().toString();
                String idDocument = etIdDocumentNonResidentNaturalBorder.getEditableText().toString();
                // sendNonResidentNaturalBorderPersonInfo
                nonResidentPerson.setPersonType(personType);
                nonResidentPerson.setLastName(lastName);
                nonResidentPerson.setFirstName(firstName);
                nonResidentPerson.setPatronymic(patronymic);
                nonResidentPerson.setIdDocument(idDocument);
                sendNonResidentNaturalPersonInfo();
            } else {
                // personType = JURIDICAL_PERSON_TYPE
                String personType = "JURIDICAL_PERSON_TYPE";
                String fullName = etFullNameNonResidentJuridicalBorder.getEditableText().toString();
                String idDocument = etIdDocumentNonResidentJuridicalBorder.getEditableText().toString();
                // sendNonResidentJuridicalBorderPersonInfo
                nonResidentPerson.setPersonType(personType);
                nonResidentPerson.setFullName(fullName);
                nonResidentPerson.setIdDocument(idDocument);
                sendNonResidentNaturalPersonInfo();
            }
        } /*else {
            // Standard || GreenCard operation
            String lastName = etLastNameNonResidentStandard.getEditableText().toString();
            String firstName = etFirstNameNonResidentStandard.getEditableText().toString();
            String patronymic = etPatronymicNonResidentStandard.getEditableText().toString();
            String pin = etPinNonResidentStandard.getEditableText().toString();
            String idDocument = etIdDocumentNonResidentStandard.getEditableText().toString();
            String idDocumentType = rbTRCNonResidentStandard.isChecked() ? "TRC" : "PRC";
            // sendNonResidentStandardPersonInfo
            nonResidentPerson.setLastName(lastName);
            nonResidentPerson.setFirstName(firstName);
            nonResidentPerson.setPatronymic(patronymic);
            nonResidentPerson.setPin(pin);
            nonResidentPerson.setIdDocument(idDocument);
            nonResidentPerson.setIdDocumentType(idDocumentType);
            sendNonResidentNaturalPersonInfo();
        }*/
    }

    // UI
    private void operationViewGroupsReDesigner() {
        switch (operationCode) {
            case STANDARD_OPERATION_CODE:

                clStandard.setVisibility(View.VISIBLE);
                clBorder.setVisibility(View.GONE);
                mPersonType = STANDARD_OPERATION;
                break;

            case BORDER_OPERATION_CODE:

                clStandard.setVisibility(View.GONE);
                clBorder.setVisibility(View.VISIBLE);
                mPersonType = BORDER_OPERATION;
                break;

            case GREEN_CARD_OPERATION_CODE:

                clStandard.setVisibility(View.VISIBLE);
                clBorder.setVisibility(View.GONE);
                mPersonType = GREEN_CARD_OPERATION;
                break;
        }

        operationViewsReDesigner(mPersonType);
    }

    private void operationViewsReDesigner(int operationType) {
        if (operationType == STANDARD_OPERATION) {
            // standard operation
            rbNaturalPerson.setChecked(true);
            cbResidentBorder.setVisibility(View.GONE);
            cbResidentStandard.setVisibility(View.VISIBLE);
            cbResidentStandard.setChecked(false);

            rbNaturalPerson.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        tilIdDocumentStandard.setVisibility(View.VISIBLE);
                        tilTINStandard.setVisibility(View.GONE);
                        clNonResidentStandard.setVisibility(View.GONE);
                        cbResidentStandard.setVisibility(View.VISIBLE);
                        cbResidentStandard.setChecked(false);
                    } else {
                        tilIdDocumentStandard.setVisibility(View.GONE);
                        tilTINStandard.setVisibility(View.VISIBLE);
                        clNonResidentStandard.setVisibility(View.GONE);
                        cbResidentStandard.setVisibility(View.GONE);
                    }
                }
            });

            cbResidentStandard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        tilIdDocumentStandard.setVisibility(View.GONE);
                        tilTINStandard.setVisibility(View.GONE);
                        clNonResidentStandard.setVisibility(View.VISIBLE);
                        //btnUserSearchEnter.setText("DAXİL ET");
                    } else {
                        tilIdDocumentStandard.setVisibility(View.VISIBLE);
                        tilTINStandard.setVisibility(View.GONE);
                        clNonResidentStandard.setVisibility(View.GONE);
                        //btnUserSearchEnter.setText("AXTAR");
                    }
                }
            });
        } else if (operationType == BORDER_OPERATION) {
            // border operation
            tilIdDocumentBorder.setVisibility(View.VISIBLE);
            tilTINBorder.setVisibility(View.GONE);
            rbNaturalPerson.setChecked(true);
            cbResidentStandard.setVisibility(View.GONE);
            cbResidentBorder.setVisibility(View.VISIBLE);
            cbResidentBorder.setChecked(false);
            clNonResidentNaturalBorder.setVisibility(View.GONE);
            clNonResidentJuridicalBorder.setVisibility(View.GONE);

            rbNaturalPerson.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        tilIdDocumentBorder.setVisibility(View.VISIBLE);
                        tilTINBorder.setVisibility(View.GONE);
                        cbResidentBorder.setChecked(false);
                        clNonResidentNaturalBorder.setVisibility(View.GONE);
                        clNonResidentJuridicalBorder.setVisibility(View.GONE);
                    } else {
                        tilIdDocumentBorder.setVisibility(View.GONE);
                        tilTINBorder.setVisibility(View.VISIBLE);
                        cbResidentBorder.setChecked(false);
                        clNonResidentNaturalBorder.setVisibility(View.GONE);
                        clNonResidentJuridicalBorder.setVisibility(View.GONE);
                    }
                }
            });

            cbResidentBorder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        tilIdDocumentBorder.setVisibility(View.GONE);
                        tilTINBorder.setVisibility(View.GONE);
                        btnUserSearchEnter.setText("Göndər");

                        if (rbNaturalPerson.isChecked()) {
                            clNonResidentNaturalBorder.setVisibility(View.VISIBLE);
                            clNonResidentJuridicalBorder.setVisibility(View.GONE);
                        } else {
                            clNonResidentNaturalBorder.setVisibility(View.GONE);
                            clNonResidentJuridicalBorder.setVisibility(View.VISIBLE);
                        }
                    } else {
                        btnUserSearchEnter.setText("Axtar");
                        if (rbNaturalPerson.isChecked()) {
                            tilIdDocumentBorder.setVisibility(View.VISIBLE);
                            tilTINBorder.setVisibility(View.GONE);
                        } else {
                            tilIdDocumentBorder.setVisibility(View.GONE);
                            tilTINBorder.setVisibility(View.VISIBLE);
                        }
                        clNonResidentNaturalBorder.setVisibility(View.GONE);
                        clNonResidentJuridicalBorder.setVisibility(View.GONE);
                    }
                }
            });
        } else {
            // greenCard operation
            rbNaturalPerson.setChecked(true);
            cbResidentBorder.setVisibility(View.GONE);
            cbResidentStandard.setVisibility(View.VISIBLE);
            cbResidentStandard.setChecked(false);

            rbNaturalPerson.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        tilIdDocumentStandard.setVisibility(View.VISIBLE);
                        tilTINStandard.setVisibility(View.GONE);
                        clNonResidentStandard.setVisibility(View.GONE);
                        cbResidentStandard.setVisibility(View.VISIBLE);
                        cbResidentStandard.setChecked(false);
                    } else {
                        tilIdDocumentStandard.setVisibility(View.GONE);
                        tilTINStandard.setVisibility(View.VISIBLE);
                        clNonResidentStandard.setVisibility(View.GONE);
                        cbResidentStandard.setVisibility(View.GONE);
                    }
                }
            });

            cbResidentStandard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        tilIdDocumentStandard.setVisibility(View.GONE);
                        tilTINStandard.setVisibility(View.GONE);
                        clNonResidentStandard.setVisibility(View.VISIBLE);
                        btnUserSearchEnter.setText("Göndər");
                    } else {
                        tilIdDocumentStandard.setVisibility(View.VISIBLE);
                        tilTINStandard.setVisibility(View.GONE);
                        clNonResidentStandard.setVisibility(View.GONE);
                        btnUserSearchEnter.setText("Axtar");
                    }
                }
            });
        }
    }

    private void setMargins(int startMargin) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) cardView.getLayoutParams();
        params.topMargin = startMargin;
    }

    // REST
    private void getNaturalPersonInfo(String idDocument) {
        Log.d(TAG, "getNaturalPersonInfo: " + idDocument);
        Call<Person> callNaturalPerson = apiInterface.naturalPersonInfo(operationId, token, appCode, idDocument);

        callNaturalPerson.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(@NonNull Call<Person> call, @NonNull Response<Person> response) {
                if (dialog.isShowing())
                    dialog.dismiss();
                int statusCode = response.code();

                Log.i(TAG, "onResponse: statusCode: " + statusCode + "\nMessage: " + response.message());

                if (statusCode == 200 && response.body() != null) {
                    isSessionFinished = true;
                    sharedPreference.saveData(Constants.IS_UNFINISHED, false);

                    naturalPerson = response.body();
                    Log.i(TAG, "onResponse: Natural Person Info: " + naturalPerson.toString());

                    sharedPreference.saveData(Constants.INSURED_FULL_NAME, naturalPerson.getFullName());

                    Gson gson = new Gson();
                    String personInfo = gson.toJson(naturalPerson);

                    Intent intent = new Intent(UserSearchActivity.this, UserDetailsActivity.class);
                    intent.putExtra(Constants.PERSON_TYPE, Constants.NATURAL_PERSON);
                    intent.putExtra(Constants.PERSON_INFO, personInfo);
                    startActivity(intent);

                } else if (statusCode == 401) {
                    showTokenExpiredDialog(UserSearchActivity.this);
                } else {
                    Log.w(TAG, "getNaturalPersonInfo: onResponse");
                    errorBodyConverter(UserSearchActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Person> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                showFailureDialog(UserSearchActivity.this);
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void getJuridicalPersonInfo(String tin) {
        Log.d(TAG, "getJuridicalPersonInfo: " + tin);
        Call<Person> callJuridicalPerson = apiInterface.juridicalPersonInfo(operationId, token, appCode, tin);

        callJuridicalPerson.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(@NonNull Call<Person> call, @NonNull Response<Person> response) {
                if (dialog.isShowing())
                    dialog.dismiss();
                int statusCode = response.code();

                Log.i(TAG, "getJuridicalPersonInfo: onResponse: statusCode: " + statusCode + "\nMessage: " + response.message());

                if (statusCode == 200 && response.body() != null) {
                    isSessionFinished = true;
                    sharedPreference.saveData(Constants.IS_UNFINISHED, false);
                    juridicalPerson = response.body();
                    Log.i(TAG, "onResponse: Juridical Person Info: " + juridicalPerson.toString());

                    sharedPreference.saveData(Constants.INSURED_FULL_NAME, juridicalPerson.getFullName());

                    Gson gson = new Gson();
                    String personInfo = gson.toJson(juridicalPerson);

                    Intent intent = new Intent(UserSearchActivity.this, UserDetailsActivity.class);
                    intent.putExtra(Constants.PERSON_TYPE, Constants.JURIDICAL_PERSON);
                    intent.putExtra("personInfo", personInfo);
                    startActivity(intent);

                } else if (statusCode == 401) {
                    showTokenExpiredDialog(UserSearchActivity.this);
                } else {
                    Log.w(TAG, "getJuridicalPersonInfo: onResponse");
                    errorBodyConverter(UserSearchActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Person> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                showFailureDialog(UserSearchActivity.this);
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void getNonResidentPersonInfo(String rcNumber, String docType){
        Log.d(TAG, "getNonResidentPersonInfo: rcNumber: " + rcNumber + "\ndocType: " + docType);
        Call<Person> callNonResidentPersonInfo = apiInterface.nonResidentPersonInfo(operationId, token, appCode, rcNumber, docType);

        callNonResidentPersonInfo.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(@NonNull Call<Person> call, @NonNull Response<Person> response) {
                if (dialog.isShowing())
                    dialog.dismiss();

                int statusCode = response.code();

                Log.i(TAG, "getNonResidentPersonInfo: onResponse: statusCode: " + statusCode +
                        "\nMessage: " + response.message());

                if (statusCode == 200 && response.body() != null) {
                    isSessionFinished = true;
                    sharedPreference.saveData(Constants.IS_UNFINISHED, false);

                    nonResidentPerson = response.body();
                    Log.i(TAG, "onResponse: nonResidentPersonInfo: " + nonResidentPerson.toString());

                    sharedPreference.saveData(Constants.INSURED_FULL_NAME, nonResidentPerson.getFullName());

                    Gson gson = new Gson();
                    String personInfo = gson.toJson(nonResidentPerson);

                    Intent intent = new Intent(UserSearchActivity.this, UserDetailsActivity.class);
                    intent.putExtra(Constants.PERSON_TYPE, Constants.NON_RESIDENT_PERSON);
                    intent.putExtra("personInfo", personInfo);
                    startActivity(intent);

                } else if (statusCode == 401) {
                    showTokenExpiredDialog(UserSearchActivity.this);
                } else {
                    Log.w(TAG, "getNonResidentPersonInfo: onResponse");
                    errorBodyConverter(UserSearchActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Person> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                showFailureDialog(UserSearchActivity.this);
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void sendNonResidentNaturalPersonInfo() {
        if (nonResidentPerson == null)
            Log.d(TAG, "getNonResidentPersonInfo: NonResidentObject is null!");

        Call<Person> sendNonResidentPersonInfoCall = apiInterface.sendNonResidentPersonInfo(operationId, token, appCode, nonResidentPerson);

        sendNonResidentPersonInfoCall.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(@NonNull Call<Person> call, @NonNull Response<Person> response) {
                if (dialog.isShowing())
                    dialog.dismiss();

                int statusCode = response.code();

                Log.d(TAG, "onResponse: statusCode: " + statusCode + "\nMessage: " + response.message());

                if (statusCode == 200 && response.body() != null) {
                    isSessionFinished = true;
                    sharedPreference.saveData(Constants.IS_UNFINISHED, false);

                    nonResidentPerson = response.body();
                    Log.i(TAG, "onResponse: Natural Person Info: " + nonResidentPerson.toString());

                    sharedPreference.saveData(Constants.INSURED_FULL_NAME, nonResidentPerson.getFullName());

                    Gson gson = new Gson();
                    String personInfo = gson.toJson(nonResidentPerson);

                    if (operationCode.equals(BORDER_OPERATION_CODE) || operationCode.equals(GREEN_CARD_OPERATION_CODE)) {
                        Intent intent = new Intent(UserSearchActivity.this, PeriodActivity.class);
                        startActivity(intent);
                    } else {
                        showProgress();
                        getContractDetails();
                    }
                } else if (statusCode == 401) {
                    showTokenExpiredDialog(UserSearchActivity.this);
                } else {
                    Log.w(TAG, "sendNonResidentNaturalPersonInfo: onResponse");
                    errorBodyConverter(UserSearchActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Person> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                showFailureDialog(UserSearchActivity.this);
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

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

                    Intent intent = new Intent(UserSearchActivity.this, ContractConfirmationActivity.class);
                    intent.putExtra(Constants.CONTRACT_PRICE_OBJECT, contractPriceObject);
                    startActivity(intent);

                } else if (statusCode == 401) {
                    showTokenExpiredDialog(UserSearchActivity.this);
                } else {
                    Log.w(TAG, "getContractDetails: onResponse");
                    errorBodyConverter(UserSearchActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ContractPrice> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                showFailureDialog(UserSearchActivity.this);
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void cancelContractOperation() {
        Log.d(TAG, "cancelContractOperation: " + operationId);
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

                    Intent intent = new Intent(UserSearchActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else if (statusCode == 401) {
                    showTokenExpiredDialog(UserSearchActivity.this);
                } else {
                    Log.w(TAG, "cancelContractOperation: onResponse");
                    errorBodyConverter(UserSearchActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                showFailureDialog(UserSearchActivity.this);
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    /* ----- Dialogs ----- */
    private void showCancelOperationDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(UserSearchActivity.this)
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

    private void showValidatingDialog() {
        new MaterialDialog.Builder(this)
                .title("Diqqət")
                .content("Zəhmət olmasa, Yaşama icazəsi sənədinin növünü seçin!")
                .positiveText("OLDU")
                .build()
                .show();
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
        sharedPreference.saveData(Constants.UNFINISHED_SESSION_ACTIVITY, UserSearchActivity.class.getName());
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
            switch (operationCode) {

                case GREEN_CARD_OPERATION_CODE:
                case STANDARD_OPERATION_CODE:
                    etIdDocumentStandard.setText(sharedPreference.getData(Constants.SESSION_USER_ID));
                    etTINStandard.setText(sharedPreference.getData(Constants.SESSION_USER_TIN));

                    if (sharedPreference.getData(Constants.SESSION_USER_RESIDENCY).equals("nonResident")) {
                        etIdDocumentNonResidentStandard.setText(sharedPreference.getData(Constants.SESSION_USER_ID));

                        switch (sharedPreference.getData(Constants.SESSION_USER_DOC_TYPE)) {
                            case "TRC":
                                rbTRCNonResidentStandard.setChecked(true);
                                rbPRCNonResidentStandard.setChecked(false);
                                break;
                            case "PRC":
                                rbPRCNonResidentStandard.setChecked(true);
                                rbTRCNonResidentStandard.setChecked(false);
                                break;
                            case "":
                                rbTRCNonResidentStandard.setChecked(false);
                                rbPRCNonResidentStandard.setChecked(false);
                                break;
                        }
                    }

                    break;

                case BORDER_OPERATION_CODE:
                    etIdDocumentBorder.setText(sharedPreference.getData(Constants.SESSION_USER_ID));
                    etTINBorder.setText(sharedPreference.getData(Constants.SESSION_USER_TIN));

                    if (sharedPreference.getData(Constants.SESSION_USER_RESIDENCY).equals("nonResident")) {
                        Gson gson = new Gson();

                        String userObject = sharedPreference.getData(Constants.SESSION_USER_OBJECT);
                        Log.d(TAG, "restoreSession: userObject: " + userObject);
                        Person nonResidentPerson = gson.fromJson(userObject, Person.class);

                        etFirstNameNonResidentNaturalBorder.setText(nonResidentPerson.getFirstName());
                        etLastNameNonResidentNaturalBorder.setText(nonResidentPerson.getLastName());
                        etPatronymicNonResidentNaturalBorder.setText(nonResidentPerson.getPatronymic());
                        etIdDocumentNonResidentNaturalBorder.setText(nonResidentPerson.getIdDocument());
                        etFullNameNonResidentJuridicalBorder.setText(nonResidentPerson.getFullName());
                        etIdDocumentNonResidentJuridicalBorder.setText(nonResidentPerson.getIdDocument());
                    }
                    break;
            }
        }
    }

    private void saveSession() {
        if (!isSessionFinished) {
            if (operationCode.equals(STANDARD_OPERATION_CODE) || operationCode.equals(GREEN_CARD_OPERATION_CODE)) {
                if (rbNaturalPerson.isChecked()) {

                    if (!cbResidentStandard.isChecked()) {
                        sharedPreference.saveData(Constants.SESSION_USER_ID, etIdDocumentStandard.getEditableText().toString());
                    } else {
                        /*Person nonResidentPerson = new Person();
                        nonResidentPerson.setFirstName(etFirstNameNonResidentStandard.getEditableText().toString());
                        nonResidentPerson.setLastName(etLastNameNonResidentStandard.getEditableText().toString());
                        nonResidentPerson.setPatronymic(etPatronymicNonResidentStandard.getEditableText().toString());
                        nonResidentPerson.setPin(etPinNonResidentStandard.getEditableText().toString());*/

                        sharedPreference.saveData(Constants.SESSION_USER_ID, etIdDocumentNonResidentStandard.getEditableText().toString());
                        sharedPreference.saveData(Constants.SESSION_USER_DOC_TYPE, rbTRCNonResidentStandard.isChecked() ? "TRC" : (rbPRCNonResidentStandard.isChecked() ? "PRC" : ""));
                        sharedPreference.saveData(Constants.SESSION_USER_RESIDENCY, "nonResident");
                    }
                } else {
                    sharedPreference.saveData(Constants.SESSION_USER_TIN, etTINStandard.getEditableText().toString());
                }
            } else if (operationCode.equals(BORDER_OPERATION_CODE)) {
                if (rbNaturalPerson.isChecked()) {

                    if (!cbResidentBorder.isChecked()) {
                        sharedPreference.saveData(Constants.SESSION_USER_ID, etIdDocumentBorder.getEditableText().toString());
                    } else {
                        Person nonResidentPerson = new Person();
                        nonResidentPerson.setFirstName(etFirstNameNonResidentNaturalBorder.getEditableText().toString());
                        nonResidentPerson.setLastName(etLastNameNonResidentNaturalBorder.getEditableText().toString());
                        nonResidentPerson.setPatronymic(etPatronymicNonResidentNaturalBorder.getEditableText().toString());
                        nonResidentPerson.setIdDocument(etIdDocumentNonResidentNaturalBorder.getEditableText().toString());

                        Gson gson = new Gson();

                        String userObject = gson.toJson(nonResidentPerson);
                        Log.d(TAG, "saveSession: userObject: " + userObject);
                        sharedPreference.saveData(Constants.SESSION_USER_OBJECT, userObject);
                        sharedPreference.saveData(Constants.SESSION_USER_RESIDENCY, "nonResident");
                    }
                } else {

                    if (!cbResidentBorder.isChecked()) {
                        sharedPreference.saveData(Constants.SESSION_USER_TIN, etTINBorder.getEditableText().toString());
                    } else {
                        Person nonResidentPerson = new Person();
                        nonResidentPerson.setFullName(etFullNameNonResidentJuridicalBorder.getEditableText().toString());
                        nonResidentPerson.setIdDocument(etIdDocumentNonResidentJuridicalBorder.getEditableText().toString());

                        Gson gson = new Gson();

                        String userObject = gson.toJson(nonResidentPerson);
                        Log.d(TAG, "saveSession: userObject: " + userObject);
                        sharedPreference.saveData(Constants.SESSION_USER_OBJECT, userObject);
                        sharedPreference.saveData(Constants.SESSION_USER_RESIDENCY, "nonResident");
                    }
                }
            }

            sharedPreference.saveData(Constants.IS_UNFINISHED, true);
            sharedPreference.saveData(Constants.UNFINISHED_SESSION_ACTIVITY, UserSearchActivity.class.getName());
        }
    }

    private void sharedFlusher() {

    }
}
