package org.simberg.cib.policywriting.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

import org.simberg.cib.policywriting.R;
import org.simberg.cib.policywriting.java.Constants;
import org.simberg.cib.policywriting.java.SharedPreference;
import org.simberg.cib.policywriting.models.Authorization;
import org.simberg.cib.policywriting.models.ContractOperation;
import org.simberg.cib.policywriting.models.Insurer;
import org.simberg.cib.policywriting.models.Operation;
import org.simberg.cib.policywriting.models.Participant;
import org.simberg.cib.policywriting.rest.ApiClient;
import org.simberg.cib.policywriting.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends ParentActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.toolbarMainActivity)
    Toolbar toolbar;

    @BindView(R.id.clStandardMainActivity)
    ConstraintLayout clStandard;

    @BindView(R.id.clBorderMainActivity)
    ConstraintLayout clBorder;

    @BindView(R.id.clGreenCardMainActivity)
    ConstraintLayout clGreenCard;

    @BindView(R.id.clStandardOperationMainActivity)
    ConstraintLayout clStandardOperation;

    @BindView(R.id.clStandardTerminateMainActivity)
    ConstraintLayout clStandardTerminate;

    @BindView(R.id.clStandardViewMainActivity)
    ConstraintLayout clStandardView;

    @BindView(R.id.clBorderOperationMainActivity)
    ConstraintLayout clBorderOperation;

    @BindView(R.id.clBorderTerminateMainActivity)
    ConstraintLayout clBorderTerminate;

    @BindView(R.id.clBorderViewMainActivity)
    ConstraintLayout clBorderView;

    @BindView(R.id.clGreenCardOperationMainActivity)
    ConstraintLayout clGreenCardOperation;

    @BindView(R.id.clGreenCardTerminateMainActivity)
    ConstraintLayout clGreenCardTerminate;

    @BindView(R.id.clGreenCardViewMainActivity)
    ConstraintLayout clGreenCardView;

    @BindView(R.id.tvStandardOperationMainActivity)
    TextView tvStandardOperation;

    @BindView(R.id.tvStandardTerminateOperationMainActivity)
    TextView tvStandardTerminateOperation;

    @BindView(R.id.tvStandardViewOperationMainActivity)
    TextView tvStandardViewOperation;

    @BindView(R.id.tvBorderOperationMainActivity)
    TextView tvBorderOperation;

    @BindView(R.id.tvBorderTerminateOperationMainActivity)
    TextView tvBorderTerminate;

    @BindView(R.id.tvBorderViewOperationMainActivity)
    TextView tvBorderView;

    @BindView(R.id.tvGreenCardOperationMainActivity)
    TextView tvGreenCardOperation;

    @BindView(R.id.tvGreenCardTerminateOperationMainActivity)
    TextView tvGreenCardTerminateOperation;

    @BindView(R.id.tvGreenCardViewOperationMainActivity)
    TextView tvGreenCardViewOperation;

    private SharedPreference sharedPreference;

    private String operationId;
    private String token;
    private String appCode;
    private String operationCode;
    private String insurerTIN;
    private String participantTIN;
    private String insurerName;
    private String participantName;

    private MaterialDialog dialog;

    private ApiInterface apiInterface;

    private Authorization authorization;

    private List<Participant> participantsList;
    private List<Insurer> insurersList;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        sharedPreference = new SharedPreference(this);

        token = sharedPreference.getData(Constants.TOKEN);
        appCode = sharedPreference.getData(Constants.APP_CODE);

        Log.d(TAG, "onCreate: token: " + token + "\nappCode: " + appCode);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        if (checkFirstRun()) {
            Intent intent = new Intent(MainActivity.this, AuthenticationActivity.class);
            intent.putExtra("firstRun", true);
            startActivity(intent);
            finish();
        }

        getBundle();

        viewClicker();

        if (!isNetworkOnline()) {
            showNetworkStateDialog(this);
            return;
        }

        Log.d(TAG, "onResponse: IS_UNFINISHED: " +  sharedPreference.getData(Constants.IS_UNFINISHED, false));
        if (sharedPreference.getData(Constants.IS_UNFINISHED, false)) {
            showSessionDialog();
        }
    }   // number: 0505697000 id: 150878

    private void getBundle() {
        if (!sharedPreference.getData("authorizationObject").equals("")) {
            jsonParser(sharedPreference.getData("authorizationObject"));
        } else {
            //Snackbar.make(view, "Əməliyyat tapılmadı. Xahiş olunur İSB ilə əlaqə saxlayın.", Snackbar.LENGTH_LONG).show();
        }
    }

    private void viewClicker() {
        viewTagger();

        clStandardOperation.setOnClickListener(this);
        clStandardTerminate.setOnClickListener(this);
        clStandardView.setOnClickListener(this);

        clBorderOperation.setOnClickListener(this);
        clBorderTerminate.setOnClickListener(this);
        clBorderView.setOnClickListener(this);

        clGreenCardOperation.setOnClickListener(this);
        clGreenCardTerminate.setOnClickListener(this);
        clGreenCardView.setOnClickListener(this);
    }

    private void viewTagger() {
        clStandardOperation.setTag(0);
        clStandardView.setTag(1);
        clStandardTerminate.setTag(2);

        clBorderOperation.setTag(3);
        clBorderView.setTag(4);
        clBorderTerminate.setTag(5);

        clGreenCardOperation.setTag(6);
        clGreenCardView.setTag(7);
        clGreenCardTerminate.setTag(8);
    }

    private void loadParticipants(int position) {
        Operation operation = authorization.getOperations().get(position);
        operationCode = operation.getCode();
        sharedPreference.saveData(Constants.OPERATION_CODE, operationCode);

        participantsList = operation.getParticipants();

        if (operationCode.contains("TERMINATE")){
            Log.d(TAG, "loadParticipants: operationCode contains TERMINATE keyword");
            terminateOperation();
        } else {
            participantsListSizeChecker();
        }
    }

    private void participantsListSizeChecker() {
        if (participantsList.size() == 1) {
            Log.d(TAG, "participantsListSizeChecker: participants size is 1");
            insurersList = participantsList.get(0).getInsurers();

            participantTIN = participantsList.get(0).getTin();
            participantName = participantsList.get(0).getName();

            sharedPreference.saveData(Constants.PARTICIPANT_TIN, participantTIN);
            sharedPreference.saveData(Constants.PARTICIPANT_NAME, participantName);

            if (insurersList.size() == 1) {
                Log.d(TAG, "participantsListSizeChecker: insurers size is 1");
                insurerTIN = insurersList.get(0).getTin();
                insurerName = insurersList.get(0).getName();

                sharedPreference.saveData(Constants.INSURER_TIN, insurerTIN);
                sharedPreference.saveData(Constants.INSURER_NAME, insurerName);

                Log.d(TAG, "participantsListSizeChecker: operation is starting ...");
                startOperation();
            } else {
                Log.d(TAG, "participantsListSizeChecker: insurers size is more than 1");
                showInsurersListDialog(insurersList);
            }

        } else {
            Log.d(TAG, "participantsListSizeChecker: participants size is more than 1");
            showParticipantsListDialog(participantsList);
        }
    }

    private void showParticipantsListDialog(List<Participant> participants) {
        List<String> myStringList = new ArrayList<>();

        for (int i = 0; i < participants.size(); i++) {
            myStringList.add(participants.get(i).getName());
        }

        new MaterialDialog.Builder(this)
                .title("İştirakçılar")
                .items(myStringList)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        Log.d(TAG, "onSelection: participant itemName: " + participantsList.get(position).getName());
                        participantTIN = participantsList.get(position).getTin();
                        participantName = participantsList.get(position).getName();

                        sharedPreference.saveData(Constants.PARTICIPANT_TIN, participantTIN);
                        sharedPreference.saveData(Constants.PARTICIPANT_NAME, participantName);

                        if (participantsList.get(position).getInsurers().size() == 1) {
                            insurerTIN = participantsList.get(position).getInsurers().get(0).getTin();
                            insurerName = participantsList.get(position).getInsurers().get(0).getName();

                            sharedPreference.saveData(Constants.INSURER_TIN, insurerTIN);
                            sharedPreference.saveData(Constants.INSURER_NAME, insurerName);

                            Log.d(TAG, "participantDialog: onSelection: operation is starting ...");
                            startOperation();
                        } else {
                            insurersList = participantsList.get(position).getInsurers();
                            showInsurersListDialog(participantsList.get(position).getInsurers());
                        }
                    }
                }).show();
    }

    private void showInsurersListDialog(List<Insurer> insurers) {
        List<String> myStringList = new ArrayList<>();

        for (int i = 0; i < insurers.size(); i++) {
            myStringList.add(insurers.get(i).getName());
        }

        new MaterialDialog.Builder(this)
                .title("Sığortaçılar")
                .items(myStringList)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        Log.d(TAG, "onSelection: insurer itemName: " + insurersList.get(position).getName());
                        insurerTIN = insurersList.get(position).getTin();
                        insurerName = insurersList.get(position).getName();

                        sharedPreference.saveData(Constants.INSURER_TIN, insurerTIN);
                        sharedPreference.saveData(Constants.INSURER_NAME, insurerName);

                        Log.d(TAG, "insurerDialog: onSelection: operation is starting ...");
                        startOperation();
                    }
                }).show();
    }

    private void jsonParser(String json) {
        Gson gson = new Gson();

        Log.d(TAG, "jsonParser: authorizationObject: " + json);

        authorization = gson.fromJson(json, Authorization.class);

        if (authorization.getOperations() == null)
            return;

        List<Operation> operationList = authorization.getOperations();

        for (int i = 0; i < operationList.size(); i++) {
            String operationCode = operationList.get(i).getCode();
            String operationType = operationCode.split("_")[0];

            switch (operationType) {
                case "STANDARD":
                    clStandard.setVisibility(View.VISIBLE);

                    switch (operationCode) {
                        case Constants.STANDARD_OPERATION:
                            clStandardOperation.setVisibility(View.VISIBLE);
                            tvStandardOperation.setText(operationList.get(i).getName());
                            break;
                        case Constants.STANDARD_TERMINATE:
                            clStandardTerminate.setVisibility(View.VISIBLE);
                            tvStandardTerminateOperation.setText(operationList.get(i).getName());
                            break;
                        case Constants.STANDARD_VIEW:
                            clStandardView.setVisibility(View.VISIBLE);
                            tvStandardViewOperation.setText(operationList.get(i).getName());
                            break;
                    }
                    break;
                case "BORDER":
                    clBorder.setVisibility(View.VISIBLE);

                    switch (operationCode) {
                        case Constants.BORDER_OPERATION:
                            clBorderOperation.setVisibility(View.VISIBLE);
                            tvBorderOperation.setText(operationList.get(i).getName());
                            break;
                        case Constants.BORDER_TERMINATE:
                            clBorderTerminate.setVisibility(View.VISIBLE);
                            tvBorderTerminate.setText(operationList.get(i).getName());
                            break;
                        case Constants.BORDER_VIEW:
                            clBorderView.setVisibility(View.VISIBLE);
                            tvBorderView.setText(operationList.get(i).getName());
                            break;
                    }
                    break;
                case "GREENCARD":
                    clGreenCard.setVisibility(View.VISIBLE);

                    switch (operationCode) {
                        case Constants.GREEN_CARD_OPERATION:
                            clGreenCardOperation.setVisibility(View.VISIBLE);
                            tvGreenCardOperation.setText(operationList.get(i).getName());
                            break;
                        case Constants.GREEN_CARD_TERMINATE:
                            clGreenCardTerminate.setVisibility(View.VISIBLE);
                            tvGreenCardTerminateOperation.setText(operationList.get(i).getName());
                            break;
                        case Constants.GREEN_CARD_VIEW:
                            clGreenCardView.setVisibility(View.VISIBLE);
                            tvGreenCardViewOperation.setText(operationList.get(i).getName());
                            break;
                    }
                    break;
            }
        }
    }

    private void startOperation() {
        Log.d(TAG, "startOperation: \noperationCode: " + operationCode +
                "\nparticipantTIN: " + participantTIN + "\ninsurerTIN: " + insurerTIN);

        switch (operationCode) {
            case Constants.STANDARD_TERMINATE:
            case Constants.BORDER_TERMINATE:
            case Constants.GREEN_CARD_TERMINATE:

                terminateOperation();
                break;
            case Constants.STANDARD_OPERATION:
            case Constants.BORDER_OPERATION:
            case Constants.GREEN_CARD_OPERATION:

                showProgress();
                startContractOperation();
                break;
            case Constants.STANDARD_VIEW:
            case Constants.BORDER_VIEW:
            case Constants.GREEN_CARD_VIEW:

                Intent intent = new Intent(MainActivity.this, ContractsActivity.class);
                intent.putExtra(Constants.OPERATION_TYPE, operationCode);
                Log.d(TAG, "startOperation: operationCode: " + operationCode);
                startActivity(intent);
                break;
            default:
                //Snackbar.make(MainActivity.class.getv, "Əməliyyat hələ hazır deyil.", Snackbar.LENGTH_SHORT).show();
                break;
        }
    }

    /* ----- REST ----- */
    private void startContractOperation() {
        ContractOperation contractOperation = new ContractOperation();
        contractOperation.setOperationType(sharedPreference.getData(Constants.OPERATION_CODE));
        contractOperation.setInsurerTIN(sharedPreference.getData(Constants.INSURER_TIN));
        contractOperation.setParticipantTIN(sharedPreference.getData(Constants.PARTICIPANT_TIN));

        Call<String> callStartContractOperation = apiInterface.startContractOperation(token, appCode, contractOperation);

        callStartContractOperation.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (dialog.isShowing())
                    dialog.dismiss();
                int statusCode = response.code();

                Log.i(TAG, "onResponse: statusCode: " + statusCode + "\nMessage: " + response.message());

                if (statusCode == 200 && response.body() != null) {

                    operationId = response.body();

                    Log.d(TAG, "onResponse: operationId: " + operationId);

                    sharedPreference.saveData(Constants.CONTRACT_OPERATION_ID, operationId);

                    if (sharedPreference.getData(Constants.OPERATION_CODE).equals(Constants.GREEN_CARD_OPERATION)) {
                        Log.d(TAG, "onResponse: greenCard");
                        Intent intent = new Intent(MainActivity.this, BlankOperationActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(MainActivity.this, VehicleSearchActivity.class);
                        startActivity(intent);
                    }
                } else if (statusCode == 401) {
                    showTokenExpiredDialog(MainActivity.this);
                } else {
                    Log.w(TAG, "startContractOperation: onResponse");
                    errorBodyConverter(MainActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call,@NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                showFailureDialog(MainActivity.this);
                Log.i(TAG, "onFailure: " + t);
            }
        });
    }

    private void terminateOperation() {
        Intent intent = new Intent(MainActivity.this, ContractTerminationActivity.class);
        startActivity(intent);
    }

    private void cancelContractOperation() {
        operationId = sharedPreference.getData(Constants.CONTRACT_OPERATION_ID);
        Call<Object> cancelContractOperationCall = apiInterface.cancelContractOperation(operationId, token, appCode);

        cancelContractOperationCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                if (dialog.isShowing())
                    dialog.dismiss();

                int statusCode = response.code();

                Log.d(TAG, "cancelContractOperation onResponse: statusCode: " + statusCode +
                        "\nmessage: " + response.message());

                if (statusCode == 200 && response.body() != null) {
                    sharedPreference.saveData(Constants.IS_UNFINISHED, false);
                } else if (statusCode == 401) {
                    showTokenExpiredDialog(MainActivity.this);
                } else {
                    Log.w(TAG, "cancelContractOperation: onResponse");
                    errorBodyConverter(MainActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                showFailureDialog(MainActivity.this);
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

    private boolean checkFirstRun() {

        if (sharedPreference.getData("firstRun", true)) {
            Log.i(TAG, "checkFirstRun: First Run: " + sharedPreference.getData("firstRun", true));
            Log.i(TAG, "AppCode: " + sharedPreference.getData("appCode", "Not Found"));
            return true;
        } else {
            Log.i(TAG, "checkFirstRun: firstRun: " +
                    sharedPreference.getData("firstRun", true) + "\nAppCode: " +
                    sharedPreference.getData("appCode", "Not Found"));
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.actionContractsHistory) {
            Intent intent = new Intent(MainActivity.this, ContractsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.actionGetAuthorisations){
            Intent intent = new Intent(MainActivity.this, AuthenticationActivity.class);
            intent.putExtra("firstRun", false);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (!isNetworkOnline()){
            showNetworkStateDialog(this);
            return;
        }

        Log.d(TAG, "onResponse: IS_UNFINISHED: " +  sharedPreference.getData(Constants.IS_UNFINISHED, false));
        if (sharedPreference.getData(Constants.IS_UNFINISHED, false)) {
            showSessionDialog();
        }

        int position = (int) view.getTag();
        loadParticipants(position);
    }

    private void showSessionDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Diqqət")
                .content("Sizin bitməmiş əməliyyatınız qalıb")
                .positiveText("Davam et")
                .negativeText("Ləğv et")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        sessionRestorer();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        showProgress();
                        cancelContractOperation();
                    }
                }).build();

        dialog.show();
    }

    private void sessionRestorer() {
        String className = sharedPreference.getData(Constants.UNFINISHED_SESSION_ACTIVITY);

        if (className.equals(VehicleSearchActivity.class.getName())) {
            Log.d(TAG, "sessionRestorer: Session Restoring from " + className);
            Intent intent = new Intent(MainActivity.this, VehicleSearchActivity.class);
            startActivity(intent);
        } else if (className.equals(VehicleDetailsActivity.class.getName())) {
            Log.d(TAG, "sessionRestorer: Session Restoring from " + className);
            Intent intent = new Intent(MainActivity.this, VehicleDetailsActivity.class);
            startActivity(intent);
        } else if (className.equals(UserSearchActivity.class.getName())) {
            Log.d(TAG, "sessionRestorer: Session Restoring from " + className);
            Intent intent = new Intent(MainActivity.this, UserSearchActivity.class);
            startActivity(intent);
        } else if (className.equals(UserDetailsActivity.class.getName())) {
            Log.d(TAG, "sessionRestorer: Session Restoring from " + className);
            Intent intent = new Intent(MainActivity.this, UserDetailsActivity.class);
            startActivity(intent);
        } else if (className.equals(ContractConfirmationActivity.class.getName())) {
            Log.d(TAG, "sessionRestorer: Session Restoring from " + className);
            Intent intent = new Intent(MainActivity.this, ContractConfirmationActivity.class);
            startActivity(intent);
        } else if (className.equals(SignContractActivity.class.getName())) {
            Log.d(TAG, "sessionRestorer: Session Restoring from " + className);
            Intent intent = new Intent(MainActivity.this, SignContractActivity.class);
            startActivity(intent);
        } else if (className.equals(ContractTerminationActivity.class.getName())) {
            Log.d(TAG, "sessionRestorer: Session Restoring from " + className);
            Intent intent = new Intent(MainActivity.this, ContractTerminationActivity.class);
            startActivity(intent);
        } else if (className.equals(BlankOperationActivity.class.getName())) {
            Log.d(TAG, "sessionRestorer: Session Restoring from " + className);
            Intent intent = new Intent(MainActivity.this, BlankOperationActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: destroyed");
        super.onDestroy();
    }
}
