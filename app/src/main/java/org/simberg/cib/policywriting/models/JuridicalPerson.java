package org.simberg.cib.policywriting.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by javadbadirkhanly on 8/18/17.
 */

public class JuridicalPerson {

    @SerializedName("personType")
    private String personType;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("birthPlace")
    private String birthPlace;

    @SerializedName("birthDate")
    private String birthDate;

    @SerializedName("tin")
    private String tin;

    @SerializedName("address")
    private String address;

    @SerializedName("idDocument")
    private String idDocument;

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    @Override
    public String toString() {
        return "JuridicalPerson{" +
                "personType='" + personType + '\'' +
                ", fullName='" + fullName + '\'' +
                ", birthPlace='" + birthPlace + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", tin='" + tin + '\'' +
                ", address='" + address + '\'' +
                ", idDocument='" + idDocument + '\'' +
                '}';
    }
}
