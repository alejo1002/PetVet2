package com.software.alejo.petvet2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.software.alejo.petvet2.Entities.Schedule;
import com.software.alejo.petvet2.Entities.VeterinariesListSerializable;
import com.software.alejo.petvet2.Entities.Veterinary;
import com.software.alejo.petvet2.Entities.WeekDay;
import com.software.alejo.petvet2.Pets.PetsFragment;

import java.net.URI;
import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String EXTRA_PET_ID = "extra_pet_id";
    private VeterinariesListSerializable vets = new VeterinariesListSerializable();
    public final static String VETERINARY_LIST = "com.software.alejo.veterinary_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fillVeterinariesMock();

        PetsFragment fragment = (PetsFragment) getSupportFragmentManager().findFragmentById(R.id.pets_container);
        if (fragment == null) {
            fragment = PetsFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.pets_container, fragment)
                    .commit();
        }

        //SET USER IN DRAWABLE HEADER
        setUser(navigationView);
    }

    private void setUser(NavigationView navigationView) {
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        View header = navigationView.getHeaderView(0);

        TextView mUserName = (TextView)header.findViewById(R.id.drw_user_name);
        TextView mUserEmail = (TextView)header.findViewById(R.id.drw_user_email);
        final ImageView mUserImage = (ImageView)header.findViewById(R.id.drw_user_image);

        if(user.getDisplayName() != null && !user.getDisplayName().equals("")){
            mUserName.setText(user.getDisplayName());
            mUserEmail.setText(user.getEmail());
        }else{
            mUserName.setText(user.getEmail());
            mUserEmail.setText("");
        }

        if(user.getPhotoUrl() != null && !user.getDisplayName().equals("")){
            Glide.with(mUserImage.getContext())
                    .load(user.getPhotoUrl())
                    .asBitmap()
                    .centerCrop()
                    .into(new BitmapImageViewTarget(mUserImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable drawable
                                = RoundedBitmapDrawableFactory.create(getResources(), resource);
                        drawable.setCircular(true);
                        mUserImage.setImageDrawable(drawable);
                    }
                });
        }
    }

    /**
     *  Controla que el botón atrás del teléfono cierre la aplicación y evite ingresar a la
     *  anterior actividad.
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return false;
    }

    public void fillVeterinariesMock() {
        Veterinary vetry1 = new Veterinary();
        Veterinary vetry2 = new Veterinary();
        //ArrayList<Schedule> schedules;
        //schedules = getScheduleList();

        vetry1.setName("El Palacio de las Mascotas");
        vetry1.setLatitude(6.254552);
        vetry1.setLongitude(-75.574509);
        //vetry1.setSchedules(schedules);
        vetry1.setHorario("Lunes a Viernes 8 am a 7 pm Sábados 8:30 a 5:00 p.m.");


        vetry2.setName("Pet Shop");
        vetry2.setLatitude(6.255874);
        vetry2.setLongitude(-75.573769);
        //vetry2.setSchedules(schedules);
        vetry2.setHorario("Lunes a Viernes 8 am a 9 pm Sábados y Domingos 8:30 a 5:00 p.m.");

        ArrayList<Veterinary> veteriesList = new ArrayList<>();
        veteriesList.add(vetry1);
        veteriesList.add(vetry2);

        vets.setVeterinaries(veteriesList);
    }

    private ArrayList<Schedule> getScheduleList() {
        ArrayList<Schedule> schedules = new ArrayList<>();
        Schedule schedule = new Schedule();

        schedule.setWeekDay(WeekDay.MONDAY);
        schedule.setOpeningTime(Time.valueOf("09:00:00"));
        schedule.setClosingTime(Time.valueOf("12:00:00"));
        schedules.add(schedule);

        schedule.setWeekDay(WeekDay.SATURDAY);
        schedule.setOpeningTime(Time.valueOf("07:00:00"));
        schedule.setClosingTime(Time.valueOf("11:00:00"));
        schedules.add(schedule);

        return schedules;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.add:
                startMapsActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.drw_notification) {

        }
        else if (id == R.id.drw_configuration) {

        }
        else if (id == R.id.drw_shout_down) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginUser.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        if (id == R.id.drw_share) {
            sharePetVet();
        }

        else if (id == R.id.drw_go_to_map) {
            startMapsActivity();
        }

        return true;
    }

    private void sharePetVet(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "PetVet es una aplicación para encontrar la mejor veterinaria para tu mascota.");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void startMapsActivity() {
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        intent.putExtra(VETERINARY_LIST, vets);
        startActivity(intent);
    }
}
