package org.simberg.cib.policywriting.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by javadbadirkhanly on 8/20/17.
 */

public class Insurer {

    @SerializedName("name")
    private String name;
    @SerializedName("tin")
    private String tin;
    /*@SerializedName("operations")
    private List<Operation> operations;*/

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

    /*public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }*/

    @Override
    public String toString() {
        return "Insurer{" +
                "name='" + name + '\'' +
                ", tin='" + tin + '\'' +
                '}';
    }
}
