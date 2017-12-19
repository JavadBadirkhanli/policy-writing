package org.simberg.cib.policywriting.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by javadbadirkhanly on 8/31/17.
 */

public class Operator {

    @SerializedName("fullName")
    private String fullName;
    @SerializedName("pinCode")
    private String pinCode;
    @SerializedName("phones")
    private String phones;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getPhones() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }

    @Override
    public String toString() {
        return "Operator{" +
                "fullName='" + fullName + '\'' +
                ", pinCode='" + pinCode + '\'' +
                '}';
    }
}
