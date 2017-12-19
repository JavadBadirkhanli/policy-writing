package org.simberg.cib.policywriting.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by javadbadirkhanly on 8/18/17.
 */

public class Auth {

    @SerializedName("phoneNumber")
    private String phoneNumber;
    @SerializedName("asanID")
    private String asanID;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAsanID() {
        return asanID;
    }

    public void setAsanID(String asanID) {
        this.asanID = asanID;
    }

    @Override
    public String toString() {
        return "Auth{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", asanID='" + asanID + '\'' +
                '}';
    }
}
