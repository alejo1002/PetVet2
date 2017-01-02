package com.software.alejo.petvet2.Pets;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.software.alejo.petvet2.AddEditPet.AddEditPetActivity;
import com.software.alejo.petvet2.Data.PetsDbHelper;
import com.software.alejo.petvet2.Entities.Pet;
import com.software.alejo.petvet2.PetDetail.PetDetailActivity;
import com.software.alejo.petvet2.PetsAdapter;
import com.software.alejo.petvet2.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PetsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PetsFragment extends Fragment {
    public static final int REQUEST_UPDATE_DELETE_PET = 2;

    private ListView mPetsList;
    private PetsAdapter mPetsAdapter;
    private ProgressBar mProgressBar;
    private FloatingActionButton mAddButton;

    //Firebase
    private FirebaseDatabase mFirebaseDataBase;
    private DatabaseReference mDataBaseReference;
    private ChildEventListener mChildEventListener;

    private String mUserId;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

////    //STORAGE
//    FirebaseStorage storage = FirebaseStorage.getInstance();
//    StorageReference storageRef = storage.getReferenceFromUrl("gs://petvet2-8d8c9.appspot.com");
//    StorageReference imagesRef = storageRef.child("pet_images");

    public PetsFragment() {
        // Required empty public constructor
    }

    public static PetsFragment newInstance() {
        return new PetsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pets, container, false);

        // Initialize references to views
        mProgressBar = (ProgressBar) root.findViewById(R.id.lading_pets);
        mPetsList = (ListView) root.findViewById(R.id.pets_list);

        // Initialize message ListView and its adapter
        List<Pet> petList = new ArrayList<>();
        mPetsAdapter = new PetsAdapter(getContext(), R.layout.list_item_pets, petList);
        mPetsList.setAdapter(mPetsAdapter);

        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        //FireBase
        mFirebaseDataBase = FirebaseDatabase.getInstance();


        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mUserId = mFirebaseUser.getUid();

        mDataBaseReference = mFirebaseDataBase.getReference().child("users").child(mUserId).child("pets");

        // Referencias UI
        mPetsList = (ListView) root.findViewById(R.id.pets_list);
        //mPetsAdapter = new PetsCursorAdapter(getActivity(), null);
        mAddButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        // Setup
        mPetsList.setAdapter(mPetsAdapter);

        // Eventos
        mPetsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Pet pet = mPetsAdapter.getItem(i);
                showDetailScreen(pet);
            }
        });
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddScreen();
            }
        });

//        getActivity().deleteDatabase(PetsDbHelper.DATABASE_NAME);

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Pet pet = dataSnapshot.getValue(Pet.class);
                pet.setPetId(dataSnapshot.getKey());
                mPetsAdapter.add(pet);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDataBaseReference.addChildEventListener(mChildEventListener);

        return root;
    }

    private void showDetailScreen(Pet pet) {
        Intent intent = new Intent(getActivity(), PetDetailActivity.class);
        intent.putExtra(PetsActivity.EXTRA_PET_ID, pet);
        //intent.putExtra("TIPO_OPERACION", "details");
        startActivityForResult(intent, REQUEST_UPDATE_DELETE_PET);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            switch (requestCode) {
                case AddEditPetActivity.REQUEST_ADD_PET:
                    showSuccessfullSavedMessage();
                    break;
                case REQUEST_UPDATE_DELETE_PET:
                    break;
            }
        }
    }

    private void showSuccessfullSavedMessage() {
        Toast.makeText(getActivity(),
                "Mascota guardada correctamente", Toast.LENGTH_SHORT).show();
    }

    private void showAddScreen() {
        Intent intent = new Intent(getActivity(), AddEditPetActivity.class);
        intent.putExtra("TIPO_OPERACION", "details");
        startActivityForResult(intent, AddEditPetActivity.REQUEST_ADD_PET);
    }

}
