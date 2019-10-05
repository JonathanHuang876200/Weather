
package com.eland.JsonMethods;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherElement {

    @SerializedName("elementName")
    @Expose
    private String elementName;
    @SerializedName("time")
    @Expose
    private List<Time> time = null;

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public List<Time> getTime() {
        return time;
    }

    public void setTime(List<Time> time) {
        this.time = time;
    }

}
