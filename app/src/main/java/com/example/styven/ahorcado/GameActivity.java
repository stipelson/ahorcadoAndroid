package com.example.styven.ahorcado;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    private AhorcadoGAme ahorcadoGame;
    private static String TAG = "gameactivity: ";
    private Chronometer chronometer;
    private TextView textviewErrores;
    private ImageView imageViewGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        firebaseAuth = FirebaseAuth.getInstance();

        respuesta = (TextView) findViewById(R.id.textViewRespuesta);
        final Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/beermoney.ttf");
        respuesta.setTypeface(custom_font);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.start();
        Log.v(TAG, "formato de cronotmetro" + chronometer.getFormat());

        if (firebaseAuth.getCurrentUser() != null){
            Intent myIntent = getIntent();
            ahorcadoGame = new AhorcadoGAme(myIntent.getStringExtra("palabraAleatoria"));
            String juegoNuevo = myIntent.getStringExtra("nuevoJuego");

            respuesta.setText(ahorcadoGame.getPalabraSecreta());
        }else{
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        editTextLetterWord = (EditText) findViewById(R.id.editTextLetterWord);
        editTextLetterWord.setTypeface(custom_font);

        gridLayoutIngresos = (GridLayout) findViewById(R.id.gridLayoutIngresos);
        textviewErrores = (TextView) findViewById(R.id.textViewErrores);
        textviewErrores.setTextColor(Color.parseColor("#ef5350"));
        textviewErrores.setTypeface(custom_font);
        imageViewGame = (ImageView) findViewById(R.id.imageViewGame);

        editTextLetterWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handle = false;
                if (actionId == EditorInfo.IME_ACTION_DONE){

                    String inputText = v.getText().toString().trim();
                    //Toast.makeText(GameActivity.this, "Tu ingreso es: " + inputText, Toast.LENGTH_SHORT).show();
                    // Ocultando el teclado
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    //validando el ingreso en el juego

                    ahorcadoGame.validar(inputText);

                    Log.v(TAG, "entradas palabra: " + inputText + " - " + ahorcadoGame.getPalabra() + " - " + ahorcadoGame.getGano() + "- es letra el ingreso: " + ahorcadoGame.getEsLetra());

                    if(ahorcadoGame.getEsLetra()){
                        // es una letra lo que se ingreso
                        Log.v(TAG, "GameActivity: " + "Es una letra");

                        // agregando el ingreso a la grid
                        TextView ingreso = new TextView(GameActivity.this);
                        ingreso.setTextSize(30);
                        ingreso.setText(inputText.toUpperCase());
                        ingreso.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                        ingreso.setTypeface(custom_font);
                        ingreso.setPadding(5, 3, 5, 0);

                        // si completa 5 errores con el ingreso actual perdio = true
                        if(ahorcadoGame.esCorrecta()){
                            //ingreso.setTextColor(Color.parseColor("#4caf50"));
                            if(!ahorcadoGame.getGano()){
                                //si no ha ganado aun
                                respuesta.setText(ahorcadoGame.getPalabraSecreta());
                            }else{
                                // si ya la completo
                                gano();
                            }
                        }else{
                            ingreso.setTextColor(Color.parseColor("#ef5350"));
                            int idImageGame = getResources().getIdentifier("game"+ahorcadoGame.getErrores(), "drawable", getPackageName());
                            textviewErrores.setText("Errores: " + ahorcadoGame.getErrores());
                            imageViewGame.setImageResource(idImageGame);
                            if (ahorcadoGame.getPerdio()){
                                perdio();
                            }else{
                                //si aun no completa 5 errores:

                            }
                        }
                        gridLayoutIngresos.addView(ingreso);

                    }else{
                        // si es una palabra, osea el ingreso.length es mayor a 1
                        if (ahorcadoGame.getGano()){
                            gano();
                        }else{
                            perdio();
                        }
                    }

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

    public void gano(){
        new AlertDialog.Builder(GameActivity.this)
                .setMessage("¡Has ganado!")
                .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                        //startActivity(new Intent(GameActivity.this, ProfileActivity.class));
                        dialog.dismiss();
                    }
                }).show();
        editTextLetterWord.setFocusable(false);
    }

    public void perdio(){

        new AlertDialog.Builder(GameActivity.this)
                .setMessage("¡Has perdido!, la palabra era: "+ ahorcadoGame.getPalabra().toString())
                .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                        //startActivity(new Intent(GameActivity.this, ProfileActivity.class));
                        dialog.dismiss();
                    }
                }).show();

        //startActivity(new Intent(GameActivity.this, Pop.class));
        editTextLetterWord.setFocusable(false);
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
