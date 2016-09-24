package com.example.styven.ahorcado;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Pelusa on 24/09/2016.
 */
public class AhorcadoGAme {
    private FirebaseDatabase firebaseDatabase;
    private static String palabra;
    private static String TAG = "AhorcadoGame";
    private DatabaseReference myRef;

    public AhorcadoGAme(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
    }

    public AhorcadoGAme(String word){
        palabra = word.toUpperCase();
    }

    public boolean setPalabraAleatoria(){
        final boolean[] handler = {false};

       return handler[0];
    }

    public String getPalabra() {
        return palabra;
    }

    public String getPalabraSecreta(){
        Log.v(TAG, "Palabra aleatoria: " + palabra);
        String temPalabra = "";
        for(int i=0; i < palabra.length() ; i++){
            if(i != (palabra.length() - 1)){
                temPalabra += "_ ";
            }else{
                temPalabra += "_";
            }
        }
        return temPalabra;
    }

    public boolean validar(String word){
        word = word.toUpperCase().trim();
        if (word.length() > 1){
            if(word == palabra){
                return true;
            }else{
                return false;
            }
        }else{

        }
        return true;
    }

}
