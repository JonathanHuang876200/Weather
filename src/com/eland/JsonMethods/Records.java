
package com.eland.JsonMethods;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Records {

    @SerializedName("datasetDescription")
    @Expose
    private String datasetDescription;
    @SerializedName("location")
    @Expose
    private List<Location> location = null;

    public String getDatasetDescription() {
        return datasetDescription;
    }

    public void setDatasetDescription(String datasetDescription) {
        this.datasetDescription = datasetDescription;
    }

    public List<Location> getLocation() {
        return location;
    }

    public void setLocation(List<Location> location) {
        this.location = location;
    }

}
