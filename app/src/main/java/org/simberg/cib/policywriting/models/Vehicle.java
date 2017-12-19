package org.simberg.cib.policywriting.models;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

/**
 * Created by javadbadirkhanly on 8/18/17.
 */

public class Vehicle {

    @SerializedName("bodyNumber")
    private String bodyNumber;
    @SerializedName("chassisNumber")
    private String chassisNumber;
    @SerializedName("engineNumber")
    private String engineNumber;
    @SerializedName("carNumber")
    private String carNumber;
    @SerializedName("certificateNumber")
    private String certificateNumber;
    @SerializedName("yearOfManufacture")
    private int yearOfManufacture;
    @SerializedName("vehicleColor")
    private String vehicleColor;
    @SerializedName("make")
    private String make;
    @SerializedName("model")
    private String model;
    @SerializedName("vehicleType")
    private String vehicleType;
    @SerializedName("vehicleTypeId")
    private int vehicleTypeId;
    @SerializedName("engineCapacity")
    private BigDecimal engineCapacity;
    @SerializedName("maxPermissibleMass")
    private BigDecimal maxPermissibleMass;
    @SerializedName("passengerSeatCount")
    private int passengerSeatCount;
    @SerializedName("horsePower")
    private int horsePower;

    public String getBodyNumber() {
        return bodyNumber;
    }

    public void setBodyNumber(String bodyNumber) {
        this.bodyNumber = bodyNumber;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public int getYearOfManufacture() {
        return yearOfManufacture;
    }

    public void setYearOfManufacture(int yearOfManufacture) {
        this.yearOfManufacture = yearOfManufacture;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public int getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(int vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public BigDecimal getEngineCapacity() {
        return engineCapacity;
    }

    public void setEngineCapacity(BigDecimal engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    public BigDecimal getMaxPermissibleMass() {
        return maxPermissibleMass;
    }

    public void setMaxPermissibleMass(BigDecimal maxPermissibleMass) {
        this.maxPermissibleMass = maxPermissibleMass;
    }

    public int getPassengerSeatCount() {
        return passengerSeatCount;
    }

    public void setPassengerSeatCount(int passengerSeatCount) {
        this.passengerSeatCount = passengerSeatCount;
    }

    public int getHorsePower(){
        return horsePower;
    }

    public void setHorsePower(int horsePower){
        this.horsePower = horsePower;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "bodyNumber='" + bodyNumber + '\'' +
                ", chassisNumber='" + chassisNumber + '\'' +
                ", engineNumber='" + engineNumber + '\'' +
                ", carNumber='" + carNumber + '\'' +
                ", certificateNumber='" + certificateNumber + '\'' +
                ", yearOfManufacture=" + yearOfManufacture +
                ", vehicleColor='" + vehicleColor + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                ", vehicleTypeId=" + vehicleTypeId +
                ", engineCapacity=" + engineCapacity +
                ", maxPermissibleMass=" + maxPermissibleMass +
                ", passengerSeatCount=" + passengerSeatCount +
                ", horsePower=" + horsePower +
                '}';
    }
}
