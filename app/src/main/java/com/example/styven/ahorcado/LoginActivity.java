package com.example.styven.ahorcado;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignUp;
    private ProgressDialog progressDialog;
    private TextView titleLogin;
    private TextView textViewTitleApp;
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            // profile activity here
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        progressDialog =  new ProgressDialog(this);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword  =(EditText) findViewById(R.id.editTextPassword);
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        textViewSignUp = (TextView) findViewById(R.id.textViewSignUp);
        titleLogin = (TextView) findViewById(R.id.titleLogin);
        textViewTitleApp = (TextView) findViewById(R.id.textViewTitleApp);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/beermoney.ttf");

        buttonSignIn.setTypeface(custom_font);
        textViewSignUp.setTypeface(custom_font);
        editTextEmail.setTypeface(custom_font);
        editTextPassword.setTypeface(custom_font);
        titleLogin.setTypeface(custom_font);
        textViewTitleApp.setTypeface(custom_font);

        buttonSignIn.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);
    }

    private void userlogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Por favor ingrese un email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Por favor ingrese una contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Iniciando Sesión...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()){
                            // start profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        }
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, "Error de autenticación.",
                                    Toast.LENGTH_SHORT).show();

                            try {
                                throw task.getException();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                if(e.getErrorCode() == "ERROR_INVALID_EMAIL"){
                                    editTextEmail.setError("Dirección Email invalida");
                                    editTextEmail.requestFocus();
                                    editTextPassword.setError(null);
                                }
                                if(e.getErrorCode() == "ERROR_WRONG_PASSWORD"){
                                    editTextPassword.setError("Contraseña incorrecta");
                                    editTextPassword.requestFocus();
                                    editTextEmail.setError(null);
                                }
                                Log.v(TAG, "ERROR CODE" + e.getErrorCode());

                            } catch(FirebaseAuthInvalidUserException  e) {
                                editTextEmail.setError("El usuario no esta registrado");
                                editTextEmail.requestFocus();
                                editTextPassword.setError(null);
                            } catch(Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                        progressDialog.dismiss();
                    }
                });
    }



    @Override
    public void onClick(View view) {
        if (view == buttonSignIn ){
            userlogin();
        }
        if (view == textViewSignUp){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
