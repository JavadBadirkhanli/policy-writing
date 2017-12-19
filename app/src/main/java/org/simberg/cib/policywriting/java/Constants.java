package org.simberg.cib.policywriting.java;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by javadbadirkhanly on 8/20/17.
 */

public class Constants {

    public static final String APP_CODE = "appCode";
    public static final String PROJECT_NAME = "org.simberg.cib.policywriting";
    public static final String TOKEN = "token";

    public static final String AUTHENTICATE_OPERATION_ID = "authenticateOperationId";
    public static final String CONTRACT_OPERATION_ID = "contractOperationId";

    public static final String OPERATION_CODE = "operationCode";
    public static final String OPERATION_TYPE = "operationType";
    public static final String PARTICIPANT_TIN = "participantTIN";
    public static final String INSURER_TIN = "insurerTIN";
    public static final String PARTICIPANT_NAME = "participantName";
    public static final String INSURER_NAME = "insurerName";

    public static final String ASAN_ID = "asanID";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String CERTIFICATE_NUMBER = "certificateNumber";
    public static final String CAR_NUMBER = "carNumber";

    public static final String INSURED_PHONE_NUMBER = "insuredPhoneNumber";
    public static final String INSURED_EMAIL = "insuredEmail";
    public static final String INSURED_VALUE = "insuredValue";
    public static final String INSURED_CERTIFICATE_NUMBER = "insuredCertificateNumber";
    public static final String INSURED_CAR_NUMBER = "insuredCarNumber";
    public static final String INSURED_VEHICLE_NAME = "insuredVehicleName";
    public static final String INSURED_FULL_NAME = "insuredFullName";

    public static final String PERSON_TYPE = "personType";
    public static final String CONTRACT_NUMBER = "contractNumber";

    public static final String PERSON_INFO = "personInfo";
    public static final String NATURAL_PERSON = "NATURAL_PERSON_TYPE"; // updated
    public static final String JURIDICAL_PERSON = "JURIDICAL_PERSON_TYPE"; // updated
    public static final String NON_RESIDENT_PERSON = "NON_RESIDENT_PERSON_TYPE"; // updated

    public static final String INSURANCE_CALCULATED_PRICE = "calculatedPrice";
    public static final String INSURANCE_BM_COEFFICIENT = "bmCoefficient";
    public static final String INSURANCE_PRICE = "price";

    public static final String CONTRACT_PRICE_OBJECT = "contractPriceObject";

    public static final String AUTH_RESULT_OBJECT = "authResult";

    private static final Map<String, List<String>> OPERATIONS_LIST_MAP;

    private static final List<String> STANDARD_OPERATIONS_LIST;
    private static final List<String> BORDER_OPERATIONS_LIST;
    private static final List<String> GREEN_CARD_OPERATIONS_LIST;

    public static final String STANDARD_OPERATION = "STANDARD_CMTPL_CONTRACT_OPERATION";
    public static final String STANDARD_TERMINATE = "STANDARD_CMTPL_TERMINATE_CONTRACT_OPERATION";
    public static final String STANDARD_VIEW = "STANDARD_CMTPL_VIEW_PARTICIPANT_CONTRACTS";

    public static final String BORDER_OPERATION = "BORDER_CMTPL_CONTRACT_OPERATION";
    public static final String BORDER_TERMINATE = "BORDER_CMTPL_TERMINATE_CONTRACT_OPERATION";
    public static final String BORDER_VIEW = "BORDER_CMTPL_VIEW_PARTICIPANT_CONTRACTS";

    public static final String GREEN_CARD_OPERATION = "GREENCARD_CMTPL_CONTRACT_OPERATION";
    public static final String GREEN_CARD_TERMINATE = "GREENCARD_CMTPL_TERMINATE_CONTRACT_OPERATION";
    public static final String GREEN_CARD_VIEW = "GREENCARD_CMTPL_VIEW_PARTICIPANT_CONTRACTS";

    static {
        Log.d("Constants", "static initializer: block has loaded!");
        STANDARD_OPERATIONS_LIST = new ArrayList<>();
        STANDARD_OPERATIONS_LIST.add("STANDARD_CMTPL_CONTRACT_OPERATION");
        STANDARD_OPERATIONS_LIST.add("STANDARD_CMTPL_VIEW_PARTICIPANT_CONTRACTS");
        STANDARD_OPERATIONS_LIST.add("STANDARD_CMTPL_TERMINATE_CONTRACT_OPERATION");

        BORDER_OPERATIONS_LIST = new ArrayList<>();
        BORDER_OPERATIONS_LIST.add("BORDER_CMTPL_CONTRACT_OPERATION");
        BORDER_OPERATIONS_LIST.add("BORDER_CMTPL_VIEW_PARTICIPANT_CONTRACTS");
        BORDER_OPERATIONS_LIST.add("BORDER_CMTPL_TERMINATE_CONTRACT_OPERATION");

        GREEN_CARD_OPERATIONS_LIST = new ArrayList<>();
        GREEN_CARD_OPERATIONS_LIST.add("GREENCARD_CMTPL_CONTRACT_OPERATION");
        GREEN_CARD_OPERATIONS_LIST.add("GREENCARD_CMTPL_VIEW_PARTICIPANT_CONTRACTS");
        GREEN_CARD_OPERATIONS_LIST.add("GREENCARD_CMTPL_TERMINATE_CONTRACT_OPERATION");

        OPERATIONS_LIST_MAP = new LinkedHashMap<>();
        OPERATIONS_LIST_MAP.put("STANDARD", STANDARD_OPERATIONS_LIST);
        OPERATIONS_LIST_MAP.put("BORDER", BORDER_OPERATIONS_LIST);
        OPERATIONS_LIST_MAP.put("GREENCARD", GREEN_CARD_OPERATIONS_LIST);
    }

    /* ----- Session Objects ----- */

    public static final String UNFINISHED_SESSION_ACTIVITY = "unfinishedSessionActivity";
    public static final String IS_UNFINISHED = "isUnfinished";

    public static final String SESSION_VEHICLE_OBJECT = "sessionVehicleObject";
    public static final String SESSION_VEHICLE_DETAILS_OBJECT = "sessionVehicleDetailsObject";
    public static final String SESSION_PERSON_DETAILS_OBJECT = "sessionPersonDetailsObject";
    public static final String SESSION_PERSON_TYPE = "sessionPersonType";

    public static final String SESSION_USER_OBJECT = "sessionUserObject";
    public static final String SESSION_USER_ID = "sessionUserId";
    public static final String SESSION_USER_TIN = "sessionUserTin";
    public static final String SESSION_USER_RESIDENCY = "sessionUserResidency";
    public static final String SESSION_USER_DOC_TYPE = "sessionUserDocType";

    public static final String SESSION_BLANK_OBJECT = "sessionBlankObject";

    public static final String SESSION_PERIOD_POSITION = "sessionPeriodPosition";


    /* ----- Intent extra Objects ----- */

    public static final String EXTRA_CONTRACT_TERMINATION_OBJECT = "extraContractTerminationObject";
}
