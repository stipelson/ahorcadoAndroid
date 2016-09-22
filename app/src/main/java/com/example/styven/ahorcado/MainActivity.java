package com.example.styven.ahorcado;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignin;
    private TextView titleRegister;
    private TextView textViewTitleApp;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            // profile activity here
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        progressDialog = new ProgressDialog(this);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignin = (TextView) findViewById(R.id.textViewSignin);
        titleRegister = (TextView) findViewById(R.id.titleRegister);
        textViewTitleApp = (TextView) findViewById(R.id.textViewTitleApp);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/beermoney.ttf");

        textViewSignin.setTypeface(custom_font);
        editTextEmail.setTypeface(custom_font);
        editTextPassword.setTypeface(custom_font);
        titleRegister.setTypeface(custom_font);
        buttonRegister.setTypeface(custom_font);
        textViewTitleApp.setTypeface(custom_font);

        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
    }

    private void registerUser(){
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

        progressDialog.setMessage("Registrando Usuario...");

        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {
                                finish();
                                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        }
                        if (!task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:failed:" + task.getException());

                            Toast.makeText(MainActivity.this, "Error en el registro de usuario.",
                                    Toast.LENGTH_SHORT).show();

                            try {
                                throw task.getException();
                            } catch(FirebaseAuthWeakPasswordException e) {
                                editTextPassword.setError("Mininmo 6 caracteres");
                                editTextPassword.requestFocus();
                                editTextEmail.setError(null);
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                editTextEmail.setError("Dirección Email invalida");
                                editTextEmail.requestFocus();
                                editTextPassword.setError(null);
                            }  catch(FirebaseAuthUserCollisionException e) {
                                editTextEmail.setError("El usuario ya esta registrado");
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
        if (view == buttonRegister){
            registerUser();
        }
        if(view == textViewSignin){
            // algo pasa aqui
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
