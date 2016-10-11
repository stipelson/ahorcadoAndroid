package com.example.styven.ahorcado;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private GridLayout gridHistory;
    private Typeface custom_font;
    private TextView textViewTitle;
    private FirebaseDatabase database;
    private ImageButton imageButtonBack;
    private ImageButton imageButtonSignOut;
    private TextView textViewPalabra;
    private TextView textViewErrores;
    private TextView textViewTiempo;

    private static String TAG = "HistoryActivity: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        gridHistory = (GridLayout) findViewById(R.id.gridHistory);
        custom_font = Typeface.createFromAsset(getAssets(),  "fonts/beermoney.ttf");

        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewErrores = (TextView) findViewById(R.id.textViewErrores);
        textViewPalabra = (TextView) findViewById(R.id.textViewPalabra);
        textViewTiempo = (TextView) findViewById(R.id.textViewTiempo);

        textViewTiempo.setTypeface(custom_font);
        textViewErrores.setTypeface(custom_font);
        textViewPalabra.setTypeface(custom_font);
        textViewTitle.setTypeface(custom_font);

        imageButtonSignOut = (ImageButton) findViewById(R.id.imageButtonSignOut);
        imageButtonBack = (ImageButton) findViewById(R.id.imageButtonBack);


        imageButtonBack.setOnClickListener(this);
        imageButtonSignOut.setOnClickListener(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        if (user != null) {
            // User is signed in
            String userId = user.getUid();
            DatabaseReference myRef = database.getReference();


            myRef.child("historial").child(userId).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Game game = dataSnapshot.getValue(Game.class);
                            try {
                                for (DataSnapshot gameSnapshot : dataSnapshot.getChildren()) {
                                    //String tiempo = gameSnapshot.child("tiempo").getValue().toString();
                                    //Log.v(TAG, "Historial de tiempos: " + tiempo);

                                    TextView ganoGrid = new TextView(HistoryActivity.this);
                                    TextView palabraGrid = new TextView(HistoryActivity.this);
                                    TextView tiempoGrid = new TextView(HistoryActivity.this);
                                    TextView erroresGrid = new TextView(HistoryActivity.this);
                                    //set textSize
                                    erroresGrid.setTextSize(20);
                                    palabraGrid.setTextSize(20);
                                    tiempoGrid.setTextSize(20);
                                    ganoGrid.setTextSize(20);
                                    //set typeFace
                                    erroresGrid.setTypeface(custom_font);
                                    palabraGrid.setTypeface(custom_font);
                                    ganoGrid.setTypeface(custom_font);
                                    tiempoGrid.setTypeface(custom_font);

                                    //erroresGrid.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    //set padding

                                    erroresGrid.setPadding(10, 10, 10, 10);
                                    erroresGrid.setGravity(Gravity.CENTER_HORIZONTAL);
                                    palabraGrid.setPadding(10, 10, 10, 10);
                                    palabraGrid.setGravity(Gravity.CENTER_HORIZONTAL);
                                    ganoGrid.setPadding(10, 10, 10, 10);
                                    ganoGrid.setGravity(Gravity.CENTER_HORIZONTAL);

                                    tiempoGrid.setPadding(10, 10, 10, 10);
                                    tiempoGrid.setGravity(Gravity.CENTER_HORIZONTAL);

                                    // set content
                                    erroresGrid.setText(gameSnapshot.child("errores").getValue().toString());
                                    palabraGrid.setText(gameSnapshot.child("palabra").getValue().toString());
                                    tiempoGrid.setText(gameSnapshot.child("tiempo").getValue().toString());

                                    Log.v(TAG, "Historial de tiempos: " + gameSnapshot.child("gano").getValue().toString());
                                    if (gameSnapshot.child("gano").getValue().toString() == "true") {
                                        ganoGrid.setTextColor(Color.parseColor("#4caf50"));
                                        ganoGrid.setText("Gano");
                                    } else {
                                        ganoGrid.setTextColor(Color.parseColor("#ef5350"));
                                        ganoGrid.setText("Perdio");
                                    }

                                    gridHistory.addView(ganoGrid);
                                    gridHistory.addView(palabraGrid);
                                    gridHistory.addView(erroresGrid);
                                    gridHistory.addView(tiempoGrid);
                                }
                            }catch(Exception e){
                                Log.v(TAG, "Error en historial:" + e.getMessage());
                                Toast.makeText(HistoryActivity.this, "No hay historial para mostrar", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(HistoryActivity.this, "Imposible obtener historial", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
            );




        } else {
            // No user is signed in
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

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
