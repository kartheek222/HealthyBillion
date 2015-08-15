package com.kartheek.healthybillion.task3;

/**
 * Created by Krishna on 15/08/2015.
 */
public class LatBeans {
    private String name;
    private double lat;
    private double lang;
    private String color;

    public LatBeans(String name, double lat, double lang, String color) {
        this.name = name;
        this.lat = lat;
        this.lang = lang;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLang() {
        return lang;
    }

    public void setLang(double lang) {
        this.lang = lang;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
