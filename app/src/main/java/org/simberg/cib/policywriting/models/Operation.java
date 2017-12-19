package org.simberg.cib.policywriting.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by javadbadirkhanly on 8/20/17.
 */

public class Operation {

    @SerializedName("code")
    private String code;
    @SerializedName("name")
    private String name;
    @SerializedName("participants")
    private List<Participant> participants;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", participants=" + participants +
                '}';
    }
}
