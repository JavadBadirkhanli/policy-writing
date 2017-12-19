package org.simberg.cib.policywriting.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by javadbadirkhanly on 8/18/17.
 */

public class AuthorizationDetails {

    @SerializedName("pinCode")
    private String pinCode;
    @SerializedName("participants")
    private List<Participant> participants;

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    @Override
    public String toString() {
        return "AuthorizationDetails{" +
                "pinCode='" + pinCode + '\'' +
                ", participants=" + participants +
                '}';
    }
}
