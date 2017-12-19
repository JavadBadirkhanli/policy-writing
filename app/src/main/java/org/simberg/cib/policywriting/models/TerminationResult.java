package org.simberg.cib.policywriting.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by javadbadirkhanly on 9/5/17.
 */

public class TerminationResult {

    @SerializedName("success")
    private boolean success;
    @SerializedName("resultCode")
    private String resultCode;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    @Override
    public String toString() {
        return "TerminationResult{" +
                "success=" + success +
                ", resultCode='" + resultCode + '\'' +
                '}';
    }
}
