package org.simberg.cib.policywriting.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by javadbadirkhanly on 10/5/17.
 */

public class InsuredPerson {

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
    @SerializedName("pin")
    private String pin;
    @SerializedName("address")
    private String address;
    @SerializedName("idDocument")
    private String idDocument;
    @SerializedName("idDocumentType")
    private String idDocumentType;
    @SerializedName("phone")
    private String phone;
    @SerializedName("email")
    private String email;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "InsuredPerson{" +
                "personType='" + personType + '\'' +
                ", fullName='" + fullName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", pin='" + pin + '\'' +
                ", address='" + address + '\'' +
                ", idDocument='" + idDocument + '\'' +
                ", idDocumentType='" + idDocumentType + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
