package com.software.alejo.petvet2.AddEditPet;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.frosquivel.magicalcamera.MagicalCamera;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.software.alejo.petvet2.Data.PetsDbHelper;
import com.software.alejo.petvet2.Entities.Pet;
import com.software.alejo.petvet2.MainActivity;
import com.software.alejo.petvet2.PetDetail.PetDetailActivity;
import com.software.alejo.petvet2.R;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

//import com.software.alejo.petvet2.Data.Pet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEditPetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEditPetFragment extends Fragment {
    private static final String ARG_PET_ID = "arg_pet_id";

    private String mPetId;

    private PetsDbHelper mPetsDbHelper;

    private FloatingActionButton mSaveButton;
    private TextInputEditText mNameField;
    private TextInputEditText mAgeField;
    private TextInputEditText mPhraseField;

    private TextInputLayout mNameLabel;
    private TextInputLayout mAgeLabel;
    private TextInputLayout mPhraseLabel;

    private Spinner mAgeMagField;
    private Spinner mSpecieField;
    private RadioButton mGenderMaleField;
    private RadioButton mGenderFemaleField;

    //para la cámara
    private MagicalCamera magicalCamera;
    private int RESIZE_PHOTO_PIXELS_PERCENTAGE = 1000;
    private Activity activity;
    private Button btnTakePhoto;
    private ImageView imgPhoto;

    //Firebase
    private DatabaseReference mDatabaseReference;
    private String mUserId;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private static final String TAG = "NewPetActivity";
    private static final String REQUIRED = "Required";
    private Pet pet;

    //STORAGE
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReferenceFromUrl("gs://petvet2-8d8c9.appspot.com");
    private StorageReference imagesRef;
    private Bitmap foto;
    private byte[] datos;

    public AddEditPetFragment() {
        // Required empty public constructor
    }


    public static AddEditPetFragment newInstance(String petId) {
        AddEditPetFragment fragment = new AddEditPetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PET_ID, petId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPetId = getArguments().getString(ARG_PET_ID);
        }

        // Firebase
        mUserId = mFirebaseAuth.getInstance().getCurrentUser().getUid();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_edit_pet, container, false);

        //CAMARA
        activity = getActivity();
        magicalCamera = new MagicalCamera(getActivity(), RESIZE_PHOTO_PIXELS_PERCENTAGE);
        imgPhoto = (ImageView) root.findViewById(R.id.img_image);
        btnTakePhoto = (Button) root.findViewById(R.id.btnPhoto);

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final CharSequence[] items = new CharSequence[2];
                items[0] = "Tomar foto";
                items[1] = "Elegir una foto";

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            if (magicalCamera.takeFragmentPhoto()) {
                                startActivityForResult(magicalCamera.getIntentFragment(),
                                        MagicalCamera.TAKE_PHOTO);
                            }
                        }
                        else{
                            if (magicalCamera.selectedFragmentPicture()) {
                                startActivityForResult(
                                        Intent.createChooser(magicalCamera.getIntentFragment(), "Seleccione una Imagen"),
                                        MagicalCamera.SELECT_PHOTO);
                            }
                        }
                    }
                });
                Dialog dialog = builder.create();
                dialog.show();
            }
        });

        // Referencias UI
        mSaveButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        mNameField = (TextInputEditText) root.findViewById(R.id.txt_name);
        mAgeField = (TextInputEditText) root.findViewById(R.id.txt_age);
        mPhraseField = (TextInputEditText) root.findViewById(R.id.txt_phrase);

        mNameLabel = (TextInputLayout) root.findViewById(R.id.til_name);
        mAgeLabel = (TextInputLayout) root.findViewById(R.id.til_age);
        mPhraseLabel = (TextInputLayout) root.findViewById(R.id.til_phrase);

        mAgeMagField = (Spinner) root.findViewById(R.id.spn_age);
        mSpecieField = (Spinner) root.findViewById(R.id.spn_specie);
        mGenderMaleField = (RadioButton) root.findViewById(R.id.rbn_male);
        mGenderFemaleField = (RadioButton) root.findViewById(R.id.rbn_female);

        // Eventos
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEditPet();
            }
        });

        mPetsDbHelper = new PetsDbHelper(getActivity());

        // Carga de datos
        if (AddEditPetActivity.operation) {
            pet = PetDetailActivity.currentPet;
            loadPet();
        }

        return root;
    }

    //CAMARA
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        magicalCamera.resultPhoto(requestCode, resultCode, data);

        if (magicalCamera.getMyPhoto() != null) {
            imgPhoto.setImageBitmap(magicalCamera.getMyPhoto());

            //STORAGE
            imgPhoto.setDrawingCacheEnabled(true);
            imgPhoto.buildDrawingCache();
            foto = imgPhoto.getDrawingCache();
        }
    }

    private void loadPet() {
        mNameField.setText(pet.getName());
        mAgeField.setText(String.valueOf(pet.getAge()));
        mPhraseField.setText(pet.getPhrase());
        mAgeMagField.setSelection(setSelection(pet.getAgeManitud()));
        setGender(pet.getGender());
        mSpecieField.setSelection(setSpecie(pet.getSpecie()));

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://petvet2-8d8c9.appspot.com/");
        StorageReference imagesRef = storageRef.child(pet.getUrlImage());

        imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(imgPhoto.getContext())
                    .load(uri)
                    .error(R.drawable.ic_account_circle)
                    .centerCrop()
                    .into(imgPhoto);
            }
        });

        //new GetPetByIdTask().execute();
    }

    private int setSelection(String ageMag){
        int ans;
        switch (ageMag){
            case "s":
                ans = 0;
                break;
            case "m":
                ans = 1;
                break;
            case "a":
                ans = 2;
                break;
            default:
                ans = 0;
        }
        return ans;
    }

    private void setGender(String gender){
        if(gender.equals("m")){
            mGenderMaleField.setChecked(true);
            mGenderFemaleField.setChecked(false);
        }else{
            mGenderMaleField.setChecked(false);
            mGenderFemaleField.setChecked(true);
        }

    }

    private int setSpecie(String specie){
        int ans;
        switch (specie){
            case "dog":
                ans = 0;
                break;
            case "cat":
                ans = 1;
                break;
            case "rab":
                ans = 2;
                break;
            case "bir":
                ans = 3;
                break;
            case "tur":
                ans = 4;
                break;
            default:
                ans = 0;
        }
        return ans;
    }


    private void addEditPet() {
        boolean error = false;

        String name = mNameField.getText().toString();
        String age = mAgeField.getText().toString();
        String phrase = mPhraseField.getText().toString();

        if (TextUtils.isEmpty(name)) {
            mNameLabel.setError(getString(R.string.field_error));
            error = true;
        }

        if (TextUtils.isEmpty(age)) {
            mAgeLabel.setError(getString(R.string.field_error));
            error = true;
        }

        if (TextUtils.isEmpty(phrase)) {
            mPhraseLabel.setError(getString(R.string.field_error));
            error = true;
        }

        if (error) {
            return;
        }

        String ageMag = getAgeMag(mAgeMagField.getSelectedItem().toString());
        String specie = getSpecie(mSpecieField.getSelectedItem().toString());
        String gender = getGender(mGenderMaleField.isChecked());

        mUserId = mFirebaseUser.getUid();

        //PHOTO STORAGE
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        foto.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        datos = baos.toByteArray();
        imagesRef = storageRef.child("pet_images/" + mUserId + "--" + name +".jpg");

        Pet pety = new Pet();
        pety.setName(name);
        pety.setPhrase(phrase);
        pety.setAge(Integer.parseInt(age));
        pety.setGender(gender);
        pety.setSpecie(specie);
        pety.setAgeManitud(ageMag);
        pety.setLastVisit(pety.getFormatDate());
        pety.setUrlImage("pet_images/" + mUserId + "--" + name +".jpg");

        UploadTask uploadTask = imagesRef.putBytes(datos);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                //downloadUrl[0] = taskSnapshot.getDownloadUrl();
            }
        });

        if(AddEditPetActivity.operation){//UPDATE
            mDatabaseReference.child("users").child(mUserId).child("pets").child(this.pet.getPetId()).setValue(pety);
        }else{//CREATE
            mDatabaseReference.child("users").child(mUserId).child("pets").push().setValue(pety);
        }

        showPetsScreen(true);
    }

    private String getSpecie(String specie) {
        switch (specie) {
            case "Perro":
                return "dog";
            case "Gato":
                return "cat";
            case "Conejo":
                return "rab";
            case "Ave":
                return "bir";
            case "Tortuga":
                return "tur";
            default:
                return "not";
        }
    }

    private String getAgeMag(String ageMag) {
        switch (ageMag) {
            case "Semanas":
                return "s";
            case "Meses":
                return "m";
            case "Años":
                return "a";
            default:
                return "h";
        }
    }

    private String getGender(boolean answ) {
        if (answ == true) {
            return "m";
        } else {
            return "f";
        }
    }

    private void showPetsScreen(Boolean requery) {
        if (!requery) {
            showAddEditError();
            getActivity().setResult(Activity.RESULT_CANCELED);
        } else {
            getActivity().setResult(Activity.RESULT_OK);
        }
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void showAddEditError() {
        Toast.makeText(getActivity(),
                "Error al agregar nueva información de Mascota.", Toast.LENGTH_SHORT).show();
    }

    private void showLoadError() {
        Toast.makeText(getActivity(),
                "Error al editar Mascota", Toast.LENGTH_SHORT).show();
    }

//    private class GetPetByIdTask extends AsyncTask<Void, Void, Cursor> {
//
//        @Override
//        protected Cursor doInBackground(Void... voids) {
//            return mPetsDbHelper.getPetById(mPetId);
//        }
//
//        @Override
//        protected void onPostExecute(Cursor cursor) {
////            if (cursor != null && cursor.moveToLast()) {
////                showPet(new Pet(cursor));
////            } else {
////                showLoadError();
////                getActivity().setResult(Activity.RESULT_CANCELED);
////                getActivity().finish();
////            }
//        }
//
//    }

//    private class AddEditPetTask extends AsyncTask<Pet, Void, Boolean> {
//
//        @Override
//        protected Boolean doInBackground(Pet... pets) {
////            if (mPetId != null) {
////                return mPetsDbHelper.updatePet(pets[0], mPetId) > 0;
////
////            } else {
////                return mPetsDbHelper.savePets(pets[0]) > 0;
////            }
//            return null;//*************************************QUITAR**********************
//        }
//
//        @Override
//        protected void onPostExecute(Boolean result) {
//            showPetsScreen(result);
//        }
//
//    }
}
