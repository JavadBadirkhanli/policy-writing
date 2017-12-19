package org.simberg.cib.policywriting.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by javadbadirkhanly on 10/27/17.
 */

public class SigningResult {

    @SerializedName("data")
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
