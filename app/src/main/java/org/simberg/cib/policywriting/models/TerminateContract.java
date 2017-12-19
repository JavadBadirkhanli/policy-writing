package org.simberg.cib.policywriting.models;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

/**
 * Created by javadbadirkhanly on 9/5/17.
 */

public class TerminateContract {

    @SerializedName("contractNumber")
    private String contractNumber;
    @SerializedName("participantTIN")
    private String participantTIN;
    @SerializedName("terminationReason")
    private String terminationReason;
    @SerializedName("returnedAmount")
    private BigDecimal returnedAmount;
    @SerializedName("terminationDate")
    private String terminationDate;

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getParticipantTIN() {
        return participantTIN;
    }

    public void setParticipantTIN(String participantTIN) {
        this.participantTIN = participantTIN;
    }

    public String getTerminationReason() {
        return terminationReason;
    }

    public void setTerminationReason(String terminationReason) {
        this.terminationReason = terminationReason;
    }

    public BigDecimal getReturnedAmount() {
        return returnedAmount;
    }

    public void setReturnedAmount(BigDecimal returnedAmount) {
        this.returnedAmount = returnedAmount;
    }

    public String getTerminationDate() {
        return terminationDate;
    }

    public void setTerminationDate(String terminationDate) {
        this.terminationDate = terminationDate;
    }

    @Override
    public String toString() {
        return "TerminateContract{" +
                "contractNumber='" + contractNumber + '\'' +
                ", participantTIN='" + participantTIN + '\'' +
                ", terminationReason='" + terminationReason + '\'' +
                ", returnedAmount=" + returnedAmount +
                ", terminationDate='" + terminationDate + '\'' +
                '}';
    }
}
