package org.simberg.cib.policywriting.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by javadbadirkhanly on 8/10/17.
 */

public class VehicleDetailsActivity extends ParentActivity {

    private static final String TAG = VehicleDetailsActivity.class.getSimpleName();

    @BindView(R.id.toolbarVehicleDetailsActivity)
    Toolbar toolbar;

    @BindView(R.id.imgCancelVehicleDetailsActivity)
    ImageView imgCancel;

    @BindView(R.id.tvMakeVehicleDetailsActivity)
    TextView tvMake;

    @BindView(R.id.tvModelVehicleDetailsActivity)
    TextView tvModel;

    @BindView(R.id.tvVehicleTypeVehicleDetailsActivity)
    TextView tvVehicleType;

    @BindView(R.id.tvCertificateNumberVehicleDetailsActivity)
    TextView tvCertificateNumber;

    @BindView(R.id.tvCarNumberVehicleDetailsActivity)
    TextView tvCarNumber;

    @BindView(R.id.tvYearOfManufactureVehicleDetailsActivity)
    TextView tvYearOfManufacture;

    @BindView(R.id.tvVehicleColorVehicleDetailsActivity)
    TextView tvVehicleColor;

    @BindView(R.id.tvBodyNumberVehicleDetailsActivity)
    TextView tvBodyNumber;

    @BindView(R.id.tvChassisNumberVehicleDetailsActivity)
    TextView tvChassisNumber;

    @BindView(R.id.tvEngineNumberVehicleDetailsActivity)
    TextView tvEngineNumber;

    @BindView(R.id.tvEngineCapacityVehicleDetailsActivity)
    TextView tvEngineCapacity;

    @BindView(R.id.tvMaxPermissibleMassVehicleDetailsActivity)
    TextView tvMaxPermissibleMass;

    @BindView(R.id.tvPassengerSeatCountVehicleDetailsActivity)
    TextView tvPassengerSeatCount;

    @BindView(R.id.lnCarNumberVehicleDetailsActivity)
    LinearLayout lnCarNumber;

    @BindView(R.id.lnCertificateNumberVehicleDetailsActivity)
    LinearLayout lnCertificateNumber;

    @BindView(R.id.lnBodyNumberVehicleDetailsActivity)
    LinearLayout lnBodyNumber;

    @BindView(R.id.lnEngineNumberVehicleDetailsActivity)
    LinearLayout lnEngineNumber;

    @BindView(R.id.lnChassisNumberVehicleDetailsActivity)
    LinearLayout lnChassisNumber;

    @BindView(R.id.lnMakeVehicleDetailsActivity)
    LinearLayout lnMake;

    @BindView(R.id.lnModelVehicleDetailsActivity)
    LinearLayout lnModel;

    @BindView(R.id.lnVehicleTypeVehicleDetailsActivity)
    LinearLayout lnVehicleType;

    @BindView(R.id.lnVehicleColorVehicleDetailsActivity)
    LinearLayout lnVehicleColor;

    @BindView(R.id.lnYearOfManufactureVehicleDetailsActivity)
    LinearLayout lnYearOfManufacture;

    @BindView(R.id.llEngineCapacityVehicleDetailsActivity)
    LinearLayout llEngineCapacity;

    @BindView(R.id.llMaxPermissibleMassVehicleDetailsActivity)
    LinearLayout llMaxPermissibleMass;

    @BindView(R.id.llPassengerSeatCountVehicleDetailsActivity)
    LinearLayout llPassengerSeatCount;

    @BindView(R.id.btnConfirmVehicleDetailsActivity)
    Button btnConfirm;

    @BindView(R.id.btnMisMatchVehicleDetailsActivity)
    Button btnMisMatch;

    private ApiInterface apiInterface;

    private String operationId;
    private String token;
    private String appCode;

    private String vehicleMake;
    private String vehicleModel;

    private SharedPreference sharedPreference;

    private String vehicleInfo;
    private Vehicle vehicle;

    private MaterialDialog dialog;

    private boolean isSessionFinished;

