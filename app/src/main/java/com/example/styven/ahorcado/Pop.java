package com.example.styven.ahorcado;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by Pelusa on 27/09/2016.
 */
public class Pop extends Activity{
    private Button cerrar;
    private static String TAG = "Holi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.window_pop);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        cerrar = (Button) findViewById(R.id.id_cerrar);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        int ancho = dm.widthPixels;
        int altura = dm.heightPixels;

        getWindow().setLayout((int)(ancho*.6), (int)(altura*.6));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "cerrado el popup");
    }
}

