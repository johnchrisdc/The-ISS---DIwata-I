package xyz.jcdc.iss.model;

/**
 * Created by jcdc on 4/22/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Properties {

    @SerializedName("timestamp")
    @Expose
    private Double timestamp;
    @SerializedName("elevation")
    @Expose
    private Double elevation;

    public Double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Double timestamp) {
        this.timestamp = timestamp;
    }

    public Double getElevation() {
        return elevation;
    }

    public void setElevation(Double elevation) {
        this.elevation = elevation;
    }

}