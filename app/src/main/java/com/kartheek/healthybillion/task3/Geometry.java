package com.kartheek.healthybillion.task3;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Geometry {
    private LatLangBean location;
/*

        Geometry(LatLangBean location) {
            this.location = location;
        }
*/

    public LatLangBean getLocation() {
        return location;
    }

    public void setLocation(LatLangBean location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Geometry : " + location;
    }
}
