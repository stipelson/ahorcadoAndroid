package com.example.styven.ahorcado;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class GameActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private TextView respuesta;
    private ImageButton imageButtonSignOut;
    private ImageButton imageButtonBack;
    private EditText editTextLetterWord;
    private GridLayout gridLayoutIngresos;
    private int idCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        respuesta = (TextView) findViewById(R.id.textViewRespuesta);
        final Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/beermoney.ttf");

        respuesta.setTypeface(custom_font);

        editTextLetterWord = (EditText) findViewById(R.id.editTextLetterWord);
        editTextLetterWord.setTypeface(custom_font);

        gridLayoutIngresos = (GridLayout) findViewById(R.id.gridLayoutIngresos);

        idCount = 30;

        editTextLetterWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handle = false;
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    String inputText = v.getText().toString().trim();
                    idCount += 1;
                    //Toast.makeText(GameActivity.this, "Tu ingreso es: " + inputText, Toast.LENGTH_SHORT).show();
                    // Ocultando el teclado
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    // agregando el ingreso a la grid
                    TextView ingreso = new TextView(GameActivity.this);

                    ingreso.setTextSize(30);
                    ingreso.setId(idCount);
                    ingreso.setText(inputText + idCount);
                    ingreso.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                    ingreso.setTypeface(custom_font);
                    ingreso.setPadding(5, 3, 5, 0);
                    gridLayoutIngresos.addView(ingreso);

                    // setiando el contenido del edit text
                    editTextLetterWord.setText("");

                    handle = true;
                }
                return handle;
            }
        });

        imageButtonSignOut = (ImageButton) findViewById(R.id.imageButtonSignOut);
        imageButtonBack = (ImageButton) findViewById(R.id.imageButtonBack);
        imageButtonBack.setOnClickListener(this);
        imageButtonSignOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == imageButtonSignOut){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        if (view == imageButtonBack){
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
        }
    }
}
