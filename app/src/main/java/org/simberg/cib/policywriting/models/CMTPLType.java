package org.simberg.cib.policywriting.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by javadbadirkhanly on 10/23/17.
 */

public class CMTPLType {

    @SerializedName("code")
    private String code;

    @SerializedName("name")
    private String name;

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

    @Override
    public String toString() {
        return "CMTPLType{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
