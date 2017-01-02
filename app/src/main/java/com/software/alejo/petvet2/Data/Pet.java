package com.software.alejo.petvet2.Data;

import android.content.ContentValues;
import android.database.Cursor;

import com.software.alejo.petvet2.Data.PetsContract.PetsEntry;

import java.util.UUID;

/**
 * Created by WEY on 12/09/2016.
 */
public class Pet {
    private String id;
    private String name;
    private int age;
    private char ageMagnitud;
    private char gender;
    private String specie;
    private String phrase;
    private String avatarUri;
    private String lastVetVisit;

    public Pet(String name, int age, char ageMagnitud, char gender, String specie, String phrase, String avatarUri, String lastVetVisit) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.age = age;
        this.ageMagnitud = ageMagnitud;
        this.gender = gender;
        this.specie = specie;
        this.phrase = phrase;
        this.avatarUri = avatarUri;
        this.lastVetVisit = lastVetVisit;
    }

    public Pet(Cursor cursor) {
        id = cursor.getString(cursor.getColumnIndex(PetsEntry.ID));
        name = cursor.getString(cursor.getColumnIndex(PetsEntry.NAME));
        age = cursor.getInt(cursor.getColumnIndex(PetsEntry.AGE));
        ageMagnitud = cursor.getString(cursor.getColumnIndex(PetsEntry.AGE_MAGNITUD)).charAt(0);
        gender = cursor.getString(cursor.getColumnIndex(PetsEntry.GENDER)).charAt(0);
        specie = cursor.getString(cursor.getColumnIndex(PetsEntry.SPECIE));
        phrase = cursor.getString(cursor.getColumnIndex(PetsEntry.PHRASE));
        avatarUri = cursor.getString(cursor.getColumnIndex(PetsEntry.AVATAR_URI));
        lastVetVisit = cursor.getString(cursor.getColumnIndex(PetsEntry.LAST_VET_VISIT));
    }


    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(PetsEntry.ID, id);
        values.put(PetsEntry.NAME, name);
        values.put(PetsEntry.AGE, age);
        values.put(PetsEntry.AGE_MAGNITUD, String.valueOf(ageMagnitud));
        values.put(PetsEntry.GENDER, String.valueOf(gender));
        values.put(PetsEntry.SPECIE, specie);
        values.put(PetsEntry.PHRASE, phrase);
        values.put(PetsEntry.AVATAR_URI, avatarUri);
        values.put(PetsEntry.LAST_VET_VISIT, lastVetVisit);
        return values;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    /**
     * 'W': Weeks (Semanas)
     * 'M': Monts (Meses)
     * 'Y': Years (años)*/
    public char getAgeMagnitud() {
        return ageMagnitud;
    }

    /**
     * 'M': Male (Macho)
     * 'F': Female (Hembra)*/
    public char getGender() {
        return gender;
    }

    public String getSpecie() {
        return specie;
    }

    public String getPhrase() {
        return phrase;
    }

    public String getAvatarUri() {
        return avatarUri;
    }

    public String getLastVetVisit() { return lastVetVisit;}
}
