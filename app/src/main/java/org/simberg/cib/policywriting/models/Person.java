package org.simberg.cib.policywriting.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by javadbadirkhanly on 8/31/17.
 */

public class Person {

    @SerializedName("personType")
    private String personType;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("patronymic")
    private String patronymic;

    @SerializedName("birthPlace")
    private String birthPlace;

    @SerializedName("birthDate")
    private String birthDate;

    @SerializedName("pin")
    private String pin;

    @SerializedName("tin")
    private String tin;

    @SerializedName("address")
    private String address;

    @SerializedName("idDocument")
    private String idDocument;

    @SerializedName("idDocumentType")
    private String idDocumentType;

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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
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

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
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

    public String getIdDocumentType() {
        return idDocumentType;
    }

    public void setIdDocumentType(String idDocumentType) {
        this.idDocumentType = idDocumentType;
    }

    @Override
    public String toString() {
        return "Person{" +
                "personType='" + personType + '\'' +
                ", fullName='" + fullName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", birthPlace='" + birthPlace + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", pin='" + pin + '\'' +
                ", tin='" + tin + '\'' +
                ", address='" + address + '\'' +
                ", idDocument='" + idDocument + '\'' +
                ", idDocumentType='" + idDocumentType + '\'' +
                '}';
    }
}
