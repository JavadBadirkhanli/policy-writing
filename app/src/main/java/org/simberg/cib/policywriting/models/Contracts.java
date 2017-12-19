package org.simberg.cib.policywriting.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by javadbadirkhanly on 8/31/17.
 */

public class Contracts {

    @SerializedName("data")
    private List<Contract> data;

    public List<Contract> getData() {
        return data;
    }

    public void setData(List<Contract> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Contracts{" +
                "data=" + data +
                '}';
    }
}
