package com.software.alejo.petvet2.Pets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.software.alejo.petvet2.R;

public class PetsActivity extends AppCompatActivity {

    public static final String EXTRA_PET_ID = "extra_pet_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PetsFragment fragment = (PetsFragment) getSupportFragmentManager().findFragmentById(R.id.pets_container);

        if (fragment == null) {
            fragment = PetsFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.pets_container, fragment)
                    .commit();
        }

    }

}
