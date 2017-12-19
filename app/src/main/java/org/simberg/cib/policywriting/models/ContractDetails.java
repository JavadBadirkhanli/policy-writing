package org.simberg.cib.policywriting.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by javadbadirkhanly on 8/31/17.
 */

public class ContractDetails {

    @SerializedName("contractNumber")
    private String contractNumber;
    @SerializedName("operator")
    private Operator operator;
    @SerializedName("insuranceCompanyTIN")
    private String insuranceCompanyTIN;
    @SerializedName("insuranceCompanyName")
    private String insuranceCompanyName;
    @SerializedName("vehicle")
    private Vehicle vehicle;
    @SerializedName("insuredPerson")
    private Person insuredPerson;
    @SerializedName("contractPrice")
    private ContractPrice contractPrice;
    @SerializedName("creationDate")
    private String creationDate;
    @SerializedName("effectiveDate")
    private String effectiveDate;
    @SerializedName("expiryDate")
    private String expiryDate;
    @SerializedName("status")
    private Status status;

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public String getInsuranceCompanyTIN() {
        return insuranceCompanyTIN;
    }

    public void setInsuranceCompanyTIN(String insuranceCompanyTIN) {
        this.insuranceCompanyTIN = insuranceCompanyTIN;
    }

    public String getInsuranceCompanyName() {
        return insuranceCompanyName;
    }

    public void setInsuranceCompanyName(String insuranceCompanyName) {
        this.insuranceCompanyName = insuranceCompanyName;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Person getInsuredPerson() {
        return insuredPerson;
    }

    public void setInsuredPerson(Person insuredPerson) {
        this.insuredPerson = insuredPerson;
    }

    public ContractPrice getContractPrice() {
        return contractPrice;
    }

    public void setContractPrice(ContractPrice contractPrice) {
        this.contractPrice = contractPrice;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ContractDetails{" +
                "contractNumber='" + contractNumber + '\'' +
                ", operator=" + operator +
                ", insuranceCompanyTIN='" + insuranceCompanyTIN + '\'' +
                ", insuranceCompanyName='" + insuranceCompanyName + '\'' +
                ", vehicle=" + vehicle +
                ", insuredPerson=" + insuredPerson +
                ", contractPrice=" + contractPrice +
                ", creationDate='" + creationDate + '\'' +
                ", effectiveDate='" + effectiveDate + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", status=" + status +
                '}';
    }
}
