package com.software.alejo.petvet2.PetDetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.software.alejo.petvet2.Entities.Pet;
import com.software.alejo.petvet2.Pets.PetsActivity;
import com.software.alejo.petvet2.R;

public class PetDetailActivity extends AppCompatActivity {
    public static Pet currentPet;
    //public static String operacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String id = getIntent().getStringExtra(PetsActivity.EXTRA_PET_ID);

        PetDetailFragment fragment = (PetDetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.pet_detail_container);
        if (fragment == null) {
            fragment = PetDetailFragment.newInstance(id);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.pet_detail_container, fragment)
                    .commit();
        }

        Intent intent = getIntent();
        currentPet = (Pet)intent.getSerializableExtra(PetsActivity.EXTRA_PET_ID);
        //operacion = intent.getStringExtra("TIPO_OPERACION");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pet_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
