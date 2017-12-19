package org.simberg.cib.policywriting.models.response;

import com.google.gson.annotations.SerializedName;

import org.simberg.cib.policywriting.models.CMTPLType;
import org.simberg.cib.policywriting.models.ContractPrice;
import org.simberg.cib.policywriting.models.Operator;
import org.simberg.cib.policywriting.models.Status;
import org.simberg.cib.policywriting.models.Vehicle;

import java.math.BigDecimal;

/**
 * Created by javadbadirkhanly on 10/5/17.
 */

public class ContractTermination {

    @SerializedName("contractType")
    private ContractType contractType;

    @SerializedName("contractNumber")
    private String contractNumber;

    @SerializedName("paymentNumber")
    private String paymentNumber;

    @SerializedName("operator")
    private Operator operator;

    @SerializedName("insuranceCompanyTIN")
    private String insuranceCompanyTIN;

    @SerializedName("insuranceCompanyName")
    private String insuranceCompanyName;

    @SerializedName("insuranceCompanyAddress")
    private String insuranceCompanyAddress;

    @SerializedName("insuranceCompanyPhone")
    private String insuranceCompanyPhone;

    @SerializedName("vehicle")
    private Vehicle vehicle;

    @SerializedName("insuredPerson")
    private InsuredPerson insuredPerson;

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

    @SerializedName("period")
    private int period;

    @SerializedName("cmtplType")
    private CMTPLType cmtplType;

    @SerializedName("terminationReturningAmount")
    private BigDecimal terminationReturningAmount;

    @SerializedName("participantPhones")
    private String participantPhones;

    public ContractType getContractType() {
        return contractType;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
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

    public String getInsuranceCompanyAddress() {
        return insuranceCompanyAddress;
    }

    public void setInsuranceCompanyAddress(String insuranceCompanyAddress) {
        this.insuranceCompanyAddress = insuranceCompanyAddress;
    }

    public String getInsuranceCompanyPhone() {
        return insuranceCompanyPhone;
    }

    public void setInsuranceCompanyPhone(String insuranceCompanyPhone) {
        this.insuranceCompanyPhone = insuranceCompanyPhone;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public InsuredPerson getInsuredPerson() {
        return insuredPerson;
    }

    public void setInsuredPerson(InsuredPerson insuredPerson) {
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

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public CMTPLType getCmtplType() {
        return cmtplType;
    }

    public void setCmtplType(CMTPLType cmtplType) {
        this.cmtplType = cmtplType;
    }

    public BigDecimal getTerminationReturningAmount() {
        return terminationReturningAmount;
    }

    public void setTerminationReturningAmount(BigDecimal terminationReturningAmount) {
        this.terminationReturningAmount = terminationReturningAmount;
    }

    public String getParticipantPhones() {
        return participantPhones;
    }

    public void setParticipantPhones(String participantPhones) {
        this.participantPhones = participantPhones;
    }

    @Override
    public String toString() {
        return "ContractTermination{" +
                "contractType=" + contractType +
                ", contractNumber='" + contractNumber + '\'' +
                ", paymentNumber='" + paymentNumber + '\'' +
                ", operator=" + operator +
                ", insuranceCompanyTIN='" + insuranceCompanyTIN + '\'' +
                ", insuranceCompanyName='" + insuranceCompanyName + '\'' +
                ", insuranceCompanyAddress='" + insuranceCompanyAddress + '\'' +
                ", insuranceCompanyPhone='" + insuranceCompanyPhone + '\'' +
                ", vehicle=" + vehicle +
                ", insuredPerson=" + insuredPerson +
                ", contractPrice=" + contractPrice +
                ", creationDate='" + creationDate + '\'' +
                ", effectiveDate='" + effectiveDate + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", status=" + status +
                ", period=" + period +
                ", cmtplType=" + cmtplType +
                '}';
    }
}
