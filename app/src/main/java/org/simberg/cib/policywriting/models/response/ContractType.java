package org.simberg.cib.policywriting.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by javadbadirkhanly on 10/5/17.
 */

public class ContractType {

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
        return "ContractType{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
