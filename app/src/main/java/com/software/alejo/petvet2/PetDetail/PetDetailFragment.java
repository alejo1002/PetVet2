package com.software.alejo.petvet2.PetDetail;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.software.alejo.petvet2.AddEditPet.AddEditPetActivity;
import com.software.alejo.petvet2.Data.PetsDbHelper;
import com.software.alejo.petvet2.Entities.Pet;
import com.software.alejo.petvet2.MainActivity;
import com.software.alejo.petvet2.Pets.PetsFragment;
import com.software.alejo.petvet2.QRActivity;
import com.software.alejo.petvet2.R;

//import com.software.alejo.petvet2.AddEditPet.AddEditPetActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PetDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PetDetailFragment extends Fragment {
    private static final String ARG_PET_ID = "petId";

    private String mPetId;

    private CollapsingToolbarLayout mCollapsingView;
    private ImageView mAvatar;
    private TextView mAge;
    private TextView mGender;
    private TextView mSpecie;
    private TextView mPhrase;
    private TextView mLastVetVisit;
    private Pet currentPet;

    private String petCode;

    public PetDetailFragment() {
        // Required empty public constructor
    }

    public static PetDetailFragment newInstance(String petId) {
        PetDetailFragment fragment = new PetDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PET_ID, petId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            //mPetId = getArguments().getString(ARG_PET_ID);

            //loadPet();
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pet_detail, container, false);
        mCollapsingView = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);
        mAvatar = (ImageView) getActivity().findViewById(R.id.iv_avatar);

        mAge = (TextView) root.findViewById(R.id.tv_age);
        mGender = (TextView) root.findViewById(R.id.tv_gender);
        mSpecie = (TextView) root.findViewById(R.id.tv_spicie);
        mPhrase = (TextView) root.findViewById(R.id.tv_phrase);
        mLastVetVisit = (TextView) root.findViewById(R.id.tv_lastVetVisit);

        this.currentPet = PetDetailActivity.currentPet;
        showPet();

        //Evento clic del botón generar QR
        Button buttonGenerateCode = (Button)root.findViewById(R.id.btnQrGenerate);
        buttonGenerateCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), QRActivity.class);
                intent.putExtra("CODIGO_QR", petCode);
                startActivity(intent);
            }
        });
        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                showEditScreen();
                break;
            case R.id.action_delete:
                confirmarEliminacion();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void confirmarEliminacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Alerta de Eliminación");
        builder.setMessage("¿Está seguro que desea eliminar la mascota?");
        builder.setPositiveButton("SI",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deletePet();
                                showPetsScreen(true);
                            }
                        });
        builder.setNegativeButton("CANCELAR",null);

        Dialog dialog = builder.create();
        dialog.show();
    }

    private void deletePet(){
        FirebaseDatabase mFirebaseDataBase = FirebaseDatabase.getInstance();
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser =  mFirebaseAuth.getCurrentUser();
        String mUserId = mFirebaseUser.getUid();
        DatabaseReference mDataBaseReference = mFirebaseDataBase.getReference().child("users").child(mUserId).child("pets").child(PetDetailActivity.currentPet.getPetId());
        mDataBaseReference.setValue(null);

        //DELETE IMAGE
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://petvet2-8d8c9.appspot.com/");
        StorageReference imagesRef = storageRef.child(currentPet.getUrlImage());
        imagesRef.delete();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PetsFragment.REQUEST_UPDATE_DELETE_PET) {
            if (resultCode == Activity.RESULT_OK) {
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        }
    }

    private void showPet() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://petvet2-8d8c9.appspot.com/");
        StorageReference imagesRef = storageRef.child(currentPet.getUrlImage());

        petCode = currentPet.getPetId();
        mCollapsingView.setTitle(currentPet.getName());

        imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(mAvatar.getContext())
                        .load(uri)
                        .centerCrop()
                        .into(mAvatar);
            }
        });

        mAge.setText(currentPet.getAgeComplete());
        mGender.setText(currentPet.getGenderComplete());
        mSpecie.setText(currentPet.getSpicieComplete());
        mPhrase.setText(currentPet.getPhrase());
        mLastVetVisit.setText(currentPet.getLastVisit());
    }

//TODO: MANDAR EL PET POR MEDIO DEL INTENT
    private void showEditScreen() {
        Intent intent = new Intent(getActivity(), AddEditPetActivity.class);
        intent.putExtra("extra_pet_id", currentPet);
        intent.putExtra("TIPO_OPERACION", "update");
        startActivityForResult(intent, 2);
    }

    private void showPetsScreen(boolean requery) {
        if (!requery) {
            showDeleteError();
            getActivity().setResult(Activity.RESULT_CANCELED);
        }
        else{
            getActivity().setResult(Activity.RESULT_OK);
        }
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivityForResult(intent, Activity.RESULT_OK);

        getActivity().finish();
    }

    private void showDeleteError() {
        Toast.makeText(getActivity(),
                "Error al eliminar Mascota.", Toast.LENGTH_SHORT).show();
    }

}
