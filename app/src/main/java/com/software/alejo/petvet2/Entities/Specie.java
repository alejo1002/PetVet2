package com.software.alejo.petvet2.Entities;

/**
 * Created by WEY on 19/11/2016.
 */
public class Specie {
    private int id;
    private int specieName;
    private boolean state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSpecieName() {
        return specieName;
    }

    public void setSpecieName(int specieName) {
        this.specieName = specieName;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
