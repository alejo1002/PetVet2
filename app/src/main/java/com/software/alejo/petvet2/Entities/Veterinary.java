package com.software.alejo.petvet2.Entities;

import java.io.Serializable;

/**
 * Created by WEY on 19/11/2016.
 */
@SuppressWarnings("serial")
public class Veterinary implements Serializable {
    private String veterinaryId;
    private String name;
    private String phone;
    private String address;
    private String horario;
    private double latitude;
    private double longitude;


    public String getVeterinaryId() {
        return veterinaryId;
    }

    public void setVeterinaryId(String veterinaryId) {
        this.veterinaryId = veterinaryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
}
