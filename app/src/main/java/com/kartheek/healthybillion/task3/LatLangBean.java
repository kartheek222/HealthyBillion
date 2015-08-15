package com.kartheek.healthybillion.task3;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by jitendra on 21/4/15.
 */
public class LatLangBean implements Parcelable {
    private double lat;
    private double lng;

//    public LatLangBean(double lat, double lng) {
//        this.lat = lat;
//        this.lng = lng;
//    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public LatLng getLatLang() {
        return new LatLng(lat, lng);
    }

    @Override
    public String toString() {
        return "LatLangBean{" +
                "lat=" + lat +
                ", lng=" + lng +
                '}';
    }

    public LatLangBean setData(LatLng latLng) {
        lat = latLng.latitude;
        lng = latLng.longitude;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(lat);
        dest.writeDouble(lng);
    }

    public static final Creator<LatLangBean> CREATOR = new Creator<LatLangBean>() {
        @Override
        public LatLangBean createFromParcel(Parcel source) {
            LatLangBean bean = new LatLangBean();
            bean.lat = source.readDouble();
            bean.lng = source.readDouble();
            return bean;
        }

        @Override
        public LatLangBean[] newArray(int size) {
            return new LatLangBean[size];
        }
    };
}
