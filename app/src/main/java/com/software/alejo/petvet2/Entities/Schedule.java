package com.software.alejo.petvet2.Entities;

import java.io.Serializable;
import java.sql.Time;

/**
 * Clase que define el horario de una veterinaria.
 * Created by WEY on 19/11/2016.
 */
@SuppressWarnings("serial")
public class Schedule implements Serializable{
    private int veterinaryId;
    private WeekDay weekDay;
    private Time openingTime;
    private Time closingTime;


    public int getVeterinaryId() {
        return veterinaryId;
    }

    public void setVeterinaryId(int veterinaryId) {
        this.veterinaryId = veterinaryId;
    }

    public WeekDay getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(WeekDay weekDay) {
        this.weekDay = weekDay;
    }

    public Time getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(Time openingTime) {
        this.openingTime = openingTime;
    }

    public Time getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(Time closingTime) {
        this.closingTime = closingTime;
    }

}