    private int mVehicleTypeId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_vehicle_details);

        ButterKnife.bind(this);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sharedPreference = new SharedPreference(this);

        operationId = sharedPreference.getData(Constants.CONTRACT_OPERATION_ID);
        token = sharedPreference.getData(Constants.TOKEN);
        appCode = sharedPreference.getData(Constants.APP_CODE);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Nəqliyyat vasitəsinin məlumatları");

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCancelOperationDialog();
            }
        });

        getBundle();

        Log.i(TAG, "onCreate: operationID" + operationId + "\nAppCode: " + appCode);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mVehicleTypeId == 7) {
                    showHorsePowerDialog();
                } else {
                    isSessionFinished = true;
                    Intent intent = new Intent(VehicleDetailsActivity.this, UserSearchActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        btnMisMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress();
                mismatchVehicleInfo();
            }
        });
    }

    /* ----- REST ----- */
    private void postHorsePower(int horsePower) {
        Call<Vehicle> horsePowerCall = apiInterface.horsePower(operationId, appCode, token, horsePower);

        horsePowerCall.enqueue(new Callback<Vehicle>() {
            @Override
            public void onResponse(@NonNull Call<Vehicle> call, @NonNull Response<Vehicle> response) {
                if (dialog.isShowing())
                    dialog.dismiss();

                int statusCode = response.code();

                Log.d(TAG, "postHorsePower: onResponse: statusCode: " + statusCode + "\nmessage: " + response.message());

                if (statusCode == 200) {
                    isSessionFinished = true;
                    sharedPreference.saveData(Constants.IS_UNFINISHED, false);
                    Intent intent = new Intent(VehicleDetailsActivity.this, UserSearchActivity.class);
                    startActivity(intent);
                    finish();
                } else if (statusCode == 401) {
                   showTokenExpiredDialog(VehicleDetailsActivity.this);
                } else {
                    Log.w(TAG, "postHorsePower: onResponse");
                    errorBodyConverter(VehicleDetailsActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Vehicle> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                showFailureDialog(VehicleDetailsActivity.this);
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void mismatchVehicleInfo() {
        Call<Object> mismatchVehicleInfoCall = apiInterface.mismatchVehicleInfo(operationId, token, appCode);

        mismatchVehicleInfoCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                if (dialog.isShowing())
                    dialog.dismiss();
                int statusCode = response.code();

                if (statusCode == 200) {
                    isSessionFinished = true;
                    sharedPreference.saveData(Constants.IS_UNFINISHED, false);
                    Intent intent = new Intent(VehicleDetailsActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else if (statusCode == 401) {
                    showTokenExpiredDialog(VehicleDetailsActivity.this);
                } else {
                    Log.w(TAG, "mismatchVehicleInfo: onResponse");
                    errorBodyConverter(VehicleDetailsActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                showFailureDialog(VehicleDetailsActivity.this);
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

                if (statusCode == 200 && response.body() != null) {
                    isSessionFinished = true;
                    sharedPreference.saveData(Constants.IS_UNFINISHED, false);
                    Intent intent = new Intent(VehicleDetailsActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else if (statusCode == 401) {
                    showTokenExpiredDialog(VehicleDetailsActivity.this);
                } else {
                    Log.w(TAG, "cancelContractOperation: onResponse");
                    errorBodyConverter(VehicleDetailsActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                showFailureDialog(VehicleDetailsActivity.this);
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    /* ----- UI ----- */
    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString("vehicleInfo") != null) {
                vehicleInfo = bundle.getString("vehicleInfo");
                Log.d(TAG, "getBundle: vehicleInfo: " + vehicleInfo);
                jsonParser(vehicleInfo);
            }
        }
        Log.i(TAG, "getBundle: Vehicle Info: " + vehicleInfo);
    }

    private void jsonParser(String json) {
        Gson gson = new Gson();
        vehicle = gson.fromJson(json, Vehicle.class);
        Log.d(TAG, "jsonParser: vehicle: " + vehicle.toString());
        fillLayouts(vehicle);
    }

    private void fillLayouts(Vehicle vehicle) {
        if (vehicle.getMake() != null) {
            lnMake.setVisibility(View.VISIBLE);
            tvMake.setText(vehicle.getMake());
            vehicleMake = vehicle.getMake();
        }

        if (vehicle.getModel() != null) {
            lnModel.setVisibility(View.VISIBLE);
            tvModel.setText(vehicle.getModel());
            vehicleModel = vehicle.getModel();
            sharedPreference.saveData(Constants.INSURED_VEHICLE_NAME, vehicleMake + " " + (vehicleModel == null ? "" : vehicleModel));
        }

        if (vehicle.getVehicleType() != null) {
            lnVehicleType.setVisibility(View.VISIBLE);
            tvVehicleType.setText(vehicle.getVehicleType());
        }

        if (vehicle.getCertificateNumber() != null) {
            lnCertificateNumber.setVisibility(View.VISIBLE);
            tvCertificateNumber.setText(vehicle.getCertificateNumber());
            sharedPreference.saveData(Constants.INSURED_CERTIFICATE_NUMBER, vehicle.getCertificateNumber());
        }

        if (vehicle.getCarNumber() != null) {
            lnCarNumber.setVisibility(View.VISIBLE);
            tvCarNumber.setText(vehicle.getCarNumber());
            sharedPreference.saveData(Constants.INSURED_CAR_NUMBER, vehicle.getCarNumber());
        }

        if (vehicle.getYearOfManufacture() != 0) {
            lnYearOfManufacture.setVisibility(View.VISIBLE);
            tvYearOfManufacture.setText(vehicle.getYearOfManufacture() + "");
        }

        if (vehicle.getVehicleColor() != null) {
            lnVehicleColor.setVisibility(View.VISIBLE);
            tvVehicleColor.setText(vehicle.getVehicleColor());
        }

        if (vehicle.getBodyNumber() != null) {
            lnBodyNumber.setVisibility(View.VISIBLE);
            tvBodyNumber.setText(vehicle.getBodyNumber());
        }

        if (vehicle.getChassisNumber() != null) {
            lnChassisNumber.setVisibility(View.VISIBLE);
            tvChassisNumber.setText(vehicle.getChassisNumber());
        }

        if (vehicle.getEngineNumber() != null) {
            lnEngineNumber.setVisibility(View.VISIBLE);
            tvEngineNumber.setText(vehicle.getEngineNumber());
        }

        mVehicleTypeId = vehicle.getVehicleTypeId();

        switch (mVehicleTypeId) {
            case 1:
                llEngineCapacity.setVisibility(View.GONE);
                llMaxPermissibleMass.setVisibility(View.GONE);
                llPassengerSeatCount.setVisibility(View.VISIBLE);
                tvPassengerSeatCount.setText(vehicle.getPassengerSeatCount() + "");
                break;
            case 2:
                llEngineCapacity.setVisibility(View.GONE);
                llMaxPermissibleMass.setVisibility(View.VISIBLE);
                llPassengerSeatCount.setVisibility(View.GONE);
                tvMaxPermissibleMass.setText(vehicle.getMaxPermissibleMass() + "");
                break;
            case 3:
                llEngineCapacity.setVisibility(View.VISIBLE);
                llMaxPermissibleMass.setVisibility(View.GONE);
                llPassengerSeatCount.setVisibility(View.GONE);
                tvEngineCapacity.setText(vehicle.getEngineCapacity() + "");
                break;
            default:
                llEngineCapacity.setVisibility(View.GONE);
                llMaxPermissibleMass.setVisibility(View.GONE);
                llPassengerSeatCount.setVisibility(View.GONE);
                break;
        }
    }

    private void showHorsePowerDialog() {
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Mühərrikin at gücünü daxil edin")
                .customView(R.layout.dialog_horse_power, true)
                .positiveText("Daxil et")
                .negativeText("Ləğv et")
                .build();

        final EditText etHorsePower = dialog.getCustomView().findViewById(R.id.etHorsePowerHorsePowerDialog);

        dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);

        etHorsePower.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() != 0)
                    dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                else
                    dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
            }
        });

        dialog.getBuilder().onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                showProgress();
                int mHorsePower = Integer.parseInt(etHorsePower.getEditableText().toString());
                postHorsePower(mHorsePower);
            }
        });

        dialog.show();
    }

    private void showCancelOperationDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(VehicleDetailsActivity.this)
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
        sharedPreference.saveData(Constants.UNFINISHED_SESSION_ACTIVITY, VehicleDetailsActivity.class.getName());
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
            vehicleInfo = sharedPreference.getData(Constants.SESSION_VEHICLE_DETAILS_OBJECT);
            Log.d(TAG, "restoreSession: vehicleDetailsObject: " + vehicleInfo);
            jsonParser(vehicleInfo);
        }
    }

    private void saveSession() {
        if (!isSessionFinished) {
            Log.d(TAG, "saveSession: vehicleDetailsObject: " + vehicleInfo);
            sharedPreference.saveData(Constants.SESSION_VEHICLE_DETAILS_OBJECT, vehicleInfo);
        }
    }
}
