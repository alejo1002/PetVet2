package com.software.alejo.petvet2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.software.alejo.petvet2.Entities.Pet;

import java.util.List;

/**
 * Created by WEY on 11/12/2016.
 */
public class PetsAdapter extends ArrayAdapter<Pet> {

    public PetsAdapter(Context context, int resource, List<Pet> Pets){
        super(context, resource, Pets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.list_item_pets, parent, false);
        }

        final ImageView petImageView = (ImageView) convertView.findViewById(R.id.iv_avatar);
        TextView petNameView = (TextView) convertView.findViewById(R.id.tv_name);
        TextView phraseView = (TextView) convertView.findViewById(R.id.tv_phrase);

        final Pet currentPet = getItem(position);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://petvet2-8d8c9.appspot.com/");
        StorageReference imagesRef = storageRef.child(currentPet.getUrlImage());

        boolean isPhoto = currentPet.getUrlImage() != null;

        if (isPhoto) {
            petNameView.setVisibility(View.VISIBLE);
            petNameView.setText(currentPet.getName());
            phraseView.setVisibility(View.VISIBLE);
            phraseView.setText(currentPet.getPhrase());
            petImageView.setVisibility(View.VISIBLE);

            imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(petImageView.getContext())
                        .load(uri)
                        .asBitmap()
                        .error(R.drawable.ic_account_circle)
                        .centerCrop()
                        .into(new BitmapImageViewTarget(petImageView) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable drawable
                                        = RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                                drawable.setCircular(true);
                                petImageView.setImageDrawable(drawable);
                            }
                        });
                }
            });

        } else {
            petNameView.setVisibility(View.VISIBLE);
            petNameView.setText(currentPet.getName());
            phraseView.setVisibility(View.VISIBLE);
            phraseView.setText(currentPet.getPhrase());
            petImageView.setVisibility(View.GONE);
        }

        return convertView;
    }
}
