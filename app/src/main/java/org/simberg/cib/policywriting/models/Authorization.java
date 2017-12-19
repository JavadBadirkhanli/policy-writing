package org.simberg.cib.policywriting.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by javadbadirkhanly on 9/26/17.
 */

public class Authorization {

    @SerializedName("pinCode")
    private String pinCode;
    @SerializedName("operations")
    private List<Operation> operations;

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }

    @Override
    public String toString() {
        return "Authorization{" +
                "pinCode='" + pinCode + '\'' +
                ", operations=" + operations +
                '}';
    }
}
