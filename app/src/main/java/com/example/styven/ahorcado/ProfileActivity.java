package com.example.styven.ahorcado;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private TextView textViewUserEmail;
    private ImageButton imageButtonSignOut;
    private Button buttonContinueGame;
    private Button buttonNewGame;
    private Button buttonGameHistory;
    private ProgressDialog progressDialog;
    private FirebaseUser user;
    private static final String TAG = "dataBase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //auth != null
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            user = firebaseAuth.getCurrentUser();
        }else{
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        buttonContinueGame = (Button) findViewById(R.id.buttonContinueGame);
        buttonGameHistory = (Button) findViewById(R.id.buttonGameHistory);
        buttonNewGame = (Button) findViewById(R.id.buttonNewGame);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/beermoney.ttf");

        buttonGameHistory.setTypeface(custom_font);
        buttonContinueGame.setTypeface(custom_font);
        buttonNewGame.setTypeface(custom_font);

        textViewUserEmail.setText("Bienvenido "+ usuario(user.getEmail()));
        textViewUserEmail.setTypeface(custom_font);

        imageButtonSignOut = (ImageButton) findViewById(R.id.imageButtonSignOut);
        progressDialog =  new ProgressDialog(this);
        imageButtonSignOut.setOnClickListener(this);
        buttonNewGame.setOnClickListener(this);
        buttonContinueGame.setOnClickListener(this);
        buttonGameHistory.setOnClickListener(this);
    }
    private String usuario(String email){
        String user = "";
        if (email.contains("@")) {
            String[] parts = email.split("@");
            user = parts[0];
        } else {
            user = email;
        }
        return user;
    }

    @Override
    public void onClick(View view) {
        if (view == imageButtonSignOut){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        if (view == buttonNewGame) {
            /*
            progressDialog.setMessage("Buscando palabra aleatoria...");
            progressDialog.show();

            //llamar el firebasedatabase y hacer el rand aqui, ademas del evenetlistener, luego enviar el parametro al indent de game.

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference myRef = firebaseDatabase.getReference();

            myRef.child("palabras").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    int countPalabras = (int) (long) dataSnapshot.getChildrenCount();
                    Log.v(TAG, "Palabras: "+ countPalabras);
                    int maxRandom = countPalabras - 1;

                    Random r = new Random();
                    int keyRandom = r.nextInt((maxRandom - 0) + 1) + 0;
                    Log.v(TAG, "numero de palabra escogida: " + keyRandom);
                    String palabra =  dataSnapshot.child(""+keyRandom).getValue().toString();
                    Log.v(TAG, "palabra escogida: " + palabra);
                    Intent intentJuego = new Intent(ProfileActivity.this, GameActivity.class);
                    intentJuego.putExtra("palabraAleatoria",palabra);
                    intentJuego.putExtra("nuevoJuego","true");

                    finish();
                    progressDialog.dismiss();
                    startActivity(intentJuego);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.v(TAG, "Palabra escogida: Error de selección");
                    Toast.makeText(ProfileActivity.this, "Imposible obtener una palabra", Toast.LENGTH_SHORT).show();
                    return;
                }
            });
            */
            Intent intentJuego = new Intent(ProfileActivity.this, GameActivity.class);
            intentJuego.putExtra("nuevoJuego","true");
            finish();
            startActivity(intentJuego);

        }
        if (view == buttonContinueGame || view == buttonGameHistory){
            Toast.makeText(this, "En construcción", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
