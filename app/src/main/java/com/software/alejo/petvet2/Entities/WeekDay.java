package com.software.alejo.petvet2.Entities;

import java.io.Serializable;

/**
 * Created by WEY on 19/11/2016.
 */
@SuppressWarnings("serial")
public enum WeekDay implements Serializable{
        MONDAY("Lunes", 1),
        TUESDAY("Martes", 2),
        WEDNESDAY("Miércoles", 3),
        THURSDAY("Jueves", 4),
        FRIDAY("Viernes", 5),
        SATURDAY("Sábado", 6),
        SUNDAY("Domingo", 7),
        FESTIVE("Festivo", 8);

    private String nameDay;
    private int numDay;

    WeekDay(String nameDay, int numDay) {
        this.nameDay = nameDay;
        this.numDay = numDay;
    }

    public String getNameDay() {
        return nameDay;
    }

    public int getNumDay() {
        return numDay;
    }
}
