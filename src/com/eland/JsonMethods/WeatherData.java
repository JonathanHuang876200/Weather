package com.eland.JsonMethods;
public class WeatherData {

    private String Wx;
    private String Pop;
    private String MinT;
    private String MaxT;
    private String startTime;
    private String endTime;
    private String locationName;

    /*alt+insert*/
    public String getWx() {
        return Wx;
    }

    public void setWx(String wx) {
        Wx = wx;
    }

    public String getPop() {
        return Pop;
    }

    public void setPop(String pop) {
        Pop = pop;
    }

    public String getMinT() {
        return MinT;
    }

    public void setMinT(String minT) {
        MinT = minT;
    }

    public String getMaxT() {
        return MaxT;
    }

    public void setMaxT(String maxT) {
        MaxT = maxT;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
