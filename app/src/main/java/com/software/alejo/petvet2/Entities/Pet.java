package com.software.alejo.petvet2.Entities;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase con los atributos de una Mascota (Pet)
 * Created by WEY on 19/11/2016.
 */
@IgnoreExtraProperties
public class Pet implements Serializable {

    private String petId;
    private String userId;
    private String name;
    private int age;
    private String gender;
    private String specie;
    private String urlImage;
    private String phrase;
    private String ageManitud;
    private String lastVisit;


    public Pet() {

    }

    public Pet(String petName, String phrase, String urlImage){
        this.name = petName;
        this.phrase = phrase;
        this.urlImage = urlImage;
    }

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public String getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(String lastVisit) {
        this.lastVisit = lastVisit;
    }

    public String getSpecie() {
        return specie;
    }

    public void setSpecie(String specie) {
        this.specie = specie;
    }

    public String getAgeManitud() {
        return ageManitud;
    }

    public void setAgeManitud(String ageManitud) {
        this.ageManitud = ageManitud;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("name", name);
        result.put("age", age);
        result.put("gender", gender);
        //result.put("urlImage", urlImage);
        result.put("phrase", phrase);
        result.put("lastVisit", lastVisit);
        result.put("specie", specie);

        return result;
    }

    public String getAgeComplete() {
        String answ = "";
        switch (ageManitud) {
            case "s":
                answ = " Semanas";
                break;
            case "m":
                answ = " Meses";
                break;
            case "a":
                answ = " Años";
                break;
            default:
                answ = "No Magnitud";
        }

        return age + answ;
    }

    public String getGenderComplete(){
        String answ = "";
        switch (gender){
            case "m":
                answ = "Macho";
                break;
            case "f":
                answ = "Hembra";
                break;
            default:
                answ = "No Gender";
        }

        return answ;
    }

    public String getSpicieComplete(){
        String answ = "";
        switch (specie){
            case "dog":
                answ = "Perro";
                break;
            case "cat":
                answ = "Gato";
                break;
            case "rab":
                answ = "Conejo";
                break;
            case "tur":
                answ = "Tortuga";
                break;
            case "bir":
                answ = "Ave";
                break;
            default:
                answ = "No Specie";
        }

        return answ;
    }

    public String getFormatDate(){
        Calendar c = new GregorianCalendar();

        String day = Integer.toString(c.get(Calendar.DATE));
        String month = Integer.toString(c.get(Calendar.MONTH));
        String year = Integer.toString(c.get(Calendar.YEAR));

        return  day + "-" + month + "-" + year;
    }

}
