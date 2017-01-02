package com.software.alejo.petvet2.Data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.software.alejo.petvet2.Data.PetsContract.PetsEntry;

/**
 * Created by WEY on 12/09/2016.
 */
public class PetsDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Pets.db";


    public PetsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + PetsContract.PetsEntry.TABLE_NAME + " ("
                + PetsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PetsEntry.ID + " TEXT NOT NULL,"
                + PetsEntry.NAME + " TEXT NOT NULL,"
                + PetsEntry.AGE + " INTEGER NOT NULL,"
                + PetsEntry.AGE_MAGNITUD + " TEXT NOT NULL,"
                + PetsEntry.GENDER + " TEXT NOT NULL,"
                + PetsEntry.SPECIE + " TEXT NOT NULL,"
                + PetsEntry.PHRASE + " TEXT NOT NULL,"
                + PetsEntry.AVATAR_URI + " TEXT NOT NULL,"
                + PetsEntry.LAST_VET_VISIT +  " TEXT NOT NULL,"
                + "UNIQUE (" + PetsEntry.ID + "))");

        // Inserción de datos ficticios para la prueba inicial
        mockData(db);
    }

    private void mockData(SQLiteDatabase db) {
        mockLawyer(db, new Pet("Tony", 2, 'y', 'm', "dog", "Mi compañía en la Soledad.", "tony.jpg", "Septiembre 6 de 2016"));
        mockLawyer(db, new Pet("Leia", 12, 'm', 'f', "cat", "Mi Compañera de Aventuras.", "leia.jpg", "Junio 12 de 2016"));
        mockLawyer(db, new Pet("Misty", 3, 'w', 'f', "rab", "Brincando pasa la vida.", "saltarina.jpg", "Enero 28 de 2016"));
    }

    public long mockLawyer(SQLiteDatabase db, Pet pet) {
        return db.insert(
                PetsEntry.TABLE_NAME,
                null,
                pet.toContentValues());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No hay operaciones
    }

    public long savePets(Pet pet) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        return sqLiteDatabase.insert(
                PetsEntry.TABLE_NAME,
                null,
                pet.toContentValues());

    }

    public Cursor getAllPets() {
        return getReadableDatabase()
                .query(
                        PetsEntry.TABLE_NAME, // Nombre de la tabla
                        null, // Lista de Columnas a consultar
                        null, // Columnas para la cláusula WHERE
                        null, // Valores a comparar con las columnas del WHERE
                        null, // Agrupar con GROUP BY
                        null, // Condición HAVING para GROUP BY
                        null); // Cláusula ORDER BY
    }

    public Cursor getPetById(String petId) {
        Cursor c = getReadableDatabase().query(
                PetsEntry.TABLE_NAME,
                null,
                PetsEntry.ID + " LIKE ?",
                new String[]{petId},
                null,
                null,
                null);
        return c;
    }

    public int deletePet(String petId) {
        return getWritableDatabase().delete(
                PetsEntry.TABLE_NAME,
                PetsEntry.ID + " LIKE ?",
                new String[]{petId});
    }

    public int updatePet(Pet pet, String petId) {
        return getWritableDatabase().update(
                PetsEntry.TABLE_NAME,
                pet.toContentValues(),
                PetsEntry.ID + " LIKE ?",
                new String[]{petId}
        );
    }
}
