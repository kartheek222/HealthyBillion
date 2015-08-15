package com.kartheek.healthybillion.task3;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;

/**
 * Created by jitendra on 21/4/15.
 */
//@JsonIgnoreProperties({"scope", "opening_hours", "photos"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceBean {
    private String icon;
    private String id;
    private String name;
    private String place_id;
    private String[] types;
    private String vicinity;
    private Geometry geometry;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public LatLangBean location() {
        if (geometry != null) {
            return geometry.getLocation();
        }
        return null;
    }


    @Override
    public String toString() {
        return "PlaceBean{" +
                "icon='" + icon + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", place_id='" + place_id + '\'' +
                ", types=" + Arrays.toString(types) +
                ", vicinity='" + vicinity + '\'' +
                ", geometry=" + geometry +
                '}';
    }
}
