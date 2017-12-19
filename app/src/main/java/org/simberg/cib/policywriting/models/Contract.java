package org.simberg.cib.policywriting.models;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

/**
 * Created by javadbadirkhanly on 8/31/17.
 */

public class Contract {

    @SerializedName("contractNumber")
    private String contractNumber;
    @SerializedName("insuredName")
    private String insuredName;
    @SerializedName("carNumber")
    private String carNumber;
    @SerializedName("price")
    private BigDecimal price;
    @SerializedName("status")
    private Status status;
    @SerializedName("creationDate")
    private String creationDate;
    @SerializedName("cmtplType")
    private String cmtplType;

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getInsuredName() {
        return insuredName;
    }

    public void setInsuredName(String insuredName) {
        this.insuredName = insuredName;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getCmtplType() {
        return cmtplType;
    }

    public void setCmtplType(String cmtplType) {
        this.cmtplType = cmtplType;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "contractNumber='" + contractNumber + '\'' +
                ", insuredName='" + insuredName + '\'' +
                ", carNumber='" + carNumber + '\'' +
                ", price=" + price +
                ", status=" + status +
                ", creationDate='" + creationDate + '\'' +
                ", cmtplType='" + cmtplType + '\'' +
                '}';
    }
}
