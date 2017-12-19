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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

import org.simberg.cib.policywriting.R;
import org.simberg.cib.policywriting.java.Constants;
import org.simberg.cib.policywriting.java.SharedPreference;
import org.simberg.cib.policywriting.models.Vehicle;
import org.simberg.cib.policywriting.rest.ApiClient;
import org.simberg.cib.policywriting.rest.ApiInterface;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by javadbadirkhanly on 10/3/17.
 */

public class VehicleSearchActivity extends ParentActivity {

    private static final String TAG = VehicleSearchActivity.class.getSimpleName();

    /* ----- Views ----- */
    @BindView(R.id.toolbarVehicleSearchActivity)
    Toolbar toolbar;

    @BindView(R.id.imgCancelVehicleSearchActivity)
    ImageView imgCancel;

    @BindView(R.id.cvVehicleInfoVehicleSearchActivity)
    CardView cardView;

    @BindView(R.id.btnVehicleSearchEnterVehicleSearchActivity)
    Button btnSearch;

    /*-------- Standard Operation ---------*/
    @BindView(R.id.clStandardVehicleSearchActivity)
    ConstraintLayout clStandard;

    @BindView(R.id.rgMIANonMIAStandardVehicleSearchActivity)
    RadioGroup rgMIANonMIAStandard;

    @BindView(R.id.rbMIAStandardVehicleSearchActivity)
    RadioButton rbMIAStandard;

    @BindView(R.id.rbNonMIAStandardVehicleSearchActivity)
    RadioButton rbNonMIAStandard;

    @BindView(R.id.tilCertificateNumberStandardVehicleSearchActivity)
    TextInputLayout tilCertificateNumberStandard;

    @BindView(R.id.etCertificateNumberStandardVehicleSearchActivity)
    EditText etCertificateNumberStandard;

    @BindView(R.id.tilCarNumberStandardVehicleSearchActivity)
    TextInputLayout tilCarNumberStandard;

    @BindView(R.id.etCarNumberStandardVehicleSearchActivity)
    EditText etCarNumberStandard;

    @BindView(R.id.tilMakeStandardVehicleSearchActivity)
    TextInputLayout tilMakeStandard;

    @BindView(R.id.etMakeStandardVehicleSearchActivity)
    EditText etMakeStandard;

    @BindView(R.id.tilModelStandardVehicleSearchActivity)
    TextInputLayout tilModelStandard;

    @BindView(R.id.etModelStandardVehicleSearchActivity)
    EditText etModelStandard;

    @BindView(R.id.tilBodyNumberStandardVehicleSearchActivity)
    TextInputLayout tilBodyNumberStandard;

    @BindView(R.id.etBodyNumberStandardVehicleSearchActivity)
    EditText etBodyNumberStandard;

    @BindView(R.id.tilEngineNumberStandardVehicleSearchActivity)
    TextInputLayout tilEngineNumberStandard;

    @BindView(R.id.etEngineNumberStandardVehicleSearchActivity)
    EditText etEngineNumberStandard;

    @BindView(R.id.tilChassisNumberStandardVehicleSearchActivity)
    TextInputLayout tilChassisNumberStandard;

    @BindView(R.id.etChassisNumberStandardVehicleSearchActivity)
    EditText etChassisNumberStandard;

    @BindView(R.id.tilYearOfManufactureStandardVehicleSearchActivity)
    TextInputLayout tilYearOfManufactureStandard;

    @BindView(R.id.etYearOfManufactureStandardVehicleSearchActivity)
    EditText etYearOfManufactureStandard;

    @BindView(R.id.tilVehicleColorStandardVehicleSearchActivity)
    TextInputLayout tilVehicleColorStandard;

    @BindView(R.id.etVehicleColorStandardVehicleSearchActivity)
    EditText etVehicleColorStandard;

    /*--------- Border Operation ---------*/
    @BindView(R.id.clBorderVehicleSearchActivity)
    ConstraintLayout clBorder;

    @BindView(R.id.tilCertificateNumberBorderVehicleSearchActivity)
    TextInputLayout tilCertificateNumberBorder;

    @BindView(R.id.etCertificateNumberBorderVehicleSearchActivity)
    EditText etCertificateNumberBorder;

    @BindView(R.id.tilCarNumberBorderVehicleSearchActivity)
    TextInputLayout tilCarNumberBorder;

    @BindView(R.id.etCarNumberBorderVehicleSearchActivity)
    EditText etCarNumberBorder;

    @BindView(R.id.tilMakeBorderVehicleSearchActivity)
    TextInputLayout tilMakeBorder;

    @BindView(R.id.etMakeBorderVehicleSearchActivity)
    EditText etMakeBorder;

    @BindView(R.id.tilModelBorderVehicleSearchActivity)
    TextInputLayout tilModelBorder;

    @BindView(R.id.etModelBorderVehicleSearchActivity)
    EditText etModelBorder;

