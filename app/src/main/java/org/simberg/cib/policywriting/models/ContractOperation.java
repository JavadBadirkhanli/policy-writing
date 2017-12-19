package org.simberg.cib.policywriting.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by javadbadirkhanly on 8/18/17.
 */

public class ContractOperation {

    @SerializedName("operationType")
    private String operationType;
    @SerializedName("participantTIN")
    private String participantTIN;
    @SerializedName("insurerTIN")
    private String insurerTIN;

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getParticipantTIN() {
        return participantTIN;
    }

    public void setParticipantTIN(String participantTIN) {
        this.participantTIN = participantTIN;
    }

    public String getInsurerTIN() {
        return insurerTIN;
    }

    public void setInsurerTIN(String insurerTIN) {
        this.insurerTIN = insurerTIN;
    }

    @Override
    public String toString() {
        return "ContractOperation{" +
                "operationType='" + operationType + '\'' +
                ", participantTIN='" + participantTIN + '\'' +
                ", insurerTIN='" + insurerTIN + '\'' +
                '}';
    }
}
