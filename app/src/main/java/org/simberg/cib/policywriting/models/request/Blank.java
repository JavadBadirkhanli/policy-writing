package org.simberg.cib.policywriting.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by javadbadirkhanly on 10/10/17.
 */

public class Blank {

    @SerializedName("blankSeries")
    private String blankSeries;

    @SerializedName("blankNumber")
    private String blankNumber;

    @SerializedName("effectiveDate")
    private String effectiveDate;

    @SerializedName("blankSeriesId")
    private int blankSeriesId;

    public String getBlankSeries() {
        return blankSeries;
    }

    public void setBlankSeries(String blankSeries) {
        this.blankSeries = blankSeries;
    }

    public String getBlankNumber() {
        return blankNumber;
    }

    public void setBlankNumber(String blankNumber) {
        this.blankNumber = blankNumber;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public int getBlankSeriesId() {
        return blankSeriesId;
    }

    public void setBlankSeriesId(int blankSeriesId) {
        this.blankSeriesId = blankSeriesId;
    }

    @Override
    public String toString() {
        return "Blank{" +
                "blankSeries='" + blankSeries + '\'' +
                ", blankNumber='" + blankNumber + '\'' +
                ", effectiveDate='" + effectiveDate + '\'' +
                '}';
    }
}
