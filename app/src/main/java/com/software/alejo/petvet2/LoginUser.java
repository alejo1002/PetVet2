package com.software.alejo.petvet2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.software.alejo.petvet2.Helpers.HelperClass;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginUser extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    //GOOGLE SIGNIN
    private static final String TAGS = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;


    private static final String TAG = "EmailPassword";
    private ProgressDialog mProgressDialog;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Unbinder unbinder;
    private boolean valid = true;
    private Bundle extras;

    @BindView(R.id.email_user) EditText mEmailField;
    @BindView(R.id.password_user) EditText mPasswordField;
    @BindView(R.id.btn_login_user) Button signOutButton;
    @BindView(R.id.btn_signup) Button signUp;
    @BindView(R.id.btn_reset_password) Button frtPass;

    //GOOGLE
    @BindView(R.id.btn_sign_in) SignInButton signInButton;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        unbinder = ButterKnife.bind(this);

        // Configure Google Sign In*******************
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(
                getString(R.string.default_web_client_id)).requestEmail().build();
        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(AppIndex.API).build();
        // Configure Google Sign In*******************

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    startActivity(new Intent(LoginUser.this, MainActivity.class));
//                    Intent intent = new Intent(LoginUser.this, MainActivity.class);
//                    startActivity(intent);
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                hideProgressDialog();
            }
        };

        //Recibir Notificación cuando la app está en segundo plano.
        try{
            extras = getIntent().getExtras();

            if (extras != null && extras.getString("notify_title") != null && extras.getString("notify_body") != null){

                String title = extras.getString("notify_title");
                String body = extras.getString("notify_body");

                Intent intentNotifications = new Intent(this, Notification.class);
                intentNotifications.putExtra("TITLE", title);
                intentNotifications.putExtra("MESSAGE", body);
                intentNotifications.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intentNotifications);
                finish();
            }
        }
        catch (Exception ex){
//            Toast toast = Toast.makeText(this, "MAIN: " + ex.toString(), Toast.LENGTH_LONG);
//            toast.show();
        }


    }

    /**
     *  Controla que el botón atrás del teléfono cierre la aplicación y evite a la
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

    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.connect();
        mAuth.addAuthStateListener(mAuthListener);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "LoginUser Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.software.alejo.petvet2/http/host/path")
        );
        AppIndex.AppIndexApi.start(mGoogleApiClient, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "LoginUser Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.software.alejo.petvet2/http/host/path")
        );
        AppIndex.AppIndexApi.end(mGoogleApiClient, viewAction);
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
        unbinder.unbind();
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        Log.d(TAG, "signIn:" + password);
        validateForm();
        if (!valid) {
            return;
        }
        showProgressDialog();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LoginUser.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else{
                            String mensaje;
                            String exception = task.getException().toString();
                            String[] parts =  exception.split(":");
                            String passwordUserInvalidos = "The password is invalid or the user does not have a password.";
                            String usuarioSinRegistro = "There is no user record corresponding to this identifier. The user may have been deleted.";
                            String falloInternet = "A network error (such as timeout, interrupted connection or unreachable host) has occurred.";

                            if (passwordUserInvalidos.equals(parts[1].trim())){
                                mensaje = "Dirección de correo o password incorrectos.";
                            }
                            else if(usuarioSinRegistro.equals(parts[1].trim())) {
                                mensaje = "Usuario sin registro en el servidor.";
                            }
                            else if(falloInternet.equals(parts[1].trim())) {
                                mensaje = "Fallo en la conexión a internet.";
                            }
                            else{
                                mensaje = "Error en el servidor.";
                            }
                            Log.w(TAG, "signInWithEmail", task.getException());
                            //TODO: LLENAR EL TOAST CON LOS MENSAJES DE ERROR
                            Toast.makeText(LoginUser.this, mensaje , Toast.LENGTH_LONG)
                                    .show();
                        }
                        hideProgressDialog();
                    }
                });
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

    @OnClick(R.id.btn_login_user)
    public void onClickCreateAccount( ) {
        signIn(mEmailField.getText().
                toString(), mPasswordField.getText().
                toString());
    }


    @OnClick(R.id.btn_signup)
    public void onClickShowDilogCreateAccount() {
        Intent intent = new Intent(LoginUser.this, LoginCreateAccount.class);
        startActivity(intent);
    }


    @OnClick(R.id.btn_reset_password)
    public void onClicResetPassword() {
        Intent intent = new Intent(LoginUser.this, ResetPassword.class);
        startActivity(intent);
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

    //GOOGLE
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not be available.
        Log.d(TAGS, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGooogle:" + acct.getId());
        showProgressDialog();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInWithCredential", task.getException());
                    Toast.makeText(LoginUser.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
                hideProgressDialog();
            }
        });
    }

    @OnClick(R.id.btn_sign_in)
    public void onClickSignInButton() {
        signIn();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();
        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                //updateUI(null);
            }
        });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();
        // Google revoke access
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                //updateUI(null);
            }
        });
    }

}
