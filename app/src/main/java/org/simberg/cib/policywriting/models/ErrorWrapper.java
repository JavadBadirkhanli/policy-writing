package org.simberg.cib.policywriting.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by javadbadirkhanly on 8/23/17.
 */

public class ErrorWrapper {

    @SerializedName("code")
    private String code;
    @SerializedName("error")
    private String error;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "ErrorWrapper{" +
                "code='" + code + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
