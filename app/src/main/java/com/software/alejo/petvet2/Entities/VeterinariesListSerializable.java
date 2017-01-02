package com.software.alejo.petvet2.Entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by WEY on 20/11/2016.
 */
@SuppressWarnings("serial")
public class VeterinariesListSerializable implements Serializable{

    private ArrayList<Veterinary> veterinaries = new ArrayList<>();


    public ArrayList<Veterinary> getVeterinaries() {
        return veterinaries;
    }

    public void setVeterinaries(ArrayList<Veterinary> veterinaries) {
        this.veterinaries = veterinaries;
    }
}
