package com.software.alejo.petvet2.Entities;

import java.util.ArrayList;

/**
 * Created by WEY on 19/11/2016.
 */
public class Veterinarian  {

    private int veterinaryId;
    private boolean isAdmin;
    private int chiefId;
    private ArrayList<Veterinarian> subalterns = new ArrayList<>();
    private ArrayList<Pet> associatedPets = new ArrayList<>();

    public int getVeterinaryId() {
        return veterinaryId;
    }

    public void setVeterinaryId(int veterinaryId) {
        this.veterinaryId = veterinaryId;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public ArrayList<Veterinarian> getSubalterns() {
        return subalterns;
    }

    public void setSubalterns(ArrayList<Veterinarian> subalterns) {
        this.subalterns = subalterns;
    }

    public ArrayList<Pet> getAssociatedPets() {
        return associatedPets;
    }

    public void setAssociatedPets(ArrayList<Pet> associatedPets) {
        this.associatedPets = associatedPets;
    }

    public int getChiefId() {
        return chiefId;
    }

    public void setChiefId(int chiefId) {
        this.chiefId = chiefId;
    }
}
