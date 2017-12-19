package org.simberg.cib.policywriting.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by javadbadirkhanly on 8/31/17.
 */

public class SearchContract {

    @SerializedName("participantTIN")
    private String participantTIN;

    @SerializedName("insurerTIN")
    private String insurerTIN;

    @SerializedName("operationType")
    private String operationType;

    @SerializedName("contractNumber")
    private String contractNumber;

    @SerializedName("carNumber")
    private String carNumber;

    @SerializedName("skip")
    private int skip;

    @SerializedName("take")
    private int take;

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

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public int getTake() {
        return take;
    }

    public void setTake(int take) {
        this.take = take;
    }

    @Override
    public String toString() {
        return "SearchContract{" +
                "participantTIN='" + participantTIN + '\'' +
                ", insurerTIN='" + insurerTIN + '\'' +
                ", operationType='" + operationType + '\'' +
                ", contractNumber='" + contractNumber + '\'' +
                ", carNumber='" + carNumber + '\'' +
                ", skip=" + skip +
                ", take=" + take +
                '}';
    }
}