    @BindView(R.id.tilBodyNumberBorderVehicleSearchActivity)
    TextInputLayout tilBodyNumberBorder;

    @BindView(R.id.etBodyNumberBorderVehicleSearchActivity)
    EditText etBodyNumberBorder;

    @BindView(R.id.tilEngineNumberBorderVehicleSearchActivity)
    TextInputLayout tilEngineNumberBorder;

    @BindView(R.id.etEngineNumberBorderVehicleSearchActivity)
    EditText etEngineNumberBorder;

    @BindView(R.id.tilChassisNumberBorderVehicleSearchActivity)
    TextInputLayout tilChassisNumberBorder;

    @BindView(R.id.etChassisNumberBorderVehicleSearchActivity)
    EditText etChassisNumberBorder;

    @BindView(R.id.tilYearOfManufactureBorderVehicleSearchActivity)
    TextInputLayout tilYearOfManufactureBorder;

    @BindView(R.id.etYearOfManufactureBorderVehicleSearchActivity)
    EditText etYearOfManufactureBorder;

    @BindView(R.id.tilVehicleColorBorderVehicleSearchActivity)
    TextInputLayout tilVehicleColorBorder;

    @BindView(R.id.etVehicleColorBorderVehicleSearchActivity)
    EditText etVehicleColorBorder;

    @BindView(R.id.tvVehicleTypeBorderVehicleSearchActivity)
    TextView tvVehicleTypeBorder;

    @BindView(R.id.spVehicleTypeBorderVehicleSearchActivity)
    Spinner spVehicleTypeBorder;

    @BindView(R.id.tilEngineCapacityBorderVehicleSearchActivity)
    TextInputLayout tilEngineCapacityBorder;

    @BindView(R.id.etEngineCapacityBorderVehicleSearchActivity)
    EditText etEngineCapacityBorder;

    @BindView(R.id.tilMaxPermissibleMassBorderVehicleSearchActivity)
    TextInputLayout tilMaxPermissibleMassBorder;

    @BindView(R.id.etMaxPermissibleMassBorderVehicleSearchActivity)
    EditText etMaxPermissibleMassBorder;

    @BindView(R.id.tilPassengerSeatCountBorderVehicleSearchActivity)
    TextInputLayout tilPassengerSeatCountBorder;

    @BindView(R.id.etPassengerSeatCountBorderVehicleSearchActivity)
    EditText etPassengerSeatCountBorder;

    @BindView(R.id.tilHorsePowerBorderVehicleSearchActivity)
    TextInputLayout tilHorsePowerBorder;

    @BindView(R.id.etHorsePowerBorderVehicleSearchActivity)
    EditText etHorsePowerBorder;

    /*-------- GreenCard --------*/
    @BindView(R.id.clGreenCardVehicleSearchActivity)
    ConstraintLayout clGreenCard;

    @BindView(R.id.tilCertificateNumberGreenCardVehicleSearchActivity)
    TextInputLayout tilCertificateNumberGreenCard;

    @BindView(R.id.etCertificateNumberGreenCardVehicleSearchActivity)
    EditText etCertificateNumberGreenCard;

    @BindView(R.id.tilCarNumberGreenCardVehicleSearchActivity)
    TextInputLayout tilCarNumberGreenCard;

    @BindView(R.id.etCarNumberGreenCardVehicleSearchActivity)
    EditText etCarNumberGreenCard;

    /* ---------- */

    private ApiInterface apiInterface;

    private String operationId;
    private String token;
    private String appCode;
    private String operationCode;

    private SharedPreference sharedPreference;

    private MaterialDialog dialog;

    private Vehicle vehicle;

    private boolean isSessionFinished;

    private static final String STANDARD_OPERATION_CODE = "STANDARD_CMTPL_CONTRACT_OPERATION";
    private static final String BORDER_OPERATION_CODE = "BORDER_CMTPL_CONTRACT_OPERATION";
    private static final String GREEN_CARD_OPERATION_CODE = "GREENCARD_CMTPL_CONTRACT_OPERATION";

    private static final int STANDARD_OPERATION = 1;
    private static final int BORDER_OPERATION = 2;
    private static final int GREEN_CARD_OPERATION = 3;

