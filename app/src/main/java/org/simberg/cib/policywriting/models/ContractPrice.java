package org.simberg.cib.policywriting.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by javadbadirkhanly on 8/18/17.
 */

public class ContractPrice {

    @SerializedName("price")
    private String price;
    @SerializedName("bmCoefficient")
    private String bmCoefficient;
    @SerializedName("calculatedPrice")
    private String calculatedPrice;
    @SerializedName("isPriceFromLastContract")
    private boolean isPriceFromLastContract;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBmCoefficient() {
        return bmCoefficient;
    }

    public void setBmCoefficient(String bmCoefficient) {
        this.bmCoefficient = bmCoefficient;
    }

    public String getCalculatedPrice() {
        return calculatedPrice;
    }

    public void setCalculatedPrice(String calculatedPrice) {
        this.calculatedPrice = calculatedPrice;
    }

    public boolean isPriceFromLastContract() {
        return isPriceFromLastContract;
    }

    public void setPriceFromLastContract(boolean priceFromLastContract) {
        isPriceFromLastContract = priceFromLastContract;
    }

    @Override
    public String toString() {
        return "ContractPrice{" +
                "price='" + price + '\'' +
                ", bmCoefficient='" + bmCoefficient + '\'' +
                ", calculatedPrice='" + calculatedPrice + '\'' +
                ", isPriceFromLastContract=" + isPriceFromLastContract +
                '}';
    }
}
