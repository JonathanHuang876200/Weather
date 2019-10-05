
package com.eland.JsonMethods;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Parameter {

    @SerializedName("parameterName")
    @Expose
    private String parameterName;
    @SerializedName("parameterValue")
    @Expose
    private String parameterValue;
    @SerializedName("parameterUnit")
    @Expose
    private String parameterUnit;

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    public String getParameterUnit() {
        return parameterUnit;
    }

    public void setParameterUnit(String parameterUnit) {
        this.parameterUnit = parameterUnit;
    }

}