    private int mVehicleType = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_search);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Nəqliyyat vasitəsi");


        sharedPreference = new SharedPreference(this);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        operationId = sharedPreference.getData(Constants.CONTRACT_OPERATION_ID);
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
                if (rbMIAStandard.isChecked() || operationCode.equals(GREEN_CARD_OPERATION_CODE)) {
                    if (isValidate()) {
                        showProgress();
                        getVehicleDetails();
                    }
                } else {
                    if (isValidate()) {
                        showProgress();
                        postVehicleDetails();
                    }
                }
            }
        });

        operationViewGroupsReDesigner();
    }

    /* ----- UI ----- */
    private void operationViewGroupsReDesigner() {
        int operationType = 0;
        switch (operationCode) {
            case STANDARD_OPERATION_CODE:
                clStandard.setVisibility(View.VISIBLE);
                clBorder.setVisibility(View.GONE);
                clGreenCard.setVisibility(View.GONE);
                operationType = STANDARD_OPERATION;
                break;
            case BORDER_OPERATION_CODE:
                clStandard.setVisibility(View.GONE);
                clBorder.setVisibility(View.VISIBLE);
                clGreenCard.setVisibility(View.GONE);
                operationType = BORDER_OPERATION;
                break;
            case GREEN_CARD_OPERATION_CODE:
                clStandard.setVisibility(View.GONE);
                clBorder.setVisibility(View.GONE);
                clGreenCard.setVisibility(View.VISIBLE);
                operationType = GREEN_CARD_OPERATION;
                break;
        }
        operationViewsReDesigner(operationType);
    }

    private void operationViewsReDesigner(int operationType) {
        if (operationType == STANDARD_OPERATION) {
            // Standard Operation
            tilCertificateNumberStandard.setVisibility(View.VISIBLE);
            tilCarNumberStandard.setVisibility(View.VISIBLE);
            tilMakeStandard.setVisibility(View.GONE);
            tilModelStandard.setVisibility(View.GONE);
            tilBodyNumberStandard.setVisibility(View.GONE);
            tilEngineNumberStandard.setVisibility(View.GONE);
            tilChassisNumberStandard.setVisibility(View.GONE);
            tilYearOfManufactureStandard.setVisibility(View.GONE);
            tilVehicleColorStandard.setVisibility(View.GONE);
            rbMIAStandard.setChecked(true);

            rbMIAStandard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        tilCertificateNumberStandard.setVisibility(View.VISIBLE);
                        tilCarNumberStandard.setVisibility(View.VISIBLE);
                        tilMakeStandard.setVisibility(View.GONE);
                        tilModelStandard.setVisibility(View.GONE);
                        tilBodyNumberStandard.setVisibility(View.GONE);
                        tilEngineNumberStandard.setVisibility(View.GONE);
                        tilChassisNumberStandard.setVisibility(View.GONE);
                        tilYearOfManufactureStandard.setVisibility(View.GONE);
                        tilVehicleColorStandard.setVisibility(View.GONE);
                        btnSearch.setText("AXTAR");
                    } else {
                        tilCertificateNumberStandard.setVisibility(View.VISIBLE);
                        tilCarNumberStandard.setVisibility(View.VISIBLE);
                        tilMakeStandard.setVisibility(View.VISIBLE);
                        tilModelStandard.setVisibility(View.VISIBLE);
                        tilBodyNumberStandard.setVisibility(View.VISIBLE);
                        tilEngineNumberStandard.setVisibility(View.VISIBLE);
                        tilChassisNumberStandard.setVisibility(View.VISIBLE);
                        tilYearOfManufactureStandard.setVisibility(View.VISIBLE);
                        tilVehicleColorStandard.setVisibility(View.VISIBLE);
                        btnSearch.setText("DAXİL ET");
                    }
                }
            });


        } else if (operationType == BORDER_OPERATION) {
            // Border Operation
            tilCertificateNumberBorder.setVisibility(View.VISIBLE);
            tilCarNumberBorder.setVisibility(View.VISIBLE);
            tilMakeBorder.setVisibility(View.VISIBLE);
            tilModelBorder.setVisibility(View.VISIBLE);
            tilBodyNumberBorder.setVisibility(View.VISIBLE);
            tilEngineNumberBorder.setVisibility(View.VISIBLE);
            tilChassisNumberBorder.setVisibility(View.VISIBLE);
            tilYearOfManufactureBorder.setVisibility(View.VISIBLE);
            tilVehicleColorBorder.setVisibility(View.VISIBLE);
            tvVehicleTypeBorder.setVisibility(View.VISIBLE);
            spVehicleTypeBorder.setVisibility(View.VISIBLE);
            spVehicleTypeBorder.setSelection(0);
            tilEngineCapacityBorder.setVisibility(View.GONE);
            tilMaxPermissibleMassBorder.setVisibility(View.GONE);
            tilPassengerSeatCountBorder.setVisibility(View.VISIBLE);
            tilHorsePowerBorder.setVisibility(View.GONE);
            btnSearch.setText("DAXİL ET");
            mVehicleType = 1;

            switch (mVehicleType) {
                case 1:
                    tilEngineCapacityBorder.setVisibility(View.GONE);
                    tilMaxPermissibleMassBorder.setVisibility(View.GONE);
                    tilPassengerSeatCountBorder.setVisibility(View.VISIBLE);
                    tilHorsePowerBorder.setVisibility(View.GONE);
                    break;
                case 2:
                    tilEngineCapacityBorder.setVisibility(View.GONE);
                    tilMaxPermissibleMassBorder.setVisibility(View.VISIBLE);
                    tilPassengerSeatCountBorder.setVisibility(View.GONE);
                    tilHorsePowerBorder.setVisibility(View.GONE);
                    break;
                case 3:
                    tilEngineCapacityBorder.setVisibility(View.VISIBLE);
                    tilMaxPermissibleMassBorder.setVisibility(View.GONE);
                    tilPassengerSeatCountBorder.setVisibility(View.GONE);
                    tilHorsePowerBorder.setVisibility(View.GONE);
                    break;
                case 7:
                    tilEngineCapacityBorder.setVisibility(View.GONE);
                    tilMaxPermissibleMassBorder.setVisibility(View.GONE);
                    tilPassengerSeatCountBorder.setVisibility(View.GONE);
                    tilHorsePowerBorder.setVisibility(View.VISIBLE);
                    break;
                default:
                    tilEngineCapacityBorder.setVisibility(View.GONE);
                    tilMaxPermissibleMassBorder.setVisibility(View.GONE);
                    tilPassengerSeatCountBorder.setVisibility(View.GONE);
                    tilHorsePowerBorder.setVisibility(View.GONE);
                    break;
            }

            spVehicleTypeBorder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i == 0) {
                        tilEngineCapacityBorder.setVisibility(View.GONE);
                        tilMaxPermissibleMassBorder.setVisibility(View.GONE);
                        tilPassengerSeatCountBorder.setVisibility(View.VISIBLE);
                        tilHorsePowerBorder.setVisibility(View.GONE);
                        mVehicleType = 1;
                    } else if (i == 1) {
                        tilEngineCapacityBorder.setVisibility(View.GONE);
                        tilMaxPermissibleMassBorder.setVisibility(View.VISIBLE);
                        tilPassengerSeatCountBorder.setVisibility(View.GONE);
                        tilHorsePowerBorder.setVisibility(View.GONE);
                        mVehicleType = 2;
                    } else if (i == 2) {
                        tilEngineCapacityBorder.setVisibility(View.VISIBLE);
                        tilMaxPermissibleMassBorder.setVisibility(View.GONE);
                        tilPassengerSeatCountBorder.setVisibility(View.GONE);
                        tilHorsePowerBorder.setVisibility(View.GONE);
                        mVehicleType = 3;
                    } else if (i == 6) {
                        tilEngineCapacityBorder.setVisibility(View.GONE);
                        tilMaxPermissibleMassBorder.setVisibility(View.GONE);
                        tilPassengerSeatCountBorder.setVisibility(View.GONE);
                        tilHorsePowerBorder.setVisibility(View.VISIBLE);
                        mVehicleType = 7;
                    } else {
                        tilEngineCapacityBorder.setVisibility(View.GONE);
                        tilMaxPermissibleMassBorder.setVisibility(View.GONE);
                        tilPassengerSeatCountBorder.setVisibility(View.GONE);
                        tilHorsePowerBorder.setVisibility(View.GONE);
                        mVehicleType = i + 1;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } else {
            // GreenCard Operation
            tilCertificateNumberGreenCard.setVisibility(View.VISIBLE);
            tilCarNumberGreenCard.setVisibility(View.VISIBLE);
        }
    }

    private boolean isValidate() {
        switch (operationCode) {
            case BORDER_OPERATION_CODE:

                if (etCarNumberBorder.getEditableText().length() == 0) {
                    tilCarNumberBorder.setErrorEnabled(true);
                    tilCarNumberBorder.setError("Məlumat boş ola bilməz");
                    return false;
                } else if (tilPassengerSeatCountBorder.getVisibility() == View.VISIBLE && etPassengerSeatCountBorder.getEditableText().length() == 0) {
                    tilCarNumberBorder.setErrorEnabled(false);
                    tilPassengerSeatCountBorder.setErrorEnabled(true);
                    tilPassengerSeatCountBorder.setError("Məlumat boş ola bilməz");
                    return false;
                } else if (tilMaxPermissibleMassBorder.getVisibility() == View.VISIBLE && etMaxPermissibleMassBorder.getEditableText().length() == 0) {
                    tilCarNumberBorder.setErrorEnabled(false);
                    tilMaxPermissibleMassBorder.setErrorEnabled(true);
                    tilMaxPermissibleMassBorder.setError("Məlumat boş ola bilməz");
                    return false;
                } else if (tilEngineCapacityBorder.getVisibility() == View.VISIBLE && etEngineCapacityBorder.getEditableText().length() == 0) {
                    tilCarNumberBorder.setErrorEnabled(false);
                    tilEngineCapacityBorder.setErrorEnabled(true);
                    tilEngineCapacityBorder.setError("Məlumat boş ola bilməz");
                    return false;
                } else if (tilHorsePowerBorder.getVisibility() == View.VISIBLE && etHorsePowerBorder.getEditableText().length() == 0) {
                    tilCarNumberBorder.setErrorEnabled(false);
                    tilHorsePowerBorder.setErrorEnabled(true);
                    tilHorsePowerBorder.setError("Məlumat boş ola bilməz");
                    return false;
                } else if (etBodyNumberBorder.getEditableText().length() == 0 && etEngineNumberBorder.getEditableText().length() == 0 && etChassisNumberBorder.getEditableText().length() == 0) {
                    tilHorsePowerBorder.setErrorEnabled(false);
                    showValidationDialog();
                    return false;
                } else {
                    return true;
                }

            case STANDARD_OPERATION_CODE:

                if (rbMIAStandard.isChecked()) {
                    if (etCertificateNumberStandard.getEditableText().length() == 0) {
                        tilCertificateNumberStandard.setErrorEnabled(true);
                        tilCertificateNumberStandard.setError("Məlumat boş ola bilməz");
                        return false;
                    } else if (etCarNumberStandard.getEditableText().length() == 0) {
                        tilCertificateNumberStandard.setErrorEnabled(false);
                        tilCarNumberStandard.setErrorEnabled(true);
                        tilCarNumberStandard.setError("Məlumat boş ola bilməz");
                        return false;
                    } else {
                        tilCarNumberStandard.setErrorEnabled(false);
                        return true;
                    }
                } else {
                    if (etCarNumberStandard.getEditableText().length() == 0) {
                        tilCarNumberStandard.setErrorEnabled(true);
                        tilCarNumberStandard.setError("Məlumat boş ola bilməz");
                        return false;
                    } else if (etEngineNumberStandard.getEditableText().length() == 0 && etBodyNumberStandard.getEditableText().length() == 0 && etChassisNumberStandard.getEditableText().length() == 0) {
                        tilCarNumberStandard.setErrorEnabled(false);
                        showValidationDialog();
                        return false;
                    } else {
                        return true;
                    }
                }

            case GREEN_CARD_OPERATION_CODE:

                if (etCertificateNumberGreenCard.getEditableText().length() == 0) {
                    tilCertificateNumberGreenCard.setErrorEnabled(true);
                    tilCertificateNumberGreenCard.setError("Məlumat boş ola bilməz");
                    return false;
                } else if (etCarNumberGreenCard.getEditableText().length() == 0) {
                    tilCertificateNumberGreenCard.setErrorEnabled(false);
                    tilCarNumberGreenCard.setErrorEnabled(true);
                    tilCarNumberGreenCard.setError("Məlumat boş ola bilməz");
                    return false;
                } else {
                    tilCarNumberGreenCard.setErrorEnabled(false);
                    return true;
                }
        }
        return false;
    }

    private void setMargins(int startMargin) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) cardView.getLayoutParams();
        params.topMargin = startMargin;

        Log.d(TAG, "setMargins: height of toolbar: " + toolbar.getHeight());
    }

    /* ----- REST ----- */
    private void getVehicleDetails() {
        String certificateNumber;
        String carNumber;
        switch (operationCode) {
            case STANDARD_OPERATION_CODE:
                certificateNumber = etCertificateNumberStandard.getEditableText().toString();
                carNumber = etCarNumberStandard.getEditableText().toString();
                getVehicleInfo(certificateNumber, carNumber);
                break;
            case BORDER_OPERATION_CODE:
                certificateNumber = etCertificateNumberBorder.getEditableText().toString();
                carNumber = etCarNumberBorder.getEditableText().toString();
                getVehicleInfo(certificateNumber, carNumber);
                break;
            case GREEN_CARD_OPERATION_CODE:
                certificateNumber = etCertificateNumberGreenCard.getEditableText().toString();
                carNumber = etCarNumberGreenCard.getEditableText().toString();
                getVehicleInfo(certificateNumber, carNumber);
                break;
        }
    }

    private void postVehicleDetails() {
        vehicle = new Vehicle();
        if (operationCode.equals(STANDARD_OPERATION_CODE)) {

            String certificateNumber = etCertificateNumberStandard.getEditableText().toString();
            String carNumber = etCarNumberStandard.getEditableText().toString();
            String make = etMakeStandard.getEditableText().toString();
            String model = etModelStandard.getEditableText().toString();
            String bodyNumber = etBodyNumberStandard.getEditableText().toString();
            String engineNumber = etEngineNumberStandard.getEditableText().toString();
            String yearOfManufacture = etYearOfManufactureStandard.getEditableText().toString();
            String vehicleColor = etVehicleColorStandard.getEditableText().toString();

            vehicle.setCertificateNumber(certificateNumber);
            vehicle.setCarNumber(carNumber);
            vehicle.setMake(make);
            vehicle.setModel(model);
            vehicle.setBodyNumber(bodyNumber);
            vehicle.setEngineNumber(engineNumber);
            vehicle.setYearOfManufacture(Integer.parseInt(yearOfManufacture));
            vehicle.setVehicleColor(vehicleColor);
            vehicle.setVehicleType("Xüsusi təyinatlı");
            vehicle.setVehicleTypeId(6);

            sendVehicleInfo(vehicle);
        } else if (operationCode.equals(BORDER_OPERATION_CODE)) {

            String certificateNumber = etCertificateNumberBorder.getEditableText().toString();
            String carNumber = etCarNumberBorder.getEditableText().toString();
            String make = etMakeBorder.getEditableText().toString();
            String model = etModelBorder.getEditableText().toString();
            String bodyNumber = etBodyNumberBorder.getEditableText().toString();
            String engineNumber = etEngineNumberBorder.getEditableText().toString();
            String yearOfManufacture = etYearOfManufactureBorder.getEditableText().toString();
            String vehicleColor = etVehicleColorBorder.getEditableText().toString();

            vehicle.setCertificateNumber(certificateNumber);
            vehicle.setCarNumber(carNumber);
            vehicle.setMake(make);
            vehicle.setModel(model);
            vehicle.setBodyNumber(bodyNumber);
            vehicle.setEngineNumber(engineNumber);
            vehicle.setYearOfManufacture(Integer.parseInt(yearOfManufacture.equals("") ? "0" : yearOfManufacture));
            vehicle.setVehicleColor(vehicleColor);
            vehicle.setVehicleType(spVehicleTypeBorder.getItemAtPosition(mVehicleType - 1).toString());
            vehicle.setVehicleTypeId(mVehicleType);

            switch (mVehicleType) {
                case 1:
                    String passengerSeatCount = etPassengerSeatCountBorder.getEditableText().toString();
                    vehicle.setPassengerSeatCount(Integer.parseInt(passengerSeatCount));
                    break;
                case 2:
                    String maxPermissibleMass = etMaxPermissibleMassBorder.getEditableText().toString();
                    vehicle.setMaxPermissibleMass(new BigDecimal(maxPermissibleMass));
                    break;
                case 3:
                    String engineCapacity = etEngineCapacityBorder.getEditableText().toString();
                    vehicle.setEngineCapacity(new BigDecimal(engineCapacity));
                    break;
                case 7:
                    String horsePower = etHorsePowerBorder.getEditableText().toString();
                    vehicle.setHorsePower(Integer.parseInt(horsePower));
                    break;
            }

            sendVehicleInfo(vehicle);
        }
    }

    private void getVehicleInfo(String certificateNumber, String carNumber) {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<Vehicle> callVehicleInfo = apiInterface.vehicleInfo(operationId, token, appCode, certificateNumber, carNumber);

        callVehicleInfo.enqueue(new Callback<Vehicle>() {
            @Override
            public void onResponse(@Nullable Call<Vehicle> call, @Nullable Response<Vehicle> response) {
                if (dialog.isShowing())
                    dialog.dismiss();
                int statusCode = response.code();

                Log.i(TAG, "onResponse: statusCode: " + statusCode + "\nmessage: " + response.message());

                if (statusCode == 200 && response.body() != null) {
                    isSessionFinished = true;
                    sharedPreference.saveData(Constants.IS_UNFINISHED, false);
                    vehicle = response.body();

                    Log.i(TAG, "onResponse: Vehicle Info: " + vehicle.toString());

                    sharedPreference.saveData(Constants.INSURED_CERTIFICATE_NUMBER, vehicle.getCertificateNumber());
                    sharedPreference.saveData(Constants.INSURED_CAR_NUMBER, vehicle.getCarNumber());
                    sharedPreference.saveData(Constants.INSURED_VEHICLE_NAME, vehicle.getMake() + " " + vehicle.getModel());

                    Gson gson = new Gson();
                    String vehicleInfo = gson.toJson(vehicle);

                    Intent intent = new Intent(VehicleSearchActivity.this, VehicleDetailsActivity.class);
                    intent.putExtra("vehicleInfo", vehicleInfo);
                    startActivity(intent);

                } else if (statusCode == 401) {
                    showTokenExpiredDialog(VehicleSearchActivity.this);
                } else {
                    Log.w(TAG, "getVehicleInfo: onResponse");
                    errorBodyConverter(VehicleSearchActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Vehicle> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                showFailureDialog(VehicleSearchActivity.this);
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void sendVehicleInfo(final Vehicle vehicle) {
        Call<Vehicle> nonMiaVehicleInfo = apiInterface.nonMiaVehicleInfo(operationId, appCode, token, vehicle);

        nonMiaVehicleInfo.enqueue(new Callback<Vehicle>() {
            @Override
            public void onResponse(@NonNull Call<Vehicle> call, @NonNull Response<Vehicle> response) {
                if (dialog.isShowing())
                    dialog.dismiss();

                int statusCode = response.code();

                Log.d(TAG, "onResponse: statusCode: " + statusCode + "\nmessage: " + response.message());

                if (statusCode == 200 && response.body() != null) {
                    isSessionFinished = true;
                    sharedPreference.saveData(Constants.IS_UNFINISHED, false);
                    Log.d(TAG, "onResponse: responseBody: " + response.body().toString());

                    Vehicle vehicleModel = response.body();
                    sharedPreference.saveData(Constants.INSURED_CERTIFICATE_NUMBER, vehicleModel.getCertificateNumber());
                    sharedPreference.saveData(Constants.INSURED_CAR_NUMBER, vehicleModel.getCarNumber());
                    sharedPreference.saveData(Constants.INSURED_VEHICLE_NAME, vehicleModel.getMake() + " " + vehicleModel.getModel());

                    Intent intent = new Intent(VehicleSearchActivity.this, UserSearchActivity.class);
                    startActivity(intent);
                } else if (statusCode == 401) {
                    showTokenExpiredDialog(VehicleSearchActivity.this);
                } else {
                    Log.w(TAG, "sendVehicleInfo: onResponse");
                    errorBodyConverter(VehicleSearchActivity.this, response);                }
            }

            @Override
            public void onFailure(@NonNull Call<Vehicle> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                showFailureDialog(VehicleSearchActivity.this);
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
                    Log.d(TAG, "onResponse: IS_UNFINISHED: " + sharedPreference.getData(Constants.IS_UNFINISHED, false));
                    Intent intent = new Intent(VehicleSearchActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if (statusCode == 401) {
                    showTokenExpiredDialog(VehicleSearchActivity.this);
                } else {
                    Log.w(TAG, "cancelContractOperation: onResponse");
                    errorBodyConverter(VehicleSearchActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                showFailureDialog(VehicleSearchActivity.this);
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    /* ----- Dialogs ----- */
    private void showProgress() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .content("Gözləyin...")
                .progress(true, 0)
                .cancelable(false);

        dialog = builder.build();
        dialog.show();
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

    private void showValidationDialog() {
        new MaterialDialog.Builder(this)
                .title("Diqqət")
                .content("Zəhmət olmasa, NV-nin ban nömrəsi, mühərrikin nömrəsi və ya şassi nömrələrindən hər hansı birini daxil edəsiniz!")
                .positiveText("Davam")
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
                .content("Sizin bitməmiş əməliyyatınız qalıb.")
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
    protected void onResume() {
        super.onResume();
        sharedPreference.saveData(Constants.IS_UNFINISHED, true);
        Log.d(TAG, "onResume: IS_UNFINISHED: " + sharedPreference.getData(Constants.IS_UNFINISHED, false));
        sharedPreference.saveData(Constants.UNFINISHED_SESSION_ACTIVITY, VehicleSearchActivity.class.getName());
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
        if (sharedPreference.getData(Constants.IS_UNFINISHED, false)) {

            String vehicleObject = sharedPreference.getData(Constants.SESSION_VEHICLE_OBJECT);

            Gson gson = new Gson();
            Vehicle vehicle = gson.fromJson(vehicleObject, Vehicle.class);

            Log.d(TAG, "restoreOperation: vehicleObject: " + vehicle.toString());

            switch (operationCode) {
                case STANDARD_OPERATION_CODE:
                    etCertificateNumberStandard.setText(vehicle.getCertificateNumber());
                    etCarNumberStandard.setText(vehicle.getCarNumber());
                    etMakeStandard.setText(vehicle.getMake());
                    etModelStandard.setText(vehicle.getModel());
                    etBodyNumberStandard.setText(vehicle.getBodyNumber());
                    etEngineNumberStandard.setText(vehicle.getEngineNumber());
                    etChassisNumberStandard.setText(vehicle.getChassisNumber());
                    etYearOfManufactureStandard.setText(vehicle.getYearOfManufacture() == 0 ? "" : (vehicle.getYearOfManufacture() + ""));
                    etVehicleColorStandard.setText(vehicle.getVehicleColor());

                    break;
                case BORDER_OPERATION_CODE:
                    etCertificateNumberBorder.setText(vehicle.getCertificateNumber());
                    etCarNumberBorder.setText(vehicle.getCarNumber());
                    etMakeBorder.setText(vehicle.getMake());
                    etModelBorder.setText(vehicle.getModel());
                    etBodyNumberBorder.setText(vehicle.getBodyNumber());
                    etEngineNumberBorder.setText(vehicle.getEngineNumber());
                    etChassisNumberBorder.setText(vehicle.getChassisNumber());
                    etYearOfManufactureBorder.setText(vehicle.getYearOfManufacture() == 0 ? "" : (vehicle.getYearOfManufacture() + ""));
                    etVehicleColorBorder.setText(vehicle.getVehicleColor());

                    mVehicleType = vehicle.getVehicleTypeId();
                    spVehicleTypeBorder.setSelection(mVehicleType - 1);

                    switch (mVehicleType) {
                        case 1:
                            etPassengerSeatCountBorder.setText(vehicle.getPassengerSeatCount() == 0 ? "" : (vehicle.getPassengerSeatCount() + ""));
                            break;
                        case 2:
                            etMaxPermissibleMassBorder.setText(vehicle.getMaxPermissibleMass().equals(BigDecimal.ZERO) ? "" : (vehicle.getMaxPermissibleMass() + ""));
                            break;
                        case 3:
                            etEngineCapacityBorder.setText(vehicle.getEngineCapacity().equals(BigDecimal.ZERO) ? "" : (vehicle.getEngineCapacity() + ""));
                            break;
                        case 7:
                            etHorsePowerBorder.setText(vehicle.getHorsePower() == 0 ? "" : (vehicle.getHorsePower() + ""));
                            break;
                    }

                    break;
            }
        }
    }

    private void saveSession() {
        if (!isSessionFinished) {
            Vehicle vehicle = new Vehicle();
            Gson gson = new Gson();
            String vehicleObject;

            switch (operationCode) {
                case STANDARD_OPERATION_CODE:
                    vehicle.setCertificateNumber(etCertificateNumberStandard.getEditableText().toString());
                    vehicle.setCarNumber(etCarNumberStandard.getEditableText().toString());
                    vehicle.setMake(etMakeStandard.getEditableText().toString());
                    vehicle.setModel(etModelStandard.getEditableText().toString());
                    vehicle.setBodyNumber(etBodyNumberStandard.getEditableText().toString());
                    vehicle.setEngineNumber(etEngineNumberStandard.getEditableText().toString());
                    vehicle.setChassisNumber(etChassisNumberStandard.getEditableText().toString());
                    vehicle.setYearOfManufacture(etYearOfManufactureStandard.getEditableText().toString().equals("") ? 0 : Integer.parseInt(etYearOfManufactureStandard.getEditableText().toString()));
                    vehicle.setVehicleColor(etVehicleColorStandard.getEditableText().toString());

                    vehicleObject = gson.toJson(vehicle);
                    Log.d(TAG, "saveOperation: vehicleObject: " + vehicleObject);
                    sharedPreference.saveData(Constants.SESSION_VEHICLE_OBJECT, vehicleObject);
                    break;
                case BORDER_OPERATION_CODE:
                    vehicle = new Vehicle();
                    vehicle.setCertificateNumber(etCertificateNumberBorder.getEditableText().toString());
                    vehicle.setCarNumber(etCarNumberBorder.getEditableText().toString());
                    vehicle.setMake(etMakeBorder.getEditableText().toString());
                    vehicle.setModel(etModelBorder.getEditableText().toString());
                    vehicle.setBodyNumber(etBodyNumberBorder.getEditableText().toString());
                    vehicle.setEngineNumber(etEngineNumberBorder.getEditableText().toString());
                    vehicle.setChassisNumber(etChassisNumberBorder.getEditableText().toString());
                    vehicle.setYearOfManufacture(etYearOfManufactureBorder.getEditableText().toString().equals("") ? 0 : Integer.parseInt(etYearOfManufactureBorder.getEditableText().toString()));
                    vehicle.setVehicleColor(etVehicleColorBorder.getEditableText().toString());
                    vehicle.setVehicleTypeId(mVehicleType);

                    switch (mVehicleType) {
                        case 1:
                            vehicle.setPassengerSeatCount(etPassengerSeatCountBorder.getEditableText().toString().equals("") ? 0 : Integer.parseInt(etPassengerSeatCountBorder.getEditableText().toString()));
                            break;
                        case 2:
                            vehicle.setMaxPermissibleMass(etMaxPermissibleMassBorder.getEditableText().toString().equals("") ? BigDecimal.ZERO : new BigDecimal(etMaxPermissibleMassBorder.getEditableText().toString()));
                            break;
                        case 3:
                            vehicle.setEngineCapacity(etEngineCapacityBorder.getEditableText().toString().equals("") ? BigDecimal.ZERO : new BigDecimal(etEngineCapacityBorder.getEditableText().toString()));
                            break;
                        case 7:
                            vehicle.setHorsePower(etHorsePowerBorder.getEditableText().toString().equals("") ? 0 : Integer.parseInt(etHorsePowerBorder.getEditableText().toString()));
                            break;
                    }

                    vehicleObject = gson.toJson(vehicle);
                    Log.d(TAG, "saveOperation: vehicleObject: " + vehicleObject);
                    sharedPreference.saveData(Constants.SESSION_VEHICLE_OBJECT, vehicleObject);

                    break;
                case GREEN_CARD_OPERATION_CODE:
                    break;
            }

            sharedPreference.saveData(Constants.IS_UNFINISHED, true);
            Log.d(TAG, "onResponse: IS_UNFINISHED: " + sharedPreference.getData(Constants.IS_UNFINISHED, false));
            sharedPreference.saveData(Constants.UNFINISHED_SESSION_ACTIVITY, VehicleSearchActivity.class.getName());
        }
    }
 }