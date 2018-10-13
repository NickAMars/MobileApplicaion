package com.example.nicholaus.fragmentss.DATABASE;

import android.graphics.Bitmap;

/**
 * Created by Nicholaus on 10/24/2017.
 */

public class MapObjects {
    private int id;
    private String referenceImage ;
    private String address;
    private Double latitude;
    private Double longitude;
    // getter
    public Double getLatitude() {return latitude;}
    public Double getLongitude() {return longitude;}
    public String getAddress() {return address;}
    public int getId() {return id;}
    public String getReferenceImage() {return referenceImage;}

    // setters
    public void setLatitude(Double latitude) {this.latitude = latitude;}
    public void setLongitude(Double longitude) {this.longitude = longitude;}
    public void setAddress(String address) {this.address = address;}
    public void setId(int id) {this.id = id;}
    public void setReferenceImage(String referenceImage) {this.referenceImage = referenceImage;}

}

