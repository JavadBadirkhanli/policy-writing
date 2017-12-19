package org.simberg.cib.policywriting.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by javadbadirkhanly on 9/11/17.
 */

public class NonResidentPerson {

    @SerializedName("address")
    private String address;

    @SerializedName("birthDate")
    private String birthDate;

    @SerializedName("birthPlace")
    private String birthPlace;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("idDocument")
    private String idDocument;

    @SerializedName("idDocumentType")
    private String idDocumentType;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("patronymic")
    private String patronymic;

    @SerializedName("personType")
    private String personType;

    @SerializedName("pin")
    private String pin;

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

    public String getFullName() {
        if (fullName == null)
            return firstName + " " + lastName;
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
        return "NonResidentPerson{" +
                "address='" + address + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", birthPlace='" + birthPlace + '\'' +
                ", firstName='" + firstName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", idDocument='" + idDocument + '\'' +
                ", idDocumentType='" + idDocumentType + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", personType='" + personType + '\'' +
                ", pin='" + pin + '\'' +
                '}';
    }
}
