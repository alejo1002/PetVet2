package com.software.alejo.petvet2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.software.alejo.petvet2.Helpers.HelperClass;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginCreateAccount extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    private ProgressDialog mProgressDialog;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Unbinder unbinder;

    private boolean valid = true;

    @BindView(R.id.user) EditText mEmailField;
    @BindView(R.id.password) EditText mPasswordField;
    @BindView(R.id.email_create_account_button) Button signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_create_account);

        unbinder =  ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    //Toast.makeText(LoginCreateAccount.this, "Authentication SUCCESS.", Toast.LENGTH_SHORT).show();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    //Toast.makeText(LoginCreateAccount.this, "Authentication FAILED.", Toast.LENGTH_SHORT).show();
                }
                hideProgressDialog();
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
        unbinder.unbind();
    }

    private void validateForm() {
        String email = mEmailField.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("El correo es requerido");
            valid = false;
            return;
        }
        else if(!HelperClass.isValidEmail(email)) {
            mEmailField.setError("Dirección de correo inválida.");
            valid = false;
            return;
        }
        else {
            mEmailField.setError(null);
            valid = true;
        }

        String password = mPasswordField.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("La Contraseña es requerida");
            valid = false;
            return;
        }
        else  if (password.length() < 6){
            mPasswordField.setError("La contraseña debe tener mas de 6 caracteres");
            valid = false;
            return;
        }else {
            mPasswordField.setError(null);
            valid = true;
        }
    }

    @OnClick(R.id.email_create_account_button)
    public void onClickCreateAccount() {

        Log.d(TAG, "createAccount:" + mEmailField.getText().toString());
        Log.d(TAG, "createPassword:" + mPasswordField.getText().toString());

        validateForm();

        if (!valid) {
            return;
        }
        showProgressDialog();
        mAuth.createUserWithEmailAndPassword(mEmailField.getText().toString(), mPasswordField.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LoginCreateAccount.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else{
                            String mensaje;
                            String exception = task.getException().toString();
                            String[] parts =  exception.split(":");
                            String cuentaYaRegistrada = "The email address is already in use by another account.";
                            String correoMalformateado = "The email address is badly formatted.";
                            String falloInternet = "A network error (such as timeout, interrupted connection or unreachable host) has occurred.";

                            if (cuentaYaRegistrada.equals(parts[1].trim())){
                                mensaje = "La dirección de correo ya está en uso por otra cuenta.";
                            }
                            else if(correoMalformateado.equals(parts[1].trim())) {
                                mensaje = "La dirección de correo esta mal formateada.";
                            }
                            else if(falloInternet.equals(parts[1].trim())) {
                                mensaje = "Fallo en la conexión a internet.";
                            }
                            else{
                                mensaje = "Error en el servidor.";
                            }
                            Log.w(TAG, "signInWithEmail", task.getException());
                            //TODO: LLENAR EL TOAST CON LOS MENSAJES DE ERROR
                            Toast.makeText(LoginCreateAccount.this, mensaje , Toast.LENGTH_LONG)
                                    .show();
                        }
                        hideProgressDialog();
                    }
                });
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
