package org.simberg.cib.policywriting.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by javadbadirkhanly on 8/18/17.
 */

public class SignContract {

    @SerializedName("operatorPhoneNumber")
    private String operatorPhoneNumber;

    @SerializedName("operatorAsanID")
    private String operatorAsanID;

    @SerializedName("insuredPhoneNumber")
    private String insuredPhoneNumber;

    @SerializedName("insuredEmail")
    private String insuredEmail;

    public String getOperatorPhoneNumber() {
        return operatorPhoneNumber;
    }

    public void setOperatorPhoneNumber(String operatorPhoneNumber) {
        this.operatorPhoneNumber = operatorPhoneNumber;
    }

    public String getOperatorAsanID() {
        return operatorAsanID;
    }

    public void setOperatorAsanID(String operatorAsanID) {
        this.operatorAsanID = operatorAsanID;
    }

    public String getInsuredPhoneNumber() {
        return insuredPhoneNumber;
    }

    public void setInsuredPhoneNumber(String insuredPhoneNumber) {
        this.insuredPhoneNumber = insuredPhoneNumber;
    }

    public String getInsuredEmail() {
        return insuredEmail;
    }

    public void setInsuredEmail(String insuredEmail) {
        this.insuredEmail = insuredEmail;
    }

    @Override
    public String toString() {
        return "SignContract{" +
                "operatorPhoneNumber='" + operatorPhoneNumber + '\'' +
                ", operatorAsanID='" + operatorAsanID + '\'' +
                ", insuredPhoneNumber='" + insuredPhoneNumber + '\'' +
                ", insuredEmail='" + insuredEmail + '\'' +
                '}';
    }
}
