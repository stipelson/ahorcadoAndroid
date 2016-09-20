package com.example.styven.ahorcado;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private TextView textViewUserEmail;
    private ImageButton imageButtonSignOut;
    private Button buttonContinueGame;
    private Button buttonNewGame;
    private Button buttonGameHistory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null){
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

        textViewUserEmail.setText("Bienvenido "+user.getEmail());
        textViewUserEmail.setTypeface(custom_font);

        imageButtonSignOut = (ImageButton) findViewById(R.id.imageButtonSignOut);
        imageButtonSignOut.setOnClickListener(this);
        buttonNewGame.setOnClickListener(this);
        buttonContinueGame.setOnClickListener(this);
        buttonGameHistory.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == imageButtonSignOut){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
