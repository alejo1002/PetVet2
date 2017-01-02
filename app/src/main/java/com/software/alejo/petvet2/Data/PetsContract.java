package com.software.alejo.petvet2.Data;

import android.provider.BaseColumns;

/**
 * Created by WEY on 12/09/2016.
 * Esta clase facilita el mantenimiento del esquema de la Base de Datos, el cual puede estar sugeto
 * a cambios de nombres en las tablas o registros.
 */
public class PetsContract {

    //Se implementa la interface BaseColumns para agregar una columna extra a la tabla.
    public static abstract class PetsEntry implements BaseColumns{
        //Clase interna que guarda el nombre de las columnas  de la tabla Pets
        public static  final String TABLE_NAME = "pet";

        public static final String ID= "id";
        public static final String NAME = "name";
        public static final String AGE = "age";
        public static final String AGE_MAGNITUD = "ageMagnitud";
        public static final String GENDER = "gender";
        public static final String SPECIE = "specie";
        public static final String PHRASE = "phrase";
        public static final String AVATAR_URI = "avatarUri";
        public static final String LAST_VET_VISIT = "lastVetVisit";
    }
}
