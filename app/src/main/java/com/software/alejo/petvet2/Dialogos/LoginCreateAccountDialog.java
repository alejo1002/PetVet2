package com.software.alejo.petvet2.Dialogos;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.software.alejo.petvet2.R;

import butterknife.BindView;
import butterknife.Unbinder;

/**
 * Created by WEY on 27/11/2016.
 */
public class LoginCreateAccountDialog extends DialogFragment {
    private static final String TAG = LoginCreateAccountDialog.class.getSimpleName();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Unbinder unbinder;

    @BindView(R.id.email_user)
    EditText mEmailField;
    @BindView(R.id.password_user)
    EditText mPasswordField;
    @BindView(R.id.btn_signups)
    Button signOutButton;


    public LoginCreateAccountDialog() {
        //unbinder =  ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createLoginDialog();
    }

    /**
     * Crea un diálogo con personalizado para comportarse
     * como formulario de login
     *
     * @return Diálogo
     */
    public AlertDialog createLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.create_account, null);

        builder.setView(v);

        Button signup = (Button) v.findViewById(R.id.btn_signups);

        signup.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Loguear...
                        //dismiss();
                    }
                }

        );

        return builder.create();
    }


    private boolean valid = true;


}