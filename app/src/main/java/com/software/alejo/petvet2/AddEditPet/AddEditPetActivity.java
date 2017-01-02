package com.software.alejo.petvet2.AddEditPet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.software.alejo.petvet2.Pets.PetsActivity;
import com.software.alejo.petvet2.R;

public class AddEditPetActivity extends AppCompatActivity {
    public static final int REQUEST_ADD_PET = 1;
    public static boolean operation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_pet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String petId = getIntent().getStringExtra(PetsActivity.EXTRA_PET_ID);
        String operacion = getIntent().getStringExtra("TIPO_OPERACION");
        try{
            if(operacion.equals("update")){
                operation = true;
            }
            else{
                operation = false;
            }
        }catch (Exception ex) {}


        setTitle(petId == null ? "Añadir Mascota" : "Editar Mascota");

        AddEditPetFragment addEditPetsFragment = (AddEditPetFragment)
                getSupportFragmentManager().findFragmentById(R.id.add_edit_pet_container);
        if (addEditPetsFragment == null) {
            addEditPetsFragment = AddEditPetFragment.newInstance(petId);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.add_edit_pet_container, addEditPetsFragment)
                    .commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
