package org.simberg.cib.policywriting.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by javadbadirkhanly on 10/10/17.
 */

public class Result {

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
        return "Result{" +
                "success=" + success +
                ", resultCode='" + resultCode + '\'' +
                '}';
    }
}
