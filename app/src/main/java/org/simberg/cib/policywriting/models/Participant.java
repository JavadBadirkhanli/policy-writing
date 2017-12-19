package org.simberg.cib.policywriting.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by javadbadirkhanly on 8/21/17.
 */

public class Participant {

    @SerializedName("name")
    private String name;
    @SerializedName("tin")
    private String tin;
    @SerializedName("hasAsanImza")
    private boolean hasAsanImza;
    @SerializedName("insurers")
    private List<Insurer> insurers;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public boolean isHasAsanImza() {
        return hasAsanImza;
    }

    public void setHasAsanImza(boolean hasAsanImza) {
        this.hasAsanImza = hasAsanImza;
    }

    public List<Insurer> getInsurers() {
        return insurers;
    }

    public void setInsurers(List<Insurer> insurers) {
        this.insurers = insurers;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "name='" + name + '\'' +
                ", tin='" + tin + '\'' +
                ", hasAsanImza=" + hasAsanImza +
                ", insurers=" + insurers +
                '}';
    }
}